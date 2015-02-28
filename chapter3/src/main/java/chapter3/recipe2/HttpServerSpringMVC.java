package chapter3.recipe2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpServerSpringMVC {
	private final String host;
	private final int port;	

	public HttpServerSpringMVC(String host,int port) {
		this.host = host;
		this.port = port;
	}

	public void run() throws Exception {
		ServerBootstrap server = new ServerBootstrap();
		NioEventLoopGroup pGroup = new NioEventLoopGroup();
		NioEventLoopGroup cGroup = new NioEventLoopGroup();
		try {
			server.group(pGroup, cGroup)
					.channel(NioServerSocketChannel.class)					
					.childHandler(new DispatcherServletChannelInitializer(WebConfig.class));
			server.bind(host, port).sync().channel().closeFuture().sync();
		}
		finally {
			cGroup.shutdownGracefully();
			pGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		String host;
		int port;
		if (args.length > 0) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			host = "127.0.0.1";
			port = 8080;
		}
		new HttpServerSpringMVC(host, port).run();
	}
}
