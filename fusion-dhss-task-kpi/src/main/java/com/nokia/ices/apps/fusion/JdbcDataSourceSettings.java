package com.nokia.ices.apps.fusion;

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
    @ConfigurationProperties(prefix="spring.jdbc.oss.datasource")
    public DataSource ossDataSource() {
        logger.debug("######################");
        logger.debug("#                    #");
        logger.debug("# init OSS Database  #");
        logger.debug("#                    #");
        logger.debug("######################");
        return DataSourceBuilder.create().build();
    }
/*    @Bean
    @ConfigurationProperties(prefix="spring.jdbc.2nd.datasource")
    public DataSource secondaryDataSource() {
        logger.debug("######################");
        logger.debug("#                    #");
        logger.debug("# init 2nd Database  #");
        logger.debug("#                    #");
        logger.debug("######################");
        return DataSourceBuilder.create().build();
    }*/
    
    @Bean(name="jdbcTemplateOss")
    public JdbcTemplate jdbcTemplateOss(){
        return new JdbcTemplate(ossDataSource());
    }
/*    @Bean(name="jdbcTemplateSecondary")
    public JdbcTemplate jdbcTemplateSecondary(){
        return new JdbcTemplate(secondaryDataSource());
    }*/
    @Bean(name="jdbcTemplatePrimary")
    @Primary
    public JdbcTemplate jdbcTemplatePrimary(){
        return new JdbcTemplate(primaryDataSource());
    }
} 