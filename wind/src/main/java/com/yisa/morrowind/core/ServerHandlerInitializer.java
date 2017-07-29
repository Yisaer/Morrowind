package com.yisa.morrowind.core;


import com.yisa.morrowind.codec.MarshallableEncoder;
import com.yisa.morrowind.codec.RequestDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private ServerContext context;

    public ServerHandlerInitializer(ServerContext context) {
        this.context = context;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("RequestDecoder", new RequestDecoder())
                .addLast("ResponseEncoder", new MarshallableEncoder())
                .addLast("dispatcher", new ProcessorHandler(context))
        ;
    }
}
