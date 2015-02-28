package chapter3.recipe2;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(@RequestParam(value = "name", required = false, defaultValue = "Netty developer") String name,	Model model) {		
		model.addAttribute("name", name);
		return "hello-view";
	}


	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public String edit(HttpServletRequest request) {
		StringBuffer s = new StringBuffer();
		s.append("<br>");
		s.append("getContentType : ").append(request.getContentType());
		s.append("<br>");
		s.append("getPathInfo : ").append(request.getPathInfo());
		s.append("<br>raw HTTP body content: ");
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				s.append("<br>");
				s.append(line);
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return s.toString();
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public String put(HttpServletRequest request, @RequestParam(value = "data", required = false, defaultValue = "Empty") String data) {
		StringBuffer s = new StringBuffer();
		s.append("<br>");
		s.append("getContentType : ").append(request.getContentType());
		s.append("<br>");
		s.append("getPathInfo : ").append(request.getPathInfo());
		s.append("<br>create data: ");
		s.append(data);
		return s.toString();
	}
}