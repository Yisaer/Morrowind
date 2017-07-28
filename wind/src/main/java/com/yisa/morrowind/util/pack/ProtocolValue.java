package com.yisa.morrowind.util.pack;

/**
 * Created by Yisa on 2017/7/28.
 */
public class ProtocolValue {

    public static int combine(int len, int protoType) {
        return protoType << 24 | len;
    }

    public static int[] parse(int firstValue) {
        int nProtoType = firstValue >> 24;
        int packetSize = firstValue & 0x00ffffff;
        return new int[]{nProtoType, packetSize};
    }
}
