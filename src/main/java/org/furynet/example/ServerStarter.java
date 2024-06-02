package org.furynet.example;

import org.furynet.example.protocol.*;
import org.furynet.server.Connection;
import org.furynet.server.Server;
import org.furynet.server.ServerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerStarter {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        Server.builder()
                .tcpPort(42000)
                .protocol(Protocol.PROTOCOL_EXAMPLE)
                .register(ServerEvent.NEW_CLIENT_CONNECTION, (connection) -> {
                    connection.sendToAllExceptTcp(new ClientConnection());
                })
                .register(ServerEvent.CLIENT_DISCONNECTED, (connection) -> {
                    connection.sendToAllTcp(new ClientDisconnection());
                })
                .register(MessageA.class, (connection, msg) -> {
                    log(connection, msg);
                    connection.sendToAllExceptTcp(msg);
                })
                .register(MessageB.class, (connection, msg) -> {
                    log(connection, msg);
                    connection.sendTcp(new MessageD("Hi from server after message B"));
                })
                .register(MessageC.class, (connection, msg) -> {
                    log(connection, msg);
                    connection.sendToAllTcp(new MessageD("Hi from server"));
                })
                .build()
                .start();
    }

    public static void log(Connection connection, Object msg) {
        logger.info("Received {} from channel {}", msg, connection.getChannelId());
    }
}
