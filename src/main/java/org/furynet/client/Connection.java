package org.furynet.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connection {

    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private final ChannelHandlerContext channelHandlerContext;

    public Connection(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public void sendTcp(Object msg) {
        channelHandlerContext.channel().writeAndFlush(msg);
    }

    public ChannelId getChannelId() {
        return channelHandlerContext.channel().id();
    }
}
