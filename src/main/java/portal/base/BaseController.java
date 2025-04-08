package portal.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.graalvm.polyglot.Context;
//import org.graalvm.polyglot.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import portal.base.dto.UserDTO;
import portal.config.CustomUserDetails;

import javax.net.ssl.*;
import javax.script.ScriptException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BaseController {

	final private BaseService baseService;
	final private JsoupService jsoupService;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@RequestMapping({"/", "/index"})
	public String index(Model model, HttpServletRequest request) throws IOException {

		log.info("#### /index page!!!");
		model.addAttribute("profile", activeProfile);
		return "thymeleaf/index";
	}

	@RequestMapping("/public/ping")
	public @ResponseBody String ping() throws ScriptException {
		log.info("#### /public/ping");
        try (Context context = Context.create("js")) {
			org.graalvm.polyglot.Value result = context.eval("js", "JSON.stringify({hello: 'world'})");
			return result.asString();
        }
	}

	@RequestMapping("/public/version")
	public @ResponseBody String version() {
		log.info("#### /version");

		try {
			String url = "http://192.168.50.98:62975/template/down";
			String apiKey = "SNOCR-3de238b328cf4bde9a73e6adc693918b";
			// 폼 데이터 설정
			String urlParameters = "api_key=" + apiKey;

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// POST 요청 설정
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			con.setRequestProperty("api_key", apiKey);

			// 출력 스트림 활성화 설정
			con.setDoOutput(true);

			// Content-Length 설정
			byte[] postData = urlParameters.getBytes("UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(postData.length));

			// 요청 본문에 데이터 쓰기
			try (java.io.OutputStream os = con.getOutputStream()) {
				os.write(postData);
			}

			int responseCode = con.getResponseCode();
			log.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

//			log.info("Response : " + response.toString());

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


		return baseService.getPortalProperties().getVersion();
	}

	@GetMapping({"/login", "/login/form"})
	public String loginForm(Model model, HttpServletRequest request) {
		log.info("#### get /login form!!!");
		String errorMessage = "";
		String logoutMessage = "";

		if(request.getParameterMap().containsKey("error")) {
			log.info("#### login error!!!");
			errorMessage = "로그인 아이디/비밀번호가 올바르지 않습니다.";
		}

		if(request.getParameterMap().containsKey("logout")) {
			log.info("#### logout!!!");
			logoutMessage = "로그아웃 되었습니다.";
		}

		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("logoutMessage", logoutMessage);

		return "thymeleaf/login";
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody Map<String, String> loginRequest) {
		log.info("#### post /login proc!!!");
		log.info("#### loginRequest : " + loginRequest);
//		Authentication authenticationRequest =
//			UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.get("username"), loginRequest.get("password"));
//		Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
		return ResponseEntity.ok().build();
	}

	@RequestMapping("/user")
	@ResponseBody
	public String user() {
		log.info("#### /user");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "User page : " + authentication.getName() + " / " + authentication.getAuthorities();
	}

	@RequestMapping("/admin")
	@ResponseBody
	public String admin() {
		log.info("#### /admin");
		// get security context value
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		return "Admin page : " + customUserDetails.getUsername()
			+ " / " + customUserDetails.getAuthorities()
			+ " / " + customUserDetails.getClientIp()
			+ " / " + customUserDetails.getUserDto().getEmail();
	}

	@RequestMapping("/apiuser")
	@ResponseBody
	public String apiuser() {
		log.info("#### /apiuser");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "API User page : " + authentication.getName() + " / " + authentication.getAuthorities();
	}

	@RequestMapping("/apiadmin")
	@ResponseBody
	public String apiadmin() {
		log.info("#### /apiadmin");
		// get security context value
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "API Admin page : " + authentication.getName() + " / " + authentication.getAuthorities();
	}


	private void trustSpecificCertificate(String url) throws Exception {
		URL serverUrl = new URL(url);
		HttpsURLConnection connection = (HttpsURLConnection) serverUrl.openConnection();
		connection.setConnectTimeout(5000);
		connection.connect();

		Certificate[] certificates = connection.getServerCertificates();
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);

		int i = 0;
		for (Certificate certificate : certificates) {
			keyStore.setCertificateEntry("server" + i++, certificate);
		}

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tmf.getTrustManagers(), null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	}

	private void disableSSLVerification() {
		try {
			// SSL 인증서 검증을 무시하는 TrustManager 생성
			TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
				}
			};

			// SSL 컨텍스트에 TrustManager 설정
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// 호스트명 검증 비활성화
			HostnameVerifier allHostsValid = (hostname, session) -> true;
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/public")
	@ResponseBody
	public UserDTO publicIndex() {
		String username = "th.kim";
		String password = "!G!493o18!";
		UserDTO userDTO = null;

		try {
			String json = jsoupService.mailPlugLogin(username, password);
			log.info("#### json : {}", json);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(json);
			String email = jsonNode.get("emailAddress").asText();
			String division = jsonNode.get("organization").asText();
			JsonNode contact = jsonNode.get("contact");
			String phone = contact.get("phone").asText();
			String name = jsonNode.get("displayName").asText();
			userDTO = UserDTO.builder()
				.userId(username)
				.name(name)
				.password(password)
				.email(email)
				.phone(phone)
				.division(division)
				.build();
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}

		return userDTO;
	}

	@RequestMapping("/public/test_tl")
	public String testThymeleaf() {
		return "thymeleaf/test";
	}
	
	@RequestMapping("/public/test_jsp")
	public String testJsp() {
		return "jsp/test";
	}

}
