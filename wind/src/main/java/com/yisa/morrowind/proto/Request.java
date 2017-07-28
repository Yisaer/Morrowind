package com.yisa.morrowind.proto;

import com.yisa.morrowind.util.pack.MarshallUtils;
import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Pack;
import com.yisa.morrowind.util.pack.Unpack;

import java.util.Map;

/**
 * Created by Yisa on 2017/7/28.
 */
public class Request implements Marshallable {

    protected int messageID;

    private String uri;
    /**
     * 对应参数
     */
    private Map<String, String> paramMap;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    @Override
    public String toString() {
        return "Req{" +
                "uri='" + uri + '\'' +
                ", paramMap=" + paramMap +
                "} " + super.toString();
    }


    public void marshal(Pack pack) {
        pack.putInt(messageID);
        pack.putVarstr(uri);
        MarshallUtils.packMap(pack, paramMap, String.class, String.class);
    }


    public void unmarshal(Unpack unpack) {
        messageID = unpack.popInt();
        uri = unpack.popVarstr();
        paramMap = MarshallUtils.unpackMap(unpack, String.class, String.class, false);
    }
}
