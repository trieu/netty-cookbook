package netty.cookbook.chapter2.recipe1;

import java.util.List;

import netty.cookbook.common.PurchaseData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class PurchaseDataEncoder extends MessageToMessageEncoder<PurchaseData> {

	@Override
	protected void encode(ChannelHandlerContext ctx, PurchaseData msg,
			List<Object> out) throws Exception {
		System.out.println("encode "+msg);
		out.add(msg);
	}

}
