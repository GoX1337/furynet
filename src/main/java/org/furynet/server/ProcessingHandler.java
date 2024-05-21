package org.furynet.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.furynet.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(String.valueOf(msg));

        ChannelFuture future = ctx.writeAndFlush(new Message(66, 33));
    }
}
