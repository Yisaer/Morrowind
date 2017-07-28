package com.yisa.morrowind.core;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Yisa on 2017/7/28.
 */
public class MethodInvoker {

    public static Object interceptorInvoker(ActionMethod actionMethod,
                    Object[] paramterValues) throws InvocationTargetException,IllegalAccessException{

        //TODO
        // 拦截器速率限定

        Object result = actionMethod.call(paramterValues);
        return result;
    }
}
