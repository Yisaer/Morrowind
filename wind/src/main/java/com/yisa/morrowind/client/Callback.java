package com.yisa.morrowind.client;

/**
 * Created by Yisa on 2017/7/29.
 */
public interface Callback<T> {
    public void onReceive(T message);
}
