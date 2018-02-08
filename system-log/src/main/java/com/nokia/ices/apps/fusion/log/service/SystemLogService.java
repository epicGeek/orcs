package com.nokia.ices.apps.fusion.log.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import com.nokia.ices.apps.fusion.log.domain.HistoryData;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;


public interface SystemLogService {


	 Page<HistoryData> findhistoryDataPageBySearch(Map<String, Object> searchParams, SystemRole systemRole,int page, int size, List<String> sortType) throws ParseException;

	 List<HistoryData> findHistoryDataListByCreator(Map<String, Object> searchParams) throws ParseException;

	 void addHistoryData(HistoryData historyData);
	
	 void deleteByIdLogData(Long id);
	 
	 void updateHistoryDataName(HistoryData historyData);
	 
	 List<HistoryData> exportData(Map<String, Object> searchParams) throws ParseException;
	 

}
