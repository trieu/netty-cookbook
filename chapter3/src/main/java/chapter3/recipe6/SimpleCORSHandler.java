package chapter3.recipe6;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import netty.cookbook.common.FileUtils;
import netty.cookbook.common.http.ContentTypePool;
import netty.cookbook.common.http.HttpEventHandler;

import com.google.gson.Gson;

public class SimpleCORSHandler  extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        
        String uri = req.getUri();
        if(uri.contains("cors.html")){
        	String data = "";
        	try {
				data = FileUtils.readFileAsString("./src/main/resources/templates/cors.html");				
			} catch (IOException e) {
				e.printStackTrace();
			} 
        	HttpResponse response = HttpEventHandler.buildHttpResponse(data  , 200, ContentTypePool.HTML_UTF8);
        	ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else if(uri.contains("/cors/")){
        	Map<String, Object> jsonData = new HashMap<>();
        	
        	jsonData.put("status", req.getMethod().name() + " OK");
        	jsonData.put("uri", req.getUri());
        	jsonData.put("data", req.content().toString(Charset.forName("UTF-8")));
        	        	
        	String data = new Gson().toJson(jsonData);        	
        	System.out.println(data);        	
			final FullHttpResponse response = HttpEventHandler.buildFullHttpResponse(data, 200, ContentTypePool.HTML_UTF8);
            response.headers().set("custom-response-header", "Some value");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
        	HttpResponse response = HttpEventHandler.buildHttpResponse("", 200, ContentTypePool.TEXT_UTF8);
        	ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
        
    }
}
