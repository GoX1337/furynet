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
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {
        int bufLen = buf.readInt();
        byte[] bytes = new byte[bufLen];
        buf.readBytes(bytes);
        Object object = fury.deserialize(bytes);
        out.add(object);
    }
}
