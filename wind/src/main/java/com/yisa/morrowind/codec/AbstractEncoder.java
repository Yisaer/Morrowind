package com.yisa.morrowind.codec;

import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Pack;
import com.yisa.morrowind.util.pack.ProtocolValue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Yisa on 2017/7/28.
 */
public abstract class AbstractEncoder extends MessageToByteEncoder<Marshallable> {


    private final static Logger LOGGER = LoggerFactory.getLogger(MarshallableEncoder.class);

    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

    protected void encode(ChannelHandlerContext channelHandlerContext, Marshallable request, ByteBuf byteBuf) throws Exception {

        try {
            Pack pack = encode(request);

            ByteBuffer data = pack.getBuffer();
            byte protoType = 0;
            if (pack.getAttachment() != null) {
                protoType = Byte.parseByte(pack.getAttachment().toString());
            }
            byteBuf = byteBuf.order(byteOrder);// 字节序转成YY协议的低端字节
            byteBuf.writeBytes(getOutBytes(data, protoType));

        } catch (Throwable e) {
            LOGGER.error("throwable: " + e.getMessage(), e);
            throw new EncoderException(e);
        }
    }

    protected byte[] getOutBytes(ByteBuffer data, byte protoType) {
        int len = data.limit() - data.position() + 4;
        ByteBuffer out = ByteBuffer.allocate(len);
        // 设置小端模式
        out.order(byteOrder);
        int nFirstValue = ProtocolValue.combine(len, protoType);
        // 长度包含包长度int 4个字节
        out.putInt(nFirstValue);
        out.put(data);
        out.flip();
        return out.array();
    }



    public abstract Pack encode(Marshallable request);
}
