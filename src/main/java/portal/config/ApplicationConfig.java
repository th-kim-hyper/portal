package portal.config;

import java.util.List;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import portal.base.Role;

@Slf4j
@Configuration("applicationConfig")
public class ApplicationConfig {

	@Bean("portalProperties")
    @ConfigurationProperties(prefix="portal")
    public PortalProperties portalProperties() {
		log.info("#### portalProperties Bean Create");
		return new PortalProperties();
    }

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> User.builder()
			.username("username")
			.password("username")
			.roles(Role.USER.name())
			.build();
	}
	
	@Getter
	@Setter
	public static class PortalProperties {
		private String name;
		private String version;
		private String storageMode;
		private Properties datasource;
		private Ext ext;
		private List<String> whitelist;
	}
	
	@Getter
	@Setter
	public static class Ext {
		private Boolean enable;
	}
	
}
