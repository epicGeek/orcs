package com.nokia.ices.apps.fusion.userdata.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.equipment.service.NumberSectionService;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.userdata.domain.UserBacthLog;
import com.nokia.ices.apps.fusion.userdata.repository.UserBacthRepository;
import com.nokia.ices.apps.fusion.userdata.service.BacthUserDataService;
import com.nokia.ices.apps.fusion.userdata.service.CommonSoapUtils;
import com.nokia.ices.core.utils.ExportExcel;

@Service
public class BacthUserDataServiceImpl implements BacthUserDataService {
	private static final Logger logger = LoggerFactory.getLogger(BacthUserDataServiceImpl.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	NumberSectionService numberSectionService;

	@Autowired
	private EquipmentUnitRepository equipmentUnitRepository;

	@Autowired
	UserBacthRepository userBacthRepository;

	public Long addBacthUserData(String[] numbers) {
		Map<String, Object> xmlMap = null;
		OutputStream out = null;
		List<String[]> vaList = new ArrayList<String[]>();
		List<String> titleNames = new ArrayList<String>();
		String phoneNumber = "";
		try {
			if ((null != numbers) && (numbers.length > 0)) {
				xmlMap = resolveUserBacthXml();
				int size = numbers.length;
				for (int i = 0; i < size; i++) {
					String[] values = new String[xmlMap.size()];
					String value = numbers[i];
					phoneNumber = phoneNumber + value + ",";

					if (StringUtils.isNotBlank(value)) {
						String type = value.matches("^86.*") ? "1" : "2";
						String formatXml = findSoapGetWIp(value, type);
						if(formatXml!=null){
							Iterator<Entry<String, Object>> it = xmlMap.entrySet().iterator();
							int index = 0;
							while (it.hasNext()) {
								Entry<String, Object> entry = it.next();
								Object val_obj = entry.getValue();
								String titleName = (String) entry.getKey();
								if (i == 0) {
									titleNames.add(titleName);
								}
								String title_val = resolveSoapXml(formatXml, val_obj, titleName);//TODO formatXml may be null.
								values[index] = title_val;
								index++;
							}
							vaList.add(values);
						}else{
							logger.info("Returned format xml is null!");
						}
					}
				}

				phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
				ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
				String userName = shiroUser.getUserName();

				String rootPath = ProjectProperties.getLogBasePath() + File.separator + "bacthUser" + File.separator;

				File operationDir = new File(rootPath);
				if ((!operationDir.exists()) && (!operationDir.isDirectory())) {
					operationDir.mkdir();
				}
				String fileName = userName + "_bacthUser_" + sdf.format(new Date()) + ".xlsx";
				out = new FileOutputStream(rootPath + fileName);
				ExportExcel<String[]> excel = new ExportExcel<String[]>();
				String[] headers = (String[]) titleNames.toArray(new String[titleNames.size()]);
				System.out.println("headers:");
				for (String string : headers) {
					System.out.println(string);
				}
				System.out.println("vaList:");
				for (String[] string : vaList) {
					for (String string2 : string) {
						System.out.println(string2);
					}
				}
				excel.exportExcel(userName + "_number", headers, vaList, out, null);
				

				UserBacthLog log = new UserBacthLog();
				log.setCreateName(userName);
				log.setCreateTime(new Date());
				log.setPath(fileName);
				log.setNumbers(phoneNumber);
				this.userBacthRepository.saveAndFlush(log);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != out)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out = null;
		}

		return Long.valueOf(0L);
	}

	@SuppressWarnings("unchecked")
	private static String resolveSoapXml(String localPath, Object val_obj, String titleName)
			throws FileNotFoundException, IOException, DocumentException {
		
		//InputStream fileIn = new FileInputStream("F:\\dhlr.download\\soapxml\\luopf_20160503091550_soapxml.xml");
		InputStream fileIn = new ByteArrayInputStream(localPath.getBytes());
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(fileIn);
		String result = "";
		DefaultElement oj;
		if ((val_obj instanceof String)) {
			String _path = val_obj.toString();
			List<Element> listAll = document.selectNodes("/" + _path);
			for (Element e : listAll)
				result = result + e.getTextTrim() + ",";
		} else {
			String[] arrData = (String[]) val_obj;
			String _path = arrData[0];
			String property = arrData[1];
			String include_val = arrData[2];
			String valuedes = arrData[3];
			List<Element> listAll = document.selectNodes("/" + _path);
			for (Element e : listAll) {
				String parentName = e.getName();
				if ((StringUtils.isNotBlank(include_val)) && (StringUtils.isNotBlank(property))) {
					List<DefaultElement> listDef = e.elements(property);
					if ((null != listDef) && (listDef.size() > 0)) {
						oj = listDef.get(0);
						String basiVal = oj.getTextTrim();
						if (!include_val.endsWith(basiVal))
							continue;
					}
				} else {
					List<Element> listAlls = e.elements();
					for (Element ee : listAlls) {
						String name = ee.getName();
						if (titleName.equalsIgnoreCase(parentName + "-" + name)) {
							String val = ee.getTextTrim();
							if (StringUtils.isNotBlank(valuedes)) {
								String[] desVal = valuedes.split(",");
								for (String _val : desVal) {
									String key = _val.split(":")[0];
									String value = _val.split(":")[1];
									if (val.equals(key)) {
										val = value;
									}
								}
							}

							result = result + val + ",";
							break;
						}
					}
				}
			}
		}
		
		if (StringUtils.isNotBlank(result)) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	private String findSoapGetWIp(String value, String type) throws IOException, DocumentException {
		String formatXml = null;
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<EquipmentUnit> soapUrlList = this.numberSectionService.findAllUnitTypeByNumberSection(value, type, "PGW",
				shiroUser);
		if ((null != soapUrlList) && (soapUrlList.size() > 0)) {
			logger.debug("Them roughly matched pattern get pgwIp");
		} else {
			soapUrlList = this.equipmentUnitRepository.findListByUnitTypeEquals(
					(EquipmentUnitType) EquipmentUnitType.valueOf(EquipmentUnitType.class, "PGW"));

			logger.debug("Database query mode get pgwIp all");
		}

		Object[] result = CommonSoapUtils.getUserXml(type, value, soapUrlList);

		if ((null != result) && (result.length > 0)) {
			String resultXml = result[0].toString();
			if ("error".equals(resultXml))
				logger.debug("resultXml information error");
			else {
				formatXml = CommonSoapUtils.formatXML(resultXml);
			}
		}

		return formatXml;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> resolveUserBacthXml()
			throws FileNotFoundException, IOException, DocumentException {
		Map<String, Object> data_map = new HashMap<String, Object>();

		String filePath = ProjectProperties.getUserRuleParse() + "user_bacth.xml";
		FileInputStream fileIn = new FileInputStream(filePath);
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(fileIn);
		Element node = document.getRootElement();
		List<Element> elementAll = node.elements();
		for (Element e : elementAll) {
			String name = e.getName();
			String path = e.attributeValue("path");
			String[] atts;
			if (e.hasContent()) {
				atts = new String[4];
				List<Element> listElement = e.elements();
				for (Element element : listElement) {
					String property = element.attributeValue("property");
					String valueDes = element.attributeValue("valueDes");
					atts[0] = path;
					if (StringUtils.isNotBlank(valueDes)) {
						atts[3] = valueDes;
					}
					if (StringUtils.isNotBlank(property)) {
						atts[1] = property;
						atts[2] = element.getTextTrim();
					} else {
						name = e.getName() + "-" + element.getTextTrim();
						data_map.put(name, atts);
					}
				}
			} else {
				data_map.put(name, path);
			}
		}

		return data_map;
	}

	public Page<UserBacthLog> findUserBacthLogPageBySearch(Map<String, Object> searchParams, ShiroUser shiroUser,
			Pageable pageable) {

		// 模糊查询对象
		List<SearchFilter> searchFilterOr = new ArrayList<SearchFilter>();
		String searchField = searchParams.get("searchField").toString();
		if (StringUtils.isNotEmpty(searchField)) {
			searchFilterOr.add(new SearchFilter("numberSection", Operator.LIKE, searchField));
			searchFilterOr.add(new SearchFilter("createName", Operator.LIKE, searchField));
		}
		Specification<UserBacthLog> specListOR = DynamicSpecifications.bySearchFilter(searchFilterOr,
				BooleanOperator.OR, UserBacthLog.class);
		// Specification<UserDataLog> specListAll =
		// DynamicSpecifications.concatSpecification(BooleanOperator.AND,specListOR);
		return userBacthRepository.findAll(Specifications.where(specListOR), pageable);

	}
}