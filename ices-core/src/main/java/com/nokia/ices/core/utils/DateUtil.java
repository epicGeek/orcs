package com.nokia.ices.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {


	private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	/**
	 * 时间周期验证
	 * 
	 * @param startTime
	 * @param endTime
	 * @param period
	 * @return
	 */
	public static String checkDateTime(String startTime, String endTime,
			String period) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = "ok";
		try {
			Date startDate = sdf.parse(startTime);
			Date endDate = sdf.parse(endTime);
			long different = endDate.getTime() - startDate.getTime();
			if (different <= 0) {
				return "开始日期不能大于结束日期";
			}
			long periodDay = 0;
			long secondsInMilli = 1000;
			long minutesInMilli = secondsInMilli * 60;
			long hoursInMilli = minutesInMilli * 60;
			long daysInMilli = hoursInMilli * 24;

			long elapsedDays = different / daysInMilli;
			different = different % daysInMilli;

			long elapsedHours = different / hoursInMilli;
			different = different % hoursInMilli;

			long elapsedMinutes = different / minutesInMilli;
			different = different % minutesInMilli;

			long elapsedSeconds = different / secondsInMilli;

			if ("MI".equalsIgnoreCase(period)) {
				periodDay = 1;
				result = printDifference(elapsedDays, elapsedHours,
						elapsedMinutes,elapsedSeconds, periodDay);
			} else if ("H".equalsIgnoreCase(period)) {
				periodDay = 7;
				result = printDifference(elapsedDays, elapsedHours,
						elapsedMinutes,elapsedSeconds, periodDay);
			} else if ("D".equalsIgnoreCase(period)) {
				periodDay = 30;
				result = printDifference(elapsedDays, elapsedHours,
						elapsedMinutes,elapsedSeconds, periodDay);
			} else if ("MO".equalsIgnoreCase(period)) {
				periodDay = 30;
				if (elapsedDays < periodDay) {
					result = "时间范围不能小于" + periodDay + "天";
				}
			}
			System.out.printf("%d days, %d hours, %d minutes, %d seconds%n",
					elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

		} catch (ParseException e) {
			logger.debug("Time validation error..." + e.getMessage());
		}

		return result;
	}

	/**
	 * 
	 * @param elapsedDays
	 *            天
	 * @param elapsedHours
	 *            小时
	 * @param elapsedMinutes
	 *            分钟
	 * @param periodDay
	 *            监控周期
	 * @return
	 */
	public static String printDifference(long elapsedDays, long elapsedHours,
			long elapsedMinutes,long elapsedSeconds, long periodDay) {
		if(elapsedDays==periodDay && elapsedHours==0 &&  elapsedMinutes==0 && elapsedSeconds==0){
			return "OK";
		}else if(elapsedDays<periodDay){
			return "OK";
		}else{
			return "时间范围不能超过" + periodDay + "天";
		}
	}
	
	public static String[] getFormatStartAndDate(String currentDate,String endTime){
		
	    String [] datas = new String[2];
    	try{
    		if(("".equals(currentDate)||null==currentDate) || ("".equals(endTime)||null==endTime)){
    			//获取当前的时间
    			String startTimeStr =getCurrentDate4()+" 00:00:00";
    			String endTimeStr = getCurrentDate4()+" 23:59:59";
    			datas[0] =startTimeStr;
    			datas[1] =endTimeStr;
    		}else{
    			datas[0] = currentDate;
    			datas[1] = endTime;
    		}
    		
    	}catch(Exception e){
    	}
		return datas;
}

	/**
	 * 获取当天开始和结束时间
	 * 
	 * @param currentDate
	 * @param endTime
	 * @return
	 */

	public static String[] getStartAndTime(String currentDate, String endTime) {

		String[] datas = new String[2];
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdfnull = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			if (("".equals(currentDate) || null == currentDate)
					|| ("".equals(endTime) || null == endTime)) {
				// 获取当前的时间
				String startTimeStr = getSystemDate() + "000000";
				String endTimeStr = getSystemDate() + "235959";
				datas[0] = startTimeStr;
				datas[1] = endTimeStr;
			} else {
				datas[0] = sdf2.format(sdfnull.parse(currentDate));
				datas[1] = sdf2.format(sdfnull.parse(endTime));
			}

		} catch (Exception e) {
		}
		return datas;
	}

	public static Date[] getStartAndTimeDate(String currentDate, String endTime) {

		Date[] datas = new Date[2];
		SimpleDateFormat sdfnull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (("".equals(currentDate) || null == currentDate)
					|| ("".equals(endTime) || null == endTime)) {
				// 获取当前的时间
				String startTimeStr = getCurrentDateString() + " 00:00:00";
				String endTimeStr = getCurrentDateString() + " 23:59:59";
				datas[0] = sdfnull.parse(startTimeStr);
				datas[1] = sdfnull.parse(endTimeStr);
			} else {
				datas[0] = sdfnull.parse(currentDate);
				datas[1] = sdfnull.parse(endTime);
			}

		} catch (Exception e) {
		}
		return datas;
	}

	/**
	 * 如果当前系统时间=数据库时间往后推迟3个月的时间就触发
	 * 
	 * @param dateStr
	 * @return
	 */
	public static int getCompareTo(String dateStr) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();// 数据库时间
		Calendar c2 = Calendar.getInstance();// 获取当前时间
		int as = 0;// 默认时间相等
		try {
			Date today = format.parse(dateStr);
			long time = 0;
			int day = 90;
			time = (today.getTime() / 1000) + 60 * 60 * 24 * day;
			Date newDate = new Date();
			newDate.setTime(time * 1000);
			c1.setTime(newDate);
			c2.setTime(new Date());
			as = c1.compareTo(c2);// ==0开始触发

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return as;
	}

	/**
	 * 把一个毫秒时间转换成字符形式
	 *
	 * @param mill
	 *            long
	 * @return String
	 */
	public static final String LongToStr(long mill) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return SDF.format(new Date(mill));
	}

	public static final String LongToStrDate(long mill) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
		return SDF.format(new Date(mill));
	}

	public static final Date LongToDate2(long mill) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
		return getStringToDate(SDF.format(new Date(mill)));
	}

	public static final Date LongToDate(long mill) {
		// SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return new Date(mill);
	}

	public static String formatDateTime(Date date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = myformat.format(date);
		return datetime;
	}

	public static String formatDateTime22(Date date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = myformat.format(date);
		return datetime;
	}

	public static Date formatString2Date(String date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return myformat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date getDateAdd1(String dd) {
		long ti = getDateLong(dd);
		ti += 1000l * 60 * 60 * 24;
		dd = LongToStr(ti);
		Date da = getStringToDate(dd);
		return da;
	}

	/**
	 * uuid产生随机数
	 * 
	 * @return String
	 */
	public static String createKey() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 返回当前时间的毫秒数
	 *
	 * @return Long
	 */
	public static final long getDateLong() {
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
		String datetime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime());
		return getDateLong(datetime1);
	}

	public static Date getPreDate() {
		long da = getDateLong() - (1000l * 60 * 60 * 24);
		return LongToDate2(da);
	}

	public static String getPreNDate(long time) {
		long da = getDateLong() - (time);
		return LongToStrDate(da);
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}

	public static List<String> getEvaluateMonth(int num) {
		SimpleDateFormat sdfYYYYMM = new SimpleDateFormat("yyyyMM");

		List<String> monthList = new ArrayList<String>();
		Date currentMonth = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentMonth);
		calendar.add(Calendar.MONTH, num);
		Date planMonthDate = calendar.getTime();
		calendar.setTime(currentMonth);
		if (num > 0) {
			while (!planMonthDate.before(calendar.getTime())) {
				monthList.add(sdfYYYYMM.format(calendar.getTime()));
				calendar.add(Calendar.MONTH, 1);
			}
		} else {
			while (!planMonthDate.after(calendar.getTime())) {
				monthList.add(sdfYYYYMM.format(calendar.getTime()));
				calendar.add(Calendar.MONTH, -1);
			}
			Collections.reverse(monthList);
		}
		return monthList;
	}

	public static List<String> getNextEvaluateMonth(int num) {
		List<String> relist = new ArrayList<String>();
		String da = getCurrentDateMonth();
		String curdaString = da;
		relist.add(da);
		for (int i = 0; i < num; i++) {
			curdaString = getDateNextMonth(curdaString);
			relist.add(curdaString);
		}
		return relist;
	}

	public static List<String> getPreEvaluateMonth(int num) {
		List<String> list = new ArrayList<String>();
		List<String> relist = new ArrayList<String>();
		String da = getCurrentDateMonth();
		String curdaString = da;
		for (int i = 0; i < num; i++) {
			curdaString = getDatePreMonth(curdaString);
			list.add(curdaString);
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			relist.add(list.get(i));
		}
		return relist;
	}

	public static Date getNextDate() {
		long da = getDateLong() + (1000l * 60 * 60 * 24);
		return LongToDate2(da);
	}

	public static String getPreMonthDate() {
		long da = getDateLong() - (1000l * 60 * 60 * 24 * 30);
		return LongToStrDate(da);
	}

	public static Date getNextDate(String date) {
		long da = getDateLong(date) + (1000l * 60 * 60 * 24);
		return LongToDate2(da);
	}

	/**
	 * 返回指定时间的毫秒数 参数格式：2003-06-20 00:00:00
	 *
	 * @return Long
	 */
	/*
	 * public static final long getDateLong(String date) { DateFormat d1 =
	 * DateFormat.getDateTimeInstance(); long time = 0; try { Date date2 =
	 * d1.parse(date); time = date2.getTime(); } catch (Exception e) {
	 * e.printStackTrace(); } return time; }
	 */

	public static final long getDateLong(String date) {
		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = 0;
		try {
			Date date2 = d1.parse(date);
			time = date2.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static final long getDateLong2(String date) {
		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
		long time = 0;
		try {
			Date date2 = d1.parse(date);
			time = date2.getTime();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			;
		}
		return time;
	}

	/**
	 * 返回两个毫秒时间的相差天数
	 *
	 * @return Long 1毫秒=1000毫秒
	 */
	public static long getDateDays(long date1, long date2) {
		long days = 0;
		long day = 86400000l;
		long dateCha = date1 - date2;
		if (dateCha < 0)
			dateCha = dateCha * -1;
		days = dateCha / day;
		return days;
	}

	/**
	 * 得到两个日期之间相差的天数
	 *
	 * @param newDate
	 *            大的日期
	 * @param oldDate
	 *            小的日期
	 * @return newDate-oldDate相差的天数
	 */
	public static int daysBetweenDates(Date newDate, Date oldDate) {
		int days = 0;
		Calendar calo = Calendar.getInstance();
		Calendar caln = Calendar.getInstance();
		calo.setTime(oldDate);
		caln.setTime(newDate);
		int oday = calo.get(Calendar.DAY_OF_YEAR);
		int nyear = caln.get(Calendar.YEAR);
		int oyear = calo.get(Calendar.YEAR);
		while (nyear > oyear) {
			calo.set(Calendar.MONTH, 11);
			calo.set(Calendar.DATE, 31);
			days = days + calo.get(Calendar.DAY_OF_YEAR);
			oyear = oyear + 1;
			calo.set(Calendar.YEAR, oyear);
		}
		int nday = caln.get(Calendar.DAY_OF_YEAR);
		days = days + nday - oday;
		if (days < 0)
			days = days * -1;
		return days;
	}

	/**
	 * 取本地系统时间（日期值 英文）
	 *
	 * @return Date
	 */
	public static java.util.Date getCurrentDate2() {
		java.util.Date date = new java.util.Date();
		// 取本地系统时间：2000-10-27 09:36:58
		return date;// 返回本地系统时间：2000/10/27 09:36:58
	}

	/**
	 * 取本地系统时间（日期值 英文）
	 *
	 * @return Date
	 */
	public static String getSystemDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		java.util.Date date = new java.util.Date();
		return sdf.format(date);// 返回本地系统时间：2000/10/27 09:36:58
	}

	/**
	 * 取本地系统时间（日期值 数字）
	 *
	 * @return Date
	 */
	public static java.sql.Date getCurrentDate() {
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		// 取本地系统时间：2000-10-27 09:36:58
		return date;// 返回本地系统时间：2000/10/27 09:36:58
	}

	public static String getCurrentDateString() {
		TimeZone tz = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(tz);
		String datetime1 = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime());

		return datetime1;
	}

	/**
	 * 取本地系统时间（字符串值）
	 *
	 * @return String
	 */
	public static String getCurrentTime() {
		java.util.Date date = new java.util.Date();
		// 取本地系统时间：2000-10-27 09:36:58
		String currTime = DateFormat.getDateTimeInstance().format(date);
		return currTime;// 返回本地系统时间：2000/10/27 09:36:58
	}

	/**
	 * 格式如1999-12-02的当前时间
	 *
	 * @return String
	 */
	public static String getCurrentDateTime() {
		TimeZone tz = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(tz);
		String datetime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime());

		return datetime1;
	}

	public static String getCurrentDatePreMonth(String dateTime) {
		int year = 0;
		int month = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = sdf.parse(dateTime.split("-")[0]);
			year = Integer.parseInt(sdf.format(date));
			DateFormat mm = new SimpleDateFormat("MM");
			Date date2 = mm.parse(dateTime.split("-")[1]);
			month = Integer.parseInt(mm.format(date2));
			if (month == 1) {
				year -= 1;
				month = 12;
			} else {
				month -= 1;
			}
			if ((month + "").length() == 1)
				return year + "0" + month;
			return year + "" + month;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getDatePreMonth(String dateTime) {
		int year = 0;
		int month = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = sdf.parse(dateTime.substring(0, 4));
			year = Integer.parseInt(sdf.format(date));
			DateFormat mm = new SimpleDateFormat("MM");
			Date date2 = mm.parse(dateTime.substring(4, 6));
			month = Integer.parseInt(mm.format(date2));
			if (month == 1) {
				year -= 1;
				month = 12;
			} else {
				month -= 1;
			}
			if ((month + "").length() == 1)
				return year + "0" + month;
			return year + "" + month;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getDateNextMonth(String dateTime) {
		int year = 0;
		int month = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = sdf.parse(dateTime.substring(0, 4));
			year = Integer.parseInt(sdf.format(date));
			DateFormat mm = new SimpleDateFormat("MM");
			Date date2 = mm.parse(dateTime.substring(4, 6));
			month = Integer.parseInt(mm.format(date2));

			if (month == 12) {
				year += 1;
				month = 1;
			} else {
				month += 1;
			}
			if ((month + "").length() == 1)
				return year + "0" + month;
			return year + "" + month;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getCurrentDatePreMonth2(String dateTime) {
		int year = 0;
		int month = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = sdf.parse(dateTime.split("-")[0]);
			year = Integer.parseInt(sdf.format(date));
			DateFormat mm = new SimpleDateFormat("MM");
			Date date2 = mm.parse(dateTime.split("-")[1]);
			month = Integer.parseInt(mm.format(date2));
			if (month == 1) {
				year -= 1;
				month = 12;
			} else {
				month -= 1;
			}
			if ((month + "").length() == 1)
				return year + "-0" + month;
			return year + "-" + month;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date getCurtDate() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
		currenttime = dd.format(new java.util.Date());

		return getStringToDate(currenttime);
	}

	public static String getStrDate(String strDate) {

		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat d2 = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			Date date = d2.parse(strDate);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			return d1.format(c.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getStrToFormatDate(String dataTime,String format) {

		SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat d1 = new SimpleDateFormat(format);
		try {
			Date date = d2.parse(dataTime);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			return d1.format(c.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据字符串日期返回Date日期
	 *
	 * @param String
	 *            日期
	 * @return Date 日期
	 */
	public static Date getStringToDate(String dateTime) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getStringToDate2(String dateTime) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			date = sdf.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getStringToDate4(String dateTime) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String currenttime = "";
		try {
			date = sdf.parse(dateTime);
			DateFormat dd = new SimpleDateFormat("yyyy-MM");
			currenttime = dd.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currenttime;
	}

	public static String getStringToDate5(String dateTime) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String currenttime = "";
		try {
			date = sdf.parse(dateTime);
			DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
			currenttime = dd.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currenttime;
	}

	public static String getCurrentDateTime2(Date d) {
		String currenttime = null;
		try {
			DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			currenttime = dd.format(d);
		} catch (Exception e) {
			DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
			currenttime = dd.format(d);
		}
		return currenttime;
	}

	public static String getCurrentDate4() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDateMonth() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMM");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDate5() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMMddHH");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDate6() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMMdd");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDateDay() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMM");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDateTime2() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMMddHHmmss");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDateTime3() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMMddHHmm");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static final long getDateLong5(String date) {
		DateFormat dd = new SimpleDateFormat("yyyyMMddHH");
		long time = 0;
		try {
			Date date2 = dd.parse(date);
			time = date2.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static final String LongToDate3(long mill) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHH");
		return SDF.format(new Date(mill));
	}

	public static String formatDateTime2(Date date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		DateFormat myformat = new SimpleDateFormat("yyyyMMddHH");
		String datetime = myformat.format(date);
		return datetime;
	}

	// 获得当前时间的前一个小时
	public static String getCurrentDatePreHour() {
		String dateString = getCurrentDate5();
		long time = getDateLong5(dateString);
		time = time - (1000l * 60 * 60);
		String datetime = LongToDate3(time);
		return datetime;
	}

	public static String getDayTime() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("HH:mm:ss");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static int getdayHours() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int getday() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static Date DateAddMinutes(Date date, int plus) {
		Date rtnDate = date;
		rtnDate.setTime(date.getTime() + 1000 * 60 * plus);
		return rtnDate;
	}

	/**
	 * 获得指定年月
	 * 
	 * @param num
	 *            >0 取num个月 =0当前月
	 * @return yyyyMM
	 */
	public static String getNextYearMonth(int num) {
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(zone);
		DateFormat dd = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		if (num != 0) {
			// c.add(Calendar.MONTH, num);
			c.set(Calendar.MONTH, num - 1);
		}
		return dd.format(c.getTime());
	}

	/**
	 * 获得当前时间整5分 向前取整
	 * 
	 * @param num
	 *            >0 向后取num*5分 =0当前整五分 <0向前取num*5分
	 * @return yyyy-MM-dd HH:mm:00
	 */
	public static String getNextMinute(int num) {
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(zone);
		DateFormat dd = new SimpleDateFormat("HHmm");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - c.get(Calendar.MINUTE)
				% 5);
		if (num != 0) {
			c.add(Calendar.MINUTE, num);
		}
		return dd.format(c.getTime());
	}

	public static String getNextHours(int num, int minute) {
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(zone);
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.SECOND, 0);
		if (num != 0) {
			c.add(Calendar.MINUTE, num * minute);
		}
		return dd.format(c.getTime());
	}

	/**
	 * 获得当前时间整5分 向前取整
	 * 
	 * @param num
	 *            >0 向后取num*5分 =0当前整五分 <0向前取num*5分
	 * @return yyyy-MM-dd HH:mm:00
	 */
	public static String getNextDate(int num, int minute) {
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(zone);
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - c.get(Calendar.MINUTE)
				% minute);
		c.set(Calendar.SECOND, 0);
		if (num != 0) {
			c.add(Calendar.MINUTE, num);
		}
		return dd.format(c.getTime());
	}

	/**
	 * @author yudq
	 * @param minute
	 *            整数, 可以取到整5分时间值
	 * @return yyyyMMddHHmm 例如201207071335
	 */
	public static String getCurrentDateTime(int minute) {
		return getCurrentDate6() + getNextMinute(minute);
	}

	/**
	 * 获得本周开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfThisWeek(Date date,
			int firstDayOfThisLocale) {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		cal.setTime(date);
		cal.add(Calendar.DATE, firstDayOfThisLocale - dayOfWeek);
		return cal.getTime();
	}

	public static Date getLastDateOfThisWeek(Date date, int firstDayOfThisLocale) {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		cal.setTime(date);
		cal.add(Calendar.DATE, firstDayOfThisLocale - dayOfWeek + 7);
		return cal.getTime();
	}

	public static Date getFirstDateOfThisMonth(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf_long = new SimpleDateFormat("yyyyMMdd");
		String dateString = sdf.format(date) + "01";
		return sdf_long.parse(dateString);
	}

	public static Date getLastDateOfThisMonth(Date date) throws ParseException {
		Date firstDateOfThisMonth = getFirstDateOfThisMonth(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(firstDateOfThisMonth);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}

}
