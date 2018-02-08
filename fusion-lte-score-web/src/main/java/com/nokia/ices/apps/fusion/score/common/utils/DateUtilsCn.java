package com.nokia.ices.apps.fusion.score.common.utils;

import hirondelle.date4j.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtilsCn {
	public static String DEFAULT_DATE_FORMAT = "YYYYMMDDhh";
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//String date = sdf.format(new Date()).toString();
	/**
	 * 本周周一 中国时间
	 * @param dateTime
	 * yyyy-MM-dd
	 * @return
	 */
	public static DateTime getFirstDayOfWeek(String dateTime) {
		DateTime today = new DateTime(dateTime);
		DateTime firstDayThisWeek = today;
		int todaysWeekday = today.getWeekDay();
		int SUNDAY = 2;
		if (todaysWeekday > SUNDAY) {
			int numDaysFromSunday = todaysWeekday - SUNDAY;
			firstDayThisWeek = today.minusDays(numDaysFromSunday);

		}
		return firstDayThisWeek;
	}

	public static DateTime getEndDayOfWeek(String dateTime) {
		DateTime today = new DateTime(dateTime);
		DateTime endDayThisWeek = today;
		int todaysWeekday = today.getWeekDay();// 周一到周日为一周
		endDayThisWeek = today.plusDays(7 - todaysWeekday);
		return endDayThisWeek;
	}

	public static DateTime firstDayOfThisWeek() {
		DateTime today = DateTime.today(TimeZone.getDefault());
		DateTime firstDayThisWeek = today; // start value
		int todaysWeekday = today.getWeekDay();
		int SUNDAY = 1;
		if (todaysWeekday > SUNDAY) {
			int numDaysFromSunday = todaysWeekday - SUNDAY;
			firstDayThisWeek = today.minusDays(numDaysFromSunday);
		}
		return firstDayThisWeek;
	}

	public static DateTime endDayOfThisWeek() {
		DateTime today = DateTime.today(TimeZone.getDefault());
		DateTime endDayThisWeek = today; // start value
		int todaysWeekday = today.getWeekDay();
		endDayThisWeek = today.plusDays(7 - todaysWeekday);

		return endDayThisWeek;
	}

	public static Integer getWeekOfYear(String dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cl = Calendar.getInstance();
		try {
			cl.setTime(sdf.parse(dateTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week = cl.get(Calendar.WEEK_OF_YEAR);
		return week;
	}

	public static Integer getYearOfWeeks(String dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		try {
			cl.setTime(sdf.parse(dateTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH);
		if (getWeekOfYear(dateTime) == 1 && month == 11) {
			year += 1;

		}
		return year;
	}

	public static Integer getLastWeekOfYear(String dateTime) {
		DateTime lastWeekFirst = getFirstDayOfWeek(dateTime);
		lastWeekFirst = lastWeekFirst.minusDays(1);// 减去一天
		return getWeekOfYear(lastWeekFirst.toString());

	}

	public static Integer getYearOfLastWeek(String dateTime) {
		DateTime lastWeekFirst = getFirstDayOfWeek(dateTime);
		lastWeekFirst = lastWeekFirst.minusDays(1);// 减去一天
		return getYearOfWeeks(lastWeekFirst.toString());
	}

	public static Integer getLastMonth(String dateTime) {
		DateTime lastMonthEnd = new DateTime(dateTime);
		lastMonthEnd = lastMonthEnd.getStartOfMonth();
		lastMonthEnd = lastMonthEnd.minusDays(1);// 减去一天
		return lastMonthEnd.getMonth();
	}

	public static Integer getYearOfLastMonth(String dateTime) {
		DateTime lastMonthEnd = new DateTime(dateTime);
		lastMonthEnd = lastMonthEnd.getStartOfMonth();
		lastMonthEnd = lastMonthEnd.minusDays(1);// 减去一天
		return lastMonthEnd.getYear();
	}

	/**
	 * 上周周一
	 */
	public static DateTime getFirstDayOfLastWeek(String dateTime) {
		DateTime lastWeekFirst = getFirstDayOfWeek(dateTime);
		lastWeekFirst = lastWeekFirst.minusDays(7);// 减去天
		lastWeekFirst = getFirstDayOfWeek(lastWeekFirst.toString());
		return lastWeekFirst;
	} 
	
	/**
	 * 获取上月第一天
	 * @return
	 */
	public static String getMonth() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String firstDay = format.format(cal_1.getTime());
       return firstDay;
	}

	public static void main(String[] args) throws ParseException {
		
			DateTime today =  new DateTime("2016-02-02");
	//	String today = sdf.format(new Date()).toString();
		System.out.println("getFirstDayOfWeek+aaa:"
				+ getFirstDayOfWeek(today.toString()));
		System.out.println("getEndDayOfWeek:"
				+ getEndDayOfWeek(today.toString()));
		System.out.println("上一周所在的年份getYearOfLastWeek: "
				+ getYearOfLastWeek(today.toString()));

		System.out.println("上一周周一getFirstDayOfLastWeek: "
				+ getFirstDayOfLastWeek(today.toString()));
		System.out.println("上一周是所在年份的第N周getLastWeekOfYear： "
				+ getLastWeekOfYear(today.toString()));
		System.out.println("这个月最后一天getEndOfMonth : "
				+ today.getEndOfMonth().format("YYYY-MM-DD"));
		System.out.println(today.toString() + "所在的周 getWeekOfYear: "
				+ getWeekOfYear(today.toString()));
		System.out.println(today.toString() + "所在的年份 getYearOfWeeks: "
				+ getYearOfWeeks(today.toString()));

		System.out.println("上个月所在的年份getYearOfLastMonth: "
				+ getYearOfLastMonth(today.toString()));
		System.out
				.println("上个月getLastMonth： " + getLastMonth(today.toString()));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(sdf.parse(today.toString()));
		System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
		System.out.println("上月第一天："+getMonth());
	}
}
