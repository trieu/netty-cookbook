package chapter3.recipe6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import netty.cookbook.common.http.ContentTypePool;
import netty.cookbook.common.http.HttpEventHandler;
import chapter3.recipe6.JavaScriptProcessor.SimpleHttpRequest;
import chapter3.recipe6.JavaScriptProcessor.SimpleHttpResponse;

@Sharable
public class HttpJsChannelHandler extends SimpleChannelInboundHandler<Object> {
	JavaScriptProcessor processor;
	public HttpJsChannelHandler() {
		try {
			processor =  new JavaScriptProcessor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;

			String uri = request.getUri();
			QueryStringDecoder decoder = new QueryStringDecoder(uri);
			String path = decoder.path();
			//System.out.println("path "+path);
			SimpleHttpResponse resp =  processor.process(new SimpleHttpRequest(path));
			String data = resp.getData();
			HttpResponse response = HttpEventHandler.buildHttpResponse(data, 200, ContentTypePool.TEXT_UTF8);			
			 
			ctx.write(response);
			ctx.flush().close();
		}
	}
	
}