package portal.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class RpaService {

    private static final int TIMEOUT = 10000; // 10초
    private static final String domain = "hyperinfo.co.kr";
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36";
    private Map<String, String> defaultHeaders = new HashMap<>(){
        {
            put("Accept", "*/*");
            put("Accept-Encoding", "gzip, deflate");
            put("Connection", "keep-alive");
            put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        }
    };

    public String rpaAdminLogin(String userId, String password) throws IOException, URISyntaxException {
        String result = null;
        String url = "https://192.168.20.51:8080/admin";

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieStore cookieStore = cookieManager.getCookieStore();
        Connection connection = Jsoup.connect(url)
            .cookieStore(cookieStore)
            .userAgent(userAgent)
            .timeout(TIMEOUT)
            .headers(defaultHeaders);
        Map<String, String> formData = new HashMap<>();
        String host = "192.168.20.51:8080";

        // 01. 로그인폼 (GET)
        Connection.Response response = connection
            .url(url)
            .header("Host", host)
//            .ignoreContentType(true)
            .followRedirects(true)
            .method(Connection.Method.GET)
            .execute();
        Document document = null;
//        Map<String, String> responseCookies = response.cookies();
//
//        responseCookies.forEach((key, value) -> {
//           log.info("Cookie " + key + " : " + value);
//        });

        // 02. 테넌트 목록 가져오기 (POST)
        url = "https://192.168.20.51:8080/admin/authenticateUser";

        formData.clear();
        formData.put("j_username", userId);
        formData.put("j_password", password);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(formData);

        defaultHeaders.put("Content-Type", "application/json+lego; charset=UTF-8");

        response = connection
            .url(url)
//            .cookieStore(cookieStore)
            .ignoreContentType(true)
            .followRedirects(false)
            .headers(defaultHeaders)
            .method(Connection.Method.POST)
            .requestBody(requestBody)
            .execute();

        document = response.parse();
        result = document.text();
        log.info("#### result: {}", result);


        // 03. 로그인 (POST)
        url = "https://192.168.20.51:8080/admin/j_spring_security_check";

        formData.clear();
        formData.put("loginTenant", "TN_32df97191f364ddba5219fcca777a0b0");
        formData.put("tenantPortalPlaceHolder", "테넌트를 선택해주세요.");
        formData.put("language", "KO");
        formData.put("j_username", userId);
        formData.put("j_password", password);

//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper.writeValueAsString(formData);

        defaultHeaders.put("Content-Type", "application/x-www-form-urlencoded");

        response = connection
            .url(url)
//            .cookieStore(cookieStore)
            .ignoreContentType(true)
            .followRedirects(true)
            .headers(defaultHeaders)
            .method(Connection.Method.POST)
            .data(formData)
            .execute();

        document = response.parse();
        result = document.html();

        String regex = "var _loginUser = \\{[^\\};]+\\};";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            result = matcher.group();
        } else {
            result = null;
        }

        if(result != null && result.length() > 0) {
            int beginIndex = result.indexOf("var _loginUser = ") + 17;
            int endIndex = result.indexOf("};", beginIndex) + 1;
            result = result.substring(beginIndex, endIndex);
            result = result.replaceAll("'", "\"");
            result = result.replaceAll("\\t\\t\\t", "\t\t\t\"");
            result = result.replaceAll(" : ", "\" : ");
        }

        log.info("#### result: {}", result);
//
//        // 03. 로그인 성공 여부 확인
//        boolean isLoggedIn = html.contains("\"/main\"");
//        log.info("#### Login Success: {}", isLoggedIn);
//
//        if (isLoggedIn) {
//            String bearerToken = "";
//
//            // 04. 로그인 성공 후 추가 요청
//            url = "https://m109.mailplug.com/main";
//            executeGet(connection, url);
//
//            // 05. 추가 요청 후 쿠키 저장
//            url = "https://m109.mailplug.com/webmail/lists";
//            response = executeGet(connection, url);
//            responseCookies = response.cookies();
//            bearerToken = responseCookies.get("MP_TAG");
//
//            log.info("#### Bearer Token: {}", bearerToken);
//
//            setCookies(cookieStore, responseCookies, url, ".mailplug.com");
//            cookieStoreCookies.stream().forEach(httpCookie -> {
//                log.info("#### {} CookieStore: {} = {}, {}", "lists", httpCookie.getName(), httpCookie.getValue(), httpCookie.getDomain());
//            });
//
//            // 06. 메일 페이지 요청(GET)
//            url = "https://gw.mailplug.com/mail";
//            connection.cookieStore(cookieStore);
//            executeGet(connection, url);
//
//            // 07. 프로필 요청(GET)
//            url = "https://m109.mailplug.com/api/v2/me/profile";
//            setCookies(cookieStore, responseCookies, url, ".mailplug.com");
//            connection
//                .header("Host", "m109.mailplug.com")
//                .header("Authorization", "Bearer " + bearerToken)
//                .cookies(responseCookies)
//                .cookieStore(cookieStore);
//            response = executeGet(connection, url);
//            document = response.parse();
//            result = document.text();
//        }

        return result;
    }

    private void setCookies(CookieStore cookieStore, Map<String, String> cookies, String url, String domain) {
        cookies.forEach((key, value) -> {
            URI uri = null;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            if ("MP_AUT".equals(key)){
                log.info("#### SET MP_AUT Cookie: {} = {}", key, value);
                HttpCookie cookie = new HttpCookie(key, value);
                cookie.setDomain(domain); // 도메인 지정
                cookie.setHttpOnly(true);
                cookie.setPath("/");
//                        cookie.setMaxAge(0);
                cookieStore.add(uri, cookie);
            } else if ("MP_RES".equals(key)){
                log.info("#### SET MP_RES Cookie: {} = {}", key, value);
                HttpCookie cookie = new HttpCookie(key, value);
                cookie.setDomain(domain); // 도메인 지정
                cookie.setPath("/");
//                        cookie.setMaxAge(0);
                cookieStore.add(uri, cookie);
            } else if ("MP_SES".equals(key)){
                log.info("#### SET MP_SES Cookie: {} = {}", key, value);
                HttpCookie cookie = new HttpCookie(key, value);
                cookie.setDomain(domain); // 도메인 지정
                cookie.setPath("/");
//                        cookie.setMaxAge(0);
                cookieStore.add(uri, cookie);
            } else if ("MP_TAG".equals(key)){
                log.info("#### SET MP_TAG Cookie: {} = {}", key, value);
                HttpCookie cookie = new HttpCookie(key, value);
                cookie.setDomain(domain); // 도메인 지정
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(3600);
                cookieStore.add(uri, cookie);
            } else if ("lang".equals(key)){
                log.info("#### SET lang Cookie: {} = {}", key, value);
                HttpCookie cookie = new HttpCookie(key, "kr");
                cookie.setDomain(domain); // 도메인 지정
                cookie.setPath("/");
//                        cookie.setMaxAge(0);
                cookieStore.add(uri, cookie);
            } else if ("login_domain".equals(key)){
                value = "hyperinfo.co.kr";
                log.info("#### SET login_domain Cookie: {} = {}", key, value);
                HttpCookie cookie = new HttpCookie(key, value);
                cookie.setDomain(domain); // 도메인 지정
                cookie.setHttpOnly(true);
                cookie.setPath("/");
//                        cookie.setMaxAge(0);
                cookieStore.add(uri, cookie);
            }
        });
    }

    private Connection.Response executeGet(Connection connection, String url) {

        Connection.Response response = null;

        try{
            String host = new URL(url).getHost();
            response = connection
                .url(url)
                .header("Host", host)
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.GET)
                .execute();
        } catch (Exception e) {
            log.error("#### executeGet", e);
        }

        return response;
    }

}
