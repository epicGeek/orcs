package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;

public interface IndexScoringRuleService {

	Page<IndexScoringRule> findIndexScoringRulePageBySearch(Map<String, Object> searchParams, SystemRole systemRole, int page, int size, List<String> sortType);

	//List<IndexScoringRule> findIndexScoringRuleListByCreator(Map<String, Object> searchParams, List<String> sortType);

	List<IndexScoringRule> findIndexScoringRuleAll();

	void addIndexScoringRule(IndexScoringRule indexScoringRule);
	
	List<IndexScoringRule> getKpiValueDataList(Map<String,Object> params);

	List<IndexScoringRule> findIndexScoringRuleListByCreator(Map<String, Object> searchParams);
}
