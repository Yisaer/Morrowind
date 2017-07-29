package com.yisa.morrowind;

import com.yisa.morrowind.core.ServerContext;
import com.yisa.morrowind.core.ServerHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created by Yisa on 2017/7/29.
 */
public class BootServer implements Server{

    private static final Logger LOGGER = LoggerFactory.getLogger(BootServer.class);
    private ApplicationContext context;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap b;
    private DefaultEventExecutorGroup executorGroup;
    private AppConfig config;
    private ServerContext Servercontext;


    public BootServer(ApplicationContext context, AppConfig config) {
        this.context = context;
        this.config = config;
        Servercontext = new ServerContext(config,this.context);
        init();
    }

    private void init(){
        executorGroup = new DefaultEventExecutorGroup
                (
                        Runtime.getRuntime().availableProcessors()*2 ,
                        new DefaultThreadFactory("decoder-worker-thread-pool")
                );
        ChannelInitializer channelInitializer = new ServerHandlerInitializer(Servercontext);
        init(channelInitializer);

    }

    private void init(ChannelInitializer channelInitializer) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2);
        b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,config.tcpNoDelay())
                .option(ChannelOption.SO_KEEPALIVE,config.soKeepAlive())
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(channelInitializer);
    }


    public void start() {

        try {
            ChannelFuture f = b.bind(config.port()).sync();
            LOGGER.info("server start:{}",config.port());
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            stop();
        }
    }

    public void stop() {

        if(bossGroup!=null){
            bossGroup.shutdownGracefully();
        }
        if(workerGroup!=null){
            workerGroup.shutdownGracefully();
        }
    }


    public BootServer stopWithJVMShutdown(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                stop();
            }
        }));
        return this;
    }
}
