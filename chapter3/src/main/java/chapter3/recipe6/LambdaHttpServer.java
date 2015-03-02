package chapter3.recipe6;

import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;
import java.util.function.ToIntFunction;

import netty.cookbook.common.NettyServerUtil;
import netty.cookbook.common.StringUtil;
import netty.cookbook.common.http.ContentTypePool;
import chapter3.recipe6.functions.Decorator;
import chapter3.recipe6.functions.Filter;
import chapter3.recipe6.functions.Processor;


public class LambdaHttpServer {
	
	static String ip = "127.0.0.1";
	static int port = 8080;
	
	public static void main(String[] args) throws Exception {
		//the function pipeline for this server
		FunctionPipeline pipe = new FunctionPipeline();
		
		//filtering not authorized request
		Filter filterAccessAdmin = req -> {
			req.setNotAuthorized(req.getUri().contains("admin"));
			return req;			
		};		
		
		//the logic handler
		Processor logicFunction = req -> {
			SimpleHttpResponse resp = new SimpleHttpResponse();
			String uri = req.getUri();
			Map<String,List<String>> params = req.getParameters();
			
			if(uri.contains("/compute") && params.containsKey("x") && params.containsKey("operator") ){
				String operator = params.get("operator").get(0);
				StringBuilder head = new StringBuilder();
				
				//mapping from String to Integer
				ToIntFunction<String> f = x -> {					
					return StringUtil.safeParseInt(x);
				};
				
				//binary function mapping x1 and x2 with operator
				IntBinaryOperator op = (int x1, int x2) -> {
					int n = 0;
					switch (operator) {
						case "plus":
							n = x1 + x2;
							head.append(x1).append(" + ").append(x2);
							break;
						case "multiply":							
							n = x1 * x2;
							head.append(x1).append(" * ").append(x2);
							break;	
						default:
							break;
					}
					return n;					
				};
				int n = params.get("x").stream().mapToInt(f).reduce(op).getAsInt();				
				resp.setHead(head.toString());
				resp.setBody(String.valueOf(n));				
			}
			return resp;
		};
		
		//the decorator of output
		Decorator formatingResult = resp -> {		
			if(resp.getBody() != null){
				resp.setContentType(ContentTypePool.JSON);
				resp.setStatus(200);
				resp.setTime(System.currentTimeMillis());
			} else {
				resp.setStatus(400);
			}
			return resp;
		};
		pipe.apply(filterAccessAdmin).apply(logicFunction).apply(formatingResult);
		
		NettyServerUtil.newHttpServerBootstrap(ip, port, new FunctionsChannelHandler(pipe) );
	}
}
