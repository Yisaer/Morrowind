package com.yisa.morrowind.core;

import com.yisa.morrowind.codec.MarshallableEncoder;
import com.yisa.morrowind.codec.RequestDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Yisa on 2017/7/28.
 */
public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {


    private ServerContext context;

    public ServerHandlerInitializer(ServerContext context) {
        this.context = context;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("RequestDecoder",new RequestDecoder())
                .addLast("ResponseEncoder",new MarshallableEncoder())
                .addLast("dispatcher",new ProcessorHandler(context));
    }
}
