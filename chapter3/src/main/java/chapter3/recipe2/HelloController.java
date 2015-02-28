package chapter3.recipe2;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String hello(@RequestParam(value = "name", required = false, defaultValue = "SpringMVC developer") String name,	Model model) {		
		model.addAttribute("name", name);
		return "hello-view";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String edit(HttpServletRequest request, @PathVariable int id, @RequestParam(value = "data", required = false, defaultValue = "Empty") String data) {
		StringBuffer s = new StringBuffer();
		s.append("<br>");
		s.append("getContentType : ").append(request.getContentType());
		s.append("<br>");
		s.append("id : ").append(id);
		s.append("<br>");
		s.append("getPathInfo : ").append(request.getPathInfo());
		s.append("<br>create data: ");
		s.append(data);
		
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println(cookie.getName() + " : " + cookie.getValue());
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