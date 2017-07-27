package com.yisa.wind;

/**
 * Created by Yisa on 2017/7/27.
 */
public enum EnvEnum {

    DEV("dev"),UAT("uat"),PROD("prod"),;

    private String env;

    EnvEnum(String env) {
        this.env = env;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
