package com.yisa.morrowind.proto;

import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Pack;
import com.yisa.morrowind.util.pack.Unpack;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Yisa on 2017/7/27.
 */
public class IDMessage implements Marshallable{


    protected int messageID;

    private byte[] bytes;

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }


    public void marshal(Pack pack) {
        pack.putInt(messageID);
        pack.putBuffer(ByteBuffer.wrap(bytes));
    }

    public void unmarshal(Unpack unpack) throws IOException {
        messageID = unpack.popInt();
        int remaining = unpack.getBuffer().remaining();
        bytes = unpack.popFetch(remaining);
    }
}
