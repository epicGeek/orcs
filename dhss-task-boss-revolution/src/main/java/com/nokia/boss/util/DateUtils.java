package com.nokia.boss.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
	private static SimpleDateFormat SDF_TO_HOUR = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
	private static SimpleDateFormat SDF_TO_DAY = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static SimpleDateFormat SDF_TO_MONTH = new SimpleDateFormat("yyyy-MM-01 00:00:00");
	private static SimpleDateFormat SDF_SHORT = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 根据指定时间减去多少天
	 * @param specifiedDay
	 * @param format
	 * @param subDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String format,int subDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day -subDay);

		String dayAfter = new SimpleDateFormat(format)
				.format(c.getTime());
		return dayAfter;
	}

	/**
	 * 取各个时间节点的整点信息，也就是KPI的起始时间，上个小时开始，昨天开始，上个月开始时间。
	 * @return
	 */
	public static Map<String,String> getTargetTime(){
		Map<String,String> m = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		/*************** TODAY *****************/
		Date today = calendar.getTime();
		String todayStr = SDF_SHORT.format(today);
		/*************** LAST HOUR *****************/
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
		Date lastHour = calendar.getTime();
		String lastHourStr = SDF_TO_HOUR.format(lastHour);
		calendar = Calendar.getInstance();
		/*************** YESTERDAY *****************/
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		Date yesterday = calendar.getTime();
		String yesterdayStr = SDF_TO_DAY.format(yesterday);
		calendar = Calendar.getInstance();
		/************** LAST MONTH ****************/
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		Date lastMonth = calendar.getTime();
		String lastMonthStr = SDF_TO_MONTH.format(lastMonth);
		calendar = Calendar.getInstance();
		/************** TOMORROW ****************/
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		Date tomorrow = calendar.getTime();
		String tomorrowStr = SDF_SHORT.format(tomorrow);
		
		m.put("TODAY", todayStr);
		m.put("LAST HOUR", lastHourStr);
		m.put("YESTERDAY", yesterdayStr);
		m.put("LAST MONTH", lastMonthStr);
		m.put("TOMORROW", tomorrowStr);
		return m ;
	}

}
