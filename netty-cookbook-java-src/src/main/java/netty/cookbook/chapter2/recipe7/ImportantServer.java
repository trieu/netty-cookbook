package netty.cookbook.chapter2.recipe7;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import netty.cookbook.common.tcp.BootstrapTemplate;

/**
 * @author trieu
 * 
 * demo for HeartBeat monitor
 *
 */
public class ImportantServer {
	private static final int PORT = 8080;
	public static void main(String[] args) throws Exception {
		EventLoopGroup loopGroup = new NioEventLoopGroup();
		try {			
			BootstrapTemplate.newBootstrapUDP(loopGroup, new HeartBeatHandler(), PORT)
			.sync().channel().closeFuture().await();
		} finally {
			loopGroup.shutdownGracefully();
		}
	}
}