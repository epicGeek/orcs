package com.nokia.ices.apps.fusion.log.service;

import java.util.List;
import com.nokia.ices.apps.fusion.log.domain.WorkPackage;

/**
 * 创建日志各种下拉获取顶层接口
 * @author Administrator
 *
 */
public interface SelectService {
	
	public List<?> queryByListAll(String type);
	
	public List<WorkPackage> queryByWorkPackageList(Long workTypeId);

}
