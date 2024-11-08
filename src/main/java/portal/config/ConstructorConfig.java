//package portal.config;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import lombok.Data;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@RequiredArgsConstructor
//@ConfigurationProperties(prefix="portal")
//public final class ConstructorConfig {
//
//	private final String name;
//	private final String version;
//	private final String storageMode;
//	private final LinkedHashMap<String, Object> datasource;
//	private final Ext ext;
//	
//	@Bean
//    @ConfigurationProperties(prefix="portal")
//    public Map<String, Object> propertyMap() {
//		return new LinkedHashMap<String, Object>();
//    }
//	
//	@Getter
//	@RequiredArgsConstructor
//	public static final class Ext {
//		private final Boolean enable;
//	}
//	
//}
