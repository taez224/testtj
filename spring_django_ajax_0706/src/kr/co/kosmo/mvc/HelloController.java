package kr.co.kosmo.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ������ / 2021. 7. 6. / ���� 10:07:03
 */
@Controller
public class HelloController {
	
	@GetMapping(value="/hello")
	public String hello(Model m) {
		m.addAttribute("msg", "������ �ٱ�Ծ���ääää�");
		return "hello";
	}

}
