package chapter3.recipe6;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import netty.cookbook.common.BootstrapTemplate;
import chapter3.recipe6.functions.Decorator;
import chapter3.recipe6.functions.Filter;
import chapter3.recipe6.functions.FinalProcessor;


public class LambdaHttpServer {
	
	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		int port = 8080;
		
		Functions functions = new Functions();
		
		Filter filterAccessAdmin = req -> {
			req.setNotAuthorized(req.getUri().contains("admin"));
			return req;			
		};		
		FinalProcessor logicFunction = req -> {
			SimpleHttpResponse resp = new SimpleHttpResponse();
			if(req.getUri().contains("edit/123")){
				resp.setData("this is the data for 123");
			} 
			else if(req.getParameters().containsKey("version")){
				resp.setData("the value of version : " + req.getParameters().get("version"));
			}
			return resp;
		};
		Decorator formatingResult = resp -> {
			resp.setData("(" + resp.getData() + ")");
			return resp;
		};
		functions.apply(filterAccessAdmin).apply(logicFunction).apply(formatingResult);
		
		ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {			
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast("decoder", new HttpRequestDecoder());
				p.addLast("aggregator", new HttpObjectAggregator(65536));		
				p.addLast("encoder", new HttpResponseEncoder());
				p.addLast("chunkedWriter", new ChunkedWriteHandler());		        
				p.addLast("handler", new FunctionsChannelHandler(functions));
			}
		};
		BootstrapTemplate.newHttpServerBootstrap(ip, port, channelInitializer );
	}
}
