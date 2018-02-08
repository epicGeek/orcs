package com.nokia.ices.apps.fusion.onekey.backup.service;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.emergency.domian.StepConfTable;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.onekey.backup.mbmodel.BackupRSInfo;
import com.nokia.ices.apps.fusion.onekey.domian.OnekeyBackup;
import com.nokia.ices.apps.fusion.onekey.domian.OnekeyBackupMinitor;


/**
 * 一键备份功能的Service
 * 1.定义数据新增和查询的方法
 * 2.定义执行备份的操作
 * User: stev.zhang
 * Date: 2015/5/24
 * Time: 16:56
 */
public interface BackupService {
    /****
     * 根据条件查询一键备份的历史数据
     * @param params 查询使用的条件
     * @return List<BackupHistory> 查询到的数据结果集
     * ***/
    public List<Map<String, Object>> searchBackupHistory(Map<String,Object> params);
    /****
     * 根据条件查询一键备份的历史数据总数
     * @param params 查询使用的条件
     * @return int 查询到的数据结果总数
     * ***/
    public Page<OnekeyBackup> searchBackupHistory(Map<String, Object> params,Pageable pageable);

    /*****
     *  根据参数查询备份服务器的信息，包含:
     *  1.服务器IP,端口,登录服务器用的用户名,密码 ,连接超时时间
     *  2.连接服务器后需要执行的命令
     *  @param params  查询备份服务器和命令信息使用的参数
     *  @return  BackupRSInfo 查询到的需要执行备份的服务器和指令信息
     * ****/
    public BackupRSInfo searchBackupRSInfoParams(Map<String,Object> params);

    /******
     * 根据传入的需要备份的服务器信息以及需要执行的指令信息,
     * 连接远程服务器,执行指令集合,并记录执行结果
     * @param param
     * @return BackupRSInfo 执行后的结果，记录执行过程日志信息和结果信息
     * ******/
    public BackupRSInfo executeBackupCommands(BackupRSInfo param);

    /***
     * 在执行完备份功能后，生成备份的历史数据
     * 记录关联的文件名为最后一条指令执行的结果
     * @param params 页面上传入的查询参数
     * @param backupRSInfo  保存指令执行的结果参数
     * @return int 保存影响的记录行数
     * *****/
    public int  saveBackupDataAsHistory(Map<String,Object> params,BackupRSInfo backupRSInfo);
    public int  saveBackupDataAsHistory(Map<String,Object> params);
	public List<Map<String, Object>> units(Map<String, Object> params);
	public List<Map<String, Object>> neTypes(Map<String, Object> params);
	public List<Map<String, Object>> sites();
	public int getNeType(Map<String, Object> params);
	public List<StepConfTable> getStepList(Map<String, Object> params);
	public List<Map<String, Object>> queryLog(Map<String, Object> params);
	public List<Map<String, Object>> queryAccount(Map<String, Object> params);
	
	public String getUnitTypeNameById(Map<String, Object> params);
	public boolean saveBackupMinitorLog(Map<String, String> params);
	public boolean updBackupMinitor(Map<String, Object> params);
	public boolean delBackupMinitor() ;
	List<OnekeyBackupMinitor> getMonitorByOther(Map<String, Object> Parm);
	public List<OnekeyBackupMinitor> getBacuMonitorLog();
    public int getBackUpIngCount();
    public List<Map<String, Object>> getBacuMonitorByFailure(String result_dir);
	public List<Map<String, Object>> getMonitorNameByFailure();
	public List<OnekeyBackupMinitor> getExecutingMonitorLog(List<String> array);
	/***
	 * 得到所有网元类型列表	_t表示通用的 
	 * 目前是 得到所有的
	 * @return
	 */
	public List<Map<String, Object>> getNeType_t(Map<String, String> neTypeparam);
	/***
	 * 得到所有符合条件的网元名称列表	_t表示通用的 
	 * 根据网元类型ID
	 * @return
	 */
	public List<EquipmentNe> getNeNeme_t(Map<String, Object> neNameParm);
	
	/***
	 * 得到所有办卡类型列表	_t表示通用的 
	 * 目前是 得到所有的
	 * @return
	 */
	public List<Map<String, Object>> getUnitType_t(Map<String, String> neTypeParm);
	
	
	/***
	 * 得到所有办卡类型列表	_t表示通用的 
	 * 目前是 根据网元ID,网元类型ID,板卡类型ID
	 * @return
	 */
	public List<EquipmentUnit> getUnitName_t(Map<String, Object> unitNameParm);
	
	/***
	 * 查看备份历史中存在的办卡类型
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getUnitTypeByBackUpHist(Map<String, String> params);
	
}
