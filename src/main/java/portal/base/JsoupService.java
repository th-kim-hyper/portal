package portal.base;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import portal.base.dto.UserDTO;

import java.io.IOException;
import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JsoupService {

    private static final int TIMEOUT = 10000; // 10초

    public UserDTO mailPlugLogin(String userId, String password) throws IOException {
        String domain = "hyperinfo.co.kr";
        String url = "https://m109.mailplug.com/member/login?host_domain=" + domain + "&cid=" + userId;
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36";
        String phone = "010-1234-5678"; // 예시 전화번호
        String division = "exampleDivision"; // 예시 구분
        String email = userId + "@" + domain;
        Map<String, String> data = new HashMap<>();
        UserDTO userDTO = null;

        Connection connection = Jsoup.connect(url);
        Connection.Response response = connection
             .userAgent(userAgent)
             .timeout(TIMEOUT)
             .method(Connection.Method.GET)
             .execute();

        Document document = response.parse();
        String html = "";
        Map<String, String> cookies = response.cookies();
        cookies.forEach((key, value) -> {
            log.info("Cookie: {} = {}", key, value);
        });
        String csrfMailplugToken = cookies.get("csrf_mailplug_token");
        String ciSession = cookies.get("cisession");
        String showRsb = cookies.get("show_rsb");
        String myValue = cookies.get("myvalue");

//        long timestamp = System.currentTimeMillis();
//        url = "https://m109.mailplug.com/lw_api/passport?t=" + String.valueOf(timestamp) + "&mailplug_token=" + csrfMailplugToken;
//
//        response = connection
//            .userAgent(userAgent)
//            .timeout(TIMEOUT)
//            .ignoreContentType(true)
//            .followRedirects(true)
//            .headers(Map.of(
//                "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8",
//                "Host", "m109.mailplug.com",
//                "Origin", "https://m109.mailplug.com"
//            ))
//            .method(Connection.Method.GET)
//            .url(url)
//            .execute();
//
//        cookies = response.cookies();
//        cookies.forEach((key, value) -> {
//            log.info("Cookie: {} = {}", key, value);
//        });
//
//        document = response.parse();
//        html = document.html();
//        log.info("HTML: {}", html);

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
            .headers(Map.of(
                "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
                "Accept-Encoding", "gzip, deflate",
                "Accept-Language", "ko,ko-KR;q=0.9,en-US;q=0.8,en;q=0.7",
                "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8",
                "Host", "m109.mailplug.com",
                "Origin", "https://m109.mailplug.com",
                "Pragma", "no-cache",
                "Referer", "https://m109.mailplug.com/member/login"
            ))
            .method(Connection.Method.POST)
            .data(data)
            .url(url)
            .execute();

//        var headers = response.headers();
//        headers.forEach((key, value) -> {
//            log.info("Header: {} = {}", key, value);
//        });
//
        cookies = response.cookies();
        cookies.forEach((key, value) -> {
            log.info("Cookie: {} = {}", key, value);
        });

        document = response.parse();
        html = document.html();
        log.info("HTML: {}", html);

        boolean isLoggedIn = html.contains("\"/main\"");

        if (isLoggedIn) {
           url = "https://m109.mailplug.com/main";
           url = "/main";
           response = connection
               .userAgent(userAgent)
               .timeout(TIMEOUT)
               .ignoreContentType(true)
               .followRedirects(true)
               .headers(Map.of(
                   "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
                   "Accept-Encoding", "gzip, deflate",
                   "Accept-Language", "ko,ko-KR;q=0.9,en-US;q=0.8,en;q=0.7",
                   "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8",
                   "Host", "m109.mailplug.com",
                   "Origin", "https://m109.mailplug.com",
                   "Pragma", "no-cache",
                   "Referer", "https://m109.mailplug.com/member/do_login"
                ))
                .method(Connection.Method.GET)
                .url(url)
                .execute();

            document = response.parse();
            html = document.html();
            log.info("HTML: {}", html);

            userDTO = UserDTO.builder()
                .userId(userId)
                .password(password)
                .email(email)
                .phone(phone)
                .division(division)
                .build();
        }

        return userDTO;
    }

}
