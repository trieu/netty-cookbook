package netty.cookbook.chapter2.recipe2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.cookbook.common.BootstrapTemplate;

public class Receiver {
	static final int PORT = 8007;
	static final String HOST = "127.0.0.1";
	public static void main(String[] args) throws Exception {
		ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new StringEncoder());
				p.addLast(new StringDecoder());
				p.addLast(new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						System.out.println(msg);
						ctx.close();
					}
				});
			}
		};
		BootstrapTemplate.newServerBootstrap(HOST, PORT, initializer);
	}
}
