package com.nokia.ices.apps.fusion.log.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.nokia.ices.apps.fusion.log.domain.Product;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;


/**
 * 创建日志各种下拉获取顶层接口
 * @author Administrator
 *
 */
public interface BaseDataService {
	
	void deleteByProduct(Long id);  //删除

	void updateProduct(Product product); //编辑

	 void addProduct(Product product);  //添加
	 /**
	  *基础数据维护 产品
	  * @param searchParams
	  * @param iDisplayStart
	  * @param iDisplayLength
	 * @param iDisplayLength2 
	  * @param sortType
	  * @return
	  * @throws ParseException
	  */
	 Page<Product> findProductDataPageBySearch(SystemRole systemRole,Integer iDisplayStart,
			 Integer iDisplayLength, List<String> sortType) throws ParseException;

	 List<Product> findProductDataListByCreator(Map<String, Object> searchParams) throws ParseException;
	 /**
	  * 基础数据维护
	  * @param searchParams
	  * @param systemRole
	  * @param page
	  * @param pageSize
	  * @param sortType
	  * @return
	  */
	Page<?> queryByListAll(Map<String, Object> searchParams, SystemRole systemRole, Integer page, Integer pageSize,
			List<String> sortType);

	Page<?> findListByCreator(Map<String, Object> searchParams) throws ParseException;
}
