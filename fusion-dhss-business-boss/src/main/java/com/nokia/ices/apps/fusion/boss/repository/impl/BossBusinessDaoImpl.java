package com.nokia.ices.apps.fusion.boss.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nokia.ices.apps.fusion.boss.repository.BossBusinessDao;

@Repository("bossBusinessDao")
public class BossBusinessDaoImpl implements BossBusinessDao {
	
	private static final Logger logger = LoggerFactory.getLogger(BossBusinessDaoImpl.class);
	
	
	@Autowired
    @Qualifier("jdbcTemplateBoss")
    JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> findMonitorType() {
		String sql = "select business_type,business_type_cn from boss_cmd_type";
		return jdbcTemplate.queryForList(sql);
		
	}

	@Override
	public List<String> findNeParentName() {
		String sql = "select DISTINCT  hlrsn from boss_soap";
		return jdbcTemplate.queryForList(sql,String.class);
	}

	@Override
	public List<Map<String,Object>> findErrorCode() {
		String sql = "SELECT error_code,error_code_desc FROM boss_error_code";
		return jdbcTemplate.queryForList(sql);
	}
	
	@Override
	public List<String> findCmdTypeName() {
		// TODO Auto-generated method stub
		String sql = "SELECT distinct operation_name FROM boss_cmd_type where boss_version = 'chinamobile'";
		return jdbcTemplate.queryForList(sql,String.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public Integer getBossCount(Map<String, Object> param){
	  
    	Vector<Object> params = getCondition(param);
    	List<String> args = (List<String>)params.get(1);
		String condtion  = params.get(0).toString();
    	
		String sql = "SELECT count(1) "+condtion;
		Integer count = jdbcTemplate.queryForObject(sql, args.toArray(), Integer.class);
		return count;
    	
    }
    
    private Vector<Object> getCondition(Map<String, Object> param){
    	
    	StringBuffer sb = new StringBuffer("from boss_soap bs where 1=1 AND id>0 ");
    	List<String> args = new ArrayList<String>();
    	Vector<Object> params = new Vector<>();
    	
    	String startTime = param.get("startTime")!=null?param.get("startTime").toString():"";
		String endTime = param.get("endTime")!=null?param.get("endTime").toString():"";
		//String imsi = param.get("imsi")!=null?param.get("imsi").toString():"";
		//String msisdn = param.get("msisdn")!=null?param.get("msisdn").toString():"";
		int imsiNumber = 0;
		int msisdnNumber = 0;
		List<String> imsiList = new ArrayList<>();
		List<String> msisdnList = new ArrayList<>();
		while(true){
			String imsiKey = "imsi"+String.valueOf(imsiNumber);
			if(param.containsKey(imsiKey)){
				imsiList.add(param.get(imsiKey).toString());
				imsiNumber++;
			}else{
				break;
			}
		}
		while(true){
			String msisdnKey = "msisdn"+String.valueOf(msisdnNumber);
			if(param.containsKey(msisdnKey)){
				msisdnList.add(param.get(msisdnKey).toString());
				msisdnNumber++;
			}else{
				break;
			}
		}
		String result = param.get("result")!=null?param.get("result").toString():"";
		String errorCode = param.get("errorCode")!=null?param.get("errorCode").toString():"";
		String hlrsn = param.get("hlrsn")!=null?param.get("hlrsn").toString():"";
		String businessType = param.get("businessType")!=null?param.get("businessType").toString():"";
		String cmdType = param.get("cmdType")!=null?param.get("cmdType").toString():"";
		
		if(StringUtils.isNotEmpty(startTime)){
			sb.append(" AND bs.response_time >= ?");
			 args.add(startTime);
		}
        if(StringUtils.isNotEmpty(endTime)){ 
        	sb.append(" AND bs.response_time < ?");
        	 args.add(endTime);
		}

//        if(StringUtils.isNotEmpty(imsi) && StringUtils.isNotEmpty(msisdn)){
//        	if(StringUtils.isNotEmpty(imsi)){
//            	sb.append("  AND  (bs.imsi = ? OR bs.isdn=?) "); 
//    	       	args.add(imsi);
//    	       	args.add(msisdn);
//    		}
//        }else{
//        	if(StringUtils.isNotEmpty(imsi)){
//        		sb.append("  AND  bs.imsi = ?");
//        		args.add(imsi);
//        	}
//        	
//        	if(StringUtils.isNotEmpty(msisdn)){
//        		sb.append(" AND  bs.isdn = ?");
//        		args.add(msisdn);
//        	} 
//        }
        
        if(StringUtils.isNotEmpty(result)){
        	sb.append(" AND  bs.callback_result = ?");
	       	 args.add(result);
		}
        
        if(StringUtils.isNotEmpty(errorCode)){
        	sb.append(" AND  bs.errorcode = ?");
	       	 args.add(errorCode);
		}
        
        if(StringUtils.isNotEmpty(hlrsn)){
        	sb.append("  AND  bs.hlrsn = ?");
	       	 args.add(hlrsn);
		}
        if(StringUtils.isNotEmpty(cmdType)){
        	sb.append("  AND  bs.operationname = ?");
	       	args.add(cmdType);
        }
       
        if(StringUtils.isNotEmpty(businessType)){
        	sb.append(" AND bs.b_class like '%"+businessType+"%'");
		}
        String[] numberConditionArray = new String[imsiList.size()+msisdnList.size()];
        int conditionNumber = 0;
        for (String imsi : imsiList) {
			String imsiCondition = " bs.imsi = ? ";
			args.add(imsi);
			numberConditionArray[conditionNumber] = imsiCondition;
			conditionNumber++;
		}
        for (String msisdn : msisdnList) {
			String msisdnCondition = " bs.isdn = ? ";
			args.add(msisdn);
			numberConditionArray[conditionNumber] = msisdnCondition;
			conditionNumber++;
		}
        if(numberConditionArray.length == 1){
        	sb.append(" AND"+numberConditionArray[0]);
        }else if(numberConditionArray.length > 1){
        	sb.append(" AND(");
        	for (String string : numberConditionArray) {
        		sb.append(string+"OR");
			}
        	sb = new StringBuffer(sb.substring(0,sb.length()-2));
        	sb.append(")");
        }
        params.add(sb);
        params.add(args);
    	return params;
    }
    

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findUserBossBusiness(Map<String, Object> param) {
		
		
		String sql = " SELECT bs.re_id,bs.isdn,bs.imsi,bs.operationname,bs.callback_result,"
				   +" bs.errorcode,bs.errormessage as errormessage,bs.re_time,bs.b_class,bs.hlrsn,bs.context,bs.user_2 ";
		//String sql = " SELECT re_id,isdn,imsi,operationname,callback_result,"
				   //+" errorcode,message as errormessage,re_time,b_class,hlrsn,context,user_2 ";
		logger.debug(new Date()+".........start..........list");
		Integer total = 5000;
		Vector<Object> params = getCondition(param);
		List<String> args = (List<String>)params.get(1);
		String condtion  = params.get(0).toString();
		sql+=condtion+" order by response_time desc LIMIT "+total;
		
        logger.debug(sql);
      
       List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql,args.toArray());
       logger.debug(new Date()+"..........end.........list");
       Map<String,Object> resultMaps = new HashMap<String, Object>();
       resultMaps.put("content", resultList);
       resultMaps.put("totalElements", resultList.size());
       
	   return resultMaps;
       
	}

	@Override
	public List<Map<String, Object>> findBossMonitorDataDao(
			Map<String, Object> param) {
    
		String tabName = param.get("tabName").toString();
		String inputNeName = param.get("inputNeName").toString();
		String inputBusiness = param.get("inputBusiness").toString();
		String startdatetime = param.get("startdatetime").toString();
		String enddatetime = param.get("enddatetime").toString();
		
		String sql = "select bs.datestr,sum(fail_count) as fail_count,"
	               +"sum(total) as total from "+tabName+" bs where 1=1 ";
		
		List<String> args = new ArrayList<String>();
		if(StringUtils.isNotEmpty(inputNeName)){
			 sql+="  AND  bs.hlrsn = ?";
			 args.add(inputNeName);
		}
		
		if(StringUtils.isNotEmpty(inputBusiness)){
	      	 sql+=" AND bs.operationname like '%"+inputBusiness+"%'";
		}
		if(StringUtils.isNotEmpty(startdatetime)){
	      	 sql+=" AND bs.datestr >= ?";
	      	 args.add(startdatetime);
		}
		if(StringUtils.isNotEmpty(enddatetime)){
	      	 sql+=" AND bs.datestr <= ?";
	      	 args.add(enddatetime);
		}
	
		sql+="GROUP BY bs.datestr";
		
		Object [] array = args.toArray();
		
		logger.debug("bossMonitor{"+sql+"}");
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql,array);
		return resultList;
	}

	@Override
	public String getBossMessage(String column, String reId,String startTime,String endTime) {
		// TODO Auto-generated method stub
		
		logger.debug("getBossMessage start date:"+new Date());
		String message = "";
		String sql = "SELECT "+column+" FROM boss_soap where re_id=? AND callback_result='success'"
				   + " AND response_time >=? AND response_time<? LIMIT 1";
		
		logger.debug("SQL:{"+sql+"} params:{reId="+reId+",startTime="+startTime+",endTime="+endTime+"}");
		
		List<String> list =  jdbcTemplate.queryForList(sql, new Object []{reId,startTime,endTime}, String.class);
		if(null!=list && list.size()>0){
			message  = list.get(0);
			message = message.replaceAll("~", "@");
		}
		
		logger.debug("getBossMessage end date:"+new Date());
		return message;
	}

	@Override
	public List<Map<String, Object>> cmdTypeAndBusinessTypeMapping() {
		// 1.select boss version
		String bossVersion = "chinamobile";
		//String bossVersion = "unicom";
		String sql = "select operation_name,business_type from boss_cmd_type where boss_version = ?";
		List<Map<String, Object>> cmdTypeAndBusinessTypeMapList = jdbcTemplate.queryForList(sql,bossVersion);
		return cmdTypeAndBusinessTypeMapList;
	}
}
