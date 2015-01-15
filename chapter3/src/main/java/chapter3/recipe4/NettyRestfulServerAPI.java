package chapter3.recipe4;


import io.netty.handler.codec.http.HttpMethod;

import java.nio.charset.Charset;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;

/**
 * @author toddf
 * @since June 29, 2012
 */
public class NettyRestfulServerAPI {
	static class RestfulHandler {
		public String read(Request req, Response res) {
			String value = req.getHeader("echo");
			res.setContentType("text/xml");
			System.out.println("data " + req.getBody().toString(Charset.defaultCharset()));

			if (value == null) {
				return "<http_test><error>no value specified</error></http_test>";
			} else {
				return String.format(
						"<http_test><value>%s</value></http_test>", value);
			}
		}
	}

	public static void main(String[] args) {
		RestExpress server = new RestExpress().setName("Echo");

		server.uri("/echo", new RestfulHandler()).method(HttpMethod.GET).noSerialization();

		server.bind(8000);
		server.awaitShutdown();
	}
}