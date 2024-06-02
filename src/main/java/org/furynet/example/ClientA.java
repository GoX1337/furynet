package org.furynet.example;

import org.furynet.client.Client;
import org.furynet.example.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.furynet.example.util.RandomUtils.randNum;

public class ClientA {

    private static final Logger logger = LoggerFactory.getLogger(ClientA.class);

    public static void main(String[] args) throws InterruptedException {
        Client sender = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .protocol(Protocol.PROTOCOL_EXAMPLE)
                .register(ClientConnection.class, (connection, msg) -> logger.info("New player connected"))
                .register(ClientDisconnection.class, (connection, msg) -> logger.info("A player disconnected"))
                .register(MessageB.class, (connection, msg) -> logger.info("Received msg B {}", msg))
                .register(MessageC.class, (connection, msg) -> logger.info("Received msg C {}", msg))
                .register(MessageD.class, (connection, msg) -> logger.info("Received msg D {}", msg))
                .build();
        sender.start();

        while (true) {
            Message message = new MessageA(randNum(), randNum(), "hi from client a");
            sender.sendTcp(message);
            logger.info("Message {} sent", message);
            Thread.sleep(1000);
        }
    }
}
