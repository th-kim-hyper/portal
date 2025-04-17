package portal.config;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ApplicationConfig {

	@PostConstruct
	private void init() {
		log.info("#### ApplicationConfig init");
	}

	@Bean("portalProperties")
	@ConfigurationProperties(prefix = "portal")
	public PortalProperties portalProperties() {
		log.info("#### portalProperties Bean Create");
		return new PortalProperties();
	}

	@Setter
	@Getter
	public static class PortalProperties {
		private String name;
		private String version;
		private String storageMode;
		private Properties datasource;
		private List<String> publicPaths;
		private IpBlock ipBlock;
		private String ocrBaseUrl;
		private String ocrApiKey;
	}

	@Getter
	@Setter
	public static class IpBlock {
		private Boolean enable;
		private List<String> blacklist;
		private List<String> whitelist;
	}

}
