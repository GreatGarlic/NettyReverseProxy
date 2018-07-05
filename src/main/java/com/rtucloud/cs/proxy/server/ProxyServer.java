package com.rtucloud.cs.proxy.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Future;

@Component
public class ProxyServer {

    @Autowired
    ExecutorsServer executorsServer;

    private volatile Future future = null;

    @PostConstruct
    public synchronized void startProxyServer() {
        if (future == null || future.isDone()) {
            future = executorsServer.initProxyServer();
        }
    }

    public synchronized void stopProxyServer() {
        if (future != null && (!future.isDone())) {
            future.cancel(true);
        }
    }
}