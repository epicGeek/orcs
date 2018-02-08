package com.nokia.ices.apps.fusion.equipment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNumberSection;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNumberSectionRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.equipment.service.NumberSectionService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.repository.SystemAreaRepository;

@Service("numberSectionService")
public class NumberSectionServiceImpl implements NumberSectionService {

	private final static Logger logger = LoggerFactory
			.getLogger(NumberSectionServiceImpl.class);

	@Autowired
	private EquipmentUnitRepository equipmentUnitRepository;

	@Autowired
	private EquipmentNumberSectionRepository equipmentNumberSectionRepository;
	
	@Autowired
	private SystemAreaRepository systemAreaRepository;

	/**
	 * 根据用户号码，单元类型，操作类型获取匹配的单元IP
	 */
	@SuppressWarnings("unchecked")
	public List<EquipmentUnit> findAllUnitTypeByNumberSection(String ns,
			String type, String unitTypeName, ShiroUser shiroUser) {
		//非admin用户进去地区权限过滤
		List<Long> matchIdList = null;
		/**
		 * 根据用户获取所有资源
		 */
        Subject subject = SecurityUtils.getSubject();

		Iterable<SystemArea> iterArea =  systemAreaRepository.findAll();
        List<SystemArea> resultArea = new ArrayList<SystemArea>();

		for (SystemArea systemArea : iterArea) {
            if(subject.isPermitted("area:"+systemArea.getAreaCode())){
                resultArea.add(systemArea);
            }
        }

		if(resultArea.size()==0){
			logger.debug("Not area assign permissions......");
			return null;
		}
		//查询具有该地区权限的number
		List<EquipmentNumberSection> numberList = equipmentNumberSectionRepository.findEquipmentNumberSectionListByAreaIn(resultArea);
	    matchIdList = new ArrayList<Long>();
		for (EquipmentNumberSection nse : numberList) {
			String str = "";
			if ("1".equals(type) && StringUtils.isNotEmpty(nse.getNumber())) {
				str = nse.getNumber().trim();
			} else {
				if(StringUtils.isNotEmpty(nse.getImsi())){
					str = nse.getImsi().trim();
				}
			}
			if (StringUtils.isNotEmpty(str)) {
				if (ns.equals(str)) {
					matchIdList.add(nse.getId());
				} else if (ns.contains(str)) {
					matchIdList.add(nse.getId());
				}else if(str.contains("X")){
					//460077021X 特殊号码处理
					String [] x_imsi = {"0","1","2","3","4","5","6","7","8","9"};
					for(String imsi :x_imsi){
						String newImsi = str.replaceAll("X", imsi);
						if (ns.contains(newImsi)) {
							matchIdList.add(nse.getId());
						}
					}
				}else if(str.contains("-")){
					//861582150-3、5-9 号码匹配处理
					 Map<String,Object> mapNumbers = getNumberStr(str);
					 String perfixNumber = mapNumbers.get("perfix").toString();
					 List<Integer> numbers =(List<Integer>) mapNumbers.get("suffix");
					 for(Integer number :numbers){
						if (ns.contains(perfixNumber+number)) {
							matchIdList.add(nse.getId());
						}
					 }
					
				}
			}/* else {
				logger.debug("IMSI/NUMBER 未获取到，请检查配置........   ID="+ nse.getId());
			}*/
		}
		/**
		 * 查找与号码段匹配的PGW单元
		 */ 
		return equipmentUnitRepository.findListByNeNumberSectionIdInAndUnitTypeEquals(matchIdList,EquipmentUnitType.valueOf(EquipmentUnitType.class, unitTypeName));

	}
	
	
	/**
	 * 获取匹配好的imsi或者msisdn
	 * @param imsiOrMsisdn
	 * @return
	 */
	public static Map<String,Object> getNumberStr(String imsiOrMsisdn){
	  
	  Map<String,Object> mapNumber = new HashMap<String,Object>();
	  List<Integer> numbers = new ArrayList<>();
	  String  prefixNumber = null;
	  String [] aa = imsiOrMsisdn.split("、");
	  
	  for(int i=0;i<aa.length;i++){
		  if(i>0){
			  matenumber(aa[i],numbers);
		  }else{
			  if(aa[0].contains("-")){
				  //循环匹配号段值
				  int startLength = aa[0].indexOf("-")-1;
				  prefixNumber = aa[0].substring(0, startLength);
				  String newImsi = aa[0].substring(startLength, aa[0].length());
				  matenumber(newImsi,numbers);
			  }else{
				  //直接使用该值
				  prefixNumber = aa[0];
			  }
		  }
		  
	  }
	  mapNumber.put("perfix", prefixNumber);
	  mapNumber.put("suffix", numbers);
	  return mapNumber;
	}
	
	
	
	/**
	 * 根据861582150-3、5-9 号段进行循环匹配
	 * @param 号段 suffix 后缀 
	 * @param numbers 后缀变化值
	 */
	private static void matenumber(String  suffix,List<Integer> numbers){
		 Integer startims = Integer.parseInt(suffix.split("-")[0]);
		  Integer endims = Integer.parseInt(suffix.split("-")[1]);
		  numbers.add(startims);
		  while(startims<endims){
			  startims++;
			  numbers.add(startims);
		  }
	}
	
	

	@Override
	public List<EquipmentNumberSection> findEquipmentNumberSectionListByAreaAreaCodeEquals(String areaCode) {
		return equipmentNumberSectionRepository.findEquipmentNumberSectionListByAreaAreaCodeEquals(areaCode);
	}

}
