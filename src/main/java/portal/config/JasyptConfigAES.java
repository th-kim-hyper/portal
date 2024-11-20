//package portal.config;
//
//import org.jasypt.encryption.StringEncryptor;
//import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
//import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Configuration
//@EnableEncryptableProperties
//@RequiredArgsConstructor
//public class JasyptConfigAES {
//
//	final private ApplicationConfig applicationConfig;
//
//	@DependsOn("applicationConfig")
//	@Bean("jasyptEncryptorAES")
//	public StringEncryptor stringEncryptor() {
//
//		log.info("#### jasyptEncryptorAES Bean Create");
//
//		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
////		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//		SimpleStringPBEConfig config = applicationConfig.simpleStringPBEConfig();
//
//		log.info("#### Password: {}", config.getPassword());
//		log.info("#### Algorithm: {}", config.getAlgorithm());
//		log.info("#### KeyObtentionIterations: {}", config.getKeyObtentionIterations());
//		log.info("#### PoolSize: {}", config.getPoolSize());
//		log.info("#### ProviderName: {}", config.getProviderName());
//		log.info("#### StringOutputType: {}", config.getStringOutputType());
////		config.setPassword("gdh-password"); // 암호화키
////		config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); // 알고리즘
////		config.setKeyObtentionIterations("1000"); // 반복할 해싱 회수
////		config.setPoolSize("1"); // 인스턴스 pool
////		config.setProviderName("SunJCE");
////		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
////		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
////        config.setStringOutputType("base64"); //인코딩 방식
//		encryptor.setConfig(config);
//		return encryptor;
//	}
//}
