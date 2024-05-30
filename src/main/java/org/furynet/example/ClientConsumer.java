package org.furynet.example;

import org.furynet.client.Client;
import org.furynet.example.protocol.Message;
import org.furynet.example.protocol.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ClientConsumer.class);

    public static void main(String[] args) {
        Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .protocol(Message.class, Ping.class)
                .register(Ping.class, (ch, pingMsg) -> logger.info("Ping received {}", pingMsg))
                .build()
                .start();
    }
}
