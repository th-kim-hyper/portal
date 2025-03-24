package portal.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BaseController {

//	private final AuthenticationManager authenticationManager;
	final private BaseService baseService;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@RequestMapping({"/", "/index"})
	public String index(Model model, HttpServletRequest request) throws IOException {

		log.info("#### /index page!!!");
		model.addAttribute("profile", activeProfile);
		return "thymeleaf/index";
	}

	@GetMapping({"/login", "/login/form"})
	public String loginForm() {
		log.info("#### get /login form!!!");
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
		// HttpURLConnection 이용하여 http://192.168.50.98:62975/template/down 호출 api_key = SNOCR-3de238b328cf4bde9a73e6adc693918b
		try {
			String url = "http://192.168.50.98:62975/template/down";
			String apiKey = "SNOCR-3de238b328cf4bde9a73e6adc693918b";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// POST 요청 설정
			con.setRequestMethod("POST");
			con.setRequestProperty("api_key", apiKey);

			int responseCode = con.getResponseCode();
			log.info("Response Code : " + responseCode);

//			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}



		return "User page";
	}

	@RequestMapping("/admin")
	@ResponseBody
	public String admin() {
		log.info("#### /admin");
		return "Admin page";
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

	@RequestMapping("/public/ping")
	public @ResponseBody String ping() {
		log.info("#### /public/ping");

		try {
			String url = "https://192.168.20.51:8777/asset/api/v1/objects/type/process";
			String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNzE4NjExNzk4Nzg1LTRiYmFjMjcyLTcxODZmMzIzIiwiaXNzIjoiQVVUSF9DTElFTlRfQ0VSVElGSUNBVEUiLCJhdWQiOiJBVVRIX0FQSV9TRVJWRVIiLCJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJjbGllbnRUeXBlIjoiQVBJX0tFWSIsImNsaWVudElkIjoiQVVUSF9BUElfU0VSVkVSIiwidXNlcklkIjoiYWRtaW4iLCJjaGFsbGVuZ2UiOiIxNzE4NjExNzk4Nzg1LWI5MmFkZTgyLWMyNDA1MDlmIiwiaXBBZGRyIjoiMTI3LjAuMC4xIiwidGVuYW50SWQiOiJUTl8zMmRmOTcxOTFmMzY0ZGRiYTUyMTlmY2NhNzc3YTBiMCIsInNlY3VyaXR5VHlwZSI6InYyIiwiaWF0IjoxNzE4NjExNzk4LCJleHAiOjE3NTAxNzIzOTl9.BsI6XuBj_LYZNVr6CJMJlnCOkil_d4b77zn3tjMYHNA";

			disableSSLVerification();

			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			// GET 요청 설정
			con.setRequestMethod("GET");
//			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Authorization", apiKey);

//			// 출력 스트림 활성화 설정
//			con.setDoOutput(true);
//
//			// Content-Length 설정
//			byte[] postData = urlParameters.getBytes("UTF-8");
//			con.setRequestProperty("Content-Length", Integer.toString(postData.length));

//			// 요청 본문에 데이터 쓰기
//			try (java.io.OutputStream os = con.getOutputStream()) {
//				os.write(postData);
//			}

			int responseCode = con.getResponseCode();
			log.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			log.info("Response : " + response.toString());

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


		return "pong";
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
	
	@RequestMapping("/public/test_tl")
	public String testThymeleaf() {
		return "thymeleaf/test";
	}
	
	@RequestMapping("/public/test_jsp")
	public String testJsp() {
		return "jsp/test";
	}

}
