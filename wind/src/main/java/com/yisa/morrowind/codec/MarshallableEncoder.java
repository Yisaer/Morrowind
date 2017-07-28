package com.yisa.morrowind.codec;

import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Pack;

/**
 * Created by Yisa on 2017/7/28.
 */
public class MarshallableEncoder extends AbstractEncoder {
    @Override
    public Pack encode(Marshallable request) {
        Pack pack = new Pack();
        request.marshal(pack);
        return pack;
    }
}
