package org.furynet.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.fury.Fury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.Language;
import org.furynet.protocol.Message;
import org.furynet.protocol.Ping;
import org.furynet.serde.FuryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final String hostname;
    private final Integer tcpPort;
    private final Integer udpPort;
    private final ThreadSafeFury fury;
    private final Map<Class<?>, BiConsumer<ChannelHandlerContext, Object>> consumers;
    private Channel channel;

    private Client(String hostname, Integer tcpPort, Integer udpPort, List<Class<?>> registeredClasses, Map<Class<?>, BiConsumer<ChannelHandlerContext, Object>> listeners) {
        this.hostname = hostname;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.consumers = listeners;
        this.fury = FuryBuilder.buildFurySerde(registeredClasses);
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .register(Message.class, (ctx, o) -> System.out.println("Message consumer: " + o))
                .register(Ping.class, (ctx, o) -> System.out.println("Ping consumer: " + o))
                .build();

        client.start();

        while (true) {
            client.sendTcp(new Message(randNum(), randNum()));
            Thread.sleep(500);
            client.sendTcp(new Ping("ping msg " + randNum()));
            Thread.sleep(1000);
        }
    }

    private static int randNum() {
        return new Random().nextInt((100 - 1) + 1) + 1;
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
                            new RequestDataEncoder(fury),
                            new ResponseDataDecoder(fury),
                            new ClientHandler(consumers)
                    );
                }
            });
            ChannelFuture f = b.connect(hostname, tcpPort).sync();
            this.channel = f.sync().channel();
            //f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendTcp(Object object) {
        this.channel.writeAndFlush(object);
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public static class ClientBuilder {

        private String hostname;
        private Integer tcpPort;
        private Integer udpPort;
        private final List<Class<?>> registeredClasses = new ArrayList<>();
        private final Map<Class<?>, BiConsumer<ChannelHandlerContext, Object>> consumers = new HashMap<>();

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

        public ClientBuilder register(Class<?> clazz, BiConsumer<ChannelHandlerContext, Object> listener) {
            this.registeredClasses.add(clazz);
            this.consumers.put(clazz, listener);
            return this;
        }

        public Client build() {
            return new Client(this.hostname, this.tcpPort, this.udpPort, this.registeredClasses, this.consumers);
        }
    }
}
