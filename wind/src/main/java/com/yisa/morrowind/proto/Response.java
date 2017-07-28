package com.yisa.morrowind.proto;

import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Pack;
import com.yisa.morrowind.util.pack.Unpack;

import java.io.IOException;

/**
 * Created by Yisa on 2017/7/28.
 */
public class Response implements Marshallable {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Resp{" +
                "data='" + data + '\'' +
                "} " + super.toString();
    }


    public void marshal(Pack pack) {

        pack.putVarstr(data);
    }


    public void unmarshal(Unpack unpack) throws IOException {
        data = unpack.popVarstr();
    }
}
