package chapter3.recipe2;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.CharsetUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;


@Sharable
public class ServletNettyChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final String UTF_8 = "UTF-8";

	private final Servlet servlet;

	private final ServletContext servletContext;

	public ServletNettyChannelHandler(Servlet servlet) {
		this.servlet = servlet;
		this.servletContext = servlet.getServletConfig().getServletContext();
	}

	private MockHttpServletRequest createHttpServletRequest(FullHttpRequest fullHttpReq) {
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(fullHttpReq.getUri()).build();

		MockHttpServletRequest servletRequest = new MockHttpServletRequest(this.servletContext);
		servletRequest.setRequestURI(uriComponents.getPath());
		servletRequest.setPathInfo(uriComponents.getPath());
		servletRequest.setMethod(fullHttpReq.getMethod().name());
		servletRequest.setCharacterEncoding(UTF_8);

		if (uriComponents.getScheme() != null) {
			servletRequest.setScheme(uriComponents.getScheme());
		}
		if (uriComponents.getHost() != null) {
			servletRequest.setServerName(uriComponents.getHost());
		}
		if (uriComponents.getPort() != -1) {
			servletRequest.setServerPort(uriComponents.getPort());
		}
		HttpHeaders headers = fullHttpReq.headers();
		for (String name : headers.names()) {
			servletRequest.addHeader(name, headers.get(name));
		}
		servletRequest.setContentType(headers.get(HttpHeaders.Names.CONTENT_TYPE));
		
		ByteBuf bbContent = fullHttpReq.content();	
		
		if(bbContent.hasArray()) {				
			servletRequest.setContent(bbContent.array());
		} else {			
			if(fullHttpReq.getMethod().equals(HttpMethod.POST)){
				HttpPostRequestDecoder decoderPostData  = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpReq);
				String bbContentStr = bbContent.toString(Charset.forName(UTF_8));
				servletRequest.setContent(bbContentStr.getBytes());
				if( ! decoderPostData.isMultipart() ){
					List<InterfaceHttpData> postDatas = decoderPostData.getBodyHttpDatas();
					for (InterfaceHttpData postData : postDatas) {
						if (postData.getHttpDataType() == HttpDataType.Attribute) {
							Attribute attribute = (Attribute) postData;
							try {											
								servletRequest.addParameter(attribute.getName(),attribute.getValue());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}	
				}
			}			
		}	
		
		//System.out.println(uriComponents.getPath() + " \n  => " + fullHttpReq.getMethod().name()+ " " + fullHttpReq.content().toString(Charset.defaultCharset()));

		try {
			if (uriComponents.getQuery() != null) {
				String query = UriUtils.decode(uriComponents.getQuery(), UTF_8);
				servletRequest.setQueryString(query);
			}

			for (Entry<String, List<String>> entry : uriComponents.getQueryParams().entrySet()) {
				for (String value : entry.getValue()) {
					servletRequest.addParameter(
							UriUtils.decode(entry.getKey(), UTF_8),
							UriUtils.decode(value, UTF_8));
				}
			}
		}
		catch (UnsupportedEncodingException ex) {
			// shouldn't happen
		}
		return servletRequest;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		System.err.println(cause.getMessage());
		if (ctx.channel().isActive()) {
			sendError(ctx, INTERNAL_SERVER_ERROR);
		}
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
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
		if (!fullHttpRequest.getDecoderResult().isSuccess()) {
			sendError(channelHandlerContext, BAD_REQUEST);
			return;
		}

		MockHttpServletRequest servletRequest = createHttpServletRequest(fullHttpRequest);
		MockHttpServletResponse servletResponse = new MockHttpServletResponse();

		this.servlet.service(servletRequest, servletResponse);

		HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
		
		for (String name : servletResponse.getHeaderNames()) {
			for (String value : servletResponse.getHeaders(name)) {
				response.headers().add(name, value);
			}
		}

		// Write the initial line and the header.
		channelHandlerContext.write(response);
		InputStream contentStream = new ByteArrayInputStream(servletResponse.getContentAsByteArray());

		// Write the content and flush it.
		ChannelFuture writeFuture = channelHandlerContext.writeAndFlush(new ChunkedStream(contentStream));
		writeFuture.addListener(ChannelFutureListener.CLOSE);
	}
}