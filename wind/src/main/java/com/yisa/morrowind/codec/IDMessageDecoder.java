package com.yisa.morrowind.codec;

import com.yisa.morrowind.proto.IDMessage;
import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Unpack;

import java.io.IOException;

/**
 * Created by Yisa on 2017/7/28.
 */
public class IDMessageDecoder extends AbstractDecoder {
    public Marshallable decode(Unpack unpack) throws IOException {
        IDMessage message = new IDMessage();
        message.unmarshal(unpack);
        return message;
    }
}
