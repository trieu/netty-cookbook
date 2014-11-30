package netty.cookbook.chapter2.recipe7;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Date;

public class LogClient {
	static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_BROADCAST, true)
					.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
						@Override
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
							cause.printStackTrace();
							ctx.close();
						}
						@Override
						protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
								throws Exception {
							String response = msg.content().toString(CharsetUtil.UTF_8);
							System.out.println("LogClient: " + response);
							ctx.close();
						}

					});

			Channel ch = b.bind(0).sync().channel();

			// Broadcast the QOTM request to port 8080
			InetSocketAddress udpAddr =  new InetSocketAddress("255.255.255.255", PORT);
			for (int i=0;i<5;i++) {
				ByteBuf buf = Unpooled.copiedBuffer("logged at " + new Date(),CharsetUtil.UTF_8);
				ch.write(new DatagramPacket(buf,udpAddr));
				Thread.sleep(1000);
			}
			ch.flush().closeFuture().sync();

			//If the channel is not closed within 5 seconds,
			// print an error message and quit.
			if (!ch.closeFuture().await(5000)) {
				System.err.println("QOTM request timed out.");
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
