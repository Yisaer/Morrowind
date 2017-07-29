package com.yisa.morrowind.sample;

import com.yisa.morrowind.AppConfig;
import com.yisa.morrowind.BootServer;
import com.yisa.morrowind.Server;
import com.yisa.morrowind.util.DefConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 基于spring注解的sampleServer
 * User: Dempe
 * Date: 2016/1/28
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan
public class SampleServer {

    public static void main(String[] args) {
        // 启动spring容器
        ApplicationContext context = new AnnotationConfigApplicationContext(SampleServer.class);
        // 生成开发环境的配置
        AppConfig devConfig = DefConfigFactory.createDEVConfig();
        Server server = new BootServer(context,devConfig);
        server.start();
    }
}
