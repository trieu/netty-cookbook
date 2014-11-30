package netty.cookbook.chapter2.recipe1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.Serializable;

public class PurchaseDataEncoder extends ObjectEncoder {
	@Override
	protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf buf) throws Exception {		
		super.encode(ctx, msg, buf);
		System.out.println("encode bytes "+buf.readableBytes());
	}
}
