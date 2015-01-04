package chapter2.recipe2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.cookbook.common.BootstrapTemplate;

public class Sender {
	static final int PORT = 8007;
	static final String HOST = "127.0.0.1";
	
	public static void main(String[] args) throws InterruptedException {    
		final String msg = "This is a long message";
    	ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new StringEncoder());
				p.addLast(new StringDecoder());
				p.addLast(new ChannelInboundHandlerAdapter() {
					@Override
					public void channelActive(ChannelHandlerContext ctx)
							throws Exception {
						//on ready to send
						ctx.writeAndFlush(msg);
					}
					@Override
					public void channelRead(ChannelHandlerContext ctx,
							Object data) throws Exception {
						//on receive
						System.out.println("got " + data);
					}
				});
			}
		};
		BootstrapTemplate.newClientBootstrap(HOST, PORT, initializer );
		Thread.sleep(5000);
	}
}
