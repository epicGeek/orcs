package com.nokia.ices.apps.fusion.log.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.log.domain.HistoryData;
import com.nokia.ices.apps.fusion.log.repository.HistoryDataRepository;
import com.nokia.ices.apps.fusion.log.service.SystemLogService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@Service("systemLogService")
public class SystemLogServiceImpl implements SystemLogService{

	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	HistoryDataRepository historyDataRepository;
	

	/**
	 * 历史记录
	 * @throws ParseException 
	 * 查询
	 */
	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=true)
	public Page<HistoryData> findhistoryDataPageBySearch(Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType) throws ParseException {
		
		PageRequest pageable = buildPageRequest(page, size, sortType);   
		
		String userName = searchParams.get("userName")!=null?searchParams.get("userName").toString():"";
		String startDate = searchParams.get("startDate")!=null?searchParams.get("startDate").toString():"";
		String endDate = searchParams.get("endDate")!=null?searchParams.get("endDate").toString():"";
		
		/*ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(StringUtils.isEmpty(userName)){
			userName = shiroUser.getRealName();
		}*/
		
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
	 	 
	 	if(StringUtils.isNotEmpty(userName)){
			searchFilterAnd.add(new SearchFilter("userName", Operator.EQ, userName));
		}
	 	if(StringUtils.isNotEmpty(startDate)){
			searchFilterAnd.add(new SearchFilter("logDate", Operator.GE,  SDF.parse(startDate)));  //大于等于
		}
	 	if(StringUtils.isNotEmpty(endDate)){
			searchFilterAnd.add(new SearchFilter("logDate", Operator.LE, SDF.parse(endDate))); //小于等于
		}
	 	
		Specification<HistoryData> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,HistoryData.class);            		
		return historyDataRepository.findAll(Specifications.where(specListAnd),pageable);
		
	}

	/**
     * 创建排序请求. 历史记录
     */
    private Sort buildSort(List<String> sortType) {
    	List<Order> listSort= new ArrayList<Order>();
        for (String orderStr : sortType) {
            String[] order = orderStr.split(",");
            if (order[1].equalsIgnoreCase("asc")){
            	listSort.add(new Order (Direction.ASC,order[0]));
            }else{
            	listSort.add(new Order (Direction.DESC,order[0]));
            }
        }
        return new Sort(listSort);
    }
	  /**
     * 创建分页.
     */
    private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
        Sort sort = buildSort(sortType);
        return new PageRequest(page-1, size, sort);
    }



	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=true)
	public List<HistoryData> findHistoryDataListByCreator(Map<String, Object> searchParams) throws ParseException {
		
		String logDate = searchParams.get("logDate")==null?"":searchParams.get("logDate").toString();
		String startDate = searchParams.get("startDate")==null?"":searchParams.get("startDate").toString();
		String endDate = searchParams.get("endDate")==null?"":searchParams.get("endDate").toString();
		String userName = searchParams.get("userName")!=null?searchParams.get("userName").toString():"";
	/*	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		if(StringUtils.isEmpty(userName)){
		//	String userName = shiroUser.getRealName();
			userName = shiroUser.getRealName();
		}*/
		
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
	 	if(StringUtils.isNotEmpty(logDate)){
	 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			searchFilterAnd.add(new SearchFilter("logDate", Operator.EQ,  sdf.parse(logDate)));
		}
	 	if(StringUtils.isNotEmpty(userName)){
			searchFilterAnd.add(new SearchFilter("userName", Operator.EQ,  userName));
		}
	 	if(StringUtils.isNotEmpty(startDate)){
			searchFilterAnd.add(new SearchFilter("logDate", Operator.GE,  SDF.parse(startDate)));
		}
	 	if(StringUtils.isNotEmpty(endDate)){
			searchFilterAnd.add(new SearchFilter("logDate", Operator.LE, SDF.parse(endDate)));
		}
	 	
		Specification<HistoryData> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,HistoryData.class);            		
		return historyDataRepository.findAll(Specifications.where(specListAnd));
	}
	
	@Override
	public List<HistoryData> exportData(Map<String, Object> searchParams) throws ParseException {
		// TODO Auto-generated method stub
		
		String startDate = searchParams.get("startDate")==null?"":searchParams.get("startDate").toString();
		String endDate = searchParams.get("endDate")==null?"":searchParams.get("endDate").toString();
		String userName = searchParams.get("userName")==null?"":searchParams.get("userName").toString();
		List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
		
		if(StringUtils.isNotEmpty(userName)){
			searchFilterAnd.add(new SearchFilter("userName", Operator.EQ,  userName));
		}
	 	if(StringUtils.isNotEmpty(startDate)){
			searchFilterAnd.add(new SearchFilter("logDate", Operator.GE,  SDF.parse(startDate)));
		}
	 	if(StringUtils.isNotEmpty(endDate)){
			searchFilterAnd.add(new SearchFilter("logDate", Operator.LE, SDF.parse(endDate)));
		}
		
	 	Specification<HistoryData> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,HistoryData.class);            		
		return historyDataRepository.findAll(Specifications.where(specListAnd));
	}


	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void addHistoryData(HistoryData historyData) {
		 historyDataRepository.save(historyData);
	}

	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void deleteByIdLogData(Long id) {
		
		historyDataRepository.delete(id);
	}

	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void updateHistoryDataName(HistoryData historyData) {
		
		historyDataRepository.updateHistoryDataName(historyData.getProduct(),historyData.getModular(),historyData.getProjectName(),
				historyData.getStage(),historyData.getWorkPackageType(),historyData.getWorkPackage(),
				historyData.getTime(),historyData.getJobOperator(),historyData.getId());
		
	}

}
