package com.rtucloud.cs.proxy.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.rtucloud.cs.proxy.codec.FontendDecode;
import com.rtucloud.cs.proxy.codec.FontendEncode;
import com.rtucloud.cs.proxy.handler.ProxyFrontendHandler;

public class FontendPipeline extends ChannelInitializer<SocketChannel> {

	public FontendPipeline() {
	}

	@Autowired
	ApplicationContext applicationContext;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		// 注册handler
		pipeline.addLast(new FontendDecode());
		pipeline.addLast(new FontendEncode());
		pipeline.addLast(new ProxyFrontendHandler(applicationContext));
		
	}
}
