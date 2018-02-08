package com.nokia.ices.apps.fusion.ems.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nokia.ices.apps.fusion.RunApp;
import com.nokia.ices.apps.fusion.ems.consumer.CommandConsumer;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RunApp.class)
@WebAppConfiguration
public class CommandConsumerTest {
	
	@Autowired
	CommandConsumer CommandConsumer;
	
	@Test
	public void testReseiveMessageForEmsCheckJobTask(){
		
		
	}

}
