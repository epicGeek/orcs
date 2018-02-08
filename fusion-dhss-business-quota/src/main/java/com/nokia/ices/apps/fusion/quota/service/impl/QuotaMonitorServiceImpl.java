package com.nokia.ices.apps.fusion.quota.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.kpi.domain.KpiItem;
import com.nokia.ices.apps.fusion.kpi.repository.KpiItemRepository;
import com.nokia.ices.apps.fusion.quota.dao.QuotaMonitorDao;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitor;
import com.nokia.ices.apps.fusion.quota.model.QuotaMonitorExport;
import com.nokia.ices.apps.fusion.quota.model.QuotaMonitorHistoryExportModel;
import com.nokia.ices.apps.fusion.quota.repository.QuotaMonitorRepository;
import com.nokia.ices.apps.fusion.quota.service.QuotaMonitorService;

@Service("quotaMonitorService")
public class QuotaMonitorServiceImpl implements QuotaMonitorService {
	@Autowired
	QuotaMonitorDao quotaMonitorDao;

	@Autowired
	private QuotaMonitorRepository quotaMonitorRepository;
	
	@Autowired
	private KpiItemRepository kpiItemRepository;

	public QuotaMonitorDao getQuotaMonitorDao() {
		return quotaMonitorDao;
	}

	public void setQuotaMonitorDao(QuotaMonitorDao quotaMonitorDao) {
		this.quotaMonitorDao = quotaMonitorDao;
	}

	public List<Map<String, Object>> queryAllBscType(Map<String, Object> m) {
		return quotaMonitorDao.queryAllBscType(m);
	}

	public List<Map<String, Object>> queryAllBscName(Map<String, Object> m) {
		return quotaMonitorDao.queryAllBscName(m);
	}

	@Override
	public Page<QuotaMonitor> findQuotaMonitorFilter(Map<String, Object> paramMap, Pageable pageable) {
		Map<String, SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<QuotaMonitor> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				QuotaMonitor.class);
		Page<QuotaMonitor> page = quotaMonitorRepository.findAll(spec, pageable);
		return page;
	}

	@Override
	public List<QuotaMonitor> findQuotaMonitor(Map<String, Object> paramMap) {
		Map<String, SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<QuotaMonitor> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				QuotaMonitor.class);
		return quotaMonitorRepository.findAll(spec);

	}

	@Override
	public List<Map<String, Object>> queryQuotaMonitorData(Map<String, Object> m) {
		List<Map<String, Object>> returnm = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> temp = quotaMonitorDao.queryQuotaMonitorData(m);
		for (Map<String, Object> ob : temp) {
			if (ob.get("kpiname").equals("kpi001") || ob.get("kpiname").equals("kpi002")
					|| ob.get("kpiname").equals("kpi003") || ob.get("kpiname").equals("kpi004")
					|| ob.get("kpiname").equals("kpi005") || ob.get("kpiname").equals("kpi006")
					|| ob.get("kpiname").equals("kpi007") || ob.get("kpiname").equals("kpi010")) {
				ob.put("kpiunit", "%");
				ob.put("kpivalue", Double.parseDouble(ob.get("kpivalue").toString().trim()) * 100);
			} else {
				ob.put("kpiunit", "次");
				ob.put("kpivalue", ob.get("kpivalue").toString().trim());
			}
			returnm.add(ob);
		}
		return returnm;
	}

	@Override
	public Long queryQuotaMonitorDataCount(Map<String, Object> m) {
		return quotaMonitorDao.queryQuotaMonitorDataCount(m);
	}

	@Override
	public List<Map<String, Object>> queryAllQuotaData(Map<String, Object> m) {
		List<Map<String, Object>> returnm = new ArrayList<Map<String, Object>>();
		Map<String, Object> returnObj = new HashMap<String, Object>();

		List<Map<String, Object>> temp = quotaMonitorDao.queryAllQuotaData(m);
		if (temp == null || temp.size() <= 0)
			return returnm;
		List<String> xdateList = new ArrayList<String>();// X轴的时间标题显示
		// System.out.println("有多少数据"+temp.size());
		Map<String, Double> successRate = new HashMap<String, Double>();// 成功率,kpivalue值,
																		// key=period
		Map<String, Double> successCount = new HashMap<String, Double>();// 失败次数,kpifailcount值
																			// key=period

		String xdate = "";
		for (Map<String, Object> ob : temp) {
			xdate = ob.get("period").toString().trim().split(" ")[1];
			xdate = xdate.split(":")[0] + ":" + xdate.split(":")[1];
			if (!xdateList.contains(xdate)) {
				xdateList.add(xdate);
			}
			successRate.put(xdate, Double.parseDouble(ob.get("kpivalue").toString().trim()) * 100);
			successCount.put(xdate, Double.parseDouble(ob.get("kpifailcount").toString().trim()));
			// System.out.println(xdate+"=="+Double.parseDouble(ob.get("kpivalue").toString().trim())+"=="+Double.parseDouble(ob.get("kpifailcount").toString().trim()));
		}

		Collections.sort(xdateList);// 自然升序排序
		String datitle = "";
		for (String s : xdateList) {
			datitle += s + ",";
		}
		// System.out.println(datitle);
		returnObj.put("datitle", datitle.substring(0, datitle.length() - 1));
		returnObj.put("successRate", successRate);
		returnObj.put("successCount", successCount);

		returnm.add(returnObj);// 全计算完后返回
		return returnm;
	}
	/*
	 * @Override public List<QuotaMonitor> findPageByCriteria( Map<String,
	 * Object> m) { List<QuotaMonitor> returnList=new ArrayList<QuotaMonitor>();
	 * List<Map<String, Object>> temp=quotaMonitorDao.queryQuotaMonitorData(m);
	 * if(temp==null ||temp.size()<=0) return returnList; QuotaMonitor qm=null;
	 * for(Map<String, Object> ms:temp){ qm=new QuotaMonitor();
	 * qm.setNthlr_id(Integer.parseInt(ms.get("nthlr_id").toString().trim()));
	 * qm.setNthlr_name(ms.get("nthlr_name").toString().trim());
	 * qm.setNthlrfe_id(Integer.parseInt(ms.get("nthlrfe_id").toString().trim())
	 * ); qm.setNthlrfe_name(ms.get("nthlrfe_name").toString().trim());
	 * qm.setNode(ms.get("node").toString().trim());
	 * qm.setNetype(ms.get("netype").toString().trim());
	 * qm.setKpiid(ms.get("kpiname").toString().trim());
	 * qm.setKpiname(idtostr(ms.get("kpiname").toString().trim()));
	 * qm.setKpifailcount(ms.get("kpifailcount").toString().trim());
	 * qm.setKpirequestcount(ms.get("kpirequestcount").toString().trim());
	 * qm.setPeriod(ms.get("period").toString().trim()); //
	 * qm.setPerperiod(getEarlyNPeriodsII(ms.get("period").toString().trim(),
	 * 1440)); qm.setPerperiod(ms.get("period").toString().trim().split(" ")[0]+
	 * " 00:00:00"); qm.setScene(ms.get("scene").toString().trim());
	 * if(ms.get("kpiname").equals("kpi001") ||
	 * ms.get("kpiname").equals("kpi002") ||ms.get("kpiname").equals("kpi003")
	 * || ms.get("kpiname").equals("kpi004")
	 * ||ms.get("kpiname").equals("kpi005") ||ms.get("kpiname").equals("kpi006")
	 * ||ms.get("kpiname").equals("kpi007")||ms.get("kpiname").equals("kpi010"))
	 * { qm.setKipUnit("%");
	 * qm.setKpivalue(Double.parseDouble(ms.get("kpivalue").toString().trim())*
	 * 100+""); }else{ qm.setKipUnit("次");
	 * qm.setKpivalue(ms.get("kpivalue").toString().trim()); }
	 * returnList.add(qm); } return returnList; }
	 */

	private String idtostr(String kpiid) {
		if (kpiid.equals("kpi001"))
			return "鉴权请求成功率";
		else if (kpiid.equals("kpi002"))
			return "语音呼叫查询成功率";
		else if (kpiid.equals("kpi003"))
			return "短信被叫查询成功率";
		else if (kpiid.equals("kpi004"))
			return "位置更新请求成功率";
		else if (kpiid.equals("kpi005"))
			return "GPRS附着成功率";
		else if (kpiid.equals("kpi006"))
			return "LTE位置更新成功率";
		else if (kpiid.equals("kpi007"))
			return "LTE用户鉴权成功率";
		else if (kpiid.equals("kpi008"))
			return "MAP-CancelLocation请求次数";
		else if (kpiid.equals("kpi009"))
			return "S6A-CancelLocation请求次数";
		else if (kpiid.equals("kpi010"))
			return "HLR-LADP";
		else
			return "未识别";
	}

	public static String getEarlyNPeriodsII(String period, int n) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatterII = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formatter.parse(period);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1900 + date.getYear());
		calendar.set(Calendar.MONTH, date.getMonth());
		calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
		calendar.set(Calendar.HOUR_OF_DAY, date.getHours());
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.MINUTE, -n);
		return formatterII.format(calendar.getTime());
	}

	@Override
	public List<QuotaMonitor> queryQuotaMonitorModelData(Map<String, Object> paramMap) {
		Map<String, SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<QuotaMonitor> specM = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				QuotaMonitor.class);
		return quotaMonitorRepository.findAll(specM);
	}
	@Override
	public List<QuotaMonitorHistoryExportModel> findPageByCriteriaHistoryExport(Map<String, Object> m){
		List<QuotaMonitorHistoryExportModel> returnList = new ArrayList<QuotaMonitorHistoryExportModel>();
		List<QuotaMonitor> temp = queryQuotaMonitorModelData(m); 
		if (temp == null || temp.size() <= 0)
			return returnList;
		QuotaMonitorHistoryExportModel qm = null;
		DecimalFormat df = new DecimalFormat(".00");
		String kpiCode="";
		Map<String, Object> params = new HashMap<String, Object>();
		List<KpiItem> KpiItems = new ArrayList<KpiItem>();
		if(m.get("kpiCode_EQ") != null && !m.get("kpiCode_EQ").equals("")){
			kpiCode = m.get("kpiCode_EQ").toString();
			params.put("kpiCode_EQ", kpiCode);
			KpiItems = findKpiItems(params);
		}
		for (QuotaMonitor ms : temp) {
			qm = new QuotaMonitorHistoryExportModel();
			qm.setDhssName(ms.getDhssName());
			qm.setNeName(ms.getNeName());
			qm.setNeId(Long.valueOf(ms.getNeId().toString()));
			qm.setNeType(ms.getNeType());
			qm.setUnitName(ms.getUnitName());
			qm.setUnitId(ms.getUnitId());
			qm.setNodeName(ms.getNodeName());
			qm.setUnitNext(ms.getUnitNext());
			qm.setUnitNextId(ms.getUnitNextId());
			qm.setKpiName(ms.getKpiName());
			qm.setPeriodStartTime(ms.getPeriodStartTime());
			if(kpiCode.equals("")){
				params.put("kpiCode_EQ", ms.getKpiCode());
				KpiItems = findKpiItems(params);
			}
			String field = KpiItems.get(0).getOutPutField();
			if(field.equals("success_rate")){
				qm.setKpiUnit("%");
				Double dou = Double.parseDouble(ms.getKpiSuccessCount().toString())/Double.parseDouble(ms.getKpiRequestCount().toString());
				qm.setKpiValue( dou * 100.0 );
			}else{
				qm.setKpiUnit("次");
				if(field.equals("fail_count"))
					qm.setKpiValue( Double.valueOf(ms.getKpiRequestCount() - ms.getKpiSuccessCount())  );
				if(field.equals("total_count"))
					qm.setKpiValue( Double.valueOf(ms.getKpiRequestCount() ) );
			}
			if (ms.getKpiRequestCount() == 0) {
				qm.setKpiValue(0.0);
			}
			returnList.add(qm);
		}
		return returnList;
	}
	@Override
	public List<QuotaMonitorExport> findPageByCriteriaExport(Map<String, Object> m) {
		List<QuotaMonitorExport> returnList = new ArrayList<QuotaMonitorExport>();
		List<QuotaMonitor> temp = queryQuotaMonitorModelData(m); 
		if (temp == null || temp.size() <= 0)
			return returnList;
		QuotaMonitorExport qm = null;
		DecimalFormat df = new DecimalFormat(".00");
		String kpiCode="";
		Map<String, Object> params = new HashMap<String, Object>();
		List<KpiItem> KpiItems = new ArrayList<KpiItem>();
		if(m.get("kpiCode_EQ") != null && !m.get("kpiCode_EQ").equals("")){
			kpiCode = m.get("kpiCode_EQ").toString();
			params.put("kpiCode_EQ", kpiCode);
			KpiItems = findKpiItems(params);
		}
		for (QuotaMonitor ms : temp) {
			qm = new QuotaMonitorExport();
			qm.setNthlr_id(Integer.valueOf(ms.getNeId().toString()));
			qm.setNthlr_name(ms.getNeName());
			/*qm.setNode(ms.getNode());*/
			qm.setNetype(ms.getNeType());
			qm.setKpiname(ms.getKpiName());
			qm.setPeriod(ms.getPeriodStartTime().toString());
			if(kpiCode.equals("")){
				params.put("kpiCode_EQ", ms.getKpiCode());
				KpiItems = findKpiItems(params);
			}
			String field = KpiItems.get(0).getOutPutField();
			if(field.equals("success_rate")){
				qm.setKipUnit("%");
				Double dou = Double.parseDouble(ms.getKpiSuccessCount().toString())/Double.parseDouble(ms.getKpiRequestCount().toString());
				qm.setKpivalue( dou * 100 + "");
			}else{
				qm.setKipUnit("次");
				if(field.equals("fail_count"))
					qm.setKpivalue( String.valueOf(ms.getKpiRequestCount() - ms.getKpiSuccessCount() ) );
				if(field.equals("total_count"))
					qm.setKpivalue( String.valueOf(ms.getKpiRequestCount() ) );
			}
			if (ms.getKpiRequestCount() == 0) {
				qm.setKpivalue("0");
			}
			returnList.add(qm);
		}
		return returnList;
	}
	
	public List<KpiItem> findKpiItems(Map<String, Object> paramMap){
    	Map<String, SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<KpiItem> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				KpiItem.class);
		return kpiItemRepository.findAll(spec);
    }

	@Override
	public List<Map<String, Object>> getQuotaMonitorDataList(Map<String, Object> m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuotaMonitor> findPageByCriteria(Map<String, Object> m) {
		// TODO Auto-generated method stub
		return null;
	}

}
