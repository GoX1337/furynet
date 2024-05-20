package org.furynet.server;

import org.apache.fury.Fury;
import org.apache.fury.config.Language;

public class Server {

    private final Fury fury = Fury.builder()
            .withLanguage(Language.JAVA)
            .requireClassRegistration(true)
            .build();

    public Server() {
    }

    public void start() {

    }

    public void bind(int tcpPort, int udpPort) {

    }
}
