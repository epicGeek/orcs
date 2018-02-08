package com.nokia.ices.apps.fusion.score.service.impl;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.score.domain.InformationCell;
import com.nokia.ices.apps.fusion.score.repository.InformationCellRepository;
import com.nokia.ices.apps.fusion.score.service.InformationCellService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@Service("informationCellService")
public class InformationCellServiceImpl implements InformationCellService{

	@Autowired
	InformationCellRepository informationCellRepository;
	
	@Override
	@SuppressWarnings("unchecked")
	public Page<InformationCell> findInformationCellPageBySearch(Map<String, Object> searchParams, SystemRole systemRole, int page,
			int size, List<String> sortType) {
		
		PageRequest pageable = buildPageRequest(page, size, sortType);  
		
		String neCode = searchParams.get("neCode").toString();
	 	String icesArea = searchParams.get("icesArea").toString();
	 	/*	String areaCode = searchParams.get("areaCode").toString();*/
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	
	 	//基站ID
	 	if(StringUtils.isNotEmpty(neCode)){
			searchFilterAnd.add(new SearchFilter("neCode", Operator.EQ, neCode));
		}
	 	if(StringUtils.isNotEmpty(icesArea)){
			searchFilterAnd.add(new SearchFilter("icesArea", Operator.EQ, icesArea));
		}
	/* 	if(StringUtils.isNotEmpty(areaCode)){
			searchFilterAnd.add(new SearchFilter("areaCode", Operator.LIKE, areaCode));
		}*/
		
	 	Specification<InformationCell> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,InformationCell.class);            		
		return informationCellRepository.findAll(Specifications.where(specListAnd),pageable);
	}
	/**
     * 创建排序请求.
     */
    private Sort buildSort(List<String> sortType) {
        Sort sort = new Sort(Direction.DESC, "id");
        for (String orderStr : sortType) {
            String[] order = orderStr.split(",");
            if (order.length == 1 || order[1].equalsIgnoreCase("asc"))
                sort.and(new Sort(Direction.ASC, order[0]));
            else
                sort.and(new Sort(Direction.DESC, order[0]));

        }
        return sort;
    }
	  /**
     * 创建分页.
     */
    private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
        Sort sort = buildSort(sortType);
        return new PageRequest(page - 1, size, sort);
    }
}
