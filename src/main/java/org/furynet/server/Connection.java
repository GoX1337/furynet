package org.furynet.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;

public class Connection {

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
            channel.writeAndFlush(msg);
        }
    }

    public void sendToAllExceptTcp(Object msg) {
        for (Channel channel : this.channelGroup) {
            if (channel != channelHandlerContext.channel()) {
                channel.writeAndFlush(msg);
            }
        }
    }

    public ChannelId getChannelId() {
        return channelHandlerContext.channel().id();
    }
}
