package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

import com.nokia.ices.apps.fusion.score.domain.BtsProxy;

//基站基础信息
public interface BtsInformationCellService {
	
	  Map<String,Object>  findBtsAll(Map<String, Object> params);
	  List<BtsProxy> findBtsProxy();
}
