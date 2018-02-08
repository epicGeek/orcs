package com.dhss.app.boss.task.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocalTest {
	public static void main(String[] args) throws IOException{
		Map<String,Integer> m = new HashMap<>();
		m.put("s", 0);
		System.out.println(m.get("s"));
		Integer s = m.get("s");
		s++;
		System.out.println(s);
		System.out.println(m.get("s"));
		
	}
}
