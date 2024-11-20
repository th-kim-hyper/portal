package portal.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@DependsOn("primaryDataSource")
@Slf4j
@Configuration
@MapperScan(value="${mybatis.primary.mapper-scan-base-packages}", sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class PrimaryMyBatisConfig {
	
	@Value("${mybatis.primary.mapper-config-location}")
	private String primaryConfigLocation;
	
	@Value("${mybatis.primary.mapper-locations}")
	private String primaryMapperLocation;
	
	@Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("primaryDataSource") DataSource dataSource,
            ApplicationContext applicationContext) throws Exception {
		
		log.info("#### primaryConfigLocation:" + primaryConfigLocation);
		log.info("#### primaryMapperLocation:" + primaryMapperLocation);
		log.info("#### primarySqlSessionFactory Bean Create");
		
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(primaryConfigLocation));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(primaryMapperLocation));
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    	log.info("#### primarySqlSessionTemplate Bean Create");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    @Primary
    @Bean(name = "primaryTransaction")
    public PlatformTransactionManager transactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    } 
}
