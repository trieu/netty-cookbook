package netty.cookbook.chapter2.recipe11;

import io.netty.channel.ChannelHandler;
import netty.cookbook.common.tcp.NettyBootstrapUtil;

public class HttpDownloader {

	public static void main(String[] args) throws Exception {
		String url =  "http://www.mc2ads.com/";
		ChannelHandler handler = new HttpWebClientHandler();
		NettyBootstrapUtil.newHttpClientBootstrap(url, handler );
	}
}
