package com.nokia.ices.apps.fusion.score.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.mybatis.MapperLoadDao;
import com.nokia.ices.apps.fusion.score.domain.BtsProxy;
import com.nokia.ices.apps.fusion.score.repository.BtsInformationCellRepository;
import com.nokia.ices.apps.fusion.score.service.BtsInformationCellService;

//基站基础信息
@Service("btsService")
public class BtsInformationCellServiceImpl implements BtsInformationCellService,InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(BtsInformationCellServiceImpl.class);
	
	@Autowired
    MapperLoadDao mapperLoadDao;
	
	BtsInformationCellRepository  btsDao;
	
	@Override
	public  Map<String,Object> findBtsAll(Map<String, Object> params) {
		Integer total = btsDao.findBtsCount(params);
		List<Map<String,Object>> btsList = btsDao.findBtsAll(params);
	    Map<String,Object> resusltMap  = new HashMap<String, Object>();
	    resusltMap.put("totalElements", total);
	    resusltMap.put("content", btsList);
	        
		return resusltMap;
	}
	
	 @Override
     public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        logger.debug("init bossBusinessDao Bean success.............");
        btsDao = mapperLoadDao.getObject(BtsInformationCellRepository.class);
        
     }

	@Override
	public List<BtsProxy> findBtsProxy() {
		List<BtsProxy> btsList = btsDao.findAllBtsProxyList();
	    return btsList;
	}

}
