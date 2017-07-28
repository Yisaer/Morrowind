package com.yisa.morrowind.codec;

import com.yisa.morrowind.proto.Request;
import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Unpack;

/**
 * Created by Yisa on 2017/7/28.
 */
public class RequestDecoder extends AbstractDecoder {
    @Override
    public Marshallable decode(Unpack unpack) {
        Request proto = new Request();
        proto.unmarshal(unpack);
        return proto;
    }
}
