package chapter3.recipe4;

import static io.netty.handler.codec.http.HttpMethod.DELETE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpMethod.PUT;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import netty.cookbook.common.NettyServerUtil;

public class NettyHttpServerWithCORS {

	public static void main(String[] args) {
		String ip = "127.0.0.1";
		int port = 8080;
		ChannelInitializer<SocketChannel> channelInit = new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				
				CorsConfig corsConfig = CorsConfig.withAnyOrigin()
						.allowedRequestHeaders("content-type","accept","MyCustomHeader")
						.allowedRequestMethods(PUT,POST,GET,DELETE)
						.build();
				
				p.addLast(new HttpResponseEncoder());
				p.addLast(new HttpRequestDecoder());
				p.addLast(new HttpObjectAggregator(65536));
				p.addLast(new ChunkedWriteHandler());
				p.addLast(new CorsHandler(corsConfig));
				p.addLast(new SimpleCORSHandler());
			}
		};
		NettyServerUtil.newHttpServerBootstrap(ip, port, channelInit);
	}
}
