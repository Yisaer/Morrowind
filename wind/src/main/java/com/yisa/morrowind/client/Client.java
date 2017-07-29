package com.yisa.morrowind.client;

import com.yisa.morrowind.proto.Request;
import com.yisa.morrowind.proto.Response;

import java.util.concurrent.TimeUnit;

/**
 * Created by Yisa on 2017/7/29.
 */
public interface Client {

    public void sendOnly(Request request) throws Exception;

    public Callback call(Request request , Callback callback) throws Exception;

    public Future<Response> send(Request request) throws Exception;


    public Response sendAndWait(Request request) throws Exception;

    public Response sendAndWait(Request request, long amount , TimeUnit unit) throws Exception;


}
