package com.rtucloud.cs.proxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用程序配置属性.
 */
@Component("appConfig")
@ConfigurationProperties(prefix = "app-config")
public class AppConfig {


    private String localPort;

    private String remoteAddress;

    private long interval;


    public List<String[]> getRemoteAddress() {

        List<String[]> arrayList = new ArrayList<String[]>();

        if (null != remoteAddress && !remoteAddress.equals("")) {

            String[] remoteServerStr = remoteAddress.split(",");

            for (int i = 0; i < remoteServerStr.length; i++) {
                arrayList.add(remoteServerStr[i].split(":"));
            }

        }
        return arrayList;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }


    public String getLocalPort() {
        return localPort;
    }

    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
