package com.nokia.ices.apps.fusion.log.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.log.domain.Product;
import com.nokia.ices.apps.fusion.log.service.BaseDataService;


@RestController
public class baseDataController {

	public static final Logger logger = LoggerFactory.getLogger(baseDataController.class);

	@Autowired
	BaseDataService baseDataService;
	
	@RequestMapping(value = "/queryAll")
    public Map<String, Object> getinfoAll(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		Integer page = null == parameterMap.get("start") ? 1 : Integer.valueOf(parameterMap.get("start")[0]);
		Integer pageSize = null == parameterMap.get("length") ? 10 : Integer.valueOf(parameterMap.get("length")[0]);
		String type = null == parameterMap.get("type") ? "" : parameterMap.get("type")[0];
		
		List<String> sortSet = new ArrayList<String>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		
		searchParams.put("type", type);
		searchParams.put("start", page);
		searchParams.put("draw", page);
		searchParams.put("limit", pageSize);
		
		sortSet.add("id,asc");
		Map<String, Object> resultData = new HashMap<String, Object>();
		
		try{
			Page<?> result=  baseDataService.queryByListAll(searchParams,null, page, pageSize, sortSet);
			resultData.put("data", result.getContent());
			resultData.put("iTotalRecords", result.getTotalElements());
			resultData.put("iTotalDisplayRecords", result.getTotalElements());
		}catch(Exception e){
			logger.debug("select/baseDataController is error ........"+e.getMessage());
		}
        return resultData;
    }
 
    /**
     *基础数据维护 产品
     * @param iDisplayStart
     * @param iDisplayLength
     * @return
     */
    @RequestMapping(value = "query/productData")
	public Map<String,Object> productData(@RequestParam("start") Integer iDisplayStart, 
			@RequestParam("length") Integer iDisplayLength) {
    	
	    	List<String> sortSet = new ArrayList<String>();
			sortSet.add("orderby,asc");
			iDisplayStart= iDisplayStart/iDisplayLength+1;  //分页
			Map<String, Object> resultData = new HashMap<String, Object>();
			try{
				Page<Product> page =baseDataService.findProductDataPageBySearch(null, iDisplayStart, iDisplayLength, sortSet);
				resultData.put("data", page.getContent());
				resultData.put("iTotalRecords", page.getTotalElements());
				resultData.put("iTotalDisplayRecords", page.getTotalElements());
			}catch(Exception e){
	    		logger.debug(" query productData is error:"+e.getMessage());
	    	}
			return resultData;
   }
    
    
    /**
     * 删除 产品
     * @param ids
     * @return
     */
    @RequestMapping(value = "deleteProduct/{ids}")
    public Long deleteProduct(@PathVariable String ids){
    	
    	try{
    		String [] arrIds  = ids.split(",");
    		for(String id: arrIds){
    			baseDataService.deleteByProduct(Long.parseLong(id));
    		}
    		return 0L;
    	}catch(Exception e){
    		logger.debug("delete logData is error:"+e.getMessage());
    	}
    	return 1L;
    }
    
    /**
     * 保存  添加或者编辑产品
     * @return
     */
    @RequestMapping(value = "saveOrEdit_product")
    public String saveLog(@RequestBody Product []  product){
    	
    	String result = "false";
    	try{
    		for(Product his : product){
    			String id = his.getId()!=null?his.getId() .toString():"";
    			//his.setStatus(true);
    			if(StringUtils.isNotEmpty(id)){
    				his.setId(Long.parseLong(id));
    				baseDataService.updateProduct(his);
    			}else{
    				baseDataService.addProduct(his);
    			}
    		}
    		result = "true";
    	}catch(Exception e){
    		logger.debug("save product is error:"+e.getMessage());
    	}
    	return result;
    }
    
}
