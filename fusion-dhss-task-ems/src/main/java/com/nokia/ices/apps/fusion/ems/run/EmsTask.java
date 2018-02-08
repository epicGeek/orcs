package com.nokia.ices.apps.fusion.ems.run;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorHistoryRepository;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorRepoitory;
import com.nokia.ices.apps.fusion.ems.service.EmsCheckTaskJobService;

@Component
@EnableScheduling
public class EmsTask {
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



	@Autowired
	EmsCheckTaskJobService emsCheckTaskJobService;

	@Autowired
	EmsMonitorRepoitory emsMonitorRepoitory;

	@Autowired
	EmsMonitorHistoryRepository emsMonitorHistoryRepository;
	
	
	@Scheduled(cron = "0 0/1 * * * ?")
	public void runTask() throws ParseException {
		new Thread(new ExecTask(emsCheckTaskJobService)).start();

	}
	
	@Scheduled(cron = "0 0 9 ? * *")
	public void deleTempData() throws ParseException {

		new Thread(new ClearHistoryAlarmTask(emsMonitorRepoitory, emsCheckTaskJobService, emsMonitorHistoryRepository))
				.start();

	}

}
