package com.asiainfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class TestController {


	@GetMapping("/jwttest")
	public Object jwtTest(Authentication user) {
		return user;
	}

	public static void main(String[] args){
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:00:00" );
		Date d= new Date();
		String str = sdf.format(d);
		System.out.println(str);


	}
	
}
