package chapter1.recipe4;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Handler implementation for the TCP server.
 */
@Sharable
public class TcpServerOutboundHandler extends ChannelOutboundHandlerAdapter {	
	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {	
		super.flush(ctx);	
		ctx.close().addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future)
					throws Exception {
				System.out.println("close connection: "+future.isSuccess());
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
