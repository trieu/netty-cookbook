package netty.cookbook.chapter8.recipe3;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisException;
import netty.cookbook.common.http.ContentTypePool;
import netty.cookbook.common.http.HttpRequestEvent;
import netty.cookbook.common.http.NettyHttpUtil;
import netty.cookbook.common.redis.RedisCommand;
import netty.cookbook.common.redis.RedisInfo;

import com.allanbank.mongodb.MongoClient;
import com.allanbank.mongodb.MongoClientConfiguration;
import com.allanbank.mongodb.MongoCollection;
import com.allanbank.mongodb.MongoDatabase;
import com.allanbank.mongodb.MongoDbException;
import com.allanbank.mongodb.MongoFactory;
import com.allanbank.mongodb.MongoIterator;
import com.allanbank.mongodb.bson.Document;
import com.allanbank.mongodb.builder.QueryBuilder;

import static com.allanbank.mongodb.builder.QueryBuilder.and;
import static com.allanbank.mongodb.builder.QueryBuilder.or;
import static com.allanbank.mongodb.builder.QueryBuilder.not;
import static com.allanbank.mongodb.builder.QueryBuilder.where;

/**
 * the public handler for all Netty's message, transform to HttpRequestEvent and
 * routing all matched processors
 * 
 * @author trieu
 *
 */
public class HttpEventProcessingHandler extends
		SimpleChannelInboundHandler<Object> {

	private static final String _123456 = "123456";
	static ShardedJedisPool jedisPool = (new RedisInfo("127.0.0.1", 6379)).getShardedJedisPool();
	
	public HttpEventProcessingHandler() {
		
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			//HttpRequest request = (HttpRequest) msg;
			// TODO filter DDOS/bad/attacking requests

//			String uri = request.getUri();
//			String remoteIp = NettyHttpUtil.getRemoteIP(ctx, request);
//			String localIp = NettyHttpUtil.getLocalIP(ctx);

			// System.out.println(request.getMethod().name() + "==> uri: " +
			// uri);
			//QueryStringDecoder qDecoder = new QueryStringDecoder(uri);
			//Map<String, List<String>> params = qDecoder.parameters();

			// boolean isPOSTMethod = "POST".equals(request.getMethod().name());

			//HttpRequestEvent event = new HttpRequestEvent(localIp, remoteIp,uri, params, request);
			FullHttpResponse response = NettyHttpUtil.theHttpContent(_123456, ContentTypePool.TEXT_UTF8);;//processEvent(event);

			// set version to response header
			//response.headers().add("Server", HttpServer.SERVER_INFO_VERSION);

			// Write the response.
			ChannelFuture future = ctx.write(response);
			ctx.flush().close();

			// callback and free resources
//			if (event != null) {
//				event.clear();
//			}

			// Close the non-keep-alive connection after the write operation is
			// done.
			future.addListener(ChannelFutureListener.CLOSE);

		}

		if (msg instanceof HttpContent) {
			if (msg instanceof LastHttpContent) {
				NettyHttpUtil.response1pxGifImage(ctx);
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.flush().close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.flush().close();
	}

	FullHttpResponse processEvent(HttpRequestEvent event) {
		String rs = "";
		try {
			rs = (new RedisCommand<Long>(jedisPool) {
				@Override
				protected Long build() throws JedisException {					
					return Long.parseLong(jedis.hget("2014-12-04", "click"));
				}
			}).execute().toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		FullHttpResponse response = NettyHttpUtil.theHttpContent(rs, ContentTypePool.TEXT_UTF8);
		return response;
	}
}
