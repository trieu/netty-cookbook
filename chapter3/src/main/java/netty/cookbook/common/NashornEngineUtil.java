package netty.cookbook.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import chapter3.recipe6.SimpleHttpRequest;
import chapter3.recipe6.SimpleHttpResponse;

public class NashornEngineUtil {
	private static final NashornScriptEngineFactory engineFactory = new NashornScriptEngineFactory();
		
	public static SimpleHttpResponse process(SimpleHttpRequest httpRequest)
			throws NoSuchMethodException, ScriptException, IOException {
		CompiledScript compiled;
		Compilable engine;
		String scriptFilePath = "./src/main/resources/templates/js/script.js";
		
		engine = (Compilable) engineFactory.getScriptEngine();
		compiled = ((Compilable) engine).compile(Files.newBufferedReader(Paths.get(scriptFilePath),StandardCharsets.UTF_8));
		
		SimpleBindings global = new SimpleBindings();
		global.put("theReq", httpRequest);
		global.put("theResp", new SimpleHttpResponse());
		
		Object result = compiled.eval(global);
		SimpleHttpResponse resp = (SimpleHttpResponse) result;
		return resp;
	}
}
