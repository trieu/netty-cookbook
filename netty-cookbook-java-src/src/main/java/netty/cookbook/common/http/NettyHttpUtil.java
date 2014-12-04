package netty.cookbook.common.http;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.REFERER;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import netty.cookbook.common.StringPool;
import netty.cookbook.common.StringUtil;

import com.google.gson.Gson;

public class NettyHttpUtil {
	public static final String FAVICON_URI = "/favicon.ico";
	public static final String HEADER_REFERER_NAME = "Referer";
	public static final String HEADER_REFRESH_NAME = "Refresh";
	public static final String HEADER_LOCATION_NAME = "Location";	
	public static final String HEADER_CONNECTION_CLOSE = "Close";
	public static final String[] REFERER_SEARCH_LIST = new String[]{"\t%s","\t","%s","\r\n","\n","\r"};
	public static final String[] REFERER_REPLACE_LIST = new String[]{"","","","","",""};
		

	public static FullHttpResponse redirectPath(String uri)
			throws UnsupportedEncodingException {
		int i = uri.indexOf("/http");
		if (i > 0) {
			// String metaUri = uri.substring(0, i);
			// do something with metaUri, E.g:
			// /r/13083/142/zizgzlzmzqzlzizhzizrzoziznzhzozizgzjzrzgzozizizgzdzizlzhzdzizkzmzdzmzgzozjzm21zjzmzq1t1u1t20201v21zjzjzr

			String url = uri.substring(i + 1);
			// System.out.println(metaUri + " " + url) ;
			return redirect(URLDecoder.decode(url, StringPool.UTF_8));
		}
		return null;
	}
	
	public static FullHttpResponse redirect(String url) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,HttpResponseStatus.MOVED_PERMANENTLY);
		response.headers().set(HEADER_LOCATION_NAME, url);	
		response.headers().set(CONNECTION, HEADER_CONNECTION_CLOSE);
		return response;
	}	

	public static FullHttpResponse theHttpContent(String str) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(str.getBytes());
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK ,byteBuf);
		response.headers().set(CONTENT_TYPE, ContentTypePool.TEXT_UTF8);
		response.headers().set(CONTENT_LENGTH, byteBuf.readableBytes());
		response.headers().set(CONNECTION, HEADER_CONNECTION_CLOSE);
		return response;
	}
	
	public static FullHttpResponse theHttpContent(String str, String contentType) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(str.getBytes());	
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK ,byteBuf);
		response.headers().set(CONTENT_TYPE, contentType);
		response.headers().set(CONTENT_LENGTH, byteBuf.readableBytes());
		response.headers().set(CONNECTION, HEADER_CONNECTION_CLOSE);		
		return response;
	}
	
	public static FullHttpResponse theHttpContent(HttpOutputResource re, String contentType) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK , re.getByteBuf());
		response.headers().set(CONTENT_TYPE, contentType);
		response.headers().set(CONTENT_LENGTH, re.getLength());
		response.headers().set(CONNECTION, HEADER_CONNECTION_CLOSE);		
		//System.out.println("CONTENT_LENGTH:"+re.getLength());
		//System.out.println();
		return response;
	}
	
	public static FullHttpResponse theHttpContent(String str, HttpResponseStatus status) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(str.getBytes());
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status ,byteBuf);
		response.headers().set(CONTENT_TYPE, ContentTypePool.TEXT_UTF8);
		response.headers().set(CONTENT_LENGTH, byteBuf.readableBytes());
		response.headers().set(CONNECTION, HEADER_CONNECTION_CLOSE);
		return response;
	}

	public static String getParamValue(String name, Map<String, List<String>> params) {
		return getParamValue(name, params, StringPool.BLANK);
	}
	
	public static String getParamValue(String name, Map<String, List<String>> params, String defaultVal) {
		List<String> vals = params.get(name);
		if (vals != null) {
			if (vals.size()>0) {
				return vals.get(0);
			}
		}
		return defaultVal;
	}
	
	public static String getRemoteIP(ChannelHandlerContext ctx) {
		try {
			SocketAddress address = ctx.channel().remoteAddress();
			if(address instanceof InetSocketAddress){
				return ((InetSocketAddress)address).getAddress().getHostAddress();
			}
			return address.toString().split("/")[1].split(":")[0];
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return "0.0.0.0";
	}
	
	public static String getLocalIP(ChannelHandlerContext ctx) {
		try {
			SocketAddress address = ctx.channel().localAddress();
			if(address instanceof InetSocketAddress){
				return ((InetSocketAddress)address).getAddress().getHostAddress();
			}
			return address.toString().split("/")[1].split(":")[0];
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return "0.0.0.0";
	}
	
	static final String unknown = "unknown" ;
	//http://r.va.gg/2011/07/handling-x-forwarded-for-in-java-and-tomcat.html
	public static String getRemoteIP(ChannelHandlerContext ctx, HttpRequest request){
		String ipAddress = request.headers().get("X-Forwarded-For");		
		if ( ! StringUtil.isNullOrEmpty(ipAddress) && ! unknown.equalsIgnoreCase(ipAddress)) {			
			//LogUtil.dumpToFileIpLog(ipAddress);
			String[] toks = ipAddress.split(",");
			int len = toks.length;
			if(len > 1){
				ipAddress = toks[len-1];
			} else {				
				return ipAddress;
			}
		} else {		
			ipAddress = NettyHttpUtil.getRemoteIP(ctx);
		}		
		return ipAddress;
	}	
	
	public static boolean isBadLogRequest(String uri){
		if(StringUtil.isEmpty(uri)){
			return true;
		}
		int idx = uri.indexOf("?");
		if(idx < 0){
			return true;
		}
		String queryDetails = uri.substring(idx+1);
		if(StringUtil.isEmpty(queryDetails)){
			return true;
		}
		return false;
	}
	
	
	public static String responseAsJsonp(String callbackFunc, Map<String, Object> data){
		String jsonData = new Gson().toJson(data);
		return responseAsJsonp(callbackFunc, jsonData);
	}
	
	public static String responseAsJsonp(String callbackFunc, String jsonData){		
		if( StringUtil.isEmpty(callbackFunc) ){			
			return jsonData;
		} else {
			StringBuilder jsonp = new StringBuilder(callbackFunc);
			jsonp.append("(").append(jsonData).append(")");
			return jsonp.toString();
		}	
	}
	
	public static void sendHttpResponse(ChannelHandlerContext ctx,	FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			setContentLength(res, res.content().readableBytes());
		}
		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	public static void response1pxGifImage(ChannelHandlerContext ctx) {
		FullHttpResponse response = theBase64Image1pxGif();
	    ChannelFuture future = ctx.write(response);
	    ctx.flush();
	    ctx.close();			 
		//Close the non-keep-alive connection after the write operation is done.
		future.addListener(ChannelFutureListener.CLOSE);	     
	}
	
	public static String getRefererUrl(HttpHeaders headers){
		String refererUrl = headers.get(REFERER);
		if(StringUtil.isNotEmpty(refererUrl)){
			refererUrl = org.apache.commons.lang.StringUtils.replaceEach(refererUrl, REFERER_SEARCH_LIST,  REFERER_REPLACE_LIST);
		}
		return refererUrl;
	}
	
	static final byte[] BASE64GIF_BYTES = StringPool.BASE64_GIF_BLANK.getBytes();
	public static FullHttpResponse theBase64Image1pxGif() {
		ByteBuf byteBuf = Base64.decode(Unpooled.copiedBuffer(BASE64GIF_BYTES));
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK , byteBuf);
		response.headers().set(CONTENT_TYPE, ContentTypePool.GIF);
		response.headers().set(CONTENT_LENGTH, byteBuf.readableBytes());
		response.headers().set(CONNECTION, HEADER_CONNECTION_CLOSE);
		return response;
	}
}
