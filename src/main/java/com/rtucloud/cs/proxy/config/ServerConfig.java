package com.rtucloud.cs.proxy.config;

import com.rtucloud.cs.proxy.server.Proxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    @Bean(initMethod = "initFrontend")
    protected Proxy createProxyServer(){
        return new Proxy();
    }






}
