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
        logger.debug("#66666666666666666666#");
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix="spring.jdbc.2nd.datasource")
    public DataSource secondaryDataSource() {
        logger.debug("######################");
        logger.debug("#                    #");
        logger.debug("#  init 2nd Database #");
        logger.debug("#                    #");
        logger.debug("######################");
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix="spring.jdbc.boss.datasource")
    public DataSource bossDataSource() {
        logger.debug("######################");
        logger.debug("#                    #");
        logger.debug("# init boss Database #");
        logger.debug("#                    #");
        logger.debug("######################");
        return DataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix="spring.jdbc.boss-rev.datasource")
    public DataSource bossRevolutionDataSource() {
        logger.debug("##########################");
        logger.debug("#                        #");
        logger.debug("# init boss-rev Database #");
        logger.debug("#                        #");
        logger.debug("##########################");
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
   /* @Bean
    @ConfigurationProperties(prefix="spring.jdbc.kpi.datasource")
    public DataSource kpiDataSource() {
        logger.debug("######################");
        logger.debug("#                    #");
        logger.debug("# init kpi Database  #");
        logger.debug("#                    #");
        logger.debug("######################");
        return DataSourceBuilder.create().build();
    }*/
    
    @Bean(name="jdbcTemplateBoss-rev")
    public JdbcTemplate jdbcTemplateBossRev(){
        return new JdbcTemplate(bossRevolutionDataSource());
    }
    @Bean(name="jdbcTemplateBoss")
    public JdbcTemplate jdbcTemplateBoss(){
        return new JdbcTemplate(bossDataSource());
    }
    @Bean(name="jdbcTemplateOss")
    public JdbcTemplate jdbcTemplateOss(){
        return new JdbcTemplate(ossDataSource());
    }
    
    @Bean(name="jdbcTemplate2nd")
    public JdbcTemplate jdbcTemplate2nd(){
        return new JdbcTemplate(secondaryDataSource());
    }

    @Bean(name="jdbcTemplatePrimary")
    @Primary
    public JdbcTemplate jdbcTemplatePrimary(){
        return new JdbcTemplate(primaryDataSource());
    }
   /* @Bean(name="jdbcTemplateKpi")
    public JdbcTemplate jdbcTemplateKpi(){
        return new JdbcTemplate(kpiDataSource());
    }*/
} 