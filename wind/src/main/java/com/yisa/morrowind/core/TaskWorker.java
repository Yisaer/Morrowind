package com.yisa.morrowind.core;

import com.yisa.morrowind.proto.IDResponse;
import com.yisa.morrowind.proto.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * Created by Yisa on 2017/7/28.
 */
public class TaskWorker implements  Runnable {


    private final static Logger LOGGER = LoggerFactory.getLogger(TaskWorker.class);

    private ChannelHandlerContext ctx;
    private ServerContext context;
    private Request request;



    public TaskWorker(ChannelHandlerContext ctx, ServerContext context, Request request) {
        this.ctx = ctx;
        this.context = context;
        this.request = request;
    }

    public void run() {


        try {
            context.setLocalContext(request,ctx);
            ActionTake tack = new ActionTake(context);
            final IDResponse act = tack.act(request);

            /**
             * 这里无需显式的释放,writeandFlush已经release msg.
             */
            if(act != null ){
                ctx.writeAndFlush(act);
            }
            else{
                /**
                 * 通过引用计数器释放
                 */
                ReferenceCountUtil.release(act);
            }



        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }finally {
            /**
             * 当执行完毕后移除当前上下文
             */
            context.removeLocalContext();
        }

    }
}
