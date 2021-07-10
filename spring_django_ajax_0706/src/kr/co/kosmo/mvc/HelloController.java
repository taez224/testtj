package kr.co.kosmo.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 허태준 / 2021. 7. 6. / 오전 10:07:03
 */
@Controller
public class HelloController {
	
	@GetMapping(value="/hello")
	public String hello(Model m) {
		m.addAttribute("msg", "스프링 다까먹었어ㅓㅓㅓㅓㅓ");
		return "hello";
	}

}
