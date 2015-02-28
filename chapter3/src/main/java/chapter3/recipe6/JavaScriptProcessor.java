package chapter3.recipe6;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScriptProcessor {
	
	public static class HttpRequest {
		String uri;

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}
		
	}
	
	public static class HttpResponse {
		String data;
		long time;

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

		
		
	}

	public static void main(String[] args) throws FileNotFoundException, ScriptException, NoSuchMethodException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader("./src/main/resources/templates/js/script.js"));
		Invocable invocable = (Invocable) engine;
				

		Object result = invocable.invokeFunction("fun1", "Peter Parker");
		System.out.println(result);
		System.out.println(result.getClass());
		
		
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setUri("/get/123");
		Object result2 = invocable.invokeFunction("fun2", httpRequest, new HttpResponse());
		HttpResponse resp = (HttpResponse) result2;
		System.out.println(resp.getData());
		System.out.println(resp.getTime());
	}
}
