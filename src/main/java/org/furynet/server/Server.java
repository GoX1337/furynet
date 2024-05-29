package org.furynet.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.fury.ThreadSafeFury;
import org.furynet.protocol.Message;
import org.furynet.protocol.Ping;
import org.furynet.serde.FuryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final Integer tcpPort;
    private final Integer udpPort;
    private final ThreadSafeFury fury;
    private final Map<Class<?>, BiConsumer<ChannelHandlerContext, Object>> consumers;

    private Server(Integer tcpPort, Integer udpPort, List<Class<?>> registeredClasses, Map<Class<?>, BiConsumer<ChannelHandlerContext, Object>> consumers) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.fury = FuryBuilder.buildFurySerde(registeredClasses);
        this.consumers = consumers;
    }

    public static void main(String[] args) {
        Server.builder()
                .tcpPort(42000)
                .register(Message.class, (ctx, o) -> {
                    System.out.println("server Message consumer: " + o);
                    ctx.writeAndFlush(new Message(1, 1));
                })
                .register(Ping.class, (ctx, o) -> {
                    System.out.println("server Ping consumer: " + o);
                    ctx.writeAndFlush(new Message(2, 2));
                })
                .build()
                .start();
    }

    public void start() {
        logger.info("Server starting...");
        if (this.tcpPort != null) {
            logger.info("Listening TCP port on " + this.tcpPort);
        }
        if (this.udpPort != null) {
            logger.info("Listening UDP port on " + this.udpPort);
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new RequestDecoder(fury),
                                    new ResponseDataEncoder(fury),
                                    new ProcessingHandler(consumers)
                            );
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(this.tcpPort).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static ServerBuilder builder() {
        return new ServerBuilder();
    }

    public static class ServerBuilder {

        private Integer tcpPort;
        private Integer udpPort;
        private final List<Class<?>> registeredClasses = new ArrayList<>();
        private final Map<Class<?>, BiConsumer<ChannelHandlerContext, Object>> consumers = new HashMap<>();

        public ServerBuilder tcpPort(int port) {
            this.tcpPort = port;
            return this;
        }

        public ServerBuilder udpPort(int port) {
            this.udpPort = port;
            return this;
        }

        public ServerBuilder register(Class<?> clazz, BiConsumer<ChannelHandlerContext, Object> consumer) {
            this.registeredClasses.add(clazz);
            this.consumers.put(clazz, consumer);
            return this;
        }

        public Server build() {
            return new Server(this.tcpPort, this.udpPort, this.registeredClasses, this.consumers);
        }
    }
}
