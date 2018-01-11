package com.whycools.transport.crash.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * yyyy-MM-dd
	 */
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 得到当前日期
	 * 
	 * @return
	 */
	public static Date getNowDate() {
		Date date = new Date();
		return date;
	}

	/**
	 * 得到当前日期字符串
	 * 
	 * @param
	 *            格式
	 * @return
	 */
	public static String getNowDateStr() {
		Date date = getNowDate();
		return DEFAULT_DATE_FORMAT.format(date);

	}

	/**
	 * 得到指定天数之后的日期，days可以为负数
	 * 
	 * @param days
	 * @return
	 */
	public static Date getAfterDays(int days, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		try {
			date = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		Date newDate = new Date(c.getTimeInMillis());

		return newDate;
	}

	/**
	 * 按格式化字符把日期转换成字符串型
	 * 
	 * @param date
	 * @param format
	 *            指定格式
	 * @return String
	 */
	public static String date2String(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(date);// 把时间类型转为String类型
		return s;
	}

	public static String date2String(Date date, SimpleDateFormat format) {
		if (date == null || format == null)
			return "";
		try {
			String s = format.format(date);// 把时间类型转为String类型
			return s;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将字符串转换为日期
	 * 
	 * @param str
	 *            需要转换的日期字符串
	 * @param format
	 *            指定格式
	 * @return
	 */
	public static Date string2Date(String str, String format) {
		return string2Date(str, new SimpleDateFormat(format));
	}

	/**
	 * 将字符串转换为日期
	 * 
	 * @param str
	 *            需要转换的日期字符串
	 * @param format
	 *            指定格式
	 * @return
	 */
	public static Date string2Date(String str, SimpleDateFormat format) {
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 得到两个日期间隔天数
	 * 
	 * @param str1
	 *            较大的日期
	 * @param str2
	 *            较小的日期
	 * @return
	 */
	public static int getTwoDate(String str1, String str2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			Date date1 = sdf.parse(str1);
			Date date2 = sdf.parse(str2);
			day = (date1.getTime() - date2.getTime()) / (60 * 60 * 24 * 1000);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int) day;

	}

	/**
	 * 得到两个日期间隔天数
	 * 
	 * @param date1
	 *            较大的日期
	 * @param date2
	 *            较小的日期
	 * @return
	 */
	public static int getTwoDate(Date date1, Date date2) {
		long day = (date1.getTime() - date2.getTime()) / (60 * 60 * 24 * 1000);
		return (int) day;
	}

	/**
	 * 比较两个日期是否相等
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean equalDate(Date date1, Date date2) {
		if (date1 == date2) {
			return true;
		} else if (null == date1) {
			return false;
		}
		return date1.equals(date2);
	}

	/**
	 * 比较两个日期是否相等
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalDate(String str1, String str2) {
		Date date1 = string2Date(str1, "yyyy-MM-dd HH:mm:ss");
		Date date2 = string2Date(str2, "yyyy-MM-dd HH:mm:ss");
		return equalDate(date1, date2);
	}

	/**
	 * 得到当前月的第一天
	 * 
	 * @param
	 */
	public static Date getThisMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		Date d = cal.getTime();
		return d;
	}

	/**
	 * @param
	 *            sourceTime 待转化的时间
	 * @param
	 *            dataFormat 日期的组织形式
	 * @return long 当前时间的长整型格式,如 1247771400000
	 */
	public static long string2long(String sourceTime, String dataFormat) {
		long longTime = 0L;
		DateFormat f = new SimpleDateFormat(dataFormat);
		Date d = null;
		try {
			d = f.parse(sourceTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		longTime = d.getTime();
		return longTime;
	}

	/**
	 * 将long类型时间转Date类型
	 * 
	 * @param longTime
	 * @return
	 */
	public static Date long2Date(long longTime) {
		return new Date(longTime);
	}

	/**
	 * 长整型转换为日期类型
	 * 
	 * @param  longTime 长整型时间
	 * @param
	 *            dataFormat 时间格式
	 * @return String 长整型对应的格式的时间
	 */
	public static String long2String(long longTime, String dataFormat) {
		Date d = new Date(longTime);
		SimpleDateFormat s = new SimpleDateFormat(dataFormat);
		String str = s.format(d);
		return str;
	}

	
	/** 
     * 得到几天前的时间 
     *  
     * @param d 
     * @param day 
     * @return 
     */  
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();  
    }    
    /** 
     * 得到几天后的时间 
     *  
     * @param d 
     * @param day 
     * @return 
     */  
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();  
    }
}
