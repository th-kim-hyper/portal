package portal.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix="portal")
@Getter
@Setter
@Configuration
public class ApplicationConfig {

	private String name;
	private String version;
	private String storageMode;
	private LinkedHashMap<String, Object> datasource;
	private Ext ext;
	
	@Bean
    @ConfigurationProperties(prefix="portal")
    public Map<String, Object> propertyMap() {
		return new LinkedHashMap<String, Object>();
    }
	
	@Data
	public class Ext {
		private Boolean enable;
	}
	
}
