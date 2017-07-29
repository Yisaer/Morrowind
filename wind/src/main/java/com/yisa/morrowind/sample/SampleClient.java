package com.yisa.morrowind.sample;

import com.yisa.morrowind.client.CommonClient;
import com.yisa.morrowind.client.Future;
import com.yisa.morrowind.proto.Request;
import com.yisa.morrowind.proto.Response;
import com.yisa.morrowind.util.MeticThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 简单client示例
 * User: Dempe
 * Date: 2016/1/28
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class SampleClient {

    public static void main(String[] args) throws Exception {
//        futureClientExample();
        stressTesting();
    }

    /**
     * FutureClient example
     * @throws Exception
     */
    public static void futureClientExample() throws Exception {
        CommonClient client = new CommonClient("localhost", 12345);
        // 构造json请求协议
        Request request = buildRequest();
        Future<Response> future = client.send(request);
        System.out.println(future.await());
    }

    public static Request buildRequest() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", "tom");
        data.put("age", "1");
        Request request = new Request();
        request.setUri("/sample/hello");
        request.setParamMap(data);
        return request;
    }

    public static void stressTesting() throws Exception {
        MeticThread thread = new MeticThread("client");
        List<CommonClient> clientList = new ArrayList<CommonClient>();
        int size = 8;
        for (int i = 0; i < size; i++) {
            clientList.add(new CommonClient("localhost", 12345));
        }
        int i = 0;
        while (true) {
            i++;
            thread.increment();
            CommonClient client = clientList.get(i % size);
            // 初始化client
            Request request = buildRequest();
            //发送请求并返回响应
            Future<Response> future = client.send(request);
            System.out.println(future.await());
            if (i % 100000 == 0) {
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

}
