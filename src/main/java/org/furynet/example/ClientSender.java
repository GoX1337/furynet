package org.furynet.example;

import org.furynet.client.Client;
import org.furynet.example.protocol.Message;
import org.furynet.example.protocol.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ClientSender {

    private static final Logger logger = LoggerFactory.getLogger(ClientSender.class);

    public static void main(String[] args) throws InterruptedException {
        Client sender = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .protocol(Message.class, Ping.class)
                .build();
        sender.start();

        while (true) {
            Message message = new Message(randNum(), randNum());
            sender.sendTcp(message);
            logger.info("Message {} sent", message);
            Thread.sleep(1000);
        }
    }

    private static int randNum() {
        return new Random().nextInt((100 - 1) + 1) + 1;
    }
}
