package com.nokia.ices.apps.fusion.mybatis;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class InitDataSource {
    
    private final static Logger logger = LoggerFactory.getLogger(InitDataSource.class);
    
    /**
     * 数据连接
     * @param dbType
     * @return
     */
    public static DataSource  getDataSource(final String  dbType,CustomDataSourceProperties customDataSourceProperties){
    	
    	DataSource   dataSource =null;
        try{
        	  dataSource = new DataSource();
        	  dataSource.setDriverClassName(customDataSourceProperties.getDriverClassName());
              dataSource.setInitialSize(customDataSourceProperties.getInitialSize());
              dataSource.setMaxActive(customDataSourceProperties.getMaxActive());
              dataSource.setMinIdle(customDataSourceProperties.getMinIdle());
              dataSource.setMaxWait(customDataSourceProperties.getMaxWait());
              boolean testOnBorrow = Boolean.parseBoolean(customDataSourceProperties.getTestOnBorrow());
              dataSource.setTestOnBorrow(testOnBorrow);
              boolean testWhileIdle = Boolean.parseBoolean(customDataSourceProperties.getTestWhileIdle());
              dataSource.setTestWhileIdle(testWhileIdle);
              boolean testOnReturn = Boolean.parseBoolean(customDataSourceProperties.getTestOnReturn());
              dataSource.setTestOnReturn(testOnReturn);
              dataSource.setMinEvictableIdleTimeMillis(customDataSourceProperties.getMinEvictableIdleTimeMillis());
              dataSource.setTimeBetweenEvictionRunsMillis(customDataSourceProperties.getTimeBetweenEvictionRunsMillis());
              dataSource.setValidationQuery(customDataSourceProperties.getValidationQuery());
              dataSource.setValidationInterval(customDataSourceProperties.getValidationInterval());
        	  if("defaultMysql".equalsIgnoreCase(dbType)){
        		  dataSource.setUrl(customDataSourceProperties.getUrl());
                  dataSource.setUsername(customDataSourceProperties.getUsername());
                  dataSource.setPassword(customDataSourceProperties.getPassword());
        	  }else if("btsMysql".equalsIgnoreCase(dbType)){
        		  dataSource.setUrl(customDataSourceProperties.getIceUrl());
                  dataSource.setUsername(customDataSourceProperties.getIceUsername());
                  dataSource.setPassword(customDataSourceProperties.getIcePassword());
        	  }else{
        		  logger.debug("Does not support the database connection..........."); 
        	  }
        	  
        }catch(Exception e){
            logger.debug("Access to the database connection failed..........."); 
        }
        return dataSource;
    }
    
   

}
