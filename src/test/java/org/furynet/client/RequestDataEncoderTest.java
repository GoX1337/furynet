package org.furynet.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.fury.ThreadSafeFury;
import org.furynet.serde.FuryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestDataEncoderTest {

    private RequestDataEncoder requestDataEncoder;
    private ThreadSafeFury fury;

    @BeforeEach
    public void setUp() {
        fury = FuryBuilder.buildFurySerde(List.of(Ping.class));
        requestDataEncoder = new RequestDataEncoder(fury);
    }

    @Test
    public void testEncode() throws Exception {
        Ping ping = new Ping("Hi");
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();

        requestDataEncoder.encode(null, ping, buf);

        int bufLen = buf.readInt();
        byte[] bytes = new byte[bufLen];
        buf.readBytes(bytes);

        assertThat(bufLen).isEqualTo(8);
        assertThat(bytes).isEqualTo(new byte[]{2, -1, -80, 2, 0, 8, 72, 105});
        assertThat((Ping) fury.deserialize(bytes)).isEqualTo(ping);
    }
}