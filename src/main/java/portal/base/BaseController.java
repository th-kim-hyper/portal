package portal.base;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BaseController {
	
	final private BaseService baseService;
	
	@RequestMapping("/")
	public String index(Model model) {
		log.info("#### /index page!");
		Map<String, Object> props = baseService.getProps();
		baseService.printProps(props);
		model.addAttribute("props", props);		
		return "thymeleaf/index";
	}
	
	@RequestMapping("/ping")
	public @ResponseBody String ping() {
		log.info("#### /ping");
		return "pong";
	}

	@RequestMapping("/version")
	public @ResponseBody String version() {
		log.info("#### /version");
		return baseService.getVersion();
	}
	
	
	@RequestMapping("/test_tl")
	public String testThymeleaf() {
		return "thymeleaf/test";
	}
	
	@RequestMapping("/test_jsp")
	public String testJsp() {
		return "jsp/test";
	}

}
