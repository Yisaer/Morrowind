package com.yisa.morrowind;

import org.aeonbits.owner.Config;

/**
 * Created by Yisa on 2017/7/27.
 */
@Config.Sources("classpath:application.properties")
public interface AppConfig extends Config{

    @Key("port")
    @DefaultValue("12345")
    int port();

    @DefaultValue("true")
    boolean tcpNoDelay();

    @DefaultValue("true")
    boolean soKeepAlive();

    @Key("common.package")
    @DefaultValue("com.yisa.morrowind")
    String getPackageName();

    /**
     *  可以通过env参数实现不同开发环境的隔离
     */

    @Key("env")
    @DefaultValue("uat")
    String env();

    @Key("servers.${env}.appId")
    @DefaultValue("34445")
    int appId();
}
