package com.rtucloud.cs.proxy.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.SocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 后端解码器.
 */
public class BackendDecode extends ByteToMessageDecoder {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendDecode.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// 防止不发报文就关闭连接出现的错误
		if (!in.isReadable()) {
			return;
		}
		LOGGER.info(String.format("[%d]收到数据:%s",  ByteBufUtil.hexDump(in)));
		byte[] ss = new byte[in.readableBytes()];

		in.readBytes(ss);

		out.add(ss);
	}
}
