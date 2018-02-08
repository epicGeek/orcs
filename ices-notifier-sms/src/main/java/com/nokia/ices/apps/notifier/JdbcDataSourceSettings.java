package com.nokia.ices.apps.notifier;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate; 
 
@Configuration 
@ConfigurationProperties
public class JdbcDataSourceSettings {
    
   
	private static final Logger logger = LoggerFactory.getLogger(JdbcDataSourceSettings.class);
    
    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource primaryDataSource() {

        logger.debug("######################");
        logger.debug("#                    #");
        logger.debug("#  init 1st Database #");
        logger.debug("#                    #");
        logger.debug("######################");
        return DataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix="spring.other.datasource")
    public DataSource dataSourceSMS() {
        logger.debug("#########################");
        logger.debug("#                       #");
        logger.debug("# init OSS Database 1st #");
        logger.debug("#                       #");
        logger.debug("#########################");
        return DataSourceBuilder.create().build();
    }
 
    
    @Bean(name="jdbcTemplateSMS")
    public JdbcTemplate jdbcTemplateSMS(){
        return new JdbcTemplate(dataSourceSMS());
    }
    @Bean(name="jdbcTemplatePrimary")
    @Primary
    public JdbcTemplate jdbcTemplatePrimary(){
        return new JdbcTemplate(primaryDataSource());
    }
} 