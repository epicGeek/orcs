package com.nokia.ices.apps.fusion.node.isolation.service.impl;


import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.emergency.domian.EmergencySecurityState;
import com.nokia.ices.apps.fusion.emergency.domian.StepConfTable;
import com.nokia.ices.apps.fusion.emergency.domian.StepExecute;
import com.nokia.ices.apps.fusion.emergency.repository.EmergencySecurityStateRepository;
import com.nokia.ices.apps.fusion.emergency.repository.StepConfTableRepository;
import com.nokia.ices.apps.fusion.emergency.repository.StepExecuteRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.node.isolation.service.EmergencySecurityService;


@Service("emergencySecurityService")
public class EmergencySecurityServiceImpl implements EmergencySecurityService {
	
	@Autowired
	private EmergencySecurityStateRepository emergencySecurityStateRepository;
	
	@Autowired
	private StepExecuteRepository stepExecuteRepository;
	
	@Autowired
	private StepConfTableRepository stepConfTableRepository;
	
	@Autowired
	private EquipmentUnitRepository equipmentUnitRepository;
	
	@Override
	public Page<EmergencySecurityState> querySecurityState(
			Map<String, Object> params,Pageable pageable) {  
		
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<EmergencySecurityState> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EmergencySecurityState.class);
		return emergencySecurityStateRepository.findAll(spec, pageable);
	}
	
	@Override
	public List<EmergencySecurityState> querySecurityStateList(
			Map<String, Object> params) {  
		
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<EmergencySecurityState> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EmergencySecurityState.class);
		return emergencySecurityStateRepository.findAll(spec);
	}
	
	@Autowired
//    @Qualifier("jdbcTemplate2nd")
    JdbcTemplate jdbcTemplate;

	@Override
	public List<String> queryLocation() {
//		String sql="select DISTINCT location as NAME from ne order by name ";
		String sql="SELECT  ne_type_name as name FROM ne_type";
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql, String.class);
	}

	@Override
	public List<Map<String, Object>> queryNeType(String location) {
		String sql="		SELECT DISTINCT t.id ,t.ne_type_name as NAME FROM ne n, ne_type t "
		+" WHERE n.ne_type=t.id AND n.location='"+location+"'";
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> queryUnit(Map<String, Object> params) {
		String sql="		SELECT u.id, u.unit FROM unit u LEFT JOIN ne n ON n.id= u.ne "
		+" WHERE	u.ne_type ="+params.get("ne_type").toString()+" and n.location ="
				+params.get("location").toString();
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<StepConfTable> getStepByType(Map<String, Object> params) {
		
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<StepConfTable> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, StepConfTable.class);
		return stepConfTableRepository.findAll(spec);
		/*String sql="		SELECT s.id,s.step_seq,s.step_explain,s.step_describe,step_command "
		+" FROM step_conf_table s "
		+" LEFT JOIN ne_type t ON t.id = s.ne_type "
		+" WHERE "
		+" t.id="+params.get("ne_type").toString()+" AND s.conf_type = "+params.get("conf_type").toString()+" order by s.step_seq ";
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql);*/
	}



	@Override
	public void saveStepExecute(Map<String, Object> params) {
		String sql=" INSERT INTO "
		+" step_execute(emergency_security_state,step_conf_table,execute_state) "
		+" VALUES("+params.get("emergency_security_state").toString()+","+params.get("step_conf_table").toString()+","+params.get("execute_state").toString()+")";
		System.out.println(sql);
		jdbcTemplate.execute(sql);
	}

	@Override
	public List<StepExecute> queryEmergencySecurity(
			Map<String, Object> params) {
		
		   
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<StepExecute> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, StepExecute.class);
		return stepExecuteRepository.findAll(spec);
		
		/*
		StringBuffer sql=new StringBuffer("	SELECT * FROM step_execute s "
		+" LEFT JOIN step_conf_table c ON s.step_conf_table = c. id "
		+" LEFT JOIN emergency_security_state e ON s.emergency_security_state = e.id ");
		StringBuffer sb_condition = new StringBuffer("WHERE 1=1 ");
		
		if(params.get("conf_type")!=null && !params.get("conf_type").equals("")){
			sb_condition.append("  AND c.conf_type ="+params.get("conf_type").toString());
		}
		if(params.get("ne_type")!=null && !params.get("ne_type").equals("")){
			sb_condition.append("  AND c.ne_type ="+params.get("ne_type").toString());
		}
		if(params.get("unit_id")!=null && !params.get("unit_id").equals("")){
			sb_condition.append("  AND e.unit_id ="+params.get("unit_id").toString());
		}
		if(params.get("ne_id")!=null && !params.get("ne_id").equals("")){
			sb_condition.append("  AND e.ne_id ="+params.get("ne_id").toString());
		}

		System.out.println(sql.append(sb_condition).toString());
		
		return jdbcTemplate.queryForList(sql.toString());*/
	}
	
	
	@Override
	public void saveEmergencySecurity(Map<String, Object> params) {
		StringBuffer sql=new StringBuffer(" INSERT INTO emergency_security_state "
		+" (operate_date,unit_id,operate_state,operator,ne_id,operate,ne_name,unit_name) "    
+" VALUES('"+params.get("operate_date").toString()+"',"+params.get("unit_id").toString()+","+params.get("operate_state").toString()+","
+ "'"+params.get("operator").toString()+"',"+params.get("ne_id").toString()+","+params.get("operate").toString()+",'"+params.get("ne_name")+"','"+params.get("unit_name")+"')");
		System.out.println(sql.toString());
		jdbcTemplate.execute(sql.toString());

	}

	@Override
	public EmergencySecurityState getEmergencySecurityByUnit(Map<String, Object> params) {
		
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<EmergencySecurityState> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EmergencySecurityState.class);
		List<EmergencySecurityState> list = emergencySecurityStateRepository.findAll(spec);
		return list.size() == 0 ? null : list.get(0);
		
		
		/*StringBuffer sql=new StringBuffer("	SELECT s.id FROM emergency_security_state s ");
		StringBuffer sb_condition = new StringBuffer("WHERE 1=1 ");
		
		if(params.get("unit_id")!=null && !params.get("unit_id").equals("")){
			sb_condition.append(" AND s.unit_id = "+params.get("unit_id").toString()+" AND operate=2 ");
		}
		if(params.get("ne_id")!=null && !params.get("ne_id").equals("")){
			sb_condition.append("  AND s.ne_id ="+params.get("ne_id").toString()+" AND operate=1 ");
		}
	
		System.out.println(sql.append(sb_condition).toString());
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql.toString());
		if(list.size()>0){
			return list.get(0).get("id").toString();
		}else{
			return "-1";
		}*/
	}

	

	@Override
	public int getSecurityStateTotal(
			Map<String, Object> params) {
		

		
		StringBuffer sql=new StringBuffer("	SELECT count(*) FROM emergency_security_state e "
		+" LEFT JOIN ne n ON n.id = e.ne_id ");
		StringBuffer sb_condition = new StringBuffer(" where e.operate_state <> 0 ");
		if(params.get("operate_state")!=null && !params.get("operate_state").equals("")){
			sb_condition.append(" AND e.operate_state ="+params.get("operate_state").toString());
		}
		if(params.get("neState")!=null && !params.get("neState").equals("")){
			sb_condition.append("  AND e.operate_state ="+params.get("neState").toString());
		}
		if(params.get("startdatetime")!=null && !params.get("startdatetime").equals("")){
			sb_condition.append("  AND e.operate_date >= '"+params.get("startdatetime").toString()+"'");
		}
		if(params.get("enddatetime")!=null && !params.get("enddatetime").equals("")){
			sb_condition.append(" AND e.operate_date <= '"+params.get("enddatetime").toString()+"'");
		}
		if(params.get("rows")!=null && !params.get("rows").equals("")){
			sb_condition.append("	limit "+params.get("rows")+" offset "+params.get("page")+" ");
		}

		System.out.println(sql.append(sb_condition).toString());
		
		return jdbcTemplate.queryForInt(sql.toString());
	}

	@Override
	public void updateStepExecute(Map<String, Object> params) {
		
		StringBuffer sql=new StringBuffer(" UPDATE step_execute "); 
		StringBuffer sb_condition = new StringBuffer(" set ");
		if(params.get("execute_state")!=null && !params.get("execute_state").equals("")){
			sb_condition.append(" execute_state ="+params.get("execute_state").toString());
		}
		sb_condition.append(" WHERE step_conf_table IN(SELECT c.id FROM step_conf_table c WHERE 1=1 ");
		if(params.get("stepId")!=null && !params.get("stepId").equals("")){
			sb_condition.append(" AND c.step_seq ='"+params.get("stepId").toString()+"'");
		}
		sb_condition.append(" AND c.conf_type="+params.get("conf_type")+") "
		+" AND emergency_security_state IN(SELECT e.id FROM emergency_security_state "
		+" e WHERE e.unit_id="+params.get("unit")+") ");
		System.out.println(sql.append(sb_condition).toString());
//		MysqlDB.updateData(sql.toString());
		jdbcTemplate.execute(sql.toString());
		
	}

	@Override
	public void updateEmergencySecurity(Map<String, Object> params) {
		
		String sql=" UPDATE "
		+" emergency_security_state SET "
		+" operate_state = "+params.get("operate_state")+",operate_date = '"+params.get("operate_date")+"',operator = "
		+" "+params.get("operator")+" WHERE unit_id = "+params.get("unit_id")+" ";
		System.out.println(sql);
		jdbcTemplate.execute(sql);
	}

	@Override
	public void updateReturnInfo(Map<String, Object> params) {
		
		String sql=" UPDATE "
		+" emergency_security_state SET return_info = "+params.get("return_info")+" WHERE "
		+" unit_id = "+params.get("unit");
		System.out.println(sql);
		jdbcTemplate.execute(sql);
		
	}

	@Override
	public String getOperateStateByUnit(Map<String, Object> params) {
		String sql="		SELECT operate_state FROM emergency_security_state WHERE "
		+" operate=2 AND unit_id="+params.get("unit_id");
		System.out.println(sql);
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			return list.get(0).get("operate_state").toString();
		}else{
			return "";
		}
	}

	@Override
	public List<Map<String, Object>> queryNe(String location) {
		
//		String sql=" SELECT n.id,n.ne as NAME,n.ne_type FROM ne n WHERE n.location= 
		String sql="SELECT n.id,n.ne as NAME,n.ne_type FROM ne n left join ne_type t on n.ne_type=t.id where t.ne_type_name='"+location+"'";
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> queryUnitByNe(Map<String, Object> params) {
		
		String sql="SELECT u.id, u.unit FROM unit u WHERE u.ne ="+params.get("neId");
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql);	
	}

	@Override
	public int getNeTypeByUnit(int unitId) {
		
		
		String sql=" select ne_type from unit where id= "+unitId;
		System.out.println(sql);
		return jdbcTemplate.queryForInt(sql);	
	}

	@Override
	public String getOperateStateByNe(Map<String, Object> params) {
		String sql=" SELECT operate_state FROM emergency_security_state WHERE operate=1 AND "
		+" ne_id= "+params.get("neId");
		System.out.println(sql);
		
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			return list.get(0).get("operate_state").toString();
		}else{
			return "";
		}
	}

	@Override
	public int getNeTypeByNeId(int neId) {
		String sql="select ne_type from ne where id= "+neId;
		System.out.println(sql);
		return jdbcTemplate.queryForInt(sql);	
	}

	@Override
	public List<Map<String, Object>> getAllNe() {
		
		
		String sql="SELECT id,ne FROM ne";
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql);	
	}

	@Override
	public String getNeNameById(int neId) {
		
		String sql="SELECT ne FROM ne WHERE id="+neId;
		System.out.println(sql);
		
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			return list.get(0).get("ne").toString();
		}else{
			return "";
		}
	}

	@Override
	public String getUnitNameById(int unitId) {
		String sql="select unit from unit where id="+unitId ;
		System.out.println(sql);
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			return list.get(0).get("unit").toString();
		}else{
			return "";
		}
	}


	@Override
	public String getNeTypeNameByNeId(int neId) {
		String sql="select type.ne_type_name as name from ne_type as type left join ne as ne "
				+ "on ne.ne_type = type.id where ne.id="+neId;
		System.out.println(sql);
		
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			return list.get(0).get("name").toString();
		}else{
			return "";
		}
	}



	@Override
	public List<EquipmentUnit> queryAccount(Map<String, Object> params) {
		
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<EquipmentUnit> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EquipmentUnit.class);
		return equipmentUnitRepository.findAll(spec);
		/*String sql=" select acc.username as username,acc.password as password from unit as u "
		+" left join unit_account as a on a.unit = u.id "
		+" left join account as acc on acc.id= a.account "
		+" where u.unit='"+params.get("unit_name").toString()+"'";
		System.out.println(sql);
//		return MysqlDB.queryForList(sql);
		return jdbcTemplate.queryForList(sql);*/
	}

}
