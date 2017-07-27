package com.yisa.wind.util.pack;

import java.io.IOException;

/**
 * Created by Yisa on 2017/7/27.
 */
public interface Marshallable {

    /**
     * 消息编码类
     * @param pack
     */
    void marshal(Pack pack);

    void unmarshal(Unpack unpack) throws IOException;
}
