package org.furynet.client;

import org.apache.fury.ThreadLocalFury;
import org.furynet.example.protocol.ClientConnection;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientTest {

    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);


    @Test
    @SuppressWarnings("unchecked")
    void builder() throws NoSuchFieldException, IllegalAccessException {
        Client client = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .udpPort(43000)
                .protocol(List.of(ClientConnection.class, Ping.class))
                .register(ClientConnection.class, (connection, msg) -> logger.info("client connection"))
                .register(Ping.class, (connection, msg) -> logger.info("ping {}", msg))
                .build();

        Field hostnameField = client.getClass().getDeclaredField("hostname");
        hostnameField.setAccessible(true);
        String hostname = (String) hostnameField.get(client);
        assertThat(hostname).isEqualTo("127.0.0.1");

        Field tcpPortField = client.getClass().getDeclaredField("tcpPort");
        tcpPortField.setAccessible(true);
        Integer tcpPort = (Integer) tcpPortField.get(client);
        assertThat(tcpPort).isEqualTo(42000);

        Field udpPortField = client.getClass().getDeclaredField("udpPort");
        udpPortField.setAccessible(true);
        Integer udpPort = (Integer) udpPortField.get(client);
        assertThat(udpPort).isEqualTo(43000);

        Field consumersField = client.getClass().getDeclaredField("consumers");
        consumersField.setAccessible(true);
        Map<Class<?>, BiConsumer<Connection, Object>> consumers = (Map<Class<?>, BiConsumer<Connection, Object>>) consumersField.get(client);
        assertThat(consumers).hasSize(2);
        assertThat(consumers).containsKey(ClientConnection.class);
        assertThat(consumers).containsKey(Ping.class);

        Field furyField = client.getClass().getDeclaredField("fury");
        furyField.setAccessible(true);
        ThreadLocalFury fury = (ThreadLocalFury) furyField.get(client);
        assertThat(fury).isNotNull();
    }

    @Test
    void start() {
    }

    @Test
    void sendTcp() {
    }
}