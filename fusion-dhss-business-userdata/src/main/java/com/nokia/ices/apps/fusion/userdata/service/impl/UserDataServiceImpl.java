package com.nokia.ices.apps.fusion.userdata.service.impl;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.dom4j.xpath.DefaultXPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.equipment.service.NumberSectionService;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.subtool.domain.CheckSubtoolResult;
import com.nokia.ices.apps.fusion.userdata.domain.UserDataLog;
import com.nokia.ices.apps.fusion.userdata.repository.UserDataRepository;
import com.nokia.ices.apps.fusion.userdata.service.CommonSoapUtils;
import com.nokia.ices.apps.fusion.userdata.service.UserDataService;
import com.nokia.ices.core.utils.DateUtil;
import com.nokia.ices.core.utils.ModuleDownLoadNameDefinition;

@Service("userDataService")
public class UserDataServiceImpl implements UserDataService {

	private final static Logger logger = LoggerFactory.getLogger(UserDataServiceImpl.class);

	private final static String USER_RULE_PARSE = "userRuleParse.xml";
	private final static String USER_RULE_PARSE_ADVANCED = "userRuleParse_advanced.xml";

	@Autowired
	UserDataRepository userDataRepository;

	@Autowired
	NumberSectionService numberSectionService;

	@Autowired
	private EquipmentUnitRepository equipmentUnitRepository;

	@Override
	public Page<UserDataLog> findUserDataPageBySearch(Map<String, Object> searchParams, ShiroUser shiroUser,
			Pageable pageable) {

		// 模糊查询对象
		List<SearchFilter> searchFilterOr = new ArrayList<SearchFilter>();
		String searchField = searchParams.get("searchField").toString();
		if (StringUtils.isNotEmpty(searchField)) {
			searchFilterOr.add(new SearchFilter("numberSection", Operator.LIKE, searchField));
			searchFilterOr.add(new SearchFilter("createName", Operator.LIKE, searchField));
		}
		
		 List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isNotBlank(searchParams.get("startTime").toString())){
			 try {
				searchFilterAnd.add(new SearchFilter("createTime", Operator.GE, sdf.parse(searchParams.get("startTime").toString())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		 }
		 if(StringUtils.isNotBlank(searchParams.get("endTime").toString())){
			 try {
				searchFilterAnd.add(new SearchFilter("createTime", Operator.LT, sdf.parse(searchParams.get("endTime").toString())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		 }
		
		Specification<UserDataLog> specListOR = DynamicSpecifications.bySearchFilter(searchFilterOr, BooleanOperator.OR,
				UserDataLog.class);
		
		Specification<UserDataLog> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,UserDataLog.class);

		
		// Specification<UserDataLog> specListAll =
		// DynamicSpecifications.concatSpecification(BooleanOperator.AND,specListOR);
		return userDataRepository.findAll(Specifications.where(specListOR).and(specListAnd), pageable);

	}

	
	/**
	 * 获取soap数据
	 */
	@Override
	public Map<String, Object> saveUserData(String type, String value, Boolean isAdvance) {
		logger.debug("开始获取用户数据..............." + value);

		// 查询当前号段的soapurl list
		List<EquipmentUnit> soapUrlList = null;
		Map<String, Object> resultMap = null;
		String pattern = null;
		try {
			if (value.contains("xml")) {// 打开操作
				// 文件下载路径
				String filePath = ProjectProperties.getLogBasePath() + File.separator
						+ ModuleDownLoadNameDefinition.DOWNLOAD_SOAPXML + File.separator;
				resultMap = getResultXmlData(filePath + value, isAdvance);
			} else {
				/**
				 * 根据号码精确匹配PGWip，如果未匹配到ip，则使用所有PGWip
				 */
				ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
				soapUrlList = numberSectionService.findAllUnitTypeByNumberSection(value, type, "PGW", shiroUser);
				if (null != soapUrlList && soapUrlList.size() > 0) {
					// 号段匹配模式
					pattern = "Y";
					logger.debug("Them roughly matched pattern get pgwIp");
				} else {
					pattern = "N";
					soapUrlList = equipmentUnitRepository
							.findListByUnitTypeEquals(EquipmentUnitType.valueOf(EquipmentUnitType.class, "PGW"));
					logger.debug("Database query mode get pgwIp");
				}
				logger.debug("start reqeust  soap date " + new Date());
				Object[] result =CommonSoapUtils.getUserXml(type, value, soapUrlList);
				
				// 保存处理数据
				logger.debug("handleDate date " + new Date());
				
				if(null!=result && result.length>0){
					String resultXml = result[0].toString();
					String logPath = shiroUser.getUserName() + "_" + DateUtil.getCurrentDateTime2() + "_soapxml.xml";
					resultMap = handleDate(isAdvance, resultXml,logPath);
					if (null != resultMap && resultMap.size()>0) {
						resultMap.put("pattern", pattern);
						// 持久化处理
						UserDataLog log = new UserDataLog();
						if (null != result[1] && result[1] instanceof EquipmentUnit) {
							EquipmentUnit unit = (EquipmentUnit) result[1];
							log.setUnitName(unit.getUnitName());
							resultMap.put("unitName", unit.getUnitName());// 显示单元名称
						}
						log.setCreateName(shiroUser.getUserName());
						log.setNumberSection(value);
						log.setPath(logPath);
						log.setCreateTime(new Date());
						userDataRepository.save(log);
					}else{
						logger.debug("user bacth operation......");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}

		logger.debug("end date " + new Date());

		return resultMap;
	}

	private Map<String, Object> handleDate(Boolean isAdvance, String resultXml,String logPath) throws Exception {

		Map<String, Object> userList = null;
		if (StringUtils.isNotBlank(resultXml)) {
			if ("error".equalsIgnoreCase(resultXml)) {
				logger.debug("errorMessage: search userData fail.................. ");
				return userList;
			}

			String formatXml = CommonSoapUtils.formatXML(resultXml);
			// 保存到本地xml
			String filePath = ProjectProperties.getLogBasePath() + File.separator
					+ ModuleDownLoadNameDefinition.DOWNLOAD_SOAPXML + File.separator;
			File operationDir = new File(filePath);
			File fielDir = new File(filePath + logPath);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielDir.exists()) {
				fielDir.createNewFile();
			}
			// 写入文件文件
			CommonSoapUtils.fileWriter(fielDir.getPath(), formatXml);
			// 返回页面结果数据
			userList = getResultXmlData(fielDir.getPath(), isAdvance);
		}
		return userList;
	}

	

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getResultXmlData(String filePath, Boolean isAdvance)
			throws JsonParseException, JsonMappingException, IOException, IllegalAccessException,
			InstantiationException, InvocationTargetException, IntrospectionException, DocumentException {

		XmlMapper xmlMapper = new XmlMapper();
		Map<String, Object> userData = new HashMap<String, Object>();
		// Resource resource = new ClassPathResource("/soap/userRuleParse.xml");
		File file = null;
		if (isAdvance) {
			file = new File(ProjectProperties.getUserRuleParse() + USER_RULE_PARSE_ADVANCED);
		} else {
			file = new File(ProjectProperties.getUserRuleParse() + USER_RULE_PARSE);
		}

		HashMap<String, Object> map = xmlMapper.readValue(file, HashMap.class);
		map.remove("parentName");
		Map<Integer, Object> orderMap = orderTab(map);

		/**
		 * update 2015-11-10 处理多节点值顺序显示问题
		 */
		// 获取解析规则显示的数据项
		Map<String, Object> ruleMap = getRuleParseData(file, null);
		// 根据解析规则项获取数据值
		Map<String, Object> dataMap = getRuleParseData(filePath, ruleMap);

		// Map<String,Object> valMap = new HashMap<String,Object>();
		// getValueMap(map,valMap,filePath);
		userData.put("rulePars", orderMap);
		userData.put("keyAll", dataMap);
		return userData;
	}

	/**
	 * 规则解析以及soapxml值获取
	 * 
	 * @param obj
	 * @param ruleMap
	 *            解析规则path数据
	 * @return
	 */
	public static Map<String, Object> getRuleParseData(Object obj, Map<String, Object> ruleMap) {

		Document document = null;
		Map<String, Object> resultData = null;
		FileInputStream fileIn = null;
		try {
			SAXReader saxReader = new SAXReader();
			if (obj instanceof Resource) {
				File resource = (File) obj;
				fileIn = new FileInputStream(resource);
				document = saxReader.read(fileIn);
			} else {
				fileIn = new FileInputStream(new File(obj.toString()));
				document = saxReader.read(fileIn);
			}
			resultData = new HashMap<String, Object>();
			Element node = document.getRootElement();// 获取根节点

			if (ruleMap == null) {
				// 获取解析规则显示值
				getNodesRule(node, resultData, null);
			} else {
				getNodes(document, resultData, ruleMap);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultData;
	}

	/**
	 * 获取解析规则需要显示的path值
	 * 
	 * @param node
	 * @param dataMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void getNodesRule(Element node, Map<String, Object> dataMap, String key) {

		String parentName = node.attributeValue("parentName");
		if (null != parentName) {
			List<String> list = (List<String>) dataMap.get(parentName);
			if (list == null) {
				List<String> paths = new ArrayList<String>();
				dataMap.put(parentName, paths);
				key = parentName;
			}
		} else {
			String path = node.attributeValue("path");
			String showType = node.attributeValue("showType");
			String valueDes = node.attributeValue("valueDes");

			if (null != valueDes && "text".equals(showType)) {
				// String keyPath = path.substring(path.lastIndexOf("/") + 1,
				// path.length());
				if (!dataMap.containsKey(path)) {
					dataMap.put(path, valueDes);
				}
			}

			if (null != path) {
				if (null != key && path.contains(key)) {
					List<String> list = (List<String>) dataMap.get(key);
					if (!list.contains(path)) {
						list.add(path);
						dataMap.put(key, list);
					}
				} else {
					if (!dataMap.containsKey(path)) {
						dataMap.put(path, "path");
					}
				}
			}
		}

		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			getNodesRule(e, dataMap, key);// 递归
		}

	}

	/**
	 * 多个节点值处理
	 * 
	 * @param node
	 */
	public static void getManytoNodes(Element node, List<String> tabName) {
		// 当前节点的名称、文本内容和属性
		String nodeName = node.getName();
		tabName.add(nodeName);

		// System.out.println("当前节点名称：" + nodeName);// 当前节点名称
	}

	/**
	 * 根据解析规则数据获取 soapxml的值
	 * 
	 * @param node
	 * @param dataMap
	 * @param rulePath
	 */
	@SuppressWarnings("unchecked")
	public static void getNodes(Document document, Map<String, Object> dataMap, Map<String, Object> rulePath) {
		if (null != rulePath) {
			Set<Entry<String, Object>> entry = rulePath.entrySet();
			Iterator<Entry<String, Object>> it = entry.iterator();
			while (it.hasNext()) {
				Entry<String, Object> en = it.next();
				String key = en.getKey();// 取值Path
				Object obj = en.getValue();// value path对应的值
				if (obj instanceof List) {
					List<Element> listAll = document.selectNodes("/" + key);
					List<String> ruleList = (List<String>) obj;
					int nodeSize = listAll.size();
					for (int index = 0; index < nodeSize; index++) {
						for (String _path : ruleList) {
							if (dataMap.containsKey(_path)) {
								Object old_value = dataMap.get(_path);// 旧的值
								String new_value = getNewValue(listAll.get(index), index, _path, rulePath);// 新的值
								if (old_value instanceof Vector) {
									Vector<String> ve = (Vector<String>) old_value;
									ve.add(new_value);
									dataMap.put(_path, ve);
								} else {
									Vector<String> ve = new Vector<String>();
									ve.add(old_value.toString());
									ve.add(new_value);
									dataMap.put(_path, ve);
								}
							} else {
								String _value = getNewValue(listAll.get(index), index, _path, rulePath);
								dataMap.put(_path, _value);
							}
						}
					}
				} else {

					if (!dataMap.containsKey(key)) {
						List<String> vaList = new ArrayList<String>();
						List<DefaultElement> value_list = document.selectNodes("/" + key);
						if (null != value_list && value_list.size() > 0) {
							for (DefaultElement de : value_list) {
								String _value = de.getText();
								if (StringUtils.isNotEmpty(_value)) {
									String newValue = getXmlValue(_value, key, rulePath);
									vaList.add(newValue);
								}
							} 
							dataMap.put(key, vaList);
						} else {
							dataMap.put(key, "");
						}
					}

				}

			}
		}
	}

	private static String getXmlValue(String _value, String key, Map<String, Object> rulePath) {

		Map<String, String> desMap = new HashMap<>();
		// String label = key.substring(key.lastIndexOf("/") + 1, key.length());
		String valueDes = rulePath.get(key) == null ? null : rulePath.get(key).toString();
		if (valueDes != null && !"path".equalsIgnoreCase(valueDes)) {
			String[] str = valueDes.split(",");
			for (String value : str) {
				desMap.put(value.split(":")[0], value.split(":")[1]);
			}
		} else {
			return _value;
		}
		return desMap.get(_value) == null ? _value : desMap.get(_value);
	}

	/**
	 * 根据xmlpath获取对应值
	 * 
	 * @param document
	 * @param index
	 * @param _path
	 * @param rulePath
	 * @return
	 */
	private static String getNewValue(Element element, int index, String _path, Map<String, Object> rulePath) {

		String label = _path.substring(_path.lastIndexOf("/") + 1, _path.length());
		List<?> elementList = element.elements();
		Iterator<?> child = elementList.iterator();
		while (child.hasNext()) {
			// 将每个属性转化为一个抽象属性，然后获取其名字和值
			Element chlid = (Element) child.next();
			String name = chlid.getName();
			if (name.equalsIgnoreCase(label)) {
				String va = element.elementText(chlid.getName());
				if (StringUtils.isNotEmpty(va)) {
					return getXmlValue(va, _path, rulePath);
				}
			}
			// 循环获取子元素值
			if (chlid.elements().size() > 0) {
				return getNewValue(chlid, index, _path, rulePath);
			}

		}

		return "";

	}

	@SuppressWarnings("unchecked")
	private static Map<Integer, Object> orderTab(HashMap<String, Object> map) {

		Map<Integer, Object> orderMap = new HashMap<Integer, Object>();
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			String key = it.next().getKey();
			HashMap<String, Object> oldMap = (HashMap<String, Object>) map.get(key);
			String order = oldMap.get("order").toString();
			orderMap.put(Integer.parseInt(order), oldMap);
		}
		List<Map.Entry<Integer, Object>> orderList = new ArrayList<Map.Entry<Integer, Object>>(orderMap.entrySet());

		Collections.sort(orderList, new Comparator<Map.Entry<Integer, Object>>() {
			public int compare(Map.Entry<Integer, Object> o1, Map.Entry<Integer, Object> o2) {
				return (o1.getKey() - o2.getKey());

			}
		});

		return orderMap;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	private static void getValueMap(HashMap<String, Object> map, Map<String, Object> valMap, String filePath) {

		Set<Entry<String, Object>> entry = map.entrySet();
		Iterator<Entry<String, Object>> it = entry.iterator();
		try {
			while (it.hasNext()) {
				Entry<String, Object> en = it.next();
				String key = en.getKey();
				Object obj = en.getValue();
				if (obj instanceof HashMap) {
					HashMap<String, Object> enmap = (HashMap<String, Object>) obj;
					getValueMap(enmap, valMap, filePath);
				} else {

					if ("path".equals(key)) {
						String[] values = getXmlPath(obj.toString(), filePath);
						valMap.put(obj.toString(), values);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 根据指定xml节点路径获取值
	 * 
	 * @param xmlPath
	 * @param filePath
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static String[] getXmlPath(String xmlPath, String filePath) {
		List<String> valuesList = new ArrayList<String>();
		FileInputStream resource = null;
		try {
			resource = new FileInputStream(new File(filePath));
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(resource);
			DefaultXPath xpath = new DefaultXPath("/" + xmlPath);
			List<DefaultElement> list = xpath.selectNodes(document);
			int size = list.size();

			for (int i = 0; i < size; i++) {
				DefaultElement node = list.get(i);
				String value = node.getText();
				valuesList.add(value);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				resource.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return (String[]) valuesList.toArray();

	}

	/**
	 * 创建排序请求.
	 */
	private Sort buildSort(List<String> sortType) {
		Sort sort = new Sort(Direction.DESC, "id");
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
	@SuppressWarnings("unused")
	private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
		Sort sort = buildSort(sortType);
		return new PageRequest(page - 1, size, sort);
	}

	@Override
	public EquipmentUnit findEquipmentUnitByUnitName(String unitName) {
		// TODO Auto-generated method stub
		return equipmentUnitRepository.findEquipmentUnitByUnitName(unitName);
	}

}
