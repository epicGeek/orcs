package com.nokia.ices.apps.fusion.log.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mydb")
public class TestController {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping("db")
	public void testDb(){
		
		String date = jdbcTemplate.queryForObject("SELECT NOW()",String.class);
		System.out.println(date);
	}

}
