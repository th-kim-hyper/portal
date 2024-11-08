package portal.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnProperty("portal.datasource.rpa")
@Slf4j
@Configuration
@MapperScan(value="${mybatis.rpa.mapper-scan-base-packages}", sqlSessionTemplateRef ="rpaSqlSessionTemplate")
public class RpaMyBatisConfig {
	
	@Value("${mybatis.rpa.mapper-config-location}")
	private String rpaConfigLocation;
	
	@Value("${mybatis.rpa.mapper-locations}")
	private String rpaMapperLocation;
	
    @Bean(name = "rpaSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("rpaDataSource") DataSource dataSource,
            ApplicationContext applicationContext) throws Exception {
    	
		log.info("#### rpaConfigLocation:" + rpaConfigLocation);
		log.info("#### rpaMapperLocation:" + rpaMapperLocation);
		log.info("#### rpaSqlSessionFactory Bean Create");
		
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(rpaConfigLocation));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(rpaMapperLocation));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "rpaSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("rpaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    	log.info("#### rpaSqlSessionTemplate Bean Create");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    @Bean(name = "rpaTransaction")
    public PlatformTransactionManager transactionManager(@Qualifier("rpaDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
