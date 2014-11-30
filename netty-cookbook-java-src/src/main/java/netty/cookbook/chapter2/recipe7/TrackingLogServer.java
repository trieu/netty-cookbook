package netty.cookbook.chapter2.recipe7;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class TrackingLogServer {
	private static final int PORT = 8080;
	public static void main(String[] args) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_BROADCAST, true)
					.handler(new TrackingLogHandler());
			b.bind(PORT).sync().channel().closeFuture().await();
		} finally {
			group.shutdownGracefully();
		}
	}
}
