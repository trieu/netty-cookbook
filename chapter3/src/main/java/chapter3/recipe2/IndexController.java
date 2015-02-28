package chapter3.recipe2;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	private static final String HELLO = "hello";

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		// System.out.println(name);
		model.addAttribute("name", name);
		return HELLO;
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
    @ResponseBody
    public String edit(@PathVariable int id, @RequestBody String data) {
        System.out.println("id "+ id + " data "  + data);
        return "ok";
    }
}