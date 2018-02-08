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
import com.nokia.ices.apps.fusion.score.domain.Scorelevel;
import com.nokia.ices.apps.fusion.score.repository.ScorelevelRepository;
import com.nokia.ices.apps.fusion.score.service.ScorelevelService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@Service("scorelevelService")
public class ScorelevelServiceImpl implements ScorelevelService{

	@Autowired
	ScorelevelRepository scorelevelRepository;
	
	@Override
	@SuppressWarnings("unchecked")
	public Page<Scorelevel> findScorelevelPageBySearch(
			Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType) {
		
		PageRequest pageable = buildPageRequest(page, size, sortType);   
		String scorefrom = searchParams.get("scorefrom").toString();
	 	String scoreto = searchParams.get("scoreto").toString();
	 	String level = searchParams.get("level").toString();
		
		//List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>(); 
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	if(StringUtils.isNotEmpty(scorefrom)){
			searchFilterAnd.add(new SearchFilter("scorefrom", Operator.EQ, scorefrom));
		}
	 	if(StringUtils.isNotEmpty(scoreto)){
			searchFilterAnd.add(new SearchFilter("scoreto", Operator.EQ,  scoreto));
		}
	 	if(StringUtils.isNotEmpty(level)){
			searchFilterAnd.add(new SearchFilter("level", Operator.EQ,  level));
		}
	 	
	 	Specification<Scorelevel> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,Scorelevel.class);            		
		return scorelevelRepository.findAll(Specifications.where(specListAnd),pageable);
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
    
	@Override
	@SuppressWarnings("unchecked")
	public List<Scorelevel> findScorelevelListByCreator(Map<String, Object> searchParams, List<String> sortType) {
		
		String scorefrom = searchParams.get("scorefrom").toString();
	 	String scoreto = searchParams.get("scoreto").toString();
	 	String level = searchParams.get("level").toString();
		
		//List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>(); 
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	if(StringUtils.isNotEmpty(scorefrom)){
			searchFilterAnd.add(new SearchFilter("scorefrom", Operator.EQ, scorefrom));
		}
	 	if(StringUtils.isNotEmpty(scoreto)){
			searchFilterAnd.add(new SearchFilter("scoreto", Operator.EQ,  scoreto));
		}
	 	if(StringUtils.isNotEmpty(level)){
			searchFilterAnd.add(new SearchFilter("level", Operator.EQ,  level));
		}
	 	
	 	Specification<Scorelevel> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,Scorelevel.class);            		
		return scorelevelRepository.findAll(Specifications.where(specListAnd));
	}
	@Override
	public List<Scorelevel> findScorelevelAll() {
		return scorelevelRepository.findAll();
	}
	@Override
	public void addScorelevel(Scorelevel scorelevel) {
		scorelevelRepository.saveAndFlush(scorelevel);
	}

}
