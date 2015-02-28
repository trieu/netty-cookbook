package chapter3.recipe6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import com.google.gson.Gson;

public class JavaScriptProcessor {

	public static class SimpleHttpRequest {
		String uri;

		public SimpleHttpRequest() {
			// TODO Auto-generated constructor stub
		}

		public SimpleHttpRequest(String uri) {
			super();
			this.uri = uri;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		@Override
		public String toString() {		
			return new Gson().toJson(this);
		}
	}

	public static class SimpleHttpResponse {
		String data;
		long time;
		
		public SimpleHttpResponse() {
			// TODO Auto-generated constructor stub
		}

		public SimpleHttpResponse(String data) {
			super();
			this.data = data;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}
		
		@Override
		public String toString() {		
			return new Gson().toJson(this);
		}

	}

	private static final NashornScriptEngineFactory engineFactory = new NashornScriptEngineFactory();
	CompiledScript compiled;
	Compilable engine;
	String scriptFilePath = "./src/main/resources/templates/js/script.js";

	public JavaScriptProcessor() throws ScriptException, IOException {
		engine = (Compilable) engineFactory.getScriptEngine();
		compiled = ((Compilable) engine).compile(Files.newBufferedReader(Paths.get(scriptFilePath),StandardCharsets.UTF_8));		
	}

	public SimpleHttpResponse process(SimpleHttpRequest httpRequest)
			throws NoSuchMethodException, ScriptException {
		SimpleBindings global = new SimpleBindings();
		global.put("theReq", httpRequest);
		global.put("theResp", new SimpleHttpResponse());
		
		Object result = compiled.eval(global);
		SimpleHttpResponse resp = (SimpleHttpResponse) result;
		return resp;
	}

	public static void main(String[] args) throws Exception {

		JavaScriptProcessor processor = new JavaScriptProcessor();

		SimpleHttpRequest httpRequest = new SimpleHttpRequest();
		httpRequest.setUri("/get/123");

		SimpleHttpResponse resp1 = processor.process(httpRequest);
		System.out.println(resp1.getData());
		//System.out.println(resp.getTime());
		
		UnaryOperator<SimpleHttpRequest> filterMySelf = req -> {
			if(req.getUri().contains("admin")){
				//no permission
				return null;
			}
			return req;			
		};				
		Function<SimpleHttpRequest , SimpleHttpResponse> logicFunction = req -> {
			if(req != null){
				if(req.getUri().contains("123")){
					return new SimpleHttpResponse("123 data");
				}
			}			
			return new SimpleHttpResponse("empty data");
		};
		UnaryOperator<SimpleHttpResponse> formatingResult = resp -> {
			resp.data = "(" + resp.data + ")";
			return resp;
		};
		
		System.out.println(formatingResult.apply(logicFunction.apply(filterMySelf.apply(new SimpleHttpRequest("admin/edit/123")))));
		System.out.println(formatingResult.apply(logicFunction.apply(filterMySelf.apply(new SimpleHttpRequest("user/edit/123")))));
		System.out.println(formatingResult.apply(logicFunction.apply(filterMySelf.apply(new SimpleHttpRequest("user/edit/456")))));
	}
}
