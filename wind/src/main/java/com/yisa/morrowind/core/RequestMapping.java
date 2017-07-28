package com.yisa.morrowind.core;

/**
 * Created by Yisa on 2017/7/28.
 */

import com.yisa.morrowind.AppConfig;
import com.yisa.morrowind.Path;
import com.yisa.morrowind.util.PackageUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由消息映射
 */
public class RequestMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMapping.class);

    public Map<String, ActionMethod> mapping = new ConcurrentHashMap<String, ActionMethod>();

    private AppConfig config;

    private ApplicationContext context;

    public RequestMapping(AppConfig config, ApplicationContext context) {
        this.config = config;
        this.context = context;
        initMapping();
    }

    /**
     * 扫描packet下面所有的映射，初始化mapping
     */

    public void initMapping() {
        LOGGER.info("handles begin initiating");
        List<String> packages = new ArrayList<String>();

        packages.add(config.getPackageName());
        LOGGER.info("scanned packages :{} ", packages);
        for (String scanPackage : packages) {
            LOGGER.info("begin get classes from package {} : ", scanPackage);
            String[] classNames = PackageUtils.findClassesInPackage(scanPackage + ".*"); // 目录下通配
            for (String className : classNames) {
                try {
                    Class<?> actionClass = Class.forName(className);
                    Controller action = actionClass.getAnnotation(Controller.class);
                    if (action == null) {
                        continue;
                    }
                    String actionVal = action.value();
                    if (StringUtils.isBlank(actionVal)) {
                        actionVal = StringUtils.uncapitalize(actionClass.getSimpleName());
                    }
                    LOGGER.info("registering action  :{} ", actionVal);
                    for (Method method : actionClass.getDeclaredMethods()) {
                        if (method.getModifiers() == Modifier.PUBLIC) {
                            Path refs = method.getAnnotation(Path.class);
                            if (refs != null) {
                                String pathVal = String.valueOf(refs.value());
                                if (StringUtils.isBlank(pathVal)) {
                                    pathVal = method.getName();
                                }
                                String uri = "/" + actionVal + "/" + pathVal;
                                if (mapping.containsKey(uri)) {
                                    LOGGER.warn("Method:{} declares duplicated jsonURI:{}, previous one will be overwritten", method, uri);
                                }
                                makeAccessible(method);
                                /**
                                 * 从spring ioc容器中获取相应的bean
                                 */
                                Object target = context.getBean(actionClass);
                                ActionMethod actionMethod = new ActionMethod(target, method);
                                LOGGER.info("[REQUEST MAPPING] = {}, jsonURI = {}", actionVal, uri);
                                mapping.put(uri, actionMethod);
                            }
                        }
                    }

                } catch (ClassNotFoundException e) {
                    LOGGER.error("FAIL to initiate handle instance", e);
                } catch (Exception e) {
                    LOGGER.error("FAIL to initiate handle instance", e);
                }
            }
        }
        LOGGER.info("Handles  Initialization successfully");
    }


    /**
     * 根据uri获取这个uri请求的所对应的执行目标方法
     * @param uri
     * @return
     */
    public ActionMethod tack(String uri) {
        return mapping.get(uri);
    }


    /**
     * 关闭安全检查
     * @param method
     */
    protected void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }



}
