package netty.cookbook.chapter2.recipe1;

import netty.cookbook.common.NettyMonitorIO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

public class PurchaseDataDecoder extends ObjectDecoder {
	public PurchaseDataDecoder() {
		super(ClassResolvers.weakCachingConcurrentResolver(null));		
	}
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buf)
			throws Exception {
		Object object = super.decode(ctx, buf);
		NettyMonitorIO.updateDataIn(buf);
		return object;
	}
}