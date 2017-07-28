package com.yisa.morrowind.core;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Yisa on 2017/7/28.
 */
public interface Take<R,T> {

    T act(R request) throws InvocationTargetException,IllegalAccessException,
            ClassNotFoundException,InstantiationException;

}
