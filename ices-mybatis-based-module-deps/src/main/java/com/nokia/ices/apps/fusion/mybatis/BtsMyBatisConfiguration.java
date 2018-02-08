package com.nokia.ices.apps.fusion.mybatis;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableConfigurationProperties(CustomDataSourceProperties.class)
public class BtsMyBatisConfiguration{
    
    private final static Logger logger = LoggerFactory.getLogger(BtsMyBatisConfiguration.class);
    
    private  final static String BTSMYSQL = "btsMysql";
    
    @javax.annotation.Resource(name="dbConfig")
    CustomDataSourceProperties customDataSourceProperties;
    
    /**
     * 根据 文件路径加载Mapper文件到SqlSessionFactoryBean
     * @param MapperXmlPath userBossMonitor.xml
     * @return
     */
    @Bean(name="btsSqlSessionFactory")
    @PostConstruct
    public  SqlSessionTemplate getMyBatisConfigurationBts(){
        
         logger.debug("init SqlSessionFactory  bts...............");
        
         SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
         SqlSessionFactory sqlSessionFactory = null;
         SqlSessionTemplate template = null; 
         try {  
             Resource resource = new ClassPathResource("bts-configuration.xml");
             DataSource dataSource = InitDataSource.getDataSource(BTSMYSQL,customDataSourceProperties);
             bean.setDataSource(dataSource);
             bean.setConfigLocation(resource);
             sqlSessionFactory =  bean.getObject();
             template = new SqlSessionTemplate(sqlSessionFactory);
         }catch (Exception e) {  
        	 e.printStackTrace();
             logger.debug("get SqlSessionFactory failed..........bts");
             System.exit(0);  
         }
         return template;
    }


}
