package netty.cookbook.common.http;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;

public class BasicHttpResponseHandler implements HttpEventHandler {
	String data;
	int status;

	public BasicHttpResponseHandler(String data, int status) {
		super();
		this.data = data;
		this.status = status;
	}

	public HttpResponse handle(HttpRequest req, QueryStringDecoder q)
			throws Exception {
		return HttpEventHandler.buildHttpResponse(data, status, ContentTypePool.TEXT_UTF8);
	}
}
