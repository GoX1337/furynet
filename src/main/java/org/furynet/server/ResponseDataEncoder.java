package org.furynet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.fury.ThreadSafeFury;

public class ResponseDataEncoder extends MessageToByteEncoder<Object> {

    private final ThreadSafeFury fury;

    public ResponseDataEncoder(ThreadSafeFury fury) {
        this.fury = fury;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf out) throws Exception {
        byte[] payload = this.fury.serialize(obj);
        out.writeInt(payload.length);
        out.writeBytes(payload);
    }
}
