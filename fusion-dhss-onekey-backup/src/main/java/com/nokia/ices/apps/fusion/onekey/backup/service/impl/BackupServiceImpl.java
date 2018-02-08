package com.nokia.ices.apps.fusion.onekey.backup.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.emergency.domian.StepConfTable;
import com.nokia.ices.apps.fusion.emergency.repository.StepConfTableRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.monitor.domain.MonitorTable;
import com.nokia.ices.apps.fusion.onekey.backup.mbmodel.BackupCommandInfo;
import com.nokia.ices.apps.fusion.onekey.backup.mbmodel.BackupRSInfo;
import com.nokia.ices.apps.fusion.onekey.backup.service.BackupService;
import com.nokia.ices.apps.fusion.onekey.backup.utils.NetCommandUtils;
import com.nokia.ices.apps.fusion.onekey.domian.OnekeyBackup;
import com.nokia.ices.apps.fusion.onekey.domian.OnekeyBackupMinitor;
import com.nokia.ices.apps.fusion.onekey.repository.OnekeyBackupMinitorRepository;
import com.nokia.ices.apps.fusion.onekey.repository.OnekeyBackupRepository;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;

/**
 * 一键备份功能Service定义类
 * 1.数据增加方法定义
 * 2.数据查询接口定义
 * 3.执行备份功能
 * User: stev.zhang
 * Date: 2015/5/24
 * Time: 16:57
 */
@Service("backupService")
public class BackupServiceImpl implements BackupService{
	
	@Autowired
//    @Qualifier("jdbcTemplate2nd")
    JdbcTemplate jdbcTemplate;
	
//	public static final String APP_MODULE = "一键备份";
	SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	
	@Autowired
	EquipmentNeRepository equipmentNeRepository;
	
	@Autowired
	EquipmentUnitRepository equipmentUnitRepository;
	
	@Autowired
	StepConfTableRepository stepConfTableRepository;
	
	@Autowired
	OnekeyBackupMinitorRepository onekeyBackupMinitorRepository;
	
	@Autowired
	OnekeyBackupRepository onekeyBackupRepository;
    /****
     * 根据条件查询一键备份的历史数据
     * @param params 查询使用的条件
     * @return List<BackupHistory> 查询到的数据结果集
     * ***/
	public ShiroUser shiroUser=null;
    @Override
    public List<Map<String, Object>> searchBackupHistory(Map<String, Object> params) {
        List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
        try {
		StringBuffer sql=new StringBuffer("SELECT id,"
          +"  bk_site_id as bkSiteId,"
          +"  bk_site_name as bkSiteName,"
          +"  bk_site_type_name as bkSiteTypeId,"
          +"  bk_site_type_name as bkSiteTypeName,"
          +"  bk_node_id as bkNodeId,"
          +"  bk_node_name as bkNodeName,"
          +"  bk_date as bkDate,"
          +"  bk_add_date as bkAddDate,"
          +"  bk_add_who as bkAddWho,"
          +"  bk_modify_date as bkModifyDate,"
          +"  bk_modify_who as bkModifyWho,"
          +"  bk_file_uuid_name as bkFileNameUuid,"
          +"  bk_file_display_name as bkFileDisplayName,"
          +"  bk_file_down_folder as bkFileDownFolder"
          +"  FROM onekey_backup");
		StringBuffer sb_condition = new StringBuffer(" where 1=1 ");
		if(params.get("pSiteId")!=null && !params.get("pSiteId").equals("")){
			sb_condition.append(" AND  bk_site_id= '"+params.get("pSiteId").toString()+"'");
		}
		if(params.get("pTypeId")!=null && !params.get("pTypeId").equals("")){
			sb_condition.append("  AND  bk_site_type_name= '"+params.get("pTypeId").toString()+"'");
		}
		if(params.get("pNodeName")!=null && !params.get("pNodeName").equals("")){
			sb_condition.append(" AND  bk_node_name LIKE '%"+params.get("pNodeName").toString()+"%'");
		}
		sb_condition.append(" ORDER BY bk_date desc  limit "+params.get("limit").toString()+" offset  "+params.get("start").toString());
		System.out.println(sql.append(sb_condition).toString());
//		result= MysqlDB.queryForEntityList(BackupHistory.class,sql.toString());
		result=jdbcTemplate.queryForList(sql.toString());
        } catch (Exception e) {
           // logger.error("查询一键备份历史数据失败!",e);
            result=null;
        }
        return result;
    }
   
    @Override
    public Page<OnekeyBackup> searchBackupHistory(Map<String, Object> params,Pageable pageable) {
    	Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<OnekeyBackup> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, OnekeyBackup.class);
		return onekeyBackupRepository.findAll(spec, pageable);
       /* int countResult=-1;
        try {
			StringBuffer sql=new StringBuffer(" SELECT count(*) as totalCount FROM onekey_backup ");
			StringBuffer sb_condition = new StringBuffer(" where 1=1 ");
			if(params.get("pSiteId")!=null && !params.get("pSiteId").equals("")){
				sb_condition.append(" AND  bk_site_id= '"+params.get("pSiteId").toString()+"'");
			}
			if(params.get("pTypeId")!=null && !params.get("pTypeId").equals("")){
				sb_condition.append("  AND  bk_site_type_name= '"+params.get("pTypeId").toString()+"'");
			}
			if(params.get("pNodeName")!=null && !params.get("pNodeName").equals("")){
				sb_condition.append(" AND  bk_node_name LIKE '%"+params.get("pNodeName").toString()+"%'");
			}
			System.out.println(sql.append(sb_condition).toString());
//			countResult=MysqlDB.queryForInt(sql.toString());
			countResult=jdbcTemplate.queryForInt(sql.toString());
        } catch (Exception e) {
          //  logger.error("查询一键备份历史数据总数失败!",e);
        }
        return countResult;*/
    }

    /*****
     *  根据参数查询备份服务器的信息，包含:
     *  1.服务器IP,端口,登录服务器用的用户名,密码 ,连接超时时间
     *  2.连接服务器后需要执行的命令
     *  @param params  查询备份服务器和命令信息使用的参数
     *  @return  BackupRSInfo 查询到的需要执行备份的服务器和指令信息
     * ****/
    @SuppressWarnings("unused")
	public BackupRSInfo searchBackupRSInfoParams(Map<String,Object> params){
        BackupRSInfo serverInfo=null;
	StringBuffer sql=new StringBuffer("select "
        +" net.location, "
        +" net.ne, "
        +" net.parent, "
        +" unt.ip as host, "
        +" unt.port as hostPort, "
        +" act.username as userName , "
        +" act.root_password, "
        +" act.password, "
        +" unt.protocol, "
        +" pt.name as protocolType, "
        +" act.priority, "
        +" unt.unit, "
        +" unt.ne, "
        +" unt.ne_type, "
        +" unt.unit_type, "
        +" utt.unit_type_name, "
        +" unt.switch_state");
	StringBuffer sb_condition = new StringBuffer(" where 1=1 ");
	sb_condition.append(" and net.id=unt.ne "
      +" and net.ne_type=unt.ne_type "
      +" and pt.id=unt.protocol "
      +" and utt.id=unt.unit_type "
      +" and uat.unit=unt.id "
      +" and uat.account=act.id "
      +" and net.location= "+params.get("siteId").toString()
      +" and net.ne= "+params.get("siteTypeId").toString()
      +" and net.parent="+params.get("nodeId").toString());
	System.out.println(sql.append(sb_condition).toString());
	/*serverInfo MysqlDB.queryForEntity(BackupRSInfo.class,sql.toString())*/;  
        
        try {
            if(serverInfo!=null){
     		StringBuffer sqlc=new StringBuffer(" SELECT id, "
                +" siteid, "
                +" sitetypeid, "
                +" nodeid, "
                +" command, "
                +" sshcommandtype as sshCommandType, "
                +" cmdconnectiontimeout as connectTimeOut, "
                +" commandorder, "
                +" resultbackupfolder as resultBackupFolder, "
                +" resultfilename as resultFileName, "
                +" resultfiledisplayname as resultFileDisplayName, "
                +" resultfiletype as resultFileType, "
                +" remotefolder remoteFolder, "
                +" remotefilename as remoteFileName, "
                +" addwho, "
                +" add_date, "
                +" modifywho, "
                +" modify_date "
                +" FROM onekey_backup_commands ");
    		StringBuffer sb_conditionc = new StringBuffer(" where 1=1 ");
    		sb_condition.append(" and siteid= "+params.get("siteId").toString()
             +" and sitetypeid= "+params.get("siteTypeId").toString()
             +" and nodeid= "+params.get("nodeId").toString());
    		System.out.println(sqlc.append(sb_conditionc).toString());
            	
                List<BackupCommandInfo> commandInfos=new ArrayList<BackupCommandInfo>();
                if(commandInfos==null || commandInfos.size()==0){
                    logger.error("未找到任何待执行指令信息!");
                    serverInfo=null;
                }else{
                    serverInfo.setBackupCommandInfoList(commandInfos);
                }
            }else{
               logger.warn("获取远程服务器信息失败!");
            }
        } catch (Exception e) {
            logger.error("获取远程服务器的信息错误!",e);
        } finally {
        }
       /* serverInfo.setHost("10.219.100.101");
        serverInfo.setHostPort(22);
        serverInfo.setUserName("root");
        serverInfo.setPassword("password");
        serverInfo.setProtocolType("ssh");
        List<BackupCommandInfo> backupCommandInfos=new ArrayList<BackupCommandInfo>();
        BackupCommandInfo commandInfo=new BackupCommandInfo();
        commandInfo.setCommand("cd / ; ");
        commandInfo.setSshCommandType("exec");
        commandInfo.setResultBackupFolder("E:\\lib\\telnetssh");
        commandInfo.setResultFileType(".txt");

        BackupCommandInfo commandInfo1=new BackupCommandInfo();
        commandInfo1.setCommand("get");
        commandInfo1.setSshCommandType("sftp");
        commandInfo1.setResultBackupFolder("E:\\lib\\telnetssh");
        commandInfo1.setRemoteFolder("/");
        commandInfo1.setRemoteFileName("aa.txt");

        backupCommandInfos.add(commandInfo);
        backupCommandInfos.add(commandInfo1);
        serverInfo.setBackupCommandInfoList(backupCommandInfos);*/

        return serverInfo;
    }

    /******
     * 根据传入的需要备份的服务器信息以及需要执行的指令信息,
     * 连接远程服务器,执行指令集合,并记录执行结果
     * 执行步骤：
     * 1.根据参数信息中定义的协议类型，调用NetCommandUtils备份方法
     * 2.调用 NetCommandUtils的备份方法执行备份
     * @param param
     * @return BackupRSInfo 执行后的结果，记录执行过程日志信息和结果信息
     * ******/
    public BackupRSInfo executeBackupCommands(BackupRSInfo param){
        BackupRSInfo result=null;
        if(param!=null){
            NetCommandUtils netCommandUtils=new NetCommandUtils();
            String protocol= StringUtils.stripToNull(param.getProtocolType());
            try {
                if(StringUtils.equalsIgnoreCase("telnet",protocol)){
                    netCommandUtils.executeTelentCommands(param);
                }else{        //如果协议不为"telent",其他的都使用ssh
                    netCommandUtils.executeSSHCommands(param);
                }
                result=param;
            } catch (Exception e) {
                logger.error("执行备份操作错误!",e);
            }
        }
        return result;
    }
    /***
     * 在执行完备份功能后，生成备份的历史数据
     * 记录关联的文件名为最后一条指令执行的结果
     * @param params 页面上传入的查询参数
     * @param backupRSInfo  保存指令执行的结果参数
     * @return int 保存影响的记录行数
     * *****/
    public int  saveBackupDataAsHistory(Map<String,Object> params,BackupRSInfo backupRSInfo){
        int result=0;
        int backupCommandSize=backupRSInfo.getBackupCommandInfoList().size();
        if(backupCommandSize>0){
            try {
                String fileName=backupRSInfo.getBackupCommandInfoList().get(backupCommandSize-1).getResultFileName();
                String fileDisplayName=backupRSInfo.getBackupCommandInfoList().get(backupCommandSize-1).getResultFileDisplayName();
                String downloadFolder=backupRSInfo.getBackupCommandInfoList().get(backupCommandSize-1).getResultBackupFolder();
                params.put("fileName",fileName);
                params.put("fileDisplayName",fileDisplayName);
                params.put("downloadFolder",downloadFolder);
        		String sql=" INSERT INTO onekey_backup(bk_site_id,bk_site_name,bk_site_type_name,"
        				+ "bk_node_name,bk_add_who,bk_date,bk_file_display_name,"
        				+ "bk_file_down_folder,bk_file_uuid_name) "
               +"  VALUES ('"+params.get("bk_site_name").toString()+"','"+params.get("bk_site_name").toString()+"','"+params.get("bk_site_type_name").toString()+"'  ,"
               + "'"+params.get("bk_node_name").toString()+"','"+params.get("bk_add_who").toString()+"','"+params.get("bk_date").toString()+"',"
        	   + "'"+params.get("fileDisplayName").toString()+"','"+params.get("bk_file_log").toString()+"','"+params.get("fileName").toString()+"')";
        		System.out.println(sql);
//        		MysqlDB.inesrtData(sql);
        		jdbcTemplate.execute(sql);
//                result=backupDao.saveBackupHistoryInfo(params);
            } catch (Exception e) {
               // logger.error(e);
            }
        }
        return result;
    }
    
	@Override
	public List<Map<String, Object>> neTypes(Map<String, Object> params) {
//		StringBuffer sql1=new StringBuffer("select id from  ne_type ");
//		List<Map<String, Object>> typeid = MysqlDB.queryForList(sql1.toString());
//		String ids="";
//		for(Map<String, Object> type:typeid){
//			ids+=type.get("id")+",";
//		}
//		ids=ids.substring(0,ids.length()-1);
		
		StringBuffer sql=new StringBuffer("select distinct t.id as id ,"
				+ " t.unit_type_name as name from unit_type as t left join unit as u "
				+ " on u.unit_type = t.id "
				+ " left join ne as n on n.id = u.ne ");
				StringBuffer sb_condition = new StringBuffer(" where 1=1 ");
//				sb_condition.append(" and n.location='"+params.get("location").toString()+"' and t.id in (1,2,3,4,5,6,7,8,9,10,11,12) ");
//				sb_condition.append(" and n.location='"+params.get("location").toString()+"' and t.id in (2321,2325,2327) ");
				sb_condition.append(" and n.location='"+params.get("location").toString()+"' and t.id in (9,15,17) ");
//				sb_condition.append(" and n.location='"+params.get("location").toString()+"' and u.ne_type in ("+ids+") ");
				System.out.println(sql.append(sb_condition).toString());
//				List<Map<String, Object>> neTypes = MysqlDB.queryForList(sql.toString());
				List<Map<String, Object>> neTypes = jdbcTemplate.queryForList(sql.toString());
				
		return neTypes;
	}

	@Override
	public List<Map<String, Object>> sites() {
		String sql="select  distinct   e.location  as site  from  ne e   order by site ";
		System.out.println(sql);
//		List<Map<String, Object>> sites = MysqlDB.queryForList(sql.toString());
		List<Map<String, Object>> sites = jdbcTemplate.queryForList(sql.toString());
		return sites;
	}

	@Override
	public List<Map<String, Object>> units(Map<String, Object> params) {
		String sql="select u.id as id,u.unit as unit,u.ne_type as ne_type from unit as u left join ne as n on n.id = u.ne "
		+ " where n.location ='"+params.get("site").toString()+"' and unit_type="+params.get("unit_type").toString();
		System.out.println(sql);
//		List<Map<String, Object>> units = MysqlDB.queryForList(sql.toString());
		List<Map<String, Object>> units = jdbcTemplate.queryForList(sql.toString());
		return units;
	}
    public static final Logger logger = LoggerFactory.getLogger(BackupServiceImpl.class);
	@Override
	public List<Map<String, Object>> queryLog(Map<String, Object> params) {
		String sql="select bk_file_down_folder as log from onekey_backup where id = "+params.get("id");
		System.out.println(sql);
//		return MysqlDB.queryForList(sql);
		return jdbcTemplate.queryForList(sql);
	}
	@Override
	public int getNeType(Map<String, Object> params) {
		StringBuffer sql=new StringBuffer("select ne_type from unit ");
		StringBuffer sb_condition = new StringBuffer("where unit_type= "+params.get("unit_type").toString()+
				" and unit='"+params.get("unit").toString()+"'");
		System.out.println(sql.append(sb_condition).toString());
//		return MysqlDB.queryForInt(sql.toString());
		return jdbcTemplate.queryForInt(sql.toString());
	}
	@Override
	public List<StepConfTable> getStepList(Map<String, Object> params) {
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<StepConfTable> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, StepConfTable.class);
		return stepConfTableRepository.findAll(spec);
		/*StringBuffer sql=new StringBuffer(" select step_seq,step_command,step_describe,"
			+ "step_explain from step_conf_table ");
		StringBuffer sb_condition = new StringBuffer(" where ne_type= "+params.get("ne_type").toString()
			+ " and conf_type="+params.get("conf_type").toString()+" order by step_seq");
		System.out.println(sql.append(sb_condition).toString());
//		return MysqlDB.queryForList(sql.toString());
		return jdbcTemplate.queryForList(sql.toString());*/
	}
	@Override
	public List<Map<String, Object>> queryAccount(Map<String, Object> params) {
		String sql=" select acc.username as username,acc.password as password from unit as u "
		+" left join unit_account as a on a.unit = u.id "
		+" left join account as acc on acc.id= a.account "
		+" where u.unit='"+params.get("unit_name").toString()+"'";
		System.out.println(sql);
//		return MysqlDB.queryForList(sql);
		return jdbcTemplate.queryForList(sql);
	}
	@Override
//	@SystemOperationLogAnnotation(appModule = APP_MODULE, opType = OperationType.Other, opText = "一键备份")
	public int saveBackupDataAsHistory(Map<String, Object> params) {
		String sql=" INSERT INTO onekey_backup(bk_site_id,bk_site_name, "
				+ " bk_site_type_name,bk_node_name,bk_add_who,bk_date, "
				+ " bk_file_display_name,bk_file_down_folder,bk_file_uuid_name) "
	+"  VALUES ('"+params.get("bk_site_name").toString()+"','"+params.get("bk_site_name").toString()+"','"+params.get("bk_site_type_name").toString()+"'  ,"
    + "'"+params.get("bk_node_name").toString()+"','"+params.get("bk_add_who").toString()+"','"+params.get("bk_date").toString()+"',"
   	+ "'"+params.get("fileDisplayName").toString()+"','"+params.get("bk_file_log").toString()+"','"+params.get("fileName").toString()+"')";
		System.out.println(sql);
//		MysqlDB.inesrtData(sql);
		jdbcTemplate.execute(sql);
		return 1;
	}
	@Override
	public String getUnitTypeNameById(Map<String, Object> params) {
		
		String sql="select unit_type_name from unit_type where id = "+params.get("id").toString();
		System.out.println(sql.toString());
//		return MysqlDB.queryForString(sql.toString());
		return jdbcTemplate.queryForMap(sql.toString()).get("unit_type_name").toString();
	}
	
	public boolean saveBackupMinitorLog(Map<String, String> params) {
		try {
			StringBuffer nextSeqVal=new StringBuffer("SELECT nextval('backup_monitor_id_seq'::regclass) as retId  ");
			Integer nextSeqValInt =/*MysqlDB.queryForInt(nextSeqVal.toString())*/0;
			System.out.println(nextSeqValInt+"------"+nextSeqVal.toString());
			String command=params.get("command").toString();
			command=command.replace("'", "''");
			String sql=" INSERT INTO onekey_backup_minitor(id,site, "
					+ " unit_type,unit_name,command,start_time, "
					+ " end_time,result_dir,result_code,is_fulish,step_id,system_user,error_msg) "
					+"  VALUES ("+nextSeqValInt+",'"+params.get("site").toString()+"','"+params.get("unit_type").toString()+"','"+params.get("unit_name").toString()+"'  ,"
					+ "'"+command+"','"+sdfd.format(new Date())+"',"+null+","
					+ "'"+params.get("result_dir").toString()+"','"+params.get("result_code").toString()+"','"+params.get("is_fulish").toString()+"','"+params.get("step_id").toString()+"','"+params.get("system_user").toString()+"','"+params.get("error_msg").toString()+"')";
			System.out.println(sql);
//			MysqlDB.inesrtData(sql);
			jdbcTemplate.execute(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean delBackupMinitor() {
		try {
			
			/*String exeNoSql="SELECT count(*) from onekey_backup_minitor where  executing =0 and system_user='"+this.getShiroUserName()+"'";
			if(jdbcTemplate.queryForInt(exeNoSql)==0){
				String sql="delete from onekey_backup_minitor where system_user='"+this.getShiroUserName()+"'";
				System.out.println(sql);
				jdbcTemplate.execute(sql);
			}*/
//			
			List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();  
			searchFilter.add(new SearchFilter("executing", Operator.EQ, "0"));
			searchFilter.add(new SearchFilter("systemUser", Operator.EQ, this.getShiroUserName()));
	        Specification<OnekeyBackupMinitor> specList = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.AND,OnekeyBackupMinitor.class);
	        List<OnekeyBackupMinitor> data = onekeyBackupMinitorRepository.findAll(specList);
	        if(data.size() > 0 ){
//				String sql="delete from onekey_backup_minitor where system_user='"+this.getShiroUserName()+"'";
				List<SearchFilter> searchFilter1 = new ArrayList<SearchFilter>();
				searchFilter1.add(new SearchFilter("systemUser", Operator.EQ, this.getShiroUserName()));
				Specification<OnekeyBackupMinitor> specList1 = DynamicSpecifications.bySearchFilter(searchFilter1,BooleanOperator.AND,OnekeyBackupMinitor.class);
		        List<OnekeyBackupMinitor> list = onekeyBackupMinitorRepository.findAll(specList1);
		        for (OnekeyBackupMinitor onekeyBackupMinitor : list) {
		        	onekeyBackupMinitorRepository.delete(onekeyBackupMinitor);
				}
//				jdbcTemplate.execute(sql);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean updBackupMinitor(Map<String, Object> params) {
		try {
			StringBuffer sql=new StringBuffer("UPDATE onekey_backup_minitor"); 
			StringBuffer sb_condition = new StringBuffer(" set ");
			if(params.get("executing")!=null && !params.get("executing").equals("")){
				sb_condition.append(" executing ="+params.get("executing").toString());
			}
			if(params.get("end_time")!=null && !params.get("end_time").equals("")){
				sb_condition.append(" , end_time ='"+params.get("end_time").toString()+"'");
			}
			if(params.get("result_dir")!=null && !params.get("result_dir").equals("")){
				sb_condition.append(", result_dir ='"+params.get("result_dir").toString()+"'");
			}
			if(params.get("result_code")!=null && !params.get("result_code").equals("")){
				sb_condition.append(", result_code ="+params.get("result_code").toString());
			}
			if(params.get("is_fulish")!=null && !params.get("is_fulish").equals("")){
				sb_condition.append(", is_fulish ="+params.get("is_fulish").toString());
			}
			if(params.get("error_msg")!=null && !params.get("error_msg").equals("")){
				sb_condition.append(" , error_msg ='"+params.get("error_msg").toString()+"'");
			}
			sb_condition.append(" where 1=1");
			if(params.get("site")!=null && !params.get("site").equals("")){
				sb_condition.append(" and site ='"+params.get("site").toString()+"'");
			}
			if(params.get("unit_type")!=null && !params.get("unit_type").equals("")){
				sb_condition.append(" and unit_type ='"+params.get("unit_type").toString()+"'");
			}
			if(params.get("unit_name")!=null && !params.get("unit_name").equals("")){
				sb_condition.append(" and unit_name ='"+params.get("unit_name").toString()+"'");
			}
			if(params.get("step_id")!=null && !params.get("step_id").equals("")){
				sb_condition.append(" and step_id ='"+params.get("step_id").toString()+"'");
			}
			if(params.get("system_user")!=null && !params.get("system_user").equals("")){
				sb_condition.append(" and system_user ='"+params.get("system_user").toString()+"'");
			}
			System.out.println(sql.append(sb_condition).toString());
//			MysqlDB.updateData(sql.toString());
			jdbcTemplate.execute(sql.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	@Override
	public List<OnekeyBackupMinitor> getMonitorByOther(Map<String, Object> Parm) {
		Map<String,SearchFilter> filter = SearchFilter.parse(Parm);
		Specification<OnekeyBackupMinitor> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, OnekeyBackupMinitor.class);
		return onekeyBackupMinitorRepository.findAll(spec);
	}
	
	
	@Override
	public List<OnekeyBackupMinitor> getBacuMonitorLog() {
		List<SearchFilter> searchFilter1 = new ArrayList<SearchFilter>();
		searchFilter1.add(new SearchFilter("systemUser", Operator.EQ, this.getShiroUserName()));
		Specification<OnekeyBackupMinitor> specList1 = DynamicSpecifications.bySearchFilter(searchFilter1,BooleanOperator.AND,OnekeyBackupMinitor.class);
        return  onekeyBackupMinitorRepository.findAll(specList1);
        
        
		/*StringBuffer sql=new StringBuffer(" select *  from onekey_backup_minitor where system_user='"+this.getShiroUserName()+"'");
		System.out.println(sql.toString());
//		return MysqlDB.queryForList(sql.toString());
		return jdbcTemplate.queryForList(sql.toString());*/
	}
	public List<Map<String, Object>> getBacuMonitorByFailure(String result_dir) {
		StringBuffer sql=new StringBuffer(" select *  from onekey_backup_minitor where result_code !='0' and result_dir='"+result_dir+"' and system_user='"+this.getShiroUserName()+"' order by start_time");
		System.out.println(sql.toString());
//		return MysqlDB.queryForList(sql.toString());
		return jdbcTemplate.queryForList(sql.toString());
	}
    public int getBackUpIngCount() {
    	
    	List<SearchFilter> searchFilter1 = new ArrayList<SearchFilter>();
    	searchFilter1.add(new SearchFilter("resultCode", Operator.NOTEQ, "0"));
		searchFilter1.add(new SearchFilter("systemUser", Operator.EQ, this.getShiroUserName()));
		Specification<OnekeyBackupMinitor> specList1 = DynamicSpecifications.bySearchFilter(searchFilter1,BooleanOperator.AND,OnekeyBackupMinitor.class);
        int countResult = onekeyBackupMinitorRepository.findAll(specList1).size();
		return  countResult == 0 ? -1 : countResult ;
        
        
        /*int countResult=-1;
        String sql=" SELECT count(*) from onekey_backup_minitor where  result_code !='0' and system_user='"+this.getShiroUserName()+"'";
		System.out.println(sql);
//		countResult=MysqlDB.queryForInt(sql);
		countResult=jdbcTemplate.queryForInt(sql);
        return countResult;*/
    }
	@Override
	public List<Map<String, Object>> getMonitorNameByFailure() {
		StringBuffer sql=new StringBuffer(" select distinct result_dir as failname from onekey_backup_minitor where result_code !='0' and system_user='"+this.getShiroUserName()+"'");
		System.out.println(sql.toString());
//		return MysqlDB.queryForList(sql.toString());
		return jdbcTemplate.queryForList(sql.toString());
		
	}
	@Override
	public List<OnekeyBackupMinitor> getExecutingMonitorLog(List<String> array) {
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();  
		for (String str : array) {
			searchFilter.add(new SearchFilter("resultDir", Operator.EQ, str));
		}
		List<SearchFilter> searchFilterAND = new ArrayList<SearchFilter>();  
		searchFilterAND.add(new SearchFilter("isFulish", Operator.EQ, "1"));
        Specification<OnekeyBackupMinitor> specList = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,OnekeyBackupMinitor.class);
        Specification<OnekeyBackupMinitor> specListAND = DynamicSpecifications.bySearchFilter(searchFilterAND,BooleanOperator.OR,OnekeyBackupMinitor.class);
	    return onekeyBackupMinitorRepository.findAll(Specifications.where(specList).and(specListAND));
		
		
		/*Map<String,SearchFilter> filter = SearchFilter.parse(param);
		Specification<OnekeyBackupMinitor> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, OnekeyBackupMinitor.class);
		return onekeyBackupMinitorRepository.findAll(spec);*/
		
		/*StringBuffer sql=new StringBuffer(" select *  from onekey_backup_minitor where is_fulish ='1'  and result_dir in('"+param.get("result_dirs")+"') order by start_time");
		System.out.println(sql.toString());z
//		return MysqlDB.queryForList(sql.toString());
		return jdbcTemplate.queryForList(sql.toString());*/
	}
	
	public ShiroUser getShiroUser() {
		if(this.shiroUser==null){
			 this.shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal(); 
			 return this.shiroUser;
		}else{
			return this.shiroUser;
		}
	}
	public String getShiroUserName() {
		if(this.shiroUser==null){
			this.shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal(); 
			return this.shiroUser.getUserName();
		}else{
			return this.shiroUser.getUserName();
		}
	}
	
//	2016 hang 1/20
	@Override
	public List<Map<String, Object>> getNeType_t(Map<String, String> neTypeparam) {
		String sql="SELECT id as ne_type_id, ne_type_name FROM ne_type";
		System.out.println(sql);
//		List<Map<String, Object>> sites = MysqlDB.queryForList(sql.toString());
		List<Map<String, Object>> sites = jdbcTemplate.queryForList(sql.toString());
		return sites;
	}
	@Override
	public List<EquipmentNe> getNeNeme_t(Map<String, Object> neNameParm) {
		Map<String,SearchFilter> filter = SearchFilter.parse(neNameParm);
		Specification<EquipmentNe> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EquipmentNe.class);
		return equipmentNeRepository.findAll(spec);
	}
	@Override
	public List<Map<String, Object>> getUnitType_t(Map<String, String> neTypeParm) {
		// 目前 只要指定的三种类型上海 的办卡类型ID是2321,2325,2327  杭州的办卡类型ID是9,15,17
//		String sql="SELECT id as unit_type_id, unit_type_name FROM unit_type where id in(2321,2325,2327)";
		String sql="select distinct t.id as unit_type_id ,  t.unit_type_name as unit_type_name from unit_type as t left join unit as u "
				+" on t.id=u.unit_type where u.ne="+neTypeParm.get("ne_id")+" and t.id in (2321,2325,2327 ) ";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
		return list;
	}
	@Override
	public List<EquipmentUnit> getUnitName_t(Map<String, Object> unitNameParm) {	
		Map<String,SearchFilter> filter = SearchFilter.parse(unitNameParm);
		Specification<EquipmentUnit> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EquipmentUnit.class);
		return equipmentUnitRepository.findAll(spec);
		/*StringBuffer sql=new StringBuffer(" SELECT id as unit_id,  unit as unit_name FROM unit ");
		StringBuffer sb_condition = new StringBuffer(" where 1=1 ");
		if(unitNameParm.get("ne_id")!=null && !unitNameParm.get("ne_id").equals("")){
			sb_condition.append(" AND  ne= "+unitNameParm.get("ne_id"));s
		}
		if(unitNameParm.get("unit_type_id")!=null && !unitNameParm.get("unit_type_id").equals("")){
			sb_condition.append(" AND  unit_type= "+unitNameParm.get("unit_type_id"));
		}
		if(unitNameParm.get("ne_type_id")!=null && !unitNameParm.get("ne_type_id").equals("")){
			sb_condition.append(" AND  ne_type= "+unitNameParm.get("ne_type_id"));
		}
		System.out.println(sql.append(sb_condition).toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
		return list;*/
	}
	@Override
	public List<Map<String, Object>> getUnitTypeByBackUpHist(Map<String, String> params) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer("select distinct t.id as id , t.unit_type_name as name from unit_type as t right join onekey_backup as u "
				+ " on u.bk_site_type_name::integer= t.id where u.bk_site_id='"+params.get("location")+"'");
				System.out.println(sql.toString());
				List<Map<String, Object>> neTypes = jdbcTemplate.queryForList(sql.toString());
		return neTypes;
	}

}
