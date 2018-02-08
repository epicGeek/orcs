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
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;
import com.nokia.ices.apps.fusion.score.repository.IndexScoringRuleRepository;
import com.nokia.ices.apps.fusion.score.service.BtsScoreHourService;
import com.nokia.ices.apps.fusion.score.service.IndexScoringRuleService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@Service("indexScoringRuleService")
public class IndexScoringRuleServiceImpl implements IndexScoringRuleService{

	@Autowired
	IndexScoringRuleRepository indexScoringRuleRepository;
	
	@Autowired
	BtsScoreHourService btsScoreHourService;
	
	@Override
	public List<IndexScoringRule> getKpiValueDataList(Map<String,Object> params){
		List<IndexScoringRule> list = indexScoringRuleRepository.findAll();
		return list;
	}
	
	@Override
	public Page<IndexScoringRule> findIndexScoringRulePageBySearch(
			Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType) {
		
		PageRequest pageable = buildPageRequest(page, size, sortType);   
		String KPIId = searchParams.get("kpiId").toString();
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
		
		if(StringUtils.isNotEmpty(KPIId)){
			searchFilterAnd.add(new SearchFilter("kpiId", Operator.LIKE, KPIId));
		}
        Specification<IndexScoringRule> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,IndexScoringRule.class);            		
		return indexScoringRuleRepository.findAll(Specifications.where(specListAnd),pageable);
		
	}	
	/**
     * 创建排序请求.
     */
    private Sort buildSort(List<String> sortType) {
        Sort sort = new Sort(Direction.ASC, "id");
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
	public List<IndexScoringRule> findIndexScoringRuleListByCreator(Map<String, Object> searchParams) {
		
		String KPIId = searchParams.get("kpiId")!=null? searchParams.get("kpiId").toString():"";
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
		
		if(StringUtils.isNotEmpty(KPIId)){
			searchFilterAnd.add(new SearchFilter("kpiId", Operator.EQ, KPIId));
		}
        Specification<IndexScoringRule> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,IndexScoringRule.class);            		
		
		return indexScoringRuleRepository.findAll(Specifications.where(specListAnd));
	}

	@Override
	public List<IndexScoringRule> findIndexScoringRuleAll() {
		return indexScoringRuleRepository.findAll();
	}
	@Override
	public void addIndexScoringRule(IndexScoringRule indexScoringRule) {
		indexScoringRuleRepository.saveAndFlush(indexScoringRule);
	}

}
