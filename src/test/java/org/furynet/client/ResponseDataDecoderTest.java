package org.furynet.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.fury.ThreadSafeFury;
import org.furynet.serde.FuryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ResponseDataDecoderTest {

    private ThreadSafeFury fury;

    private ResponseDataDecoder responseDataDecoder;

    @BeforeEach
    public void setUp() {
        fury = FuryBuilder.buildFurySerde(List.of(Ping.class));
        responseDataDecoder = new ResponseDataDecoder(fury);
    }

    @Test
    public void testDecode() throws Exception {
        List<Object> out = new ArrayList<>();
        Ping ping = new Ping("Hi");

        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        byte[] buffer = fury.serialize(ping);
        buf.writeInt(buffer.length);
        buf.writeBytes(buffer);

        responseDataDecoder.decode(null, buf, out);

        assertThat(out).hasSize(1);
        assertThat(out.get(0)).isEqualTo(ping);
    }
}