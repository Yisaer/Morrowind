package com.yisa.wind.util;

import com.yisa.wind.AppConfig;
import com.yisa.wind.EnvEnum;
import org.aeonbits.owner.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yisa on 2017/7/27.
 */
public class DefConfigFactory {

    public static AppConfig createUATConfig(){
        return createConfig(EnvEnum.UAT.getEnv());
    }

    public static AppConfig createDEVConfig(){
        return createConfig(EnvEnum.DEV.getEnv());
    }

    public static AppConfig createPRODConfig(){
        return createConfig(EnvEnum.PROD.getEnv());
    }
    
    public static AppConfig createConfig(String env){
        Map myvars = new HashMap();
        myvars.put("env",env);
        System.setProperty("env",env);
        return ConfigFactory.create(AppConfig.class,myvars);
    }
}
