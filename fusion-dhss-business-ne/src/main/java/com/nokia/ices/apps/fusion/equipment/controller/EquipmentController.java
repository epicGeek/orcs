package com.nokia.ices.apps.fusion.equipment.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNumberSection;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentTypeRelNeUnit;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentTypeRelUnitWeb;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentWebInterface;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentProtocol;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentWebInterfaceType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNumberSectionRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentTypeRelNeUnitRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitTypeWebTypeRelRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentWebInterfaceRepository;
import com.nokia.ices.apps.fusion.equipment.service.EquipmentService;
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.jms.vo.Context;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;
import com.nokia.ices.core.utils.Encodes;
import com.nokia.ices.core.utils.JsonMapper;

@RepositoryRestController
@RestController
public class EquipmentController {

    public static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);
    
    public static final String unitManage = "单元管理";

    @Autowired
    private EquipmentService equipmentService;
    
    @Autowired
    private EquipmentNumberSectionRepository equipmentNumberSectionRepository;

    @Autowired
    private SystemRoleRepository systemRoleRepository;

    @Autowired
    private EquipmentNeRepository equipmentNeRepository;
    
    @Autowired
    private EquipmentUnitRepository equipmentUnitRepository;
    
    @Autowired
    private EquipmentUnitTypeWebTypeRelRepository equipmentUnitTypeWebTypeRelRepository;
    
    @Autowired
    private EquipmentWebInterfaceRepository equipmentWebInterfaceRepository;

    @SuppressWarnings("rawtypes")
    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;
    
    @Autowired
    DHSSMessageProducer messageProducer;
    
    @Autowired
    SystemOperationLogRepository systemOperationLogRepository;
    
    @Autowired
    EquipmentTypeRelNeUnitRepository equipmentTypeRelNeUnitRepository;
    
    
    
    @RequestMapping("find/DropDownList")
    public Map<String, Object> DropDownList(){
    	Iterable<EquipmentNe> neIterable = equipmentNeRepository.findAll();
    	String[] neTypes = getEquipmentNeType();
    	Iterable<EquipmentTypeRelNeUnit> types = equipmentTypeRelNeUnitRepository.findAll();
    	
    	Set<String> dhssNames = new HashSet<String>();
    	Set<String> locations = new HashSet<String>();
    	
    	
    	for (EquipmentNe equipmentNe : neIterable) {
    		dhssNames.add(equipmentNe.getDhssName());
    		locations.add(equipmentNe.getDhssName()+ "_" + equipmentNe.getLocation());
		}
    	Map<String, Object> allMap = new HashMap<String, Object>();
    	allMap.put("dhssNames", dhssNames);
    	allMap.put("locations", locations);
    	allMap.put("neTypes", neTypes);
    	allMap.put("nes", neIterable);
    	allMap.put("types", types);
    	return allMap;
    }
    
    
    @RequestMapping(value = "equipment-unit/search/all")
    public List<EquipmentUnit> getEquipmentUnitAll(Sort sort) {
        Subject subject = SecurityUtils.getSubject();
        Iterable<EquipmentUnit> iter =  equipmentUnitRepository.findAll();
        List<EquipmentUnit> result = new ArrayList<EquipmentUnit>();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        for (EquipmentUnit equipmentUnit : iter) {
            if(subject.isPermitted("resource:"+equipmentUnit.getId()) || 
            		shiroUser.getUserName().equals((equipmentUnit.getCreateUser() == null ? "" : equipmentUnit.getCreateUser()))){
                result.add(equipmentUnit);
			}
		}
        return result;
    }
    
    @RequestMapping(value = "/findEquipmentNumberSectionListByAreaAreaCodeEquals")
    public List<EquipmentNumberSection> findEquipmentNumberSectionListByAreaAreaCodeEquals(@RequestParam("areaCode")String areaCode){
    	return equipmentNumberSectionRepository.findEquipmentNumberSectionListByAreaAreaCodeEquals(areaCode);
    }
    
    @RequestMapping(value = "/equipment-unitType/search/list")
    public String[] getEquipmentUnitType() {
    	EquipmentUnitType [] types = EquipmentUnitType.values();
    	String [] test = new String[types.length];
    	int i = 0;
    	for (EquipmentUnitType string : types) {
    		test[i] = string.name();
    		i++;
		}
    	Arrays.sort(test);
        return test;
    }
    
    @RequestMapping(value = "/equipment-neType/search/list")
    public String[] getEquipmentNeType() {
        EquipmentNeType [] types = EquipmentNeType.values();
    	String [] test = new String[types.length];
    	int i = 0;
    	for (EquipmentNeType string : types) {
    		test[i] = string.name();
    		i++;
		}
    	Arrays.sort(test);
        return test;
    }

    @RequestMapping(value = "/equipment-unit/search/list")
    public Iterable<EquipmentUnit> getEquipmentUnitList(Sort sort) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Assert.notNull(shiroUser, "没有登录");
        Collection<SystemRole> roleSet = systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName());
        Assert.notNull(roleSet, "登录角色为空");
        return equipmentUnitRepository.findAll();
    }
    


    @RequestMapping(value = "/equipment-ne/search/list")
    public Iterable<EquipmentNe> getEquipmentNeList(Sort sort) {
//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return equipmentNeRepository.findAll();
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/equipment-ne/search/page")
    public PagedResources<EquipmentNe> getEquipmentNeList(
            ModelMap paramMap,
            Pageable pageable, 
            PersistentEntityResourceAssembler assembler) {
//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        SystemRole systemRole = new SystemRole();
        Page<EquipmentNe> page = equipmentService.findEquipmentNePageBySearchFilter(paramMap,systemRole, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
    }



    @RequestMapping(value = "/equipment-unit/list/search")
    public List<EquipmentUnit> getEquipmentUnitList(@RequestParam("unitName") String unitName,
            @RequestParam("neId") String neId) {
//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        SystemRole systemRole = new SystemRole();
        List<String> searchField = new ArrayList<String>();
        searchField.add(unitName);
        searchField.add(neId);
        List<EquipmentUnit> page = equipmentService.findEquipmentUnitListBySearchFilter(systemRole, null);
//        logger.debug(new JsonMapper().toJson(page));
        return page;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/equipment-unit/search/searchByFilter")
    public PagedResources<EquipmentUnit> getEquipmentUnitList(
            @RequestParam(value="neName",required=false) String neName,
            @RequestParam(value="unitType",required=false) String unitType,
            @RequestParam(value="unitName",required=false) String unitName,
            Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
        System.out.println(neName);
        System.out.println(unitType);
        System.out.println(unitName);
        System.out.println(pageable.toString());

//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        
        Map paramMap = new HashMap();

        paramMap.put("neName", neName);
        paramMap.put("unitType", unitType);
        paramMap.put("unitName", unitName);
        
        Page<EquipmentUnit> page = equipmentService.findEquipmentUnitPageBySearchFilter(paramMap,null, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/equipment-number-section/search/searchByFilter")
    public PagedResources<EquipmentNumberSection> getEquipmentNumberSectionList(
    		@RequestParam(value="searchField",required=false) String searchField,
            ModelMap paramMap,Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        // SystemRole systemRole = shiroUser.getRole();
        SystemRole systemRole = new SystemRole();
        paramMap.put("number_LIKE", searchField);
        paramMap.put("imsi_LIKE", searchField);
        Page<EquipmentNumberSection> page = equipmentService.findEquipmentNumberSectionPageBySearchFilter(paramMap,systemRole, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
    }


//    @RequestMapping(value = "/equipment-numbersection/queryNsByNe", method = RequestMethod.GET)
//    public List<Object[]> getEquipmentNumberSectionList(@RequestParam("ne_id") Long ne_id,
//            @RequestParam("type") String type, @RequestParam(value = "keyWord", required = false) String keyWord) {
//
//        // 该NE对应的号码段
//        List<EquipmentNumberSection> numberSectionIn = equipmentNumberSectionRepository.findEquipmentNumberSectionListByNeIdEquals(ne_id);
//        // 得到所有的带keyword的EquipmentNumberSection
//        List<EquipmentNumberSection> list = equipmentService.findEquipmentNumberSectionBySearch(keyWord);
//        List<Object[]> resultList = new ArrayList<Object[]>();
//        for (EquipmentNumberSection equipmentNumberSection : list) {
//            resultList.add(new Object[] {equipmentNumberSection.getId(), equipmentNumberSection,
//                    numberSectionIn.contains(equipmentNumberSection)});
//        }
//        return resultList;
//    }

//    @RequestMapping(value = "/equipment-numbersection/queryNsByArea", method = RequestMethod.GET)
//    public List<EquipmentNumberSection> getNumberSectionListByAreaid(@RequestParam("area_id") Long area_id,
//            @RequestParam("type") String type, @RequestParam("keyWord") String keyWord) {
//        List<EquipmentNumberSection> list = equipmentService.findEquipmentNumberSectionByAreaID(keyWord, type, area_id);
//        for (EquipmentNumberSection obj : list) {
//            if (obj.getArea() != null && obj.getArea().getId() == area_id) {
//                obj.setInUse(true);
//            } else {
//                obj.setInUse(false);
//            }
//        }
//        return list;
//    }

    @RequestMapping(value = "/equipment-WebInterface/queryWebInterfaceByUnit", method = RequestMethod.GET)
    public List<EquipmentWebInterface> getWebInterfaceListbyUnitId(@RequestParam("unit_id") Long unit_id,
            @RequestParam("unit_type_id") String unit_type_id, @RequestParam("ip") String ip) {
        // 得到该单元型下所有webinterface类型
        String webInterfaceType_ids = "";
        EquipmentUnit  equipmentUnit = equipmentUnitRepository.findOne(unit_id);
        
        EquipmentUnitType  equipmentUnitType =  equipmentUnit.getUnitType();
        logger.debug("&&&&&&&&&&&&&&:"+new JsonMapper().toJson(equipmentUnitType));
        List<EquipmentTypeRelUnitWeb> list = equipmentUnitTypeWebTypeRelRepository.findByUnitType(equipmentUnitType);

        // 得到该单元类型下相对应的webinterface类型的所有EquipmentWebInterface
        List<EquipmentWebInterface> tatal_webinterface = equipmentWebInterfaceRepository.findByUnit(equipmentUnit);

        logger.debug("&&&&&&&&&&&&&&:"+new JsonMapper().toJson(list));
        logger.debug("**************:"+new JsonMapper().toJson(tatal_webinterface));
        if (tatal_webinterface.isEmpty()) {// 该单元类型下没有存在web接口
            for (EquipmentTypeRelUnitWeb etruw : list) {           	
                EquipmentWebInterface ewi = new EquipmentWebInterface();               
                EquipmentWebInterfaceType webType = etruw.getWebType();
                ewi.setInterfaceType(webType);
                ewi.setUrl(getWebInterfaceTemplate(ip, webType.name().toString()));
                
                ewi.setUserName("");
                ewi.setPassword("");
               tatal_webinterface.add(ewi);
            }
        }

        return tatal_webinterface;
    }

    @RequestMapping(value = "/equipment-unit/dele", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public EquipmentUnit deleteUnit(@RequestBody ModelMap paramMap) {
    	Long id = Long.parseLong(paramMap.get("unitId").toString());
        EquipmentUnit equipmentUnit = equipmentUnitRepository.findOne(id);
        String uuid = UUID.randomUUID().toString();
        equipmentUnit.setUuId(uuid);
        equipmentUnit.setIsForbidden(true);
        equipmentUnit = equipmentUnitRepository.save(equipmentUnit);
        // 通知MQ
        Map<String, Object> mq_map = new HashMap<String, Object>();
        /*mq_map.put("srcQ", EquipmentService.DESQ_NAME_UNIT);
        mq_map.put("destQ", ProjectProperties.getDesQName());
        mq_map.put("session", uuid);
        mq_map.put("netype", equipmentUnit.getUnitType().toString());
        mq_map.put("neName", equipmentUnit.getUnitName());
        mq_map.put("commandName", "delNet");
        mq_map.put("action", EquipmentService.UNIT_DELETE);
        mq_map.put("exeTimeoutMinutes", 30);
        mq_map.put("identification", "");
        mq_map.put("msgCode", 60013);*/
        
        
        mq_map.put("msgCode", 71000);
		mq_map.put("app", "dhss");
		mq_map.put("cacheTime", 5);
		mq_map.put("maxConnNum", 5);
		mq_map.put("type", equipmentUnit.getNeType());
		mq_map.put("srcQ", EquipmentService.DESQ_NAME_UNIT);
		mq_map.put("destQ", ProjectProperties.getDesQName());
		mq_map.put("sessionid", uuid);
		mq_map.put("ne", equipmentUnit.getUnitName());
		mq_map.put("neConnType", "DHSS_" + equipmentUnit.getServerProtocol());
		mq_map.put("password", Encodes.encodeHex(equipmentUnit.getLoginPassword() != null
				? equipmentUnit.getLoginPassword().getBytes() : "".getBytes()));
		mq_map.put("port", equipmentUnit.getServerPort());
		mq_map.put("priority", 5);
		mq_map.put("procotol", equipmentUnit.getServerProtocol());
		mq_map.put("username", equipmentUnit.getLoginName());
		mq_map.put("ip", equipmentUnit.getServerIp());
		mq_map.put("content", new Context("",2, 1));
		mq_map.put("hostname", equipmentUnit.getHostname());
		mq_map.put("netFlag", equipmentUnit.getNetFlag());
		mq_map.put("vendor", "nokia");
		mq_map.put("flag", "");
		mq_map.put("retryInterval", 3);
		mq_map.put("retryTimes", 2);
		mq_map.put("needJump", 0);
		mq_map.put("jumpCount", 0);
		mq_map.put("callInterfaceName", "");
		mq_map.put("msg", "");
		mq_map.put("src", "");
		mq_map.put("exculde", 0);
		mq_map.put("taskNum", 71007);
		mq_map.put("unitType", equipmentUnit.getUnitType());
        
        
        
        
        logger.debug("Send delete Unit informcation parameters to MQ :" + mq_map.toString());
        messageProducer.sendMessage(mq_map);
        saveSystemOperationLog("删除单元"+equipmentUnit.getUnitName(),SystemOperationLogOpType.Del);
        return equipmentUnit;
    }
   /* @RequestMapping(value = "/equipment-unit/{id:\\d+}/jcomp", method = RequestMethod.PATCH)
    public EquipmentUnit modifyUnit(@PathVariable Long id,@RequestBody EquipmentUnit equipmentUnit) {
    	
    	
        return syncWithJComp(equipmentUnit,EquipmentService.UNIT_MODIFY);
    }*/
    
    @RequestMapping(value = "/equipment-unit/addOrEdit",method = RequestMethod.POST,  produces=MediaType.APPLICATION_JSON_VALUE)
    public EquipmentUnit addOrEditUnit(@RequestBody ModelMap paramMap) {
    	
    	logger.debug(new JsonMapper().toJson(paramMap));
    	
    	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    	EquipmentUnit equipmentUnit = new EquipmentUnit();
    	String sunitId = paramMap.get("unitId").toString();
    	if(sunitId != null && !sunitId.equals("")){
    		equipmentUnit = equipmentUnitRepository.findOne(Long.parseLong(sunitId));
    	}
    	EquipmentNe equipmentNe = equipmentNeRepository.findOne(Long.parseLong(paramMap.get("neId").toString()));
    	equipmentUnit.setNeType(equipmentNe.getNeType());
    	equipmentUnit.setUnitType(EquipmentUnitType.valueOf(paramMap.get("unitType").toString()));
    	equipmentUnit.setCreateUser(shiroUser.getUserName());
    	equipmentUnit.setIsForbidden(false);
    	
    	equipmentUnit.setServerIp(paramMap.get("serverIp").toString());
    	equipmentUnit.setServerPort(Integer.parseInt(paramMap.get("serverPort").toString()));
    	equipmentUnit.setServerProtocol(EquipmentProtocol.valueOf(paramMap.get("serverProtocol").toString()));
    	equipmentUnit.setUnitName(paramMap.get("unitName").toString());
    	equipmentUnit.setNe(equipmentNe);	 
    	equipmentUnit.setUuId(UUID.randomUUID().toString());
    	if(sunitId !=""){
    		equipmentUnit.setId(Long.parseLong(sunitId));
    		equipmentUnit.setLoginPassword(paramMap.get("loginPassword").toString().equals("") ? equipmentUnit.getLoginPassword() : paramMap.get("loginPassword").toString() );
        	equipmentUnit.setRootPassword(paramMap.get("rootPassword").toString().equals("") ? equipmentUnit.getRootPassword() : paramMap.get("rootPassword").toString());
    		equipmentUnit = equipmentUnitRepository.save(equipmentUnit);
//        	syncWithJComp(equipmentUnit,EquipmentService.UNIT_MODIFY);
        	saveSystemOperationLog("修改单元"+equipmentUnit.getUnitName(),SystemOperationLogOpType.Update);
    	}else{
    		equipmentUnit.setLoginName(paramMap.get("loginName").toString());
    		equipmentUnit.setLoginPassword(paramMap.get("loginPassword").toString());
        	equipmentUnit.setRootPassword(paramMap.get("rootPassword").toString());
    		equipmentUnit = equipmentUnitRepository.save(equipmentUnit);
    		
//        	syncWithJComp(equipmentUnit,EquipmentService.UNIT_ADD);
        	saveSystemOperationLog("添加单元"+equipmentUnit.getUnitName(),SystemOperationLogOpType.Add);
    	}
    	
    	
    	
        return equipmentUnit;
    }
    
    public void saveSystemOperationLog( String OpText,SystemOperationLogOpType OpType) {
    	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setLoginUserName(shiroUser.getSelfLink());
		systemOperationLog.setApp("DHSS");
		systemOperationLog.setAppModule(unitManage);
		systemOperationLog.setOpText(OpText);
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setLogTime(new Date());
		systemOperationLogRepository.save(systemOperationLog);
	}
    
    /*@RequestMapping(value = "/equipment-unit/{id:\\d+}/jcomp", method = RequestMethod.PUT)
    public EquipmentUnit addUnit(@PathVariable Long id,@RequestBody EquipmentUnit equipmentUnit) {
    	
    	
        return syncWithJComp(equipmentUnit,EquipmentService.UNIT_ADD);
    }*/

    /*private EquipmentUnit syncWithJComp(EquipmentUnit equipmentUnit,String action) {
        // 通知MQ
        Map<String, Object> mq_map = new HashMap<String, Object>();
        mq_map.put("srcQ", EquipmentService.DESQ_NAME_UNIT);
        mq_map.put("destQ", ProjectProperties.getDesQName());
        mq_map.put("session", equipmentUnit.getUuId());
        
        
        mq_map.put("neName", equipmentUnit.getUnitName());
        mq_map.put("ip", equipmentUnit.getServerIp());
        mq_map.put("port", equipmentUnit.getServerPort());
        mq_map.put("username", equipmentUnit.getLoginName());
        mq_map.put("password", equipmentUnit.getLoginPassword());

        mq_map.put("protocal", equipmentUnit.getServerProtocol());
        mq_map.put("netype", equipmentUnit.getUnitType().toString());
        mq_map.put("commandName", "addNet");
        mq_map.put("exeTimeoutMinutes", 30);
        mq_map.put("identification", "");
        mq_map.put("msgCode", 60013);
        
        mq_map.put("action", action);

        mq_map.put("eqType", equipmentUnit.getId());
        logger.debug("Send update Unit informcation parameters to MQ :" + mq_map.toString());
        messageProducer.sendMessage(mq_map);
        return equipmentUnit;
    }*/

    @SuppressWarnings("unused")
	private String getWebInterfaceTemplate(String ip, String webinterface_type_name) {
        String url = "";
        if (webinterface_type_name.toLowerCase().equals("lumaf")) {
            url = "http://" + ip + ":9880/lumaf";
        } else if (webinterface_type_name.toLowerCase().equals("lemaf")) {
            url = "http://" + ip + ":9880/lemaf";
        } else if (webinterface_type_name.toLowerCase().equals("tsp")) {
            url = "http://" + ip + ":9099";
        } else if (webinterface_type_name.toLowerCase().equals("vc")) {
            url = "http://" + ip;
        } else if (webinterface_type_name.toLowerCase().equals("ilo")) {
            url = "https://" + ip + "/index.html";
        } else if (webinterface_type_name.toLowerCase().equals("oa")) {
            url = "http://" + ip;
        } else if (webinterface_type_name.equals("PGW开通（HLR）")) {
            url = "https://" + ip + "/ProvGwHlrGui";
        } else if (webinterface_type_name.equals("PGW开通（HSS）")) {
            url = "https://" + ip + "/ProvGwHssGui";
        }
        return url;
    }
    
    
}


