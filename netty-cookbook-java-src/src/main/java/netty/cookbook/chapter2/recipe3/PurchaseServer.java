package netty.cookbook.chapter2.recipe3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.cookbook.common.model.PurchaseData;
import netty.cookbook.common.tcp.BootstrapTemplate;

import org.apache.log4j.Logger;

public class PurchaseServer {
	final static Logger logger = Logger.getLogger(PurchaseServer.class);
	static final int PORT = Integer
			.parseInt(System.getProperty("port", "8007"));
	static final String HOST = "127.0.0.1";

	public static void main(String[] args) throws Exception {
		ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new PurchaseDataDecoder());
				p.addLast(new PurchaseDataEncoder());
				p.addLast(new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx,
							Object data) throws Exception {
						System.out.println("processed Purchase " + data);
						PurchaseData processed = new PurchaseData(data, true);
						ctx.writeAndFlush(processed);
					}
				});
			}
		};
		BootstrapTemplate.newServerBootstrap(HOST, PORT, initializer);
	}
}
