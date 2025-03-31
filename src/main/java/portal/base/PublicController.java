//package portal.base;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//
//@RequestMapping("/public")
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class PublicController {
//
//	final private BaseService baseService;
//
//	@RequestMapping("/ping")
//	public @ResponseBody String ping() throws ScriptException {
//		log.info("#### /dev/ping");
//
//		// Graal
//		ScriptEngine graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
//		var result = graalEngine.eval("{hello: 'world'}");
//
//		return result.toString();
//	}
//
//	@RequestMapping("/version")
//	public @ResponseBody String version() {
//		log.info("#### /dev/version");
//		return baseService.getPortalProperties().getVersion();
//	}
//
//	@RequestMapping("/test_tl")
//	public String testThymeleaf() {
//		return "thymeleaf/test";
//	}
//
//	@RequestMapping("/test_jsp")
//	public String testJsp() {
//		return "/WEB-INF/test.jsp";
//	}
//
//}
