package com.nokia.ices.apps.fusion.alarm.controll;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.alarm.IcesAlarmRule;
import com.nokia.ices.apps.fusion.alarm.service.IcesAlarmRuleService;


@RestController
@RepositoryRestController
public class IcesAlarmRuleController {
	
	
	
	@Autowired
    private IcesAlarmRuleService icesAlarmRuleService;

	/**
     * 告警规则
     * @param alarmNo
     * @param page
     * @return
     */
    @RequestMapping(value = "/alarmRule/search" , method = RequestMethod.GET)
    public Page<IcesAlarmRule> getAlarmRuleList(
    		@RequestParam("alarmNo") String alarmNo,
    		Pageable page ){
    	
    	return icesAlarmRuleService.getAlarmRule(alarmNo, page);
    }
    
    
    
    
    /** =====================导出=====================**/
    /**
     * 告警规则导出
     * @param request
     * @param response
     */
//    @RequestMapping(value = "/alarmRule/exportFile")
//    public void exportFileAlarmRule(HttpServletRequest request,HttpServletResponse response){
//    	
//    	String alarmNo = request.getParameter("alarmNo");
//    	
//    	List<IcesAlarmRule> list = icesAlarmRuleService.getAlarmRule(alarmNo, null).getContent();
//    	String header[] ={"厂家告警号","告警标题","告警标准名","事件对设备的影响","事件对业务的影响","告警解释","是否立即通知","是否重大告警","全网告警个数门限判断时长","全网告警个数门限","VIP基站告警个数门限判断时长","VIP基站告警个数门限","普通基站告警个数门限判断时长","普通基站告警个数门限"};
//    	String path = System.getProperty("user.dir");
//    	String batchNo = UUID.randomUUID().toString();
//    	File file = new File(path+"/IcesAlarmRule_"+batchNo+".xlsx");
//    	if (file.exists()) {
//    		file.delete();
//		}
//    	try {
//			file.createNewFile();
////			ExportData exportData = new ExportData(header, "批量查询结果");
//			OutputStream out = new FileOutputStream(file);
////			exportData.setAlarmRuleExcel(list, out);
////			FileOperateUtil.download(request, response, path+"/icesAlarmRule_"+batchNo+".xlsx", "application/octet-stream","批量查询结果.xlsx");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();	
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//  
//    }
    
    
    
    
}
