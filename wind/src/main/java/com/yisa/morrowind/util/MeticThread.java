package com.yisa.morrowind.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 * 性能监控器
 * Created by Yisa on 2017/7/27.
 */
public class MeticThread extends TimerTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeticThread.class);

    private String name;

    private AtomicInteger ps = new AtomicInteger(0);

    public MeticThread(String name) {
        this.name = name;
        new Timer().scheduleAtFixedRate(this, 1000L, 1000L);
    }

    public void increment() {
        ps.incrementAndGet();
    }

    @Override
    public void run() {
        LOGGER.info("[name=" + name + "], " + "[ps/s=" + ps.get() + "]");
        ps = new AtomicInteger(0);
    }
}
