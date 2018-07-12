package com.rtucloud.cs.proxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用程序配置属性.
 */
@Component("appConfig")
@ConfigurationProperties(prefix = "app-config")
public class AppConfig {

    private long interval;

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
