package com.rtucloud.cs.proxy.server;

import io.netty.util.AttributeKey;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rtucloud.cs.proxy.handler.ProxyFrontendHandler;

public class StartProgram {
	

	public static final AttributeKey<ProxyFrontendHandler> ProxyFrontendReference = AttributeKey.valueOf("ProxyFrontendReference");

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("ApplicationContext.xml");
	}
}
