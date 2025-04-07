package portal.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import portal.base.dto.UserDTO;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JsoupService {

    private static final int TIMEOUT = 10000; // 10초

    public String mailPlugLogin(String userId, String password) throws IOException, URISyntaxException {
        String result = null;
        String domain = "hyperinfo.co.kr";
        String url = "https://m109.mailplug.com/member/login?host_domain=" + domain + "&cid=" + userId;
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36";
        Map<String, String> data = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", userAgent);
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "ko,ko-KR;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.put("Connection", "keep-alive");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Host", "m109.mailplug.com");

        // 쿠키 매니저 설정
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieStore cookieStore = cookieManager.getCookieStore();
//        List<HttpCookie> cookieList = new ArrayList<>();

        Connection connection = Jsoup.connect(url);
        Connection.Response response = connection
            .cookieStore(cookieStore)
            .userAgent(userAgent)
            .timeout(TIMEOUT)
            .method(Connection.Method.GET)
            .execute();

        Document document = null;
        Map<String, String> cookies = response.cookies();
        List<HttpCookie> httpCookies = cookieStore.getCookies();
        String csrfMailplugToken = cookies.get("csrf_mailplug_token");
        String ciSession = cookies.get("cisession");
        String myValue = cookies.get("myvalue");

        url = "https://m109.mailplug.com/member/do_login";
        data.clear();
        data.put("mailplug_token", csrfMailplugToken);
        data.put("http_host", "m109.mailplug.com");
        data.put("domain", domain);
        data.put("myvalue", myValue);
        data.put("capcha_code", "");
        data.put("language_select", "kr");
        data.put("cid", userId);
        data.put("cpw", password);

        response = connection
            .userAgent(userAgent)
            .timeout(TIMEOUT)
            .ignoreContentType(true)
            .followRedirects(true)
            .headers(headers)
            .method(Connection.Method.POST)
            .data(data)
            .url(url)
            .execute();

        document = response.parse();
        String html = document.html();

        boolean isLoggedIn = html.contains("\"/main\"");
        log.info("#### Login Success: {}", isLoggedIn);

        if (isLoggedIn) {
            String bearerToken = "";

            url = "https://m109.mailplug.com/main";
            sendRequest(connection, url, "main");

            url = "https://m109.mailplug.com/webmail/lists";
            sendRequest(connection, url, "lists");

            cookies = connection.response().cookies();

            csrfMailplugToken = cookies.get("csrf_mailplug_token");
            ciSession = cookies.get("cisession");
            myValue = cookies.get("myvalue");
            bearerToken = cookies.get("MP_TAG");

            log.info("#### Bearer Token: {}", bearerToken);
            log.info("#### csrf_mailplug_token: {}", csrfMailplugToken);
            log.info("#### ciSession: {}", ciSession);
            log.info("#### myValue: {}", myValue);

            setCookes(cookieStore, cookies, url, ".mailplug.com");
            httpCookies.stream().forEach(httpCookie -> {
                log.info("#### {} CookieStore: {} = {}, {}", "lists", httpCookie.getName(), httpCookie.getValue(), httpCookie.getDomain());
            });

            url = "https://gw.mailplug.com/mail";

            if(headers.containsKey("Host")) {
                headers.replace("Host", new URL(url).getHost());
            }else{
                headers.put("Host", new URL(url).getHost());
            }

            connection
                .headers(headers)
                .cookieStore(cookieStore);
            sendRequest(connection, url, "mail");

            url = "https://m109.mailplug.com/api/v2/me/profile";
            setCookes(cookieStore, cookies, url, ".mailplug.com");

            if(headers.containsKey("Host")) {
                headers.replace("Host", new URL(url).getHost());
            }else{
                headers.put("Host", new URL(url).getHost());
            }

            if(headers.containsKey("Authorization")) {
                headers.replace("Authorization", "Bearer " + bearerToken);
            }else{
                headers.put("Authorization", "Bearer " + bearerToken);
            }

            connection
                .headers(headers)
                .cookieStore(cookieStore);

            try{
                response = connection
                    .headers(headers)
                    .timeout(TIMEOUT)
                    .cookies(cookies)
                    .ignoreContentType(true)
                    .followRedirects(false)
                    .method(Connection.Method.GET)
                    .url(url)
                    .execute();
                result = response.parse().text();
            } catch (Exception e) {
                log.error("profile" + " Error Send Reqeust: {}", e);
            }
        }

        return result;
    }

    private void setCookes(CookieStore cookieStore, Map<String, String> cookies, String url, String domain) {
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

    private void sendRequest(Connection connection, String url, String tile) {
        try{
            var response = connection
                .timeout(TIMEOUT)
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.GET)
                .url(url)
                .execute();
            var document = response.parse();
            var html = document.html();
            log.info("{} HTML: {}", tile, html);

            response.cookies().forEach((key, value) -> {
                log.info("{} Cookie: {} = {}", tile, key, value);
            });
        } catch (Exception e) {
            log.error(tile + " Error Send Reqeust: {}", e);
        }

//        var httpCookies = connection.cookieStore().getCookies();
//        httpCookies.stream().forEach(httpCookie -> {
//            log.info("#### {} CookieStore: {} = {}, {}", tile, httpCookie.getName(), httpCookie.getValue(), httpCookie.getDomain());
//        });
    }

}
