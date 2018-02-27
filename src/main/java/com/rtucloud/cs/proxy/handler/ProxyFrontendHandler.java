package com.rtucloud.cs.proxy.handler;

import com.rtucloud.cs.proxy.server.AppConfig;
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
import org.springframework.context.ApplicationContext;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProxyFrontendHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger log = LoggerFactory.getLogger(ProxyFrontendHandler.class);

    // 代理服务器和目标服务器之间的通道（从代理服务器出去所以是outbound过境）
    public ApplicationContext applicationContext;
    private volatile ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private volatile Channel inboundChannel = null;
    private AppConfig appConfig;
    private volatile boolean frontendConnectStatus = false;

    public ProxyFrontendHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.appConfig = applicationContext.getBean(AppConfig.class);
    }

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

        List<String[]> remoteAddress = appConfig.getRemoteAddress();

        /**
         * 客户端和代理服务器的连接通道 入境的通道
         */

        inboundChannel = ctx.channel();

        for (String[] str : remoteAddress) {
            createBootstrap(inboundChannel, str[0].trim(), Integer.valueOf(str[1].trim()));
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

//                if (future.isSuccess()) {
//
//                } else {
//                    ctx.channel().close();
//                }
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
        closeOnFlush(ctx.channel());
    }

    public void createBootstrap(Channel inboundChannel, String host, int port) {
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(inboundChannel.eventLoop());
            bootstrap.channel(NioSocketChannel.class);

            bootstrap.handler(new BackendPipeline(inboundChannel, ProxyFrontendHandler.this, host, port));

//            InetAddress ipAddress=  InetAddress.getByAddress(new byte[]{(byte)192,(byte)168,(byte)141,(byte)222});
//
//            InetAddress  remoteIpAddress=InetAddress.getByAddress(new byte[]{(byte)114,(byte)55,(byte)231,(byte)160});
//
//            InetSocketAddress localAddress= new InetSocketAddress(ipAddress,55555);
//            InetSocketAddress remoteAddress= new InetSocketAddress(remoteIpAddress, port);

            ChannelFuture f = bootstrap.connect(host, port);
//            ChannelFuture f = bootstrap.connect(remoteAddress, localAddress);

            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        allChannels.add(future.channel());
                    } else {
                        if (inboundChannel.isActive()) {
                            System.out.println("Reconnect");
                            final EventLoop loop = future.channel().eventLoop();
                            loop.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    ProxyFrontendHandler.this.createBootstrap(inboundChannel, host, port);
                                }
                            }, appConfig.getInterval(), TimeUnit.MILLISECONDS);
                        } else {
                            System.out.println("notActive");
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