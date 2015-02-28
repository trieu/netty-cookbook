package chapter3.recipe1;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import netty.cookbook.common.BootstrapTemplate;
import netty.cookbook.common.http.BasicHttpResponseHandler;
import netty.cookbook.common.http.HttpEventHandler;


public class HttpServerWithRouter {
	
	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		int port = 8080;
	
		Map<String, HttpEventHandler> routes = new LinkedHashMap<>();		
		routes.put("startsWith:/hello", (HttpRequest req, QueryStringDecoder q) -> {
			String s = "Hello " ;//+ q.parameters().getOrDefault("name", Arrays.asList("guest")).get(0);
    		return HttpEventHandler.response(s, 200);
    	});
		routes.put("endsWith:/date", (HttpRequest req, QueryStringDecoder q) -> {
    		System.out.println(req.headers().get(Names.COOKIE));
    		return HttpEventHandler.response(new Date(), 200);
    	});
		routes.put("equals:/about", new BasicHttpResponseHandler("This is a Http Netty Server", 200));
		
		HttpEventRoutingHandler routerHandler = new HttpEventRoutingHandler(routes);
		ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {			
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
		        p.addLast("decoder", new HttpRequestDecoder());
		        p.addLast("encoder", new HttpResponseEncoder());		        
				p.addLast(routerHandler);
			}
		};
		BootstrapTemplate.newHttpServerBootstrap(ip, port, channelInitializer );
	}
}
