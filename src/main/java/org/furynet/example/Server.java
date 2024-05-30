package org.furynet.example;

import io.netty.channel.Channel;
import org.furynet.example.protocol.Message;
import org.furynet.example.protocol.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(org.furynet.server.Server.class);

    public static void main(String[] args) {
        org.furynet.server.Server server = org.furynet.server.Server.builder()
                .tcpPort(42000)
                .protocol(Message.class, Ping.class)
                .register(Message.class, (ctx, cg, o) -> {
                    logger.info("receive Message event from channel id " + ctx.channel().id());

                    for (Channel c : cg) {
                        if (c != ctx.channel()) {
                            c.writeAndFlush(new Ping("ping from server!"));
                        }
                    }
                })
                .build();

        server.start();
    }
}
