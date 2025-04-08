package portal.base;

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

@Slf4j
@Service
public class JsoupService {

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

    public String mailPlugLogin(String userId, String password) throws IOException, URISyntaxException {
        String result = null;
        String url = "https://m109.mailplug.com/member/login?host_domain=" + domain + "&cid=" + userId;

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieStore cookieStore = cookieManager.getCookieStore();
        Connection connection = Jsoup.connect(url)
            .cookieStore(cookieStore)
            .userAgent(userAgent)
            .timeout(TIMEOUT)
            .headers(defaultHeaders);
        Map<String, String> formData = new HashMap<>();

        // 01. 로그인폼 (GET)
        Connection.Response response = executeGet(connection, url);
        Document document = null;
        Map<String, String> responseCookies = response.cookies();
        List<HttpCookie> cookieStoreCookies = cookieStore.getCookies();
        String csrfMailplugToken = responseCookies.get("csrf_mailplug_token");
        String myValue = responseCookies.get("myvalue");

        // 02. 로그인 처리 (POST)
        url = "https://m109.mailplug.com/member/do_login";
        formData.clear();
        formData.put("mailplug_token", csrfMailplugToken);
        formData.put("http_host", "m109.mailplug.com");
        formData.put("domain", domain);
        formData.put("myvalue", myValue);
        formData.put("capcha_code", "");
        formData.put("language_select", "kr");
        formData.put("cid", userId);
        formData.put("cpw", password);

        response = connection
            .url(url)
            .header("Host", "m109.mailplug.com")
            .ignoreContentType(true)
            .followRedirects(true)
            .headers(defaultHeaders)
            .method(Connection.Method.POST)
            .data(formData)
            .execute();

        document = response.parse();
        String html = document.html();

        // 03. 로그인 성공 여부 확인
        boolean isLoggedIn = html.contains("\"/main\"");
        log.info("#### Login Success: {}", isLoggedIn);

        if (isLoggedIn) {
            String bearerToken = "";

            // 04. 로그인 성공 후 추가 요청
            url = "https://m109.mailplug.com/main";
            executeGet(connection, url);

            // 05. 추가 요청 후 쿠키 저장
            url = "https://m109.mailplug.com/webmail/lists";
            response = executeGet(connection, url);
            responseCookies = response.cookies();
            bearerToken = responseCookies.get("MP_TAG");

            log.info("#### Bearer Token: {}", bearerToken);

            setCookies(cookieStore, responseCookies, url, ".mailplug.com");
            cookieStoreCookies.stream().forEach(httpCookie -> {
                log.info("#### {} CookieStore: {} = {}, {}", "lists", httpCookie.getName(), httpCookie.getValue(), httpCookie.getDomain());
            });

            // 06. 메일 페이지 요청(GET)
            url = "https://gw.mailplug.com/mail";
            connection.cookieStore(cookieStore);
            executeGet(connection, url);

            // 07. 프로필 요청(GET)
            url = "https://m109.mailplug.com/api/v2/me/profile";
            setCookies(cookieStore, responseCookies, url, ".mailplug.com");
            connection
                .header("Host", "m109.mailplug.com")
                .header("Authorization", "Bearer " + bearerToken)
                .cookies(responseCookies)
                .cookieStore(cookieStore);
            response = executeGet(connection, url);
            document = response.parse();
            result = document.text();
        }

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
