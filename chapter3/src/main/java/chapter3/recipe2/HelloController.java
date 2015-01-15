package chapter3.recipe2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
	private static final String HELLO = "hello";

	@RequestMapping("/hello")	
	public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		//System.out.println(name);
        model.addAttribute("name", name);
        return HELLO;
    }
}