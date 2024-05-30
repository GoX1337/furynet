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

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingHandler.class);

    private final Map<Class<?>, TriConsumer<ChannelHandlerContext, ChannelGroup, Object>> consumers;
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public ProcessingHandler(Map<Class<?>, TriConsumer<ChannelHandlerContext, ChannelGroup, Object>> consumers) {
        this.consumers = consumers;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Optional.ofNullable(this.consumers.get(msg.getClass()))
                .orElse(ProcessingHandler::unknownMessageType)
                .accept(ctx, this.channels, msg);
    }

    private static void unknownMessageType(ChannelHandlerContext channelHandlerContext, ChannelGroup channelGroup, Object o) {
        logger.warn("No consumer found for event of type : " + o.getClass().getName());
    }
}
