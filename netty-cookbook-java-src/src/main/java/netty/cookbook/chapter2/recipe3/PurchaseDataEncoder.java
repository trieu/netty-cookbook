package netty.cookbook.chapter2.recipe3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.Serializable;

import netty.cookbook.chapter2.recipe6.NettyMonitorIO;

public class PurchaseDataEncoder extends ObjectEncoder {
	@Override
	protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf buf) throws Exception {		
		super.encode(ctx, msg, buf);
		NettyMonitorIO.updateDataOut(buf);
	}
}
