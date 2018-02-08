package com.nokia.ices.apps.fusion.command.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nokia.ices.apps.fusion.FusionApplication;
import com.nokia.ices.apps.fusion.command.service.EmsMonitorService;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorHistoryRepository;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorRepoitory;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FusionApplication.class)
@WebAppConfiguration
public class EmsMonitorServiceImplTest {

	@Autowired
	EmsMonitorService emsMonitorService;
	
	
	@Autowired
	EmsMonitorRepoitory emsMonitorRepoitory;
	
	@Autowired
	EmsMonitorHistoryRepository emsMonitorHistoryRepository;
	
	@Before
	public void setUp() throws Exception {
		List<EmsMonitorHistory> list = new ArrayList<EmsMonitorHistory>();
		
		EmsMonitorHistory em1 = new EmsMonitorHistory();
		em1.setExecuteTime(new Date());
		em1.setMonitoredCommandId(1L);
		em1.setMonitoredCommandName("查看");
		em1.setMonitoredUnitId(1L);
		em1.setMonitoredUnitName("test1");
		em1.setNotificationContent("test1 test");
		em1.setResultLevel("0");
		em1.setResultPath("C:\\");
		em1.setResultValue("99");
		list.add(em1);
		
		EmsMonitorHistory em2 = new EmsMonitorHistory();
		em2.setExecuteTime(new Date());
		em2.setMonitoredCommandId(2L);
		em2.setMonitoredCommandName("导出");
		em2.setMonitoredUnitId(1L);
		em2.setMonitoredUnitName("test1");
		em2.setNotificationContent("test1 test");
		em2.setResultLevel("2");
		em2.setResultPath("C:\\");
		em2.setResultValue("89");
		list.add(em2);
		emsMonitorHistoryRepository.save(list);
	}

	@After
	public void tearDown() throws Exception {
		
//		emsMonitorRepoitory.deleteAll();
	}

	@Test
	public void testFindEmsResult() {
		emsMonitorService.findEmsResult(null);
		emsMonitorService.findEmsResult("1");
	}

}
