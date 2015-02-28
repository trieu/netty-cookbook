package chapter3.recipe1;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import netty.cookbook.common.http.BasicHttpResponseHandler;
import netty.cookbook.common.http.HttpEventHandler;

@Sharable
public class HttpEventRoutingHandler extends SimpleChannelInboundHandler<Object> {

	private Map<UriMatcher, HttpEventHandler> routes = new LinkedHashMap<>();
	private Map<String, List<HttpEventHandler>> cachedRoutes = new HashMap<>();
	private static final String STARTS_WITH = "startsWith:";
	private static final String ENDS_WITH = "endsWith:";
	private static final String EQUALS = "equals:";
	private static final int MAX_SIZE_POOL_HANDLER = 1000;
	private static final BasicHttpResponseHandler HANDLER_404 = new BasicHttpResponseHandler("Not found", 404);
		
	public HttpEventRoutingHandler(Map<String, HttpEventHandler> routes)
			throws Exception {	
		setupRoutes(routes);
	}

	private class StartsWithMatcher implements UriMatcher {
		private String route;
		private StartsWithMatcher(String route) {
			this.route = route;
		}
		public boolean match(String uri) {
			return uri.startsWith(route);
		}
	}

	private class EndsWithMatcher implements UriMatcher {
		private String route;
		private EndsWithMatcher(String route) {
			this.route = route;
		}
		public boolean match(String uri) {
			return uri.endsWith(route);
		}
	}

	private class EqualsMatcher implements UriMatcher {
		private String route;
		private EqualsMatcher(String route) {
			this.route = route;
		}
		public boolean match(String uri) {
			return uri.equals(route);
		}
	}

	private void setupRoutes(Map<String, HttpEventHandler> routes)
			throws Exception {
		for (Map.Entry<String, HttpEventHandler> m : routes.entrySet()) {
			System.out.println("key " +m.getKey());
			if (m.getKey().startsWith(STARTS_WITH)) {
				this.routes.put(new StartsWithMatcher(m.getKey().replace(STARTS_WITH,"")), m.getValue());
			} else if (m.getKey().startsWith(ENDS_WITH)) {
				this.routes.put(new EndsWithMatcher(m.getKey().replace(ENDS_WITH, "")),	m.getValue());
			} else if (m.getKey().startsWith(EQUALS)) {
				this.routes.put(new EqualsMatcher(m.getKey().replace(EQUALS, "")), m.getValue());
			} else {
				throw new Exception("No matcher found in route " + m.getKey());
			}
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
			
			HttpResponse response = findHandler(request, path).handle(request, decoder);			
			 
			ctx.write(response);
			ctx.flush().close();
		}
	}
	
	public static int randInt(int min, int max) {	    
	    return new Random().nextInt((max - min)) + min;
	}
	
	static List<HttpEventHandler> newHttpHandlerPool(int max, HttpEventHandler h){
		List<HttpEventHandler> pool = new ArrayList<HttpEventHandler>(max);
		for (int i = 0; i < max; i++) {
			HttpEventHandler newHandler = HttpEventHandler.deepClone(h);
			pool.add(newHandler);
			//System.out.println(newHandler);
		}
		return pool;
	}

	private HttpEventHandler findHandler(HttpRequest request, String path) throws Exception {
		int index = randInt(0, MAX_SIZE_POOL_HANDLER);
		List<HttpEventHandler> pool = cachedRoutes.get(path);
		if(pool != null){
			if(pool.size()>0){
				return pool.get(index);	
			}			
		}
		HttpEventHandler h = null;
		for (Map.Entry<UriMatcher, HttpEventHandler> m : routes.entrySet()) {
			if (m.getKey().match(path)) {
				h = m.getValue();
				cachedRoutes.put(path, newHttpHandlerPool(MAX_SIZE_POOL_HANDLER, h));
				break;
			}
		}		
		if(h != null){			
			return h;
		}
		return HANDLER_404;
	}
}