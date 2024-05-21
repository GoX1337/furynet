package org.furynet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.fury.ThreadSafeFury;

public class RequestDataEncoder extends MessageToByteEncoder<Object> {

    private final ThreadSafeFury fury;

    public RequestDataEncoder(ThreadSafeFury fury) {
        this.fury = fury;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] payload = fury.serialize(o);
        byteBuf.writeInt(payload.length);
        byteBuf.writeBytes(payload);
    }
}
