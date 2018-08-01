package com.rtucloud.cs.proxy.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * 后端编码器.
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FrontendEncode extends MessageToByteEncoder<byte[]> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FrontendEncode.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
		LOGGER.info(String.format("发送出的报文:[%s]", ByteBufUtil.hexDump((byte[]) msg)));
		out.writeBytes(msg);
	}
}
