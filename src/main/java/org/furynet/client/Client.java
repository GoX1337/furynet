package org.furynet.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.fury.Fury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.Language;
import org.furynet.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private String hostname;
    private Integer tcpPort;
    private Integer udpPort;
    private final ThreadSafeFury fury;

    private Client(String hostname, Integer tcpPort, Integer udpPort, List<Class<?>> registeredClasses) {
        this.hostname = hostname;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.fury = buildFurySerde(registeredClasses);
    }

    private ThreadSafeFury buildFurySerde(List<Class<?>> registeredClasses) {
        ThreadSafeFury fury = Fury.builder()
                .withLanguage(Language.JAVA)
                .requireClassRegistration(true)
                .buildThreadSafeFury();
        for (Class<?> clazz : registeredClasses) {
            fury.register(clazz);
        }
        return fury;
    }

    public void start() {
        logger.info("Client starting...");

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            //new RequestDataEncoder(),
                            //new ResponseDataDecoder(),
                            //new ClientHandler()
                    );
                }
            });
            ChannelFuture f = b.connect(hostname, tcpPort).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public int sendTcp(Object object) {
        return 0;
    }

    public int sendUdp(Object object) {
        return 0;
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public static class ClientBuilder {

        private String hostname;
        private Integer tcpPort;
        private Integer udpPort;
        private final List<Class<?>> registeredClasses = new ArrayList<>();

        public ClientBuilder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public ClientBuilder tcpPort(int port) {
            this.tcpPort = port;
            return this;
        }

        public ClientBuilder udpPort(int port) {
            this.udpPort = port;
            return this;
        }

        public ClientBuilder register(Class<?> clazz) {
            this.registeredClasses.add(clazz);
            return this;
        }

        public Client build() {
            return new Client(this.hostname, this.tcpPort, this.udpPort, this.registeredClasses);
        }
    }
}
