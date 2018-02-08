package com.nokia.boss.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 根据指定时间减去多少天
	 * @param specifiedDay
	 * @param formate
	 * @param subDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String formate,int subDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day -subDay);

		String dayAfter = new SimpleDateFormat(formate)
				.format(c.getTime());
		return dayAfter;
	}
	
	/**
	 * 根据date参数求出数据落在的时间段字符串。
	 * 例如 "2017-01-01 21:33:00" 的返回值 "2017-01-01 21:30-21:45"
	 * @param date
	 * @return
	 */
	public static String getPeriodTimeString(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer hour = c.get(Calendar.HOUR_OF_DAY);
		Integer minute = c.get(Calendar.MINUTE);
		//return like "2017-01-01 21:30-21:45";
		if (minute >=0 && minute < 15) {
			return sdf.format(date)+" "+hour+":00-"+hour+":15";
		}else if(minute >= 15 && minute < 30){
			return sdf.format(date)+" "+hour+":15-"+hour+":30";
		}else if(minute >= 30 && minute < 45){
			return sdf.format(date)+" "+hour+":30-"+hour+":45";
		}else{
			//return like "2017-01-01 21:45-22:00"
			c.add(Calendar.HOUR_OF_DAY, 1);
			Integer nextHour = c.get(Calendar.HOUR_OF_DAY);
			return sdf.format(date)+" "+hour+":45-"+nextHour+":00";
		}
		
	}

}
