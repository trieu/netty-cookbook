package netty.cookbook.chapter2.recipe3;

import netty.cookbook.chapter2.recipe6.NettyMonitorIO;
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