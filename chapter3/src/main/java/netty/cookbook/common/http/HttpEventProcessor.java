package netty.cookbook.common.http;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class HttpEventProcessor {
	final HttpRequest req;
	final QueryStringDecoder query;
	
	public HttpEventProcessor(HttpRequest req, QueryStringDecoder query) {
		super();
		this.req = req;
		this.query = query;
	}
	

}
