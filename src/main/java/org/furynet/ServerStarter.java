package org.furynet;

import org.furynet.server.Server;

public class ServerStarter {

    public static void main(String[] args) {
        Server server = Server.builder()
                .tcpPort(42000)
                .register(String.class)
                .build();
        server.start();
    }
}