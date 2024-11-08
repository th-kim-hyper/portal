package portal.app.user.home;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import portal.config.ApplicationConfig;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

	final private ApplicationConfig appConfig;
	
	@RequestMapping("/user/home")
	public String index(Model model) {
		
		log.info("#### index page!");
		
		Map<String, Object> props = appConfig.propertyMap();
		
		log.info("#### prop count : {}", props.size());
		
		for (Map.Entry<String, Object> entry : props.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue().toString();
			log.info("#### 키 : {}, 값 : {}", key, val);
		}
		
		model.addAttribute("dt", new Date());
		model.addAttribute("nm", appConfig.getName());
		model.addAttribute("ver", appConfig.getVersion());
		model.addAttribute("smode", appConfig.getStorageMode());
		model.addAttribute("props", props);
		
		return "jsp/user/home/index";
	}
	
}
