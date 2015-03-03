package chapter3.recipe3;

import netty.cookbook.common.NettyServerUtil;

public class HttpFileServer {

	public static void main(String[] args) throws Exception {
		int port = 8080;
		String ip = "127.0.01";
		NettyServerUtil.newHttpServerBootstrap(ip, port, new HttpStaticFileServerHandler() );
	}
}
