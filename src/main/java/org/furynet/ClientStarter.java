package org.furynet;

import org.furynet.client.Client;

public class ClientStarter {

    public static void main(String[] args) {
        Client client = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .register(String.class)
                .build();
        client.start();
    }
}