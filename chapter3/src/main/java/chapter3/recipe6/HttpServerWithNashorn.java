package chapter3.recipe6;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import netty.cookbook.common.BootstrapTemplate;


public class HttpServerWithNashorn {
	
	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		int port = 8080;
		
		ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {			
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast("decoder", new HttpRequestDecoder());
				p.addLast("aggregator", new HttpObjectAggregator(65536));		
				p.addLast("encoder", new HttpResponseEncoder());
				p.addLast("chunkedWriter", new ChunkedWriteHandler());		        
				p.addLast("handler", new HttpJsChannelHandler());
			}
		};
		BootstrapTemplate.newHttpServerBootstrap(ip, port, channelInitializer );
	}
}
