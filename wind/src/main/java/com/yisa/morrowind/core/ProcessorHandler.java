package com.yisa.morrowind.core;


import com.sun.org.apache.regexp.internal.RE;
import com.yisa.morrowind.proto.Request;
import com.yisa.morrowind.util.MeticThread;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by Yisa on 2017/7/28.
 */
public class ProcessorHandler extends ChannelHandlerAdapter {

    public static final Logger logger = LoggerFactory.getLogger(ProcessorHandler.class);


    /**
     * 将业务的处理线程与IO线程分离,虽然会带来线程上下文切换的性能,
     * 但会防止因为业务逻辑的阻塞而带来的IO阻塞
     */
    private final static ExecutorService workerThreadService
            = newBlockingExecutorsUseCallerRun(Runtime.getRuntime().availableProcessors() *2);

    private static MeticThread meticThread = new MeticThread("MorroWind");
    private ServerContext context;

    public ProcessorHandler(ServerContext context) {
        this.context = context;
    }

    public static ExecutorService newBlockingExecutorsUseCallerRun(int size){
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        meticThread.increment();

        if(msg instanceof Request){
            workerThreadService.submit(new TaskWorker(ctx,context,(Request)msg));
        }
    }
}
