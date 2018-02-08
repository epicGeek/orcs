package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.nokia.ices.apps.fusion.score.domain.InformationCell;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

public interface InformationCellService {

	Page<InformationCell> findInformationCellPageBySearch(
			Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType);

}
