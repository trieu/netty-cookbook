package netty.cookbook.common.http;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@FunctionalInterface
public interface HttpEventHandler extends Serializable {

	public HttpResponse handle(HttpRequest req, QueryStringDecoder query) throws Exception;

	static final String HEADER_CONNECTION_CLOSE = "Close";

	public static HttpResponse buildHttpResponse(Object data, int status,
			String ctype) {
		ByteBuf byteBuf = Unpooled
				.copiedBuffer(String.valueOf(data).getBytes());
		HttpResponseStatus httpStatus = HttpResponseStatus.valueOf(status);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				httpStatus, byteBuf);
		response.headers().set(CONTENT_TYPE, ctype);
		response.headers().set(CONTENT_LENGTH, byteBuf.readableBytes());
		response.headers().set(CONNECTION, HEADER_CONNECTION_CLOSE);
		return response;
	}

	public static FullHttpResponse buildFullHttpResponse(Object data, int status, String ctype) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(String.valueOf(data).getBytes());
		HttpResponseStatus httpStatus = HttpResponseStatus.valueOf(status);
		final FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, httpStatus, byteBuf);
		response.headers().set(CONTENT_TYPE, ctype);
		response.headers().set(CONTENT_LENGTH, byteBuf.readableBytes());
		response.headers().set(CONNECTION, HEADER_CONNECTION_CLOSE);
		return response;
	}

	public static HttpEventHandler deepClone(HttpEventHandler h) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(h);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (HttpEventHandler) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HttpResponse response(Object data, int status) {
		return buildHttpResponse(data, status, ContentTypePool.TEXT_UTF8);
	}
}
