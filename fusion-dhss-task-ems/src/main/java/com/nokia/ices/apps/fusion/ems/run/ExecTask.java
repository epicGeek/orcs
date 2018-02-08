package com.nokia.ices.apps.fusion.ems.run;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.ems.domain.EmsCheckJob;
import com.nokia.ices.apps.fusion.ems.service.EmsCheckTaskJobService;

public class ExecTask implements Runnable{
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Logger logger = LoggerFactory.getLogger(ExecTask.class);
	
	public EmsCheckTaskJobService emsCheckTaskJobService = null;


	public ExecTask(EmsCheckTaskJobService emsCheckTaskJobService) {
		super();
		this.emsCheckTaskJobService = emsCheckTaskJobService;
	}


	@Override
	public void run() {
		System.out.println(ProjectProperties.getEmsSendMoblieList());
		
		GregorianCalendar todayCal = new GregorianCalendar();
		String thisTime = format.format(todayCal.getTime());
		String groupTime = format.format(todayCal.getTime().getTime()-1000*60*5);
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			map.put("nextDate_GE", format.parse(groupTime));
			map.put("nextDate_LT", format.parse(thisTime));
		} catch (ParseException e1) {
			e1.printStackTrace();
			emsCheckTaskJobService.taskErrorSendMessage("EMS程序检查待执行任务异常！"+e1.getMessage());
		}
		
		map.put("execFlag_EQ", "1");//启动状态的任务
		
		//所有要执行的任务
		List<EmsCheckJob> list = emsCheckTaskJobService.findJobByMap(map);
		if(list.size() == 0){
			logger.info("NO EXEC JOB");
			return;
		}
		logger.info(list.size()+" JOB");
		for (EmsCheckJob emsCheckJob : list) {
			try {
				
				logger.info("UPDATE：" + emsCheckJob.getJobName() + "【NEXT EXEC DATE】");
				
				emsCheckTaskJobService.updateJobExecTime(emsCheckJob);
				
				logger.info("exec task：" + emsCheckJob.getJobName());
				
				emsCheckTaskJobService.execEmsJob(emsCheckJob);
				
			} catch (Exception e) {
				e.printStackTrace();
				emsCheckTaskJobService.taskErrorSendMessage("EMS:【" +emsCheckJob.getJobName() + "】任务执行异常！"
						+ e.getMessage() + ",执行时间:"+format.format(new Date()));
			}
		}
	}

}
