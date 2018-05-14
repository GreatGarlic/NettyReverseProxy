package com.rtucloud.cs.proxy.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProxyBackendHandler extends SimpleChannelInboundHandler<byte[]> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyBackendHandler.class);

	private Channel inboundChannel;
	private ProxyFrontendHandler proxyFrontendHandler;
	private String host;
	private int port;

	public ProxyBackendHandler(Channel inboundChannel, ProxyFrontendHandler proxyFrontendHandler, String host,
			int port) {
		this.inboundChannel = inboundChannel;
		this.proxyFrontendHandler = proxyFrontendHandler;
		this.host = host;
		this.port = port;
	}

	// 当和目标服务器的通道连接建立时
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("服务器地址：" + ctx.channel().remoteAddress());
	}

	/**
	 * msg是从目标服务器返回的消息
	 *
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, byte[] msg) throws Exception {
		LOGGER.info("服务器返回消息");
		/**
		 * 接收目标服务器发送来的数据并打印 然后把数据写入代理服务器和客户端的通道里
		 */
		// 通过inboundChannel向客户端写入数据
		inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture future) throws Exception {
				if (!future.isSuccess())
					future.channel().close();

			}
		});
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("关闭服务器连接");
		if (proxyFrontendHandler.isConnect()) {
			proxyFrontendHandler.createBootstrap(inboundChannel, host, port);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("发生异常：", cause);
		ctx.channel().close();
	}
}