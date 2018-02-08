package com.nokia.ices.apps.fusion.score.common.utils;

import hirondelle.date4j.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DateUtils {
	public static String DEFAULT_DATE_FORMAT = "YYYYMMDDhh";
	
	
	/**
	 * 当前传递时间减去指定小时后的日期
	 * @param hour
	 * @param format
	 * @return
	 */
	public static DateTime getSubtractHourDate(int hour,String dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
		Calendar cl = Calendar.getInstance();
		try {
			cl.setTime(sdf.parse(dateTime));
			cl.add(Calendar.HOUR, -hour);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DateTime(sdf.format(cl.getTime()));
		 
	}
	
	/**
	 * 小时/天/周/月时间处理
	 * @param param
	 * @return
	 */
	public static Map<String, Object> getStartDateAndEndDate(Map<String, Object> param){
		String [] date = new String[3];
		String startDate = param.get("startDate")==null?"":param.get("startDate").toString();
		String endDate = param.get("endDate")==null?"":param.get("endDate").toString();
		param.put("startYear", startDate.split("-")[0]);
		param.put("year", startDate.split("-")[0]);
		param.put("endYear", endDate.split("-")[0]);
		date[0]= startDate.split("-")[0];
		if(startDate.equalsIgnoreCase(endDate)){
			param.put("endDate", endDate.split("-")[1]);
			//param.put("endDate", (Integer.parseInt(endDate.split("-")[1]+1)));
		}else{
			param.put("endDate", endDate.split("-")[1]);
		}
		param.put("startDate", startDate.split("-")[1]);
		
		return param;
	}
	/**
	 * 
	 * @param dateTime
	 * yyyy-MM-dd
	 * @return
	 */
	public static DateTime getFirstDayOfWeek(String dateTime) {
		DateTime today = new DateTime(dateTime);
		DateTime firstDayThisWeek = today;
		int todaysWeekday = today.getWeekDay();
		int SUNDAY = 1;
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

	public static DateTime getFirstDayOfLastWeek(String dateTime) {
		DateTime lastWeekFirst = getFirstDayOfWeek(dateTime);
		lastWeekFirst = lastWeekFirst.minusDays(1);// 减去一天
		lastWeekFirst = getFirstDayOfWeek(lastWeekFirst.toString());
		return lastWeekFirst;
	} 
	
	public static String getGmtFormatStr(String gmt) throws ParseException{
		
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z",Locale.ENGLISH);
        Date d  =  sdf.parse(gmt);
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        return sf2.format(d);
	}
	
	/**
	 * 相差天
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String startDate,String endDate) throws ParseException{    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
        Date smdate=sdf.parse(startDate);
        Date  bdate=sdf.parse(endDate);  
        Calendar c1 = Calendar.getInstance();  
        Calendar c2 = Calendar.getInstance();
        c1.setTime(smdate);
        c2.setTime(bdate);    
        long time1 = c1.getTimeInMillis();                 
        long time2 = c2.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);
            
      // return Integer.parseInt(String.valueOf(between_days));   
       
       int result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

		return result == 0 ? 1 : Math.abs(result);
    } 
	
	
	public static void main(String[] args) throws ParseException {

		/*DateTime today = new DateTime("2016-07-29");
		System.out.println("getFirstDayOfWeek:"
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
		
		
		DateTime startDay = new DateTime("2017-01-09");
		String monthStart = startDay.getStartOfMonth().toString();*/
		
		int dd = daysBetween("2016-08-10","2016-08-20");
		System.out.println(dd);
		
	}
}
