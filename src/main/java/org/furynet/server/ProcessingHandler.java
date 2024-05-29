package org.furynet.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingHandler.class);

    private final Map<Class<?>, BiConsumer<ChannelHandlerContext, Object>> consumers;

    public ProcessingHandler(Map<Class<?>, BiConsumer<ChannelHandlerContext, Object>> consumers) {
        this.consumers = consumers;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Optional.ofNullable(this.consumers.get(msg.getClass()))
                .orElse(ProcessingHandler::unknownMessageType)
                .accept(ctx, msg);
    }

    private static void unknownMessageType(ChannelHandlerContext channelHandlerContext, Object o) {
        logger.warn("No consumer found for event of type : " + o.getClass().getName());
    }
}
