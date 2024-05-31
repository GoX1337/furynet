package org.furynet.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingHandler.class);

    private final Map<Class<?>, BiConsumer<Connection, Object>> consumers;
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public ProcessingHandler(Map<Class<?>, BiConsumer<Connection, Object>> consumers) {
        this.consumers = consumers;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Optional.ofNullable(this.consumers.get(msg.getClass()))
                .orElse(this::unknownMessageType)
                .accept(new Connection(ctx, channels), msg);
    }

    private void unknownMessageType(Connection connection, Object o) {
        logger.warn("Unknown message type : " + o.getClass().getName());
    }
}
