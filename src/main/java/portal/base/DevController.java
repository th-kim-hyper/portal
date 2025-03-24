package portal.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/dev")
@Slf4j
@Controller
@RequiredArgsConstructor
public class DevController {

	final private BaseService baseService;

	@RequestMapping("/ping")
	public @ResponseBody String ping() {
		log.info("#### /dev/ping");
		return "pong";
	}

	@RequestMapping("/version")
	public @ResponseBody String version() {
		log.info("#### /dev/version");
		return baseService.getPortalProperties().getVersion();
	}
	
	@RequestMapping("/test_tl")
	public String testThymeleaf() {
		return "thymeleaf/test";
	}
	
	@RequestMapping("/test_jsp")
	public String testJsp() {
		return "/WEB-INF/test.jsp";
	}

}
