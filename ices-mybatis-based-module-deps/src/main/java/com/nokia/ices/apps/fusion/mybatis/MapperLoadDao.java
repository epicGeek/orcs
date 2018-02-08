package com.nokia.ices.apps.fusion.mybatis;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;


@Component
public  class MapperLoadDao {
    
   
    
    @Resource(name="mySqlSessionFactory")
    SqlSessionTemplate template;
    
    @Resource(name="btsSqlSessionFactory")
    SqlSessionTemplate templateBts;
    
    /**
     * 根据Dao接口类获取实体Bean
     * @param classs
     * @return
     */
    public  <T> T  getObject(Class<T> classs){
        return template.getMapper(classs);
    }
    
    public  <T> T  getBtsObject(Class<T> classs){
        
        return templateBts.getMapper(classs);
    }

}
