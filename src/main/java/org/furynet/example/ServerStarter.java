package org.furynet.example;

import org.furynet.example.protocol.Message;
import org.furynet.example.protocol.Ping;
import org.furynet.example.protocol.Protocol;
import org.furynet.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerStarter {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        Server.builder()
                .tcpPort(42000)
                .protocol(Protocol.PROTOCOL_EXAMPLE)
                .register(Message.class, (connection, msg) -> {
                    logger.info("Received {} from channel {}", msg, connection.getChannelId());
                    connection.sendToAllExceptTcp(new Ping("Hi from server"));
                })
                .build()
                .start();
    }
}
