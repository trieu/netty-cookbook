package chapter3.recipe2;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	public static class User {
		String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	private static final String HELLO = "hello";

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		// System.out.println(name);
		model.addAttribute("name", name);
		return HELLO;
	}
	
	@RequestMapping(value = "/someUrl", method = RequestMethod.POST)
	@ResponseBody
	public String someMethod( @RequestBody User user) {
		System.out.println(user.getName());
		return "OK";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<String> update(HttpEntity<byte[]> requestEntity) {
		//String requestHeader = requestEntity.getHeaders().getFirst("MyRequestHeader");
		byte[] requestBody = requestEntity.getBody();

		// do something with request header and body
		System.out.println("requestBody "+new String(requestBody));
		System.out.println("requestEntity "+requestEntity.toString());

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("MyResponseHeader", "MyValue");
		return new ResponseEntity<String>("Hello World", responseHeaders,HttpStatus.CREATED);
	}
}