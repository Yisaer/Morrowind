package com.yisa.morrowind.client;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yisa on 2017/7/29.
 */
public class ChannelPool {

    private static final Logger LOGGER = Logger.getLogger(ChannelPool.class.getName());

    /**
     * 对象池生成对象
     */
    private final PooledObjectFactory<Connection> objectFactory;
    private final GenericObjectPool<Connection> pool;

    public ChannelPool(CommonClient rpcClient) {
        objectFactory = new ChannelPoolObjectFactory(rpcClient);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        pool = new GenericObjectPool<Connection>(objectFactory, config);
    }




    public Connection getChannel() throws Exception{
        Connection connection = pool.borrowObject();
        return connection;
    }


    public void returnChannel(Connection channel){
        pool.returnObject(channel);
    }

    public void stop(){
        try{

            if(pool != null){
                pool.clear();
                pool.clear();
            }

        }catch (Exception e){
            LOGGER.log(Level.SEVERE ,"stop channel failde",e);
        }
    }

}
