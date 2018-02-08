package com.nokia.ices.apps.fusion.system.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.AssertionErrors;

import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.service.SystemService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class SystemUserRepositoryIntegrationTests {
	
	@Autowired
	SystemService systemUserService;
	
	@Autowired
	SystemUserRepository systemUserRepository;
	
	
//	@Rule
//	public OutputCapture capture = new OutputCapture();

	

	@Test
	public void testService() {
		Assert.assertNotNull(systemUserService);

//		systemUserRepository.getOne(1l);
//		systemUserService.findUserByCreator(systemUserRepository.getOne(1l));
		
	}
	
//	@Test
	public void testDeleteUser(){
		try {
			systemUserRepository.deleteAll();
		} catch (Exception e) {
			AssertionErrors.fail("删除全部失败");
		}

//		List<SystemUser> all = systemUserRepository.findAll();
//		Assert.assertNotNull(all);
		
		SystemUser user = new SystemUser();
		user.setMobile("13999999999");
		user.setUserName("testuser");
		try {
//			systemUserRepository.saveAndFlush(user);
			Assert.assertNotNull(user.getId());
		} catch (Exception e) {
		}
		
		systemUserRepository.delete(user);
	}
	

}