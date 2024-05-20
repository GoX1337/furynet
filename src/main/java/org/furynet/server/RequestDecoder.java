package org.furynet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.apache.fury.ThreadSafeFury;

import java.util.List;

public class RequestDecoder extends ReplayingDecoder<Object> {

    private final ThreadSafeFury fury;

    public RequestDecoder(ThreadSafeFury fury) {
        this.fury = fury;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        out.add(fury.deserialize(in.array()));
    }
}
