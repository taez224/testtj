package kr.co.kosmo.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ������ / 2021. 7. 6. / ���� 11:17:01
 */
@Controller
public class MyChartJsonController {

	@GetMapping(value="/chart1")
	public String chartTest() {
		
		return "mychart";
	}
}
