package portal.app.user.home;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;
import portal.config.ApplicationConfig;
import portal.config.ApplicationConfig.PortalProperties;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

	final private ApplicationConfig appConfig;

	@RequestMapping("/user")
	@ResponseBody
	public String userIndex() {
		return "User page";
	}

//	@RequestMapping("/user/home")
//	public String userHome(Model model) {
//
//		log.info("#### index page!");
//
//		PortalProperties props = appConfig.portalProperties();
//
//		Field[] fields = props.getClass().getDeclaredFields();
//        for (Field field : fields) {
//        	String key = field.getName();
//        	Object val = null;
//			try {
//				val = field.get(props);
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	log.info("#### 키 : {}, 값 : {}", key, val);
//        }
//
//		model.addAttribute("dt", new Date());
//		model.addAttribute("props", props);
//
//		return "jsp/user/home/index";
//	}
	
}
