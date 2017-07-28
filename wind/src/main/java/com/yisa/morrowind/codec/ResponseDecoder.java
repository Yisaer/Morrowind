package com.yisa.morrowind.codec;

import com.yisa.morrowind.proto.Response;
import com.yisa.morrowind.util.pack.Marshallable;
import com.yisa.morrowind.util.pack.Unpack;

import java.io.IOException;

/**
 * Created by Yisa on 2017/7/28.
 */
public class ResponseDecoder extends AbstractDecoder{

        @Override
        public Marshallable decode(Unpack unpack) throws IOException {
            Response response = new Response();
            response.unmarshal(unpack);
            return response;
        }
}
