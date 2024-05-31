package org.furynet.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connection {

    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private final ChannelHandlerContext channelHandlerContext;
    private final ChannelGroup channelGroup;

    public Connection(ChannelHandlerContext channelHandlerContext, ChannelGroup channelGroup) {
        this.channelHandlerContext = channelHandlerContext;
        this.channelGroup = channelGroup;
    }

    public void sendTcp(Object msg) {
        channelHandlerContext.channel().writeAndFlush(msg);
    }

    public void sendToAllTcp(Object msg) {
        for (Channel channel : this.channelGroup) {
            logger.info("Send event {} to channel {}", msg, channel.id());
            channel.writeAndFlush(msg);
        }
    }

    public void sendToAllExceptTcp(Object msg) {
        for (Channel channel : this.channelGroup) {
            if (channel != channelHandlerContext.channel()) {
                logger.info("Send event {} to channel {}", msg, channel.id());
                channel.writeAndFlush(msg);
            }
        }
    }

    public ChannelId getChannelId() {
        return channelHandlerContext.channel().id();
    }
}
