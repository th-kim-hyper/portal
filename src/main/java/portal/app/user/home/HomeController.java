package portal.app.user.home;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import portal.config.ApplicationConfig;
import portal.config.ApplicationConfig.PortalProperties;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

	final private ApplicationConfig appConfig;
//	final private PortalProperties portalProperties;
	
	@RequestMapping("/user/home")
	public String index(Model model) {
		
		log.info("#### index page!");
		
		PortalProperties props = appConfig.portalProperties();
		
//		log.info("#### prop count : {}", props.size());
		
		Field[] fields = props.getClass().getDeclaredFields();
        for (Field field : fields) {
        	String key = field.getName();
        	Object val = null;
			try {
				val = field.get(props);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	log.info("#### 키 : {}, 값 : {}", key, val);
        }

//		for (Map.Entry<String, Object> entry : props.entrySet()) {
//			String key = entry.getKey();
//			String val = entry.getValue().toString();
//			log.info("#### 키 : {}, 값 : {}", key, val);
//		}
		
//		log.info("#### Portal props");
//		log.info("#### 키 : {}, 값 : {}", "name", portalProperties.getName());
//		log.info("#### 키 : {}, 값 : {}", "version", portalProperties.getVersion());
//		log.info("#### 키 : {}, 값 : {}", "mode", portalProperties.getStorageMode());
//		log.info("#### 키 : {}, 값 : {}", "datasource", portalProperties.getDataSource());
//		log.info("#### 키 : {}, 값 : {}", "ext", portalProperties.getExt());
		
		
		model.addAttribute("dt", new Date());
//		model.addAttribute("nm", appConfig.getName());
//		model.addAttribute("ver", appConfig.getVersion());
//		model.addAttribute("smode", appConfig.getStorageMode());
		model.addAttribute("props", props);
		
		return "jsp/user/home/index";
	}
	
}
