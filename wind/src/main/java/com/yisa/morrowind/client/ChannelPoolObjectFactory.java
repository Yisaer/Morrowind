package com.yisa.morrowind.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yisa on 2017/7/29.
 */
public class ChannelPoolObjectFactory extends BasePooledObjectFactory<Connection>{

    private static final Logger LOGGER = Logger.getLogger(ChannelPoolObjectFactory.class.getName());

    private CommonClient rpcClient;

    public ChannelPoolObjectFactory(CommonClient rpcClient) {
        this.rpcClient = rpcClient;

    }

    public Connection create() throws Exception {
        return new Connection();
    }

    public PooledObject<Connection> wrap(Connection obj) {
        Connection connection = new Connection();
        ChannelFuture future = this.rpcClient.connect();
        // Wait until the connection is made successfully.
        future.awaitUninterruptibly();
        if (!future.isSuccess()) {
            LOGGER.log(Level.SEVERE, "failed to get result from stp", future.cause());
        } else {
            connection.setIsConnected(true);
        }
        connection.setFuture(future);

        return new DefaultPooledObject<Connection>(connection);
    }

    public void destroyObject(PooledObject<Connection> p ) throws Exception{
        Connection c = p.getObject();
        Channel channel = c.getFuture().channel();
        if(channel.isOpen() && channel.isActive()){
            channel.close();
        }
    }


    public boolean validateObject(PooledObject<Connection> p ){
        Connection c = p.getObject();
        Channel channel = c.getFuture().channel();
        return channel.isOpen() && channel.isActive();
    }

    public void activateObject(PooledObject<Connection> p ) throws Exception{

    }


}
