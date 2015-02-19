package chapter3.recipe2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.cookbook.common.springmvc.DispatcherServletChannelInitializer;

public class NettyServerSpringMVC {
	private final int port;

	public NettyServerSpringMVC(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		ServerBootstrap server = new ServerBootstrap();
		NioEventLoopGroup pGroup = new NioEventLoopGroup();
		NioEventLoopGroup cGroup = new NioEventLoopGroup();
		try {
			server.group(pGroup, cGroup)
					.channel(NioServerSocketChannel.class)
					.localAddress(port)
					.childHandler(new DispatcherServletChannelInitializer(WebConfig.class));
			server.bind().sync().channel().closeFuture().sync();
		}
		finally {
			cGroup.shutdownGracefully();
			pGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new NettyServerSpringMVC(port).run();
	}
}
