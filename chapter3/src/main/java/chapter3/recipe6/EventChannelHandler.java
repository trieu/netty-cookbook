package chapter3.recipe6;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

import netty.cookbook.common.http.ContentTypePool;
import netty.cookbook.common.http.HttpEventHandler;

@Sharable
public class EventChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	final EventHandler eventHandler;
	public EventChannelHandler(EventHandler handler) {
		this.eventHandler = handler;
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		ByteBuf content = Unpooled.copiedBuffer(
				"Failure: " + status.toString() + "\r\n",
				CharsetUtil.UTF_8);
		FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
				HTTP_1_1,
				status,
				content
		);
		fullHttpResponse.headers().add(CONTENT_TYPE, "text/plain; charset=UTF-8");

		// Close the connection as soon as the error message is sent.
		ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.getDecoderResult().isSuccess()) {
			sendError(ctx, BAD_REQUEST);
			return;
		}
					
		String uri = request.getUri();
		QueryStringDecoder decoder = new QueryStringDecoder(uri);			
		SimpleHttpRequest req = new SimpleHttpRequest(decoder.path(), decoder.parameters());
					
		String cookieString = request.headers().get(HttpHeaders.Names.COOKIE);
		if (cookieString != null) {
			Set<Cookie> cookies = CookieDecoder.decode(cookieString);
			req.setCookies(cookies);
		} else {
			req.setCookies(Collections.emptySet());
		}
		req.setHeaders(request.headers());
		copyHttpBodyData(request, req);
		
		//System.out.println("path "+path);
		SimpleHttpResponse resp =  eventHandler.apply(req);
		String data = resp.getData();
		HttpResponse response = HttpEventHandler.buildHttpResponse(data, 200, ContentTypePool.TEXT_UTF8);			
		 
		ctx.write(response);
		ctx.flush().close();
		
	}
	
	void copyHttpBodyData(FullHttpRequest fullHttpReq, SimpleHttpRequest req){
		ByteBuf bbContent = fullHttpReq.content();
		if(bbContent.hasArray()) {				
			req.setBody(bbContent.array());
		} else {			
			if(fullHttpReq.getMethod().equals(HttpMethod.POST)){				
				String bbContentStr = bbContent.toString(Charset.forName("UTF-8"));
				req.setBody(bbContentStr.getBytes());
			}			
		}	
	}
}