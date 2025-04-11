package portal.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class RpaService {

    private static final int TIMEOUT = 10000; // 10초
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36";
    private final Map<String, String> defaultHeaders = new HashMap<>(){
        {
            put("Accept", "*/*");
            put("Accept-Encoding", "gzip, deflate");
            put("Connection", "keep-alive");
            put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            put("Host", "192.168.20.51:8080");
        }
    };

    public String rpaGetTenantList(String userId, String password) throws IOException, URISyntaxException {
        String result;
        String url = "https://192.168.20.51:8080/admin";
        Connection connection = Jsoup.connect(url)
            .userAgent(userAgent)
            .timeout(TIMEOUT)
            .headers(defaultHeaders);
        Map<String, String> formData = new HashMap<>();

        // 01. 로그인폼 (GET)
        connection.url(url).get();

        // 02. 테넌트 목록 가져오기 (POST)
        url = "https://192.168.20.51:8080/admin/authenticateUser";

        formData.clear();
        formData.put("j_username", userId);
        formData.put("j_password", password);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(formData);

        defaultHeaders.put("Content-Type", "application/json+lego; charset=UTF-8");

        Connection.Response response = connection
            .url(url)
            .ignoreContentType(true)
            .followRedirects(false)
            .headers(defaultHeaders)
            .requestBody(requestBody)
            .method(Connection.Method.POST)
            .execute();

        if(response != null && response.statusCode() == 200) {
            Map<String, String> cookies = response.cookies();
            String jSession = cookies.get("JSESSIONID");
            log.info("#### jSession: {}", jSession);
            result = response.body();
        } else {
            result = null;
        }

        return result;
    }

    public String rpaAdminLogin(String userId, String password, String tenantId) throws IOException, URISyntaxException {
        String result = null;
        String url = "https://192.168.20.51:8080/admin/j_spring_security_check";

        Connection connection = Jsoup.connect(url).userAgent(userAgent).timeout(TIMEOUT);

        Map<String, String> formData = new HashMap<>();
        formData.clear();
        formData.put("loginTenant", tenantId);
        formData.put("tenantPortalPlaceHolder", "테넌트를 선택해주세요.");
        formData.put("language", "KO");
        formData.put("j_username", userId);
        formData.put("j_password", password);

        defaultHeaders.put("Content-Type", "applicationation/x-www-form-urlencoded");

        Connection.Response response = connection
            .url(url)
            .ignoreContentType(true)
            .followRedirects(true)
            .headers(defaultHeaders)
            .method(Connection.Method.POST)
            .data(formData)
            .execute();

        Document document = response.parse();
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
        return result;
    }
}
