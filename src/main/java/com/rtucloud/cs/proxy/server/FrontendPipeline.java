package com.rtucloud.cs.proxy.server;

import com.rtucloud.cs.proxy.codec.FrontendDecode;
import com.rtucloud.cs.proxy.codec.FrontendEncode;
import com.rtucloud.cs.proxy.handler.ProxyFrontendHandler;
import com.rtucloud.cs.proxy.utils.ApplicationContextUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("frontendPipeline")
public class FrontendPipeline extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 注册handler
        pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, 1,
                TimeUnit.MINUTES));
        pipeline.addLast(ApplicationContextUtil.getBean(FrontendDecode.class));
        pipeline.addLast(ApplicationContextUtil.getBean(FrontendEncode.class));
        pipeline.addLast(ApplicationContextUtil.getBean(ProxyFrontendHandler.class));

    }
}
