package org.furynet.example;

import org.furynet.client.Client;
import org.furynet.example.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.furynet.example.util.RandomUtils.randNum;

public class ClientC {

    private static final Logger logger = LoggerFactory.getLogger(ClientC.class);

    public static void main(String[] args) throws InterruptedException {
        Client client = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .protocol(Protocol.PROTOCOL_EXAMPLE)
                .register(NewConnection.class, (connection, msg) -> logger.info("New player connected"))
                .register(MessageA.class, (connection, msg) -> logger.info("Received msg A {}", msg))
                .register(MessageB.class, (connection, msg) -> logger.info("Received msg B {}", msg))
                .register(MessageD.class, (connection, msg) -> logger.info("Received msg D {}", msg))
                .build();
        client.start();

        while (true) {
            Message message = new MessageC(randNum(), randNum(), 485f);
            client.sendTcp(message);
            logger.info("Message {} sent", message);
            Thread.sleep(1000);
        }
    }
}
