package com.nokia.ices.apps.fusion.alarm.controll;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.alarm.AlarmReceiveHistory;
import com.nokia.ices.apps.fusion.alarm.AlarmReceiveRecord;
import com.nokia.ices.apps.fusion.alarm.AlarmReceiveRecord_;
import com.nokia.ices.apps.fusion.alarm.NotImportantAlarm;
import com.nokia.ices.apps.fusion.alarm.UserAlarmMonitor;
import com.nokia.ices.apps.fusion.alarm.repository.AlarmReceiveHistoryRepository;
import com.nokia.ices.apps.fusion.alarm.repository.AlarmReceiveRecordRepository;
import com.nokia.ices.apps.fusion.alarm.repository.NotImportantAlarmRepository;
import com.nokia.ices.apps.fusion.alarm.repository.UserAlarmMonitorRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

/**
 * 
 * @author LAI-DELL-13
 *
 */
@RepositoryRestController
@RestController
public class AlarmRecordController {

    public static final Logger logger = LoggerFactory.getLogger(AlarmRecordController.class);


    
    @SuppressWarnings("rawtypes")
    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;
    
//    @Autowired
//	AlarmRecordRepository alarmRecordRepository;
    
    @Autowired
    private AlarmReceiveRecordRepository alarmReceiveRecordRepository;
    
    @Autowired
    private AlarmReceiveHistoryRepository alarmReceiveHistoryRepository;
    
    @Autowired
    private EquipmentNeRepository equipmentNeRepository;
    
    @Autowired
    private EquipmentUnitRepository equipmentUnitRepository;
    
    @Autowired
    private UserAlarmMonitorRepository userAlarmMonitorRepository;
    
    @Autowired
    private NotImportantAlarmRepository notImportantAlarmRepository;
    
    
    @RequestMapping(value="delUserAlarm" )
    public boolean delUserAlarm(@RequestParam(value="unitName",required=false) final String unitName){
    	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("userName_EQ", shiroUser.getUserName());
    	map.put("unitName_EQ", unitName);
    	Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<UserAlarmMonitor> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, UserAlarmMonitor.class);
		userAlarmMonitorRepository.delete(userAlarmMonitorRepository.findAll(spec));
    	return true;
    }
    
    @RequestMapping(value="findUserAlarm")
    public List<UserAlarmMonitor> findUserAlarm(){
    	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("userName_EQ", shiroUser.getUserName());
    	Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<UserAlarmMonitor> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, UserAlarmMonitor.class);
		return userAlarmMonitorRepository.findAll(spec);
    }
    
    @RequestMapping(value="addUserAlarmMonitor")
    public boolean addUserAlarmMonitor(@RequestParam(value="unitName",required=false) final String unitName,
    		@RequestParam(value="cnum",required=false)final String cnum,
    		@RequestParam(value="startTime",required=false) final String startTime,
    		@RequestParam(value="endTime",required=false) final String endTime,
    		@RequestParam(value="alarmNum",required=false) final String alarmNum,
    		@RequestParam(value="keyword",required=false) final String keyword,
    		@RequestParam(value="desc",required=false) final String desc) throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	UserAlarmMonitor alarm = new UserAlarmMonitor();
    	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    	alarm.setUserName(shiroUser.getUserName());
    	alarm.setAlarmNum(alarmNum);
    	alarm.setCnum(cnum);
    	alarm.setCreateTime(new Date());
    	alarm.setAlarmDesc(desc);
    	if(endTime!= null && !endTime.equals(""))
    		alarm.setEndTime(sdf.parse(endTime));
    	if(startTime!= null && !startTime.equals(""))
    		alarm.setStartTime(sdf.parse(startTime));
    	alarm.setKeyword(keyword);
    	alarm.setUnitName(unitName);
    	userAlarmMonitorRepository.save(alarm);
    	return true;
    }
    
    public Map<String, Object> mapAddNode(String text,boolean expanded,Integer dayCount,Integer value,Object item,String cnum,boolean falg){
    	Map<String, Object> tempDhssMap = new HashMap<String, Object>();
		tempDhssMap.put("text", text);
		tempDhssMap.put("expanded", expanded); 
		tempDhssMap.put("dayCount", dayCount == null ? 0 : dayCount);
		tempDhssMap.put("value",value == null ? 0 : value);
		tempDhssMap.put("items",item);
		tempDhssMap.put("cnum", StringUtils.isNotBlank(cnum) ? cnum : "===");
		tempDhssMap.put("flag", falg); 
		return tempDhssMap;
    }
    
    public int countAlarmSize(Map<String, Integer> map,String cnum){
    	int count = 0;
    	String strs = StringUtils.isNotEmpty(cnum) ? cnum : ",," ;
    	for (String str : map.keySet()) {
			if(str.indexOf(strs) != -1){
				count += map.get(str);
			}
		}
    	return count;
    }
    
    
    @RequestMapping(value = "alarm-record/search/unitTreeView")
    public List<Map<String, Object>> findUnitTreeView() throws ParseException{
    	
    	List<UserAlarmMonitor>  userAlarm = findUserAlarm();
    	List<String> userAlarmUnitNames = new ArrayList<String>();
    	for (UserAlarmMonitor userAlarmMonitor : userAlarm) {
    		userAlarmUnitNames.add(userAlarmMonitor.getUnitName());
		}
    	List<Map<String, Object>> treeViewList = new ArrayList<Map<String,Object>>();
    	Map<String, Object> wholeMap = new HashMap<String,Object>();
    	wholeMap.put("text", "全网");
    	wholeMap.put("expanded", true);
    	wholeMap.put("flag", false);
    	
    	
    	//查询全部活动告警
    	List<AlarmReceiveRecord> allAlarmReceiveRecord = alarmReceiveRecordRepository.
    			findAll(DynamicSpecifications.bySearchFilter(SearchFilter.parse(new HashMap<>()).values(), BooleanOperator.AND, AlarmReceiveRecord.class));
    	//全部网元
    	Iterable<EquipmentNe> neArray = equipmentNeRepository.findAll();
    	//全部单元
    	Iterable<EquipmentUnit> unitArray = equipmentUnitRepository.findAll();
    	
    	List<Object> dhssTreeView = new ArrayList<Object>();
    	
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    	Long dayDate = df.parse(df.format(new Date())).getTime();
    	
    	
    	Map<String, Integer> allSizes = new HashMap<String, Integer>();
    	Map<String, Integer> daySizes = new HashMap<String, Integer>();
    	
    	Map<String, Integer> tmpAll = new HashMap<String, Integer>();
    	
    	Map<String, Integer> tmpDay = new HashMap<String, Integer>();
    	
    	
		for (AlarmReceiveRecord record : allAlarmReceiveRecord) {
			String dhssName = StringUtils.isNotBlank(record.getDhssName()) ?    record.getDhssName() : "unknown";
			int size = isNull(allSizes.get(dhssName)); 
			allSizes.put(dhssName, size+1) ;
			if(dhssName.equals("unknown")){
				tmpAll.put(record.getAlarmCell(), 1+isNull(tmpAll.get(record.getAlarmCell())));
			}
			
			Long time = df.parse(df.format(record.getReceiveStartTime())).getTime();
			
			String neName = record.getAlarmCell();
			
			if(StringUtils.isNotBlank(record.getAlarmCell())){
				
				allSizes.put(neName, allSizes.get(neName) == null ? 1 : allSizes.get(neName)+1); 
			}
			
			if(time.equals(dayDate)){
				daySizes.put(dhssName,daySizes.get(dhssName) == null ? 1 : daySizes.get(dhssName)+1);
				daySizes.put(record.getAlarmCell(), isNull(daySizes.get(record.getAlarmCell())) +1);
				
				if(dhssName.equals("unknown")){
					tmpDay.put(record.getAlarmCell(), 1+(isNull(tmpDay.get(record.getAlarmCell()))));
				}
			}
		}
		
		Map<String, List<Map<String, Object>>> unitTreeTemp = new HashMap<String, List<Map<String, Object>>>();
		for (EquipmentUnit unit : unitArray) {
			List<Map<String, Object>> tempArr = unitTreeTemp.get(unit.getNe().getNeName());
			tempArr = tempArr == null ? new ArrayList<Map<String, Object>>() : tempArr;
			
			int daySize = countAlarmSize(daySizes,unit.getCnum());
			int allSize = countAlarmSize(allSizes,unit.getCnum());
			daySizes.put(unit.getNeId()+"",daySize+isNull(daySizes.get(unit.getNeId()+"")));
			allSizes.put(unit.getNeId()+"",allSize+isNull(allSizes.get(unit.getNeId()+"")));
			
			Map<String,Object> map = mapAddNode( unit.getUnitName(), false, daySize, allSize, null,unit.getCnum(), true);
			map.put("collection", userAlarmUnitNames.contains(unit.getUnitName()));
			tempArr.add(map);
			
			
			unitTreeTemp.put(unit.getNe().getNeName(), tempArr);
		}
		
		
		Map<String, List<Map<String, Object>>> neTreeTemp = new HashMap<String, List<Map<String, Object>>>();
		
		Set<String> t = new HashSet<String>();
		for (EquipmentNe ne : neArray) {
			List<Map<String, Object>> tempArr = neTreeTemp.get(ne.getDhssName());
			tempArr = tempArr == null ? new ArrayList<Map<String, Object>>() : tempArr;
			
			Integer all = allSizes.get(ne.getId()+""); 
			
//			for (String key : tmpAll.keySet()) {
//				if(key.indexOf(ne.getCnum()) != -1){
//					if(t.add(key+'a')){
//						all+=tmpAll.get(key);
//						allSizes.put("unknown",isNull(allSizes.get("unknown"))-isNull(tmpAll.get(key)));
//						allSizes.put(ne.getDhssName(),isNull(allSizes.get(ne.getDhssName()))+isNull(tmpAll.get(key)));
//					}
//				}
//			}
			Integer day = daySizes.get(ne.getId()+""); 
//			for (String key : tmpDay.keySet()) {
//				if(key.indexOf(ne.getCnum()) != -1){
//					if(t.add(key+'b')){
//						day+=tmpDay.get(key);
//						daySizes.put("unknown",isNull(daySizes.get("unknown"))-isNull(tmpDay.get(key)));
//						daySizes.put(ne.getDhssName(),isNull(daySizes.get(ne.getDhssName()))+isNull(tmpDay.get(key)));
//					}
//				}
//			}
			
			Map<String,Object> map = mapAddNode( ne.getNeName(), false, day, all, unitTreeTemp.get(ne.getNeName()), ne.getNeName(), true);
			map.put("collection", userAlarmUnitNames.contains(ne.getNeName()));
			tempArr.add(map);
			neTreeTemp.put(ne.getDhssName(), tempArr);
		}
		
		List<String> temp = new ArrayList<String>();
    	for (EquipmentNe ne : neArray) {
    		if(!temp.contains(ne.getDhssName())){
    			int daySize = isNull(daySizes.get(ne.getDhssName()));
    			int allSize = isNull(allSizes.get(ne.getDhssName()));
    			daySizes.put("rootSize",isNull(daySizes.get("rootSize")) + daySize);
    			allSizes.put("rootSize",isNull(allSizes.get("rootSize")) + allSize);
    			dhssTreeView.add(mapAddNode( ne.getDhssName(), true,daySize , allSize, 
    								neTreeTemp.get(ne.getDhssName()), "+++++", true));
    			temp.add(ne.getDhssName());
    		}
		}
    	
    	 
    	dhssTreeView.add(mapAddNode("unknown", true, daySizes.get("unknown"), allSizes.get("unknown"), null, "+++++", true));
    	
    	
    	
    	wholeMap.put("value", isNull(allSizes.get("rootSize")) +isNull(allSizes.get("unknown")));
    	wholeMap.put("items", dhssTreeView);
    	wholeMap.put("dayCount", isNull(daySizes.get("rootSize")) + isNull(daySizes.get("unknown")));
    	treeViewList.add(wholeMap);
    	return treeViewList;
    }
    
    private Integer isNull(Integer num){
    	return num == null ? 0 : num;
    }
    
    
    public Map<String,Integer> queryTodayAllAlarm() throws ParseException{
    	Map<String,Object> map = new HashMap<String,Object>();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdfByday = new SimpleDateFormat("yyyy-MM-dd");
    	map.put("receiveStartTime_GE",sdf.parse(sdfByday.format(new Date())+" 00:00:00"));
    	map.put("receiveStartTime_LE",sdf.parse(sdfByday.format(new Date())+" 23:59:59"));
    	Map<String,SearchFilter> filter1 = SearchFilter.parse(map);
		Specification<AlarmReceiveRecord> spec1 = 
                DynamicSpecifications.bySearchFilter(filter1.values(), BooleanOperator.AND, AlarmReceiveRecord.class);
		List<AlarmReceiveRecord> todayAlarmList =  alarmReceiveRecordRepository.findAll(Specifications.where(spec1));
		Map<String,Integer> map1 = new HashMap<String,Integer>();
		for (AlarmReceiveRecord alarmReceiveRecord : todayAlarmList) {
			Integer count = map1.get(alarmReceiveRecord.getAlarmCell());
			map1.put(alarmReceiveRecord.getAlarmCell(), (count==null?1:(count+1)));
		}
		return map1;
    } 
    
	@RequestMapping(value = "alarm-record-new/search/searchByFilter")
    public List<AlarmReceiveRecord> findAlarmRecordPageBySearchFilterNew(
    		@RequestParam(value="text",required=false) final String text,
    		@RequestParam(value="endAlarmTime",required=false) final String endAlarmTime,
    		@RequestParam(value="startAlarmTime",required=false) final String startAlarmTime,
    		@RequestParam(value="alarmNum",required=false) final String alarmNum,
    		@RequestParam(value="core",required=false) final String core,
    		@RequestParam(value="desc",required=false) final String desc,
    		@RequestParam(value="dhssName",required=false) final String dhssName,
    		@RequestParam(value="alarmType",required=false) final String alarmType) throws ParseException {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	List<SearchFilter> searchFilterAND = new ArrayList<SearchFilter>();
    	if(StringUtils.isNotBlank(startAlarmTime)){
    		searchFilterAND.add(new SearchFilter("receiveStartTime", Operator.GE, sdf.parse(startAlarmTime)));
		}
    	if(StringUtils.isNotBlank(endAlarmTime)){
    		searchFilterAND.add(new SearchFilter("receiveStartTime", Operator.LE, sdf.parse(endAlarmTime)));
		}
    	if(StringUtils.isNotBlank(alarmNum)){
    		searchFilterAND.add(new SearchFilter("alarmNo", Operator.LIKE, alarmNum));
		}
    	if(StringUtils.isNotBlank(core)){
    		searchFilterAND.add(new SearchFilter("alarmText", Operator.LIKE, core));
		}
    	
    	List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>();
    	
    	
    	if(StringUtils.isNotBlank(text) ){
    		Map<String, Object> map = new HashMap<String,Object>();
    		map.put("neName_EQ", text);
    		Map<String,SearchFilter> filter = SearchFilter.parse(map);
    		Specification<EquipmentNe> spec = 
                    DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EquipmentNe.class);
    		List<EquipmentNe> Nes = equipmentNeRepository.findAll(spec);
    		if(Nes.size() != 0){
    			for (EquipmentNe equipmentNe : Nes) {
    				Map<String, Object> mapParam = new HashMap<String,Object>();
    				mapParam.put("ne.neName_EQ", equipmentNe.getNeName());
    				List<EquipmentUnit> units = findUnit(mapParam);
    				for (EquipmentUnit equipmentUnit : units) {
        				searchFilterOR.add(new SearchFilter("alarmCell", Operator.LIKE, equipmentUnit.getCnum()));
					}
				}
    		}else{
    			searchFilterAND.add(new SearchFilter("alarmCell", Operator.LIKE, text));
    		}
    		
    		
		}
    	
    	if(StringUtils.isNotBlank(dhssName)){
    		if("unknown".equals(dhssName)){
    			Iterable<EquipmentNe> nelist = equipmentNeRepository.findAll();
    			Set<String> set = new HashSet<String>();
    			for (EquipmentNe equipmentNe : nelist) {
    				set.add(equipmentNe.getDhssName());
				}
    			for (String string : set) {
    				searchFilterAND.add(new SearchFilter("dhssName", Operator.NOTEQ, string));
				}
        		
    		}else{
    			Map<String, Object> map = new HashMap<String,Object>();
    			map.put("ne.dhssName_EQ", dhssName);
    			List<EquipmentUnit> units = findUnit(map);
        		for (EquipmentUnit equipmentUnit : units) {
        			if(StringUtils.isNotBlank(equipmentUnit.getCnum())){
        				searchFilterOR.add(new SearchFilter("alarmCell", Operator.LIKE, equipmentUnit.getCnum()));
        			}
//        			if(StringUtils.isNotBlank(equipmentUnit.getNe().getCnum())){
//        				searchFilterOR.add(new SearchFilter("alarmCell", Operator.LIKE, equipmentUnit.getNe().getCnum()));
//        			}
    			}
    		}
    		
		}
    	
    	Specification<AlarmReceiveRecord> speciFicationsAND = DynamicSpecifications
				.bySearchFilter(searchFilterAND, BooleanOperator.AND,
						AlarmReceiveRecord.class);
    	
    	Specification<AlarmReceiveRecord> speciFicationsOR = DynamicSpecifications
				.bySearchFilter(searchFilterOR, BooleanOperator.OR,
						AlarmReceiveRecord.class);
    	
    	List<SearchFilter> searchFilterAlarmOR = new ArrayList<SearchFilter>();
    	if(StringUtils.isNotBlank(alarmType)){
    		List<NotImportantAlarm> alarmList = notImportantAlarmRepository.findAll();
    		NotImportantAlarm na = alarmList.size() == 0 ? null : alarmList.get(0);
    		List<String> strList = na == null ? new ArrayList<String>() : na.getAlarmNoList();
    		
    		for (String string : strList) {
    			if(string != null && !"".equals(string)){
    				searchFilterAlarmOR.add(new SearchFilter("alarmNo", Operator.NOTEQ, string));
    			}
			}
    		
    	}
    	Specification<AlarmReceiveRecord> speciFicationsAlarmOR = DynamicSpecifications
				.bySearchFilter(searchFilterAlarmOR, BooleanOperator.AND,
						AlarmReceiveRecord.class);
    	
		
		return alarmReceiveRecordRepository.findAll(Specifications.where(speciFicationsAND).and(speciFicationsOR).and(speciFicationsAlarmOR)/*,pageable*/);
    }
    
	
	public List<EquipmentUnit> findUnit(Map<String, Object> map){
		
		Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<EquipmentUnit> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EquipmentUnit.class);
		return equipmentUnitRepository.findAll(spec);
	}
    
    
    
    
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "alarm-record/search/searchByFilter")
    public PagedResources<AlarmReceiveRecord> findAlarmRecordPageBySearchFilter(
    		@RequestParam(value="startAlarmTime",required=false) final String startAlarmTime,
    		@RequestParam(value="endAlarmTime",required=false) final String endAlarmTime,
    		@RequestParam(value="alarmId",required=false) final String alarmId,
    		@RequestParam(value="alarmNum",required=false) final String alarmNum,
    		@RequestParam(value="alarmCell",required=false) final String alarmCell,
    		@RequestParam(value="textValue",required=false) final String textValue,
    		@RequestParam(value="alarmLevel",required=false) final String alarmLevel,
    		Pageable pageable,
            PersistentEntityResourceAssembler assembler) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(startAlarmTime)){
			map.put("receiveStartTime_GE",sdf.parse(startAlarmTime));
		}
		if(StringUtils.isNotBlank(endAlarmTime)){
			map.put("receiveStartTime_LE",sdf.parse(endAlarmTime));
		}
		if(StringUtils.isNotBlank(alarmId)){
			map.put("notifyId_LIKE",alarmId);
		}
		if(StringUtils.isNotBlank(alarmNum)){
			map.put("alarmNo_LIKE",alarmNum);
		}
		if(StringUtils.isNotBlank(alarmCell)){
			map.put("alarmCell_LIKE",alarmCell);
		}
		if(StringUtils.isNotBlank(textValue)){
			map.put("alarmCell_LIKE",textValue);
		}
		if(StringUtils.isNotBlank(alarmLevel)){
			map.put("alarmLevel_EQ",alarmLevel);
		}
		Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<AlarmReceiveRecord> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, AlarmReceiveRecord.class);
		return pagedResourcesAssembler.toResource(alarmReceiveRecordRepository.findAll(spec, pageable),assembler);
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "alarm-history/search/searchByFilter")
    public PagedResources<AlarmReceiveHistory> findAlarmHistoryPageBySearchFilter(
    		@RequestParam(value="startAlarmTime",required=false) final String startAlarmTime,
    		@RequestParam(value="endAlarmTime",required=false) final String endAlarmTime,
    		@RequestParam(value="alarmId",required=false) final String alarmId,
    		@RequestParam(value="alarmNum",required=false) final String alarmNum,
    		@RequestParam(value="alarmCell",required=false) final String alarmCell,
    		@RequestParam(value="textValue",required=false) final String textValue,
    		@RequestParam(value="alarmLevel",required=false) final String alarmLevel,
    		Pageable pageable,
            PersistentEntityResourceAssembler assembler) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(startAlarmTime)){
			map.put("receiveStartTime_GE",sdf.parse(startAlarmTime));
		}
		if(StringUtils.isNotBlank(endAlarmTime)){
			map.put("receiveStartTime_LE",sdf.parse(endAlarmTime));
		}
		if(StringUtils.isNotBlank(alarmId)){
			map.put("notifyId_LIKE",alarmId);
		}
		if(StringUtils.isNotBlank(alarmNum)){
			map.put("alarmNo_LIKE",alarmNum);
		}
		if(StringUtils.isNotBlank(alarmCell)){
			map.put("alarmCell_LIKE",alarmCell);
		}
		if(StringUtils.isNotBlank(textValue)){
			map.put("alarmCell_LIKE",textValue);
		}
		if(StringUtils.isNotBlank(alarmLevel)){
			map.put("alarmLevel_EQ",alarmLevel);
		}
		Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<AlarmReceiveHistory> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, AlarmReceiveHistory.class);
		return pagedResourcesAssembler.toResource(alarmReceiveHistoryRepository.findAll(spec, pageable),assembler);
    }
    
    
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "alarmRecord/queryAlarmExtraInfo")
	public PagedResources<AlarmReceiveRecord> findqueryAlarmExtraInfoPageBySearchFilter(
			@RequestParam(value = "alarmRecordId", required = true) final String alarmRecordId, Pageable pageable,
			PersistentEntityResourceAssembler assembler) {
		Page<AlarmReceiveRecord> page = alarmReceiveRecordRepository.findAll(new Specification<AlarmReceiveRecord>() {
			@Override
			public Predicate toPredicate(Root<AlarmReceiveRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (StringUtils.isNotBlank(alarmRecordId)) {
					// predicates.add(cb.equal(root.get(AlarmRecord_.alarmId),
					// alarmRecordId));
					predicates.add(cb.equal(root.get(AlarmReceiveRecord_.notifyId), alarmRecordId));
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		}, pageable);
		return pagedResourcesAssembler.toResource(page, assembler);
	}
    
 	

}
