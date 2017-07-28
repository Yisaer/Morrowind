package com.yisa.morrowind.proto;

import com.alibaba.fastjson.JSON;
import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Pack;
import com.yisa.morrowind.util.pack.Unpack;

import java.io.IOException;

/**
 * Created by Yisa on 2017/7/28.
 */
public class IDResponse implements Marshallable{
    private int id;
    private String data;

    public IDResponse() {
    }


    public void marshal(Pack pack) {
        pack.putInt(id);
        pack.putVarstr(JSON.toJSONString(this));
    }


    public void unmarshal(Unpack unpack) throws IOException {
        id = unpack.popInt();
        data = unpack.popVarstr();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "JSONResponse{" +
                "getId=" + id +
                ", data=" + data +
                '}';
    }

}
