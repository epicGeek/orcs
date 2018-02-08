package com.nokia.ices.apps.fusion.quota.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.kpi.domain.KpiItem;
import com.nokia.ices.apps.fusion.kpi.repository.KpiItemRepository;
import com.nokia.ices.apps.fusion.quota.dao.AllQuotaDao;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitor;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitorHistory;
import com.nokia.ices.apps.fusion.quota.model.AllQuota;
import com.nokia.ices.apps.fusion.quota.model.QuotaMonitorModel;
import com.nokia.ices.apps.fusion.quota.repository.QuotaMonitorHistoryRepository;
import com.nokia.ices.apps.fusion.quota.repository.QuotaMonitorRepository;
import com.nokia.ices.apps.fusion.quota.service.AllQuotaService;
@Service("allQuotaService")
public class AllQuotaServiceImpl implements AllQuotaService {
	@Autowired
	AllQuotaDao allQuotaDao;
	
	@Autowired
	private QuotaMonitorHistoryRepository quotaMonitorHistoryRepository;
	
	@Autowired
	private QuotaMonitorRepository quotaMonitorRepository;
	
	@Autowired
	private KpiItemRepository kpiItemRepository;

	public AllQuotaDao getAllQuotaDao() {
		return allQuotaDao;
	}
	public void setAllQuotaDao(AllQuotaDao allQuotaDao) {
		this.allQuotaDao = allQuotaDao;
	}
	
	/*public List<Map<String, Object>> queryAllBscName(Map<String, Object> m){
		return allQuotaDao.queryAllBscName(m);
	}*/
	@Override
	public List<QuotaMonitorHistory> queryAllQuotaData(Map<String, Object> paramMap,Pageable pageable) {
		
		Sort s=new Sort(Direction.DESC, "periodStartTime");
		Map<String,SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<QuotaMonitorHistory> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, QuotaMonitorHistory.class);
		return quotaMonitorHistoryRepository.findAll(spec,s);
	}
	//新建查询方法	
	@Override
	public List<QuotaMonitor> queryQuotaMonitorModelData(Map<String, Object> paramMap) {
		Map<String,SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<QuotaMonitor> specM = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, QuotaMonitor.class);
		return quotaMonitorRepository.findAll(specM);}
	
	
		/*List<Map<String, Object>>  returnm=new ArrayList<Map<String,Object>>();
		Map<String,Object> returnObj=new HashMap<String,Object>();
		
		List<Map<String, Object>> temp=allQuotaDao.queryAllQuotaData(m);
		if(temp==null ||temp.size()<=0)
			return returnm;
		List<String> xdateList =new ArrayList<String>();//X轴的时间标题显示
//		System.out.println("有多少数据"+temp.size());
		Map<String,Double> successRate =new HashMap<String,Double>();//成功率,kpivalue值,  key=period
		Map<String,Double> successCount =new HashMap<String,Double>();//失败次数,kpifailcount值 key=period
		
		String xdate="";
		for(Map<String,Object> ob:temp){
//			xdate=ob.get("period").toString().trim().split(" ")[1];
//			xdate=xdate.split(":")[0]+":"+xdate.split(":")[1];
			if(!xdateList.contains(xdate)){
//				xdateList.add(xdate);
				xdateList.add(ob.get("period").toString().trim());
			}
			successRate.put(ob.get("period").toString().trim(), Double.parseDouble(ob.get("kpivalue").toString().trim())*100);
			successCount.put(ob.get("period").toString().trim(), Double.parseDouble(ob.get("kpifailcount").toString().trim()));
//			System.out.println(xdate+"=="+Double.parseDouble(ob.get("kpivalue").toString().trim())+"=="+Double.parseDouble(ob.get("kpifailcount").toString().trim()));
		}
		
		Collections.sort(xdateList);//自然升序排序
		String datitle="";
		for(String s:xdateList){
			datitle+=s+",";
		}
		System.out.println(datitle);
		returnObj.put("datitle", datitle.substring(0,datitle.length()-1));
		returnObj.put("successRate", successRate);
		returnObj.put("successCount", successCount);
		
		returnm.add(returnObj);//全计算完后返回
		return returnm;*/
	
	@Override
	public List<Map<String, Object>> queryQuotaKpiData(Map<String, Object> m) {
		// TODO Auto-generated method stub
		return allQuotaDao.queryQuotaKpiData(m);
	}
	@Override
	public List<QuotaMonitorModel> findPageByCriteriaAllQuota(Map<String, Object> m) {
		List<QuotaMonitorModel> returnList=new ArrayList<QuotaMonitorModel>();
		List<QuotaMonitor> temp=queryQuotaMonitorModelData(m);//新建查询方法
		if(temp==null ||temp.size()<=0)
			return returnList;
		QuotaMonitorModel qm=null;//新建实体
		for(QuotaMonitor qmt:temp){
			qm=new QuotaMonitorModel();
			/*qm.setNeId(qmt.getNthlrId());
			qm.setNeName(qmt.getNthlrName());
			//qm.setNthlrfe_id(Integer.parseInt(ms.get("nthlrfe_id").toString().trim()));
//			qm.setNeName(ms.get("NeName").toString().trim());
			qm.setUnitNode(qmt.getNode());
			qm.setNeType(qmt.getNetype());
			qm.setKpiName(qmt.getKpiname());
			DecimalFormat df = new DecimalFormat("0.00");
//			qm.setKpiName(idtostr(ms.get("KpiName").toString().trim()));
//			qm.setKpiFailCount(qmt.getKpifailcount());
			qm.setKpiRequestCount(qmt.getKpirequestcount());
		    qm.setPeriod(qmt.getPeriod());
//			qm.setPerperiod(getEarlyNPeriodsII(ms.get("period").toString().trim(),1440));
			qm.setScene(qmt.getScene());
			kpiList();
			if(Obj.contains(qmt.getKpiname())){
				qm.setKpiUnit("%");
				double dou = qmt.getKpirequestcount()/qmt.getKpirequestcount();
				qm.setKpiValue((1.0 - dou)*100 );
			}else{
				qm.setKpiUnit("次");
				qm.setKpiValue(qmt.getKpirequestcount() - qmt.getKpirequestcount());
			}*/
			returnList.add(qm);
		}
		return returnList;
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
	List<String> Obj = new ArrayList<String>();
	@Override
	public List<AllQuota> findPageByCriteriaAllQuotaExport(Map<String, Object> m) {
		List<AllQuota> array = new ArrayList<AllQuota>();
		List<QuotaMonitorHistory> list = queryAllQuotaData(m,null);
		if(list.size()<=0)
			return array;
		String kpiCode="";
		Map<String, Object> params = new HashMap<String, Object>();
		List<KpiItem> KpiItems = new ArrayList<KpiItem>();
		if(m.get("kpiCode_EQ") != null && !m.get("kpiCode_EQ").equals("")){
			kpiCode = m.get("kpiCode_EQ").toString();
			params.put("kpiCode_EQ", kpiCode);
			KpiItems = findKpiItems(params);
		}
		DecimalFormat df = new DecimalFormat("#.00");
		for (QuotaMonitorHistory ms : list) {
			AllQuota qm=new AllQuota();
			qm.setUnitName(ms.getUnitName());
			qm.setNeName(ms.getNeName());
			qm.setKpiname(ms.getKpiName());
			qm.setKpifailcount(Math.round(ms.getKpiRequestCount() - ms.getKpiSuccessCount())+"");
			qm.setKpirequestcount(String.valueOf(Math.round(ms.getKpiRequestCount() == null ? 0 : ms.getKpiRequestCount())));
			qm.setPeriod(ms.getPeriodStartTime().toString());
			if(kpiCode.equals("")){
				params.put("kpiCode_EQ", ms.getKpiCode());
				KpiItems = findKpiItems(params);
			}
			String field = KpiItems.get(0).getOutPutField();
			if(field.equals("success_rate")){
				qm.setKipUnit("%");
				if(ms.getKpiRequestCount()!=0){
					Double dou = Double.parseDouble(ms.getKpiSuccessCount().toString())/Double.parseDouble(ms.getKpiRequestCount().toString());
					qm.setKpivalue(df.format( dou * 100) + "");
				}else{
					qm.setKpivalue("0");
				}
			}else{
				qm.setKipUnit("次");
				if(field.equals("fail_count"))
					qm.setKpivalue( String.valueOf(ms.getKpiRequestCount() - ms.getKpiSuccessCount() ) );
				if(field.equals("total_count"))
					qm.setKpivalue( String.valueOf(ms.getKpiRequestCount() ) );
			}
			array.add(qm);
		}
		return array;
	}
	
	public List<KpiItem> findKpiItems(Map<String, Object> paramMap){
    	Map<String, SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<KpiItem> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				KpiItem.class);
		return kpiItemRepository.findAll(spec);
    }
	
	
	public void kpiList(){
		Obj.add("kpi001");
		Obj.add("kpi002");
		Obj.add("kpi003");
		Obj.add("kpi004");
		Obj.add("kpi005");
		Obj.add("kpi006");
		Obj.add("kpi007");
		Obj.add("kpi010");
	}
}
