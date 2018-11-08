package com.rtucloud.cs.proxy.handler;


import com.rtucloud.cs.proxy.config.AppConfig;
import com.rtucloud.cs.proxy.dao.entity.BackendServerInfo;
import com.rtucloud.cs.proxy.dao.repository.BackendServerRepository;
import com.rtucloud.cs.proxy.server.BackendPipeline;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProxyFrontendHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger log = LoggerFactory.getLogger(ProxyFrontendHandler.class);

    // 代理服务器和目标服务器之间的通道（从代理服务器出去所以是outbound过境）
    private volatile ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private BackendServerRepository backendServerRepository;
    private volatile boolean frontendConnectStatus = false;


    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    public static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 当客户端和代理服务器建立通道连接时，调用此方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        frontendConnectStatus = true;

        SocketAddress clientAddress = ctx.channel().remoteAddress();
        log.info("客户端地址：" + clientAddress);

        List<BackendServerInfo> backendServerInfoList = backendServerRepository.selectList(null);

        /**
         * 客户端和代理服务器的连接通道 入境的通道
         */
        Channel inboundChannel = ctx.channel();

        for (BackendServerInfo backendServerInfo : backendServerInfoList) {
            createBootstrap(inboundChannel, backendServerInfo.getIp(), backendServerInfo.getPort());
        }
    }

    /**
     * 在这里接收客户端的消息 在客户端和代理服务器建立连接时，也获得了代理服务器和目标服务器的通道outbound，
     * 通过outbound写入消息到目标服务器
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(final ChannelHandlerContext ctx, byte[] msg) throws Exception {

        log.info("客户端消息");
        allChannels.writeAndFlush(msg).addListener(new ChannelGroupFutureListener() {
            @Override
            public void operationComplete(ChannelGroupFuture future) throws Exception {
                //防止出现发送不成功造成的永久不读取消息的错误.
                ctx.channel().read();
            }
        });

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("代理服务器和客户端断开连接");
        frontendConnectStatus = false;
        allChannels.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生异常：", cause);
        ctx.channel().close();
    }

    public void createBootstrap(final Channel inboundChannel, final String host, final int port) {
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(inboundChannel.eventLoop());
            bootstrap.channel(NioSocketChannel.class);

            bootstrap.handler(new BackendPipeline(inboundChannel, ProxyFrontendHandler.this, host, port));


            ChannelFuture f = bootstrap.connect(host, port);

            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        allChannels.add(future.channel());
                    } else {
                        if (inboundChannel.isActive()) {
                            log.info("Reconnect");
                            final EventLoop loop = future.channel().eventLoop();
                            loop.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    ProxyFrontendHandler.this.createBootstrap(inboundChannel, host, port);
                                }
                            }, appConfig.getInterval(), TimeUnit.MILLISECONDS);
                        } else {
                            log.info("notActive");
                        }
                    }
                    inboundChannel.read();
                }
            });

        } catch (Exception e) {

        }
    }

    public boolean isConnect() {
        return frontendConnectStatus;
    }

}