package com.yisa.wind;

import org.aeonbits.owner.Config;

/**
 * Created by Yisa on 2017/7/27.
 */
@Config.Sources("classpath:application.properties")
public interface AppConfig extends Config{

    @Key("port")
    @DefaultValue("8888")
    int port();

    @DefaultValue("true")
    boolean tcpNoDelay();

    @DefaultValue("true")
    boolean soKeepAlive();

    @Key("common.package")
    @DefaultValue("com.yisa.wind")
    String getPackageName();

    /**
     *  可以通过env参数实现不同开发环境的隔离
     */

    @Key("env")
    @DefaultValue("uat")
    String env();

    @Key("servers.${env}.appId")
    @DefaultValue("30001")
    int appId();
}
