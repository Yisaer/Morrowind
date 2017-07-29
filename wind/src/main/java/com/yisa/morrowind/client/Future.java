package com.yisa.morrowind.client;

import java.util.concurrent.TimeUnit;

/**
 * Created by Yisa on 2017/7/29.
 */
public interface Future<T> {

    T await() throws Exception;

    T await(long amount , TimeUnit unit) throws Exception;
}
