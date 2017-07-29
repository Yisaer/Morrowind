package com.yisa.morrowind.client;

import com.yisa.morrowind.codec.IDMessageDecoder;
import com.yisa.morrowind.codec.MarshallableEncoder;
import com.yisa.morrowind.proto.IDMessage;
import com.yisa.morrowind.proto.Request;
import com.yisa.morrowind.proto.Response;
import com.yisa.morrowind.util.pack.Unpack;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yisa on 2017/7/29.
 */
public class CommonClient implements Client{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClient.class);

    protected Bootstrap b;

    protected EventLoopGroup group;
    protected ChannelPool channelPool;

    protected Map<Integer, Context> contextMap = new ConcurrentHashMap<Integer, Context>();
    private DefaultEventExecutorGroup executorGroup;
    private String host;
    private int port;
    private int nextMessageId = 1;


    public CommonClient(String host, int port) {
        this.host = host;
        this.port = port;

        init();
    }

    private void init() {

        b = new Bootstrap();
        group = new NioEventLoopGroup(4);
        executorGroup = new DefaultEventExecutorGroup(4,
                new DefaultThreadFactory("worker group"));
        b.group(group)
                .option(ChannelOption.ALLOCATOR.TCP_NODELAY,true)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        initClientChannel(socketChannel);
                    }
                });

        channelPool = new ChannelPool(this);
    }


    public void initClientChannel(SocketChannel ch){
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("RequestEncoder",new MarshallableEncoder())
                .addLast("ResponseDecoder",new IDMessageDecoder())
                .addLast("ClientHandler",new ChannelHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        Integer id = 0;
                        IDMessage message = (IDMessage) msg;
                        id = message.getMessageID();
                        byte[] bytes = message.getBytes();
                        Unpack unpack = new Unpack(bytes);
                        Response resp = new Response();
                        resp.unmarshal(unpack);
                        Context context = contextMap.remove(id);
                        if( context == null){
                            LOGGER.debug("messageID:{} , take Context null",id);
                            return;
                        }
                        context.callback.onReceive(resp);
                    }
                });
    }





    public ChannelFuture connect(){
        return b.connect(host,port);
    }


    public ChannelFuture connect (final String host , final int port){
        ChannelFuture f = null;

        try {
            f = b.connect(host,port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return f;
    }

    public void sendOnly(Request request) throws Exception {
        writeAndFlush(request);
    }

    private int getNextMessageId() {
        int rc = nextMessageId;
        nextMessageId++;
        if (nextMessageId == 0) {
            nextMessageId = 1;
        }
        return rc;
    }
    /**
     * 发送消息, 并获取Reponse
     * @param request
     * @param callback
     * @return
     * @throws Exception
     */
    public Callback call(Request request, Callback callback) throws Exception {
        int id = getNextMessageId();
        request.setMessageID(id);
        Context context = new Context(request ,callback,id);
        contextMap.put(id,context);
        writeAndFlush(request);
        return callback;

    }

    public Future<Response> send(Request request) throws Exception {
        Promise<Response> future = new Promise<Response>();
        call(request,future);
        return future;
    }

    public Response sendAndWait(Request request) throws Exception {
        Future<Response> future = send(request);
        return future.await();
    }

    public Response sendAndWait(Request request, long amount, TimeUnit unit) throws Exception {
        Future<Response> future = send(request);
        return future.await(amount,unit);
    }

    public void writeAndFlush(Object object) throws Exception{
        Connection connection = channelPool.getChannel();
        connection.doTransport(object);
    }


    public static class Context{
        final Request request;
        final Callback callback;
        private final short id;

        public Context(Request request, Callback callback, int id) {
            this.request = request;
            this.callback = callback;
            this.id = (short)id;
        }
    }
}
