package portal.base;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BaseRestController {

	final private BaseService baseService;

	@Operation(summary = "핑퐁", description = "핑 하면 퐁")
//	@Parameter(name = "str", description = "2번 반복할 문자열")
	@GetMapping("/ping")
	public String ping() {
		log.info("#### /api/v1/ping");
		return "pong";
	}

	@GetMapping("/version")
	public String version() {
		log.info("#### /api/v1/version");
		return baseService.getPortalProperties().getVersion();
	}

}
