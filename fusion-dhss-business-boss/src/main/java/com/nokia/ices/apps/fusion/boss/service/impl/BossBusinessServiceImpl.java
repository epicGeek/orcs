package com.nokia.ices.apps.fusion.boss.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.boss.repository.BossBusinessDao;
import com.nokia.ices.apps.fusion.boss.service.BossBusinessService;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.core.utils.DateUtil;

@Service(value="bossBusinessService")
public class BossBusinessServiceImpl  implements BossBusinessService {
	
	 private final static Logger logger = LoggerFactory.getLogger(BossBusinessServiceImpl.class);
   
    @Autowired
    BossBusinessDao bossBusinessDao;
    
    
    
    public Integer getBossCount(Map<String, Object> params){
    	
    	return bossBusinessDao.getBossCount(params);
    	
    }
    
	public Map<String,Object> findUserBossBusiness(Map<String, Object> params) {
		Map<String,Object> result = bossBusinessDao.findUserBossBusiness(params);
    	return result;
	}

	public List<Map<String,Object>> findBossMonitorData(Map<String, Object> param,String period) {
		List<Map<String,Object>> dataList  = null;
		String tabName= "";
		String format = "";
		if("H".equals(period)){
			tabName = "boss_soap_hour_handle";
			format = "yyyy-MM-dd HH";
		}else if("D".equals(period)){
			tabName = "boss_soap_day_handle";
			format = "yyyy-MM-dd";
		}else if("MO".equals(period)){
			tabName = "boss_soap_month_handle";
			format = "yyyy-MM";
		}else if("MI".equals(period)){
			tabName = "boss_soap_minute_handle";
			format = "yyyy-MM-dd HH:mm";
		}
		String startDate = param.get("startdatetime")!=null?param.get("startdatetime").toString():"";
		String endDate = param.get("enddatetime")!=null?param.get("enddatetime").toString():"";
		param.put("startdatetime", DateUtil.getStrToFormatDate(startDate,format));
		param.put("enddatetime", DateUtil.getStrToFormatDate(endDate,format)); 
		param.put("tabName", tabName);
		dataList = bossBusinessDao.findBossMonitorDataDao(param);
		return dataList;
	}
	
	
	public List<Map<String, Object>> findMonitorType() {
		return bossBusinessDao.findMonitorType();
	}
	
	
	@Override
	public List<Map<String,Object>> findErrorCode() {
		return bossBusinessDao.findErrorCode();
	}

	 //查询指令类型
    public List<String> findCmdTypeName(){
    	return bossBusinessDao.findCmdTypeName();
    }

	@Override
	public String getBossMessageService(String column, String reId,String re_time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cl = Calendar.getInstance();
		try {
			cl.setTime(sdf.parse(re_time));
			cl.add(Calendar.HOUR, -8);
			String startTime = sdft.format(cl.getTime());
			String endTime = sdft.format(sdf.parse(re_time));
			return bossBusinessDao.getBossMessage(column, reId,startTime,endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ParseException : "+e.toString());
		}
		return null;
		
	}

	@Override
	public List<Map<String, Object>> businessTypeAndCmdTypeMapList() {
		
		return bossBusinessDao.cmdTypeAndBusinessTypeMapping();
	}

	@Override
	public List<String> findNeParentName() {
		// TODO Auto-generated method stub
		return null;
	}

}
