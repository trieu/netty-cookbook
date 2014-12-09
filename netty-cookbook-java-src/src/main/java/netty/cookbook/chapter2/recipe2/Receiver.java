package netty.cookbook.chapter2.recipe2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.cookbook.common.BootstrapTemplate;

public class Receiver {
	static final int PORT = 8007;
	static final String HOST = "127.0.0.1";

	public static void main(String[] args) throws Exception {
		ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();			
				p.addLast(new ChannelInboundHandlerAdapter() {
					private ByteBuf buf;
				    @Override
				    public void handlerAdded(ChannelHandlerContext ctx) {
				        buf = ctx.alloc().buffer(4); // (1)
				    }
				    @Override
				    public void handlerRemoved(ChannelHandlerContext ctx) {
				        buf.release(); // (1)
				        buf = null;
				    }
					@Override
					public void channelRead(ChannelHandlerContext ctx,
							Object data) throws Exception {
						System.out.println("processed " + data);						
						ctx.writeAndFlush(2);
					}
				});
			}
		};
		BootstrapTemplate.newServerBootstrap(HOST, PORT, initializer);
	}
}
