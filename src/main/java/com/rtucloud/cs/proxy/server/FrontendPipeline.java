package com.rtucloud.cs.proxy.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.rtucloud.cs.proxy.codec.FrontendDecode;
import com.rtucloud.cs.proxy.codec.FrontendEncode;
import com.rtucloud.cs.proxy.handler.ProxyFrontendHandler;

public class FrontendPipeline extends ChannelInitializer<SocketChannel> {

	public FrontendPipeline() {
	}

	@Autowired
	ApplicationContext applicationContext;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		// 注册handler
		pipeline.addLast(new FrontendDecode());
		pipeline.addLast(new FrontendEncode());
		pipeline.addLast(new ProxyFrontendHandler(applicationContext));
		
	}
}
