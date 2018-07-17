package com.rtucloud.cs.proxy.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringIOC工具类,用于非Spring容器管理的类手动获取Spring容器中的Bean.
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getContext() {
        return ApplicationContextUtil.applicationContext;
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, requiredType);
    }


    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

}
