package com.rtucloud.cs.proxy.server;

import com.rtucloud.cs.proxy.codec.BackendDecode;
import com.rtucloud.cs.proxy.codec.BackendEncode;
import com.rtucloud.cs.proxy.handler.ProxyBackendHandler;
import com.rtucloud.cs.proxy.handler.ProxyFrontendHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class BackendPipeline extends ChannelInitializer<SocketChannel> {

	private Channel inboundChannel;
	private ProxyFrontendHandler proxyFrontendHandler;
	private String host;
	private int port;

	public BackendPipeline(Channel inboundChannel,ProxyFrontendHandler proxyFrontendHandler,String host,int port) {
		this.inboundChannel = inboundChannel;
		this.proxyFrontendHandler=proxyFrontendHandler;
		this.host=host;
		this.port=port;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		// 注册handler
		pipeline.addLast(new BackendDecode());
		pipeline.addLast(new BackendEncode());
		pipeline.addLast(new ProxyBackendHandler(inboundChannel,proxyFrontendHandler,host,port));

	}
}
