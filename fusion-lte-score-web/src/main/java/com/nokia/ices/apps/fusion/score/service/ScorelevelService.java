package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.nokia.ices.apps.fusion.score.domain.Scorelevel;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

public interface ScorelevelService {

	 Page<Scorelevel> findScorelevelPageBySearch(
			Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType);

	 List<Scorelevel> findScorelevelListByCreator(Map<String, Object> searchParams, List<String> sortType);
	 List<Scorelevel> findScorelevelAll();

	void addScorelevel(Scorelevel scorelevel);
}
