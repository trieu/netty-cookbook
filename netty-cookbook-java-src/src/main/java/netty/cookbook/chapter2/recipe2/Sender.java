package netty.cookbook.chapter2.recipe2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.cookbook.common.BootstrapTemplate;

public class Sender {
	static final int PORT = 8007;
	static final String HOST = "127.0.0.1";
	
	public static void main(String[] args) {    	
    	ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new ChannelInboundHandlerAdapter() {
					@Override
					public void channelActive(ChannelHandlerContext ctx)
							throws Exception {
						//send
						ctx.writeAndFlush(1);
					}
					@Override
					public void channelRead(ChannelHandlerContext ctx,
							Object data) throws Exception {
						//receive
						System.out.println("got " + data);
					}
				});
			}
		};
		BootstrapTemplate.newClientBootstrap(HOST, PORT, initializer );
	}
}
