package com.rtucloud.cs.proxy.server;

import com.rtucloud.cs.proxy.config.AppConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

@Component
public class ExecutorsServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorsServer.class);

    @Autowired
    FrontendPipeline frontendPipeline;
    @Autowired
    AppConfig appConfig;

    @Async("frontendWorkTaskExecutor")
    public Future<String> taskOne() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(frontendPipeline)
                    .childOption(ChannelOption.AUTO_READ, false);
            ChannelFuture f = b.bind(0).sync();
            LOGGER.debug("1端口" + ((InetSocketAddress) f.channel().localAddress()).getPort());
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("1代理服务器启动失败", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        return new AsyncResult<String>("hello world !!!!");
    }

}
