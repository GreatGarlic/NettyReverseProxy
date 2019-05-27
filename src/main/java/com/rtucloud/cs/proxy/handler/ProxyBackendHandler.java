package com.rtucloud.cs.proxy.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        LOGGER.info("目标服务器地址：" + ctx.channel().remoteAddress());
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
        //接收目标服务器发送来的数据并打印 然后把数据写入代理服务器和客户端的通道里
        //通过inboundChannel向客户端写入数据
        inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    future.channel().close();
                }
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("目标服务器关闭连接");
        if (proxyFrontendHandler.isConnect()) {
            proxyFrontendHandler.createBootstrap(inboundChannel, host, port);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("发生异常：", cause);
        ctx.channel().close();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.ALL_IDLE) {
                if (proxyFrontendHandler.isConnect()) {
                    return;
                }
                LOGGER.debug("空闲时间到，关闭连接.");
                ctx.channel().close();
            }
        }
    }
}