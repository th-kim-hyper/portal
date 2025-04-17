package portal.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Properties;

@Slf4j
@Configuration
public class HttpClientConfig {

	@PostConstruct
	private void init() {
		log.info("#### HttpClientConfig init");
	}

	@Bean
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(httpClient());
		return new RestTemplate(factory);
	}

	@Bean
	public CloseableHttpClient httpClient() {
		return HttpClients.custom()
			.setConnectionManager(new PoolingHttpClientConnectionManager())
			.build();
	}

}
