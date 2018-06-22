package com.rtucloud.cs.proxy.server;

import com.rtucloud.cs.proxy.handler.ProxyFrontendHandler;
import io.netty.util.AttributeKey;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

public class StartProgram {


    public static final AttributeKey<ProxyFrontendHandler> ProxyFrontendReference = AttributeKey.valueOf("ProxyFrontendReference");

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("ApplicationContext.xml");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
