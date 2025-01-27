package portal.base;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import portal.base.dto.PortalUserDTO;
import portal.config.ApplicationConfig.PortalProperties;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BaseController {
	
	final private BaseService baseService;

	@RequestMapping("/")
	public String index(Model model, HttpServletRequest request) {
		log.info("#### /index page!");

//		List<String> whitelist = baseService.getWhitelist();
//		whitelist.forEach(log::info);
//
//		PortalProperties props = baseService.getPortalProperties();
//		List<PortalUserDTO> userList = baseService.findAllUser();
//		model.addAttribute("props", props);
//		model.addAttribute("userList", userList);

//		String userId = request.getParameter("userId");
//		PortalUserDTO user = baseService.getPortalUser(userId);
//		model.addAttribute("user", user);
		return "thymeleaf/index";
	}

	@RequestMapping({"/login", "/login/form"})
	public String loginForm() {
		return "thymeleaf/login";
	}

//	@RequestMapping("/login/proc")
//	public String loginProc() {
//		return "thymeleaf/login";
//	}

	@RequestMapping("/ping")
	public @ResponseBody String ping() {
		log.info("#### /ping");
		return "pong";
	}

	@RequestMapping("/version")
	public @ResponseBody String version() {
		log.info("#### /version");
		return baseService.getPortalProperties().getVersion();
	}
	
	@RequestMapping("/test_tl")
	public String testThymeleaf() {
		return "thymeleaf/test";
	}
	
	@RequestMapping("/test_jsp")
	public String testJsp() {
		return "jsp/test";
	}



	@RequestMapping("/admin")
	@ResponseBody
	public String adminIndex() {
		return "Admin page";
	}

}
