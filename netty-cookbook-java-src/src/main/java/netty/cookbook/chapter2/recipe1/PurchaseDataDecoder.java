package netty.cookbook.chapter2.recipe1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import netty.cookbook.common.PurchaseData;

public class PurchaseDataDecoder extends MessageToMessageDecoder<PurchaseData> {

	@Override
	protected void decode(ChannelHandlerContext ctx, PurchaseData msg,
			List<Object> out) throws Exception {
		System.out.println("decode "+msg);
		out.add(msg);
	}


}
