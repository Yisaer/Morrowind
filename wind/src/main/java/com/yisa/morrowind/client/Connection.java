package com.yisa.morrowind.client;

/**
 * Created by Yisa on 2017/7/29.
 */

import io.netty.channel.ChannelFuture;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RPC客户端连接
 */
public class Connection {

    /**
     * 保存异步操作结果
     */
    private ChannelFuture future;
    private AtomicBoolean isConnected = new AtomicBoolean();

    public Connection() {
        this.isConnected.set(false);
        this.future = null;
    }

    public ChannelFuture getFuture() {
        return future;
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected.set(isConnected);
    }

    /**
     * 写入刷新
     * @param obj
     */
    public void doTransport(Object obj) {
        if (isConnected()) {
            future.channel().writeAndFlush(obj);
        }
    }
}
