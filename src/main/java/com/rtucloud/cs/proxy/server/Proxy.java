package com.rtucloud.cs.proxy.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;



public class Proxy {

	private static final Logger LOGGER = LoggerFactory.getLogger(Proxy.class);

	@Autowired
	public AppConfig appConfig;

	FontendPipeline fontendPipeline;

	public void initFontend() {
		
		LOGGER.debug("启动服务，端口:"+appConfig.getLocalPort());

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(fontendPipeline)
			.childOption(ChannelOption.AUTO_READ, false);
			
			ChannelFuture f = b.bind(Integer.valueOf(appConfig.getLocalPort())).sync();
			
			f.channel().closeFuture().sync();
			
		} catch (Exception e) {

		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void setFontendPipeline(FontendPipeline fontendPipeline) {
		this.fontendPipeline = fontendPipeline;
	}
}