package com.nokia.ices.apps.fusion.mybatis;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
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
public class MyBatisConfiguration{
    
    private final static Logger logger = LoggerFactory.getLogger(MyBatisConfiguration.class);
    
    private  final static String DEFAULTMYSQL = "defaultMysql";
    
    @javax.annotation.Resource(name="dbConfig")
    CustomDataSourceProperties customDataSourceProperties;
    /**
     * 根据 文件路径加载Mapper文件到SqlSessionFactoryBean
     * @return
     */
    @Bean(name="mySqlSessionFactory")
    @PostConstruct
    public  SqlSessionTemplate getMyBatisConfiguration(){
        
         logger.debug("init SqlSessionFactory ...............");
        
         SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
         SqlSessionFactory sqlSessionFactory = null;
         SqlSessionTemplate template = null; 
         try {  
             Resource resource = new ClassPathResource("configuration.xml");
             //数据库连接池参数设置
             DataSource dataSource = InitDataSource.getDataSource(DEFAULTMYSQL,customDataSourceProperties);
             bean.setDataSource(dataSource);
             bean.setConfigLocation(resource);
             sqlSessionFactory =  bean.getObject();
             template = new SqlSessionTemplate(sqlSessionFactory);
         }catch (Exception e) {  
        	 e.printStackTrace();
             logger.debug("get SqlSessionFactory failed.........."); 
             System.exit(0);  
         }
         return template;
    }
    

}
