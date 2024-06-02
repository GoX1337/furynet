package org.furynet.example;

import org.furynet.client.Client;
import org.furynet.example.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.furynet.example.util.RandomUtils.randNum;

public class ClientB {

    private static final Logger logger = LoggerFactory.getLogger(ClientB.class);

    public static void main(String[] args) throws InterruptedException {
        Client client = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .protocol(Protocol.PROTOCOL_EXAMPLE)
                .register(ClientConnection.class, (connection, msg) -> logger.info("New player connected"))
                .register(ClientDisconnection.class, (connection, msg) -> logger.info("A player disconnected"))
                .register(MessageA.class, (connection, msg) -> logger.info("Received msg A {}", msg))
                .register(MessageC.class, (connection, msg) -> logger.info("Received msg C {}", msg))
                .register(MessageD.class, (connection, msg) -> logger.info("Received msg D {}", msg))
                .build();
        client.start();

        while (true) {
            Message message = new MessageB(randNum(), randNum(), true);
            client.sendTcp(message);
            logger.info("Message {} sent", message);
            Thread.sleep(1000);
        }
    }
}
