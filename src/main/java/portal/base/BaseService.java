package portal.base;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import portal.base.dto.PortalUserDTO;
import portal.base.dto.UserDTO;
import portal.config.ApplicationConfig;
import portal.config.ApplicationConfig.PortalProperties;

@Slf4j
@RequiredArgsConstructor
@Service
public class BaseService {
	
	private final ApplicationConfig applicationConfig;
	private final BaseRepository baseRepository;
	
	public PortalProperties getPortalProperties() {
		return applicationConfig.portalProperties();
	}

	public UserDTO getUser(String userId, String password) throws IOException {
		UserDTO user = UserDTO.builder()
			.userId(userId)
			.password(password)
			.build();

		String url = "https://m109.mailplug.com/member/login?host_domain=hyperinfo.co.kr&cid=" + userId;
		String loginUrl = "https://m109.mailplug.com/member/do_login";
		Connection connection = Jsoup.connect(url);
		Connection.Response response = connection
			.method(Connection.Method.GET)
			.followRedirects(true)
			.execute();

		// 쿠키 추출
		Map<String, String> cookies = response.cookies();

		// 쿠키 출력
		cookies.forEach((key, value) -> {
			log.info("#### login form Cookie : {} = {}", key, value);
		});

		String csrf_mailplug_token = cookies.get("csrf_mailplug_token");
		String myvalue = cookies.get("myvalue");
		String host_domain = cookies.get("host_domain");

		response = connection
			.url(loginUrl)
			.cookies(cookies)
			.data("http_host", "m109.mailplug.com")
			.data("cid", userId)
			.data("cpw", password)
			.data("domain", host_domain)
			.data("mailplug_token", csrf_mailplug_token)
			.data("myvalue", myvalue)
			.data("language_select", "kr")
			.data("capcha_code", "")
			.method(Connection.Method.POST)
			.followRedirects(true)
			.execute();

		Document document = response.parse();
		String html = document.html();

//		log.info(html);

		if(html != null && !html.contains("action=\"/member/do_login\"")) {
			log.info("#### Login Success");

			response = connection
				.url("https://m109.mailplug.com/main")
				.method(Connection.Method.GET)
				.execute();

			cookies = response.cookies();
			cookies.forEach((key, value) -> {
				log.info("#### main Cookies : {} = {}", key, value);
			});

			response = Jsoup
				.connect("https://gw.mailplug.com/mail/1")
				.cookies(cookies)
				.method(Connection.Method.GET)
				.execute();

//			cookies = response.cookies();
//			cookies.forEach((key, value) -> {
//				log.info("#### GW Cookies : {} = {}", key, value);
//			});

			response = Jsoup
				.connect("https://member.mailplug.com/api/v2/member/auth/token?refresh=true")
				.ignoreContentType(true)
				.cookies(cookies)
				.header("accept", "application/json, text/plain, */*")
				.header("accept-encoding", "gzip, deflate, br, zstd")
				.header("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
				.header("cache-control", "no-cache")
				.method(Connection.Method.GET)
				.execute();

			cookies = response.cookies();
			cookies.forEach((key, value) -> {
				log.info("#### member Cookies : {} = {}", key, value);
			});

			String token = "Bearer " + cookies.get("token");

//
//			response = connection
//				.url("https://m109.mailplug.com/webmail/lists")
////				.cookies(cookies)
//				.method(Connection.Method.GET)
//				.execute();
////			connection.request().data().clear();
////			cookies = response.cookies();
//			response.cookies().forEach((key, value) -> {
//				log.info("#### List Cookies : {} = {}", key, value);
//			});

			response = connection
				.url("https://m109.mailplug.com/api/v2/me/profile")
				.header("Authorization", token)
				.cookies(cookies)
				.method(Connection.Method.GET)
				.execute();
//
//			// 응답 헤더 출력
//			response.headers().forEach((key, value) -> {
//				log.info("#### Response Header: {} = {}", key, value);
//			});
//
			document = response.parse();
			html = document.html();

			log.info(html);
		} else {
			log.info("#### Login Fail");
		}

		return user;
	}

	public List<PortalUserDTO> findAllUser() {
		return baseRepository.findAllUser();
	}
	
	public PortalUserDTO getPortalUser(String userId) {
		return baseRepository.findByUserId(userId);
	}

	public List<String> getWhitelist() {
		return applicationConfig.portalProperties().getPublicPaths();
	}

}
