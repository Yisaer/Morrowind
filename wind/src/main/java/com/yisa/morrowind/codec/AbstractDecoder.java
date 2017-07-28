package com.yisa.morrowind.codec;

import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.ProtocolValue;
import com.yisa.morrowind.util.pack.Unpack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by Yisa on 2017/7/28.
 */
public abstract class AbstractDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        // 设置小端模式
        buf = buf.order(ByteOrder.LITTLE_ENDIAN);
        int length = buf.readableBytes();
        if (length < 4) {
            return;
        }
        //
        int firstIntValue = buf.markReaderIndex().readInt();
        /**
         * 相应的值
         */
        int[] protoValue = ProtocolValue.parse(firstIntValue);
        // 获取包大小
        int curPacketSize = protoValue[1];
        // 数据大小
        int dataSize = curPacketSize - 4;
        length = buf.readableBytes();
        if (length < dataSize) {
            buf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[dataSize];
        buf.readBytes(bytes, 0, bytes.length);
        Unpack unpack = new Unpack(bytes);
        byte protoType = (byte) protoValue[0];

        if (protoType == 0) {
            Marshallable proto = decode(unpack);
            list.add(proto);
        }
    }

    public abstract Marshallable decode(Unpack unpack) throws IOException;
}
