package org.furynet.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private final Map<Class<?>, BiConsumer<Connection, Object>> consumers;

    public ClientHandler(Map<Class<?>, BiConsumer<Connection, Object>> listeners) {
        this.consumers = listeners;
    }

    /*@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //Message msg = new Message(5, 8);
        //ChannelFuture future = ctx.writeAndFlush(msg);
    }*/

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Optional.ofNullable(this.consumers.get(msg.getClass()))
                .orElse(ClientHandler::unknownMessageType)
                .accept(new Connection(ctx), msg);
    }

    private static void unknownMessageType(Connection channelHandlerContext, Object o) {
        logger.warn("No consumer found for event of type : {}", o.getClass().getName());
    }
}