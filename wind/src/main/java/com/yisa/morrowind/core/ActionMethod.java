package com.yisa.morrowind.core;

/**
 * Created by Yisa on 2017/7/28.
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  ActionMethod对应Controller中某个@Path下的方法
 */
public class ActionMethod {

    private Object target;

    private Method method;

    /**
     * @param target
     * @param method
     */
    public ActionMethod(Object target, Method method) {
        this.target = target;
        this.method = method;
    }


    /**
     * 根据参数 ,调用目标对象方法
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object call(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "ActionMethod{" +
                "target=" + target +
                ", method=" + method +
                '}';
    }
}
