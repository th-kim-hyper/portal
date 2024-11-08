package portal.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MultiDataSourceConfig {

	@Bean
    @Primary
    @Qualifier("primaryHikariConfig")
    @ConfigurationProperties(prefix="spring.datasource.hikari.primary")
    public HikariConfig primaryHikariConfig() {
		log.debug("#### primaryHikariConfig Bean Create");
		return new HikariConfig();
    }

    @Bean
    @Primary
    @Qualifier("primaryDataSource")
    public DataSource primaryDataSource() throws Exception {
    	log.debug("#### primaryDataSource Bean Create");
        return new HikariDataSource(primaryHikariConfig());
    }

	@ConditionalOnProperty("portal.datasource.rpa")
    @Bean
    @Qualifier("rpaHikariConfig")
    @ConfigurationProperties(prefix="spring.datasource.hikari.rpa")
    public HikariConfig rpaHikariConfig() {
    	log.debug("#### rpaHikariConfig Bean Create");
        return new HikariConfig();
    }

	@ConditionalOnProperty("portal.datasource.rpa")
    @Bean
    @Qualifier("rpaDataSource")
    public DataSource rpaDataSource() throws Exception {
    	log.debug("#### rpaDataSource Bean Create");
        return new HikariDataSource(rpaHikariConfig());
    }
}
