package com.yisa.morrowind.core;

import com.yisa.morrowind.Param;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yisa on 2017/7/28.
 */
public class MethodParam {


    private final static Map<Method, String[]> paramCacheMap = new ConcurrentHashMap<Method, String[]>();

    /**
     *
     * 从方法参数中获取名称
     *
     * @param method
     * @return
     */
    public static String[] getParameterNames(Method method) {
        String[] parameterNames = paramCacheMap.get(method);
        if (parameterNames == null) {
            parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                for (int i = 0; i < parameterAnnotation.length; i++) {
                    if (parameterAnnotation[i].annotationType() == Param.class) {
                        String value = ((Param) parameterAnnotation[i]).value();
                        if (StringUtils.isNotBlank(value)) {
                            parameterNames[i] = value;
                        }
                    }
                }
            }
            // set cache
            /**
             * 将参数名称映射到对应方法中
             */
            paramCacheMap.put(method, parameterNames);
        }
        return parameterNames;
    }

    public static Object[] getParameterValues(String[] parameterNames, Method method, Map<String, String> params) {
        Object[] paramTarget = null;
        if (parameterNames != null) {
            paramTarget = new Object[parameterNames.length];
            Type[] type = method.getGenericParameterTypes();
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                if (Integer.class == type[i] || StringUtils.equals(type[i].toString(), "int")) {
                    paramTarget[i] = Integer.parseInt(params.get(parameterName));
                } else if (String.class == type[i]) {
                    paramTarget[i] = params.get(parameterName);
                } else if (Boolean.class == type[i] || StringUtils.equals(type[i].toString(), "boolean")) {
                    paramTarget[i] = Boolean.parseBoolean(params.get(parameterName));
                } else if (Long.class == type[i] || StringUtils.equals(type[i].toString(), "long")) {
                    paramTarget[i] = Long.parseLong(params.get(parameterName));
                } else if (Short.class == type[i] || StringUtils.equals(type[i].toString(), "short")) {
                    paramTarget[i] = Short.parseShort(params.get(parameterName));
                } else if (Double.class == type[i] || StringUtils.equals(type[i].toString(), "double")) {
                    paramTarget[i] = Double.parseDouble(params.get(parameterName));
                } else if (Float.class == type[i] || StringUtils.equals(type[i].toString(), "float")) {
                    paramTarget[i] = Float.parseFloat(params.get(parameterName));
                }

            }
        }
        return paramTarget;
    }

}
