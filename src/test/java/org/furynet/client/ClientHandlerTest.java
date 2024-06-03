package org.furynet.client;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@ExtendWith(MockitoExtension.class)
public class ClientHandlerTest {

    private ClientHandler clientHandler;

    @Mock
    private BiConsumer<Connection, Object> consumer;

    @Mock
    private ChannelHandlerContext ctx;

    @BeforeEach
    void setUp() {
        Map<Class<?>, BiConsumer<Connection, Object>> listeners = new HashMap<>();
        listeners.put(String.class, consumer);
        clientHandler = new ClientHandler(listeners);
    }

    @Test
    void channelRead() throws Exception {
        clientHandler.channelRead(ctx, "hi");

        Mockito.verify(consumer).accept(Mockito.any(Connection.class), Mockito.any(Object.class));
    }

    @Test
    void channelReadUnknownMessage() throws Exception {
        clientHandler.channelRead(ctx, new Ping("dd"));

        Mockito.verifyNoInteractions(consumer);
    }
}