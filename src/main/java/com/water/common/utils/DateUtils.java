package com.water.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {
	private static final Logger log = LoggerFactory.getLogger(DateUtils.class);
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYMMDD = "yyMMdd";
	public static final String YYMMDDHHMMSS = "yyMMddHHmmss";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYMMDDHHMMSSSSS = "yyMMddHHmmssSSS";
	public static final String DD = "dd";
	public static final String YYYY_MM = "yyyy-MM";
	public static final String MM_DD = "MM-dd";
	public static final String MMDD = "MM/dd";
	public static final String YYYY = "yyyy";
	public static final String ZH_YYYY_MM_DD = "yyyy年MM月dd日";
	public static final String ZH_YYYY_MM = "yyyy年MM月";
	public static final String ZH_MM_DD = "MM月dd日";
	public static final String HH_MM = "HH:mm";
	public static final String HH_MM_SS = "HH:mm:ss";


	public static final String START_HHMMSS = " 00:00:00";
	public static final String END_HHMMSS = " 23:59:59";
	public static final String MM_DD_HH_MM = "MM-dd HH:mm";
	public static final String YY_MM_DD = "yy/MM/dd";

	/**
	 * 计算日期相隔多少天
	 *
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public static int betweenDays(Date sdate, Date edate) {
		Calendar beginDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		beginDate.setTime(sdate);
		endDate.setTime(edate);
		return betweenDays(beginDate, endDate);
	}

	public static Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		calendar.set(year, month, day);
		return calendar;
	}

	/**
	 * 给指定的时间Date增加monthNum个月份
	 * @param date
	 * @param monthNum
	 * @return 返回增加月份后的时间
	 */
	public static Date addDateMonth(Date date, int monthNum){
		if (monthNum <= 0){
			return date;
		}
		Calendar calendar = Calendar.getInstance();;
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, monthNum);
		return calendar.getTime();
	}

	public static Date modifyDateMonth(Date date, int monthNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, monthNum);
		return calendar.getTime();
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
		Date beginDate;
		Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 计算日期相隔多少天
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int betweenDays(Calendar beginDate, Calendar endDate) {
		if (beginDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)) {
			return endDate.get(Calendar.DAY_OF_YEAR) - beginDate.get(Calendar.DAY_OF_YEAR);
		} else {
			if (beginDate.getTimeInMillis() < endDate.getTimeInMillis()) {
				int days = beginDate.getActualMaximum(Calendar.DAY_OF_YEAR) - beginDate.get(Calendar.DAY_OF_YEAR) + endDate.get(Calendar.DAY_OF_YEAR);
				for (int i = beginDate.get(Calendar.YEAR) + 1; i < endDate.get(Calendar.YEAR); i++) {
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, i);
					days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
				}
				return days;
			} else {
				int days = endDate.getActualMaximum(Calendar.DAY_OF_YEAR) - endDate.get(Calendar.DAY_OF_YEAR) + beginDate.get(Calendar.DAY_OF_YEAR);
				for (int i = endDate.get(Calendar.YEAR) + 1; i < beginDate.get(Calendar.YEAR); i++) {
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, i);
					days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
				}
				return days;
			}
		}
	}

	public static DateFormat getDateFormat(String dateFormat) {
		return new SimpleDateFormat(dateFormat);
	}

	/**
	 * 返回当前系统时间的时间戳
	 * @return
	 */
	public static Timestamp getCurrentTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}

	// 获取当前时间
	public static String getcurrentTime() {
		String current = DateUtils.dateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		return current;
	}

	// 获取上个月的时间
	public static String getlastMonTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		String current = DateUtils.dateToString(new Date(cal.getTime().getTime()), DateUtils.YYYY_MM_DD_HH_MM_SS);
		return current;
	}

	public static long getlastMothTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		return cal.getTime().getTime();
	}

	/**
	 * 获取与给定日期参数相差天的日期
	 *
	 * @param srcDate
	 * @param differdays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date newDate(Date srcDate, int differdays) {
		if (srcDate == null) {
			return new Date();
		}
		Date date = new Date(srcDate.getTime());
		date.setDate(date.getDate() + differdays);
		return date;
	}

	/**
	 * 根据传入的日期，格式化为指定的日期格式
	 *
	 * @param date
	 *            日期参数
	 * @param dateFormat
	 *            格式 例如:yyyy-MM-dd
	 * @return
	 */
	public static String dateToString(Date date, String dateFormat) {
		try {
			if (date == null) {
				return null;
			}
			return getDateFormat(dateFormat).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将日期转化为字符串，默认格式为yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		try {
			if (date == null) {
				return "";
			}
			return getDateFormat(YYYY_MM_DD).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的日期，可根据differdays 参数设置到另一日期，再格式化日期
	 *
	 * @param srcDate
	 *            日期参数
	 * @param differdays
	 *            与给定的日期相差天数 负数表示指定日期前多少天 正数表示日期后多少天
	 * @param dateFormat
	 *            格式 例如:yyyy-MM-dd
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String dateToString(Date srcDate, int differdays, String dateFormat) {
		Date date = new Date(srcDate.getTime());
		date.setDate(date.getDate() + differdays);
		try {
			return getDateFormat(dateFormat).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定参数在一年中所处的周期
	 *
	 * @param srcDate
	 * @param differdays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getWeek(Date srcDate, int differdays) {
		Date date = new Date(srcDate.getTime());
		date.setDate(date.getDate() + differdays);
		return getDateFormat("w").format(date);
	}

	/**
	 * 根据给定日期，按需要转换的格式转换成字符串
	 *
	 * @param dateString
	 * @param dateFormat
	 * @return
	 */
	public static Date stringToDate(String dateString, String dateFormat) {
		try {
			if (StringUtils.isEmpty(dateString)) {
				return null;
			}
			return getDateFormat(dateFormat).parse(dateString);
		} catch (Exception e) {
			log.error("日期格式转换错误", e);
		}
		return null;
	}

	/**
	 * 获取一月中的最后一天
	 *
	 * @param srcDate
	 * @param differdays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getLastDayOfMonth(Date srcDate, int differdays) {
		Date date = new Date(srcDate.getTime());
		date.setDate(date.getDate() + differdays);
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = cDay1.getTime();
		lastDate.setDate(lastDay);
		return lastDate;
	}

	/**
	 * 获取一月中的第一天
	 *
	 * @param srcDate
	 * @param differdays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getFirstDayOfMonth(Date srcDate, int differdays) {
		Date date = new Date(srcDate.getTime());
		date.setDate(date.getDate() + differdays);
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		final int lastDay = cDay1.getActualMinimum(Calendar.DAY_OF_MONTH);
		Date lastDate = cDay1.getTime();
		lastDate.setDate(lastDay);
		return lastDate;
	}

	/**
	 * 获取给定日期所在周的第一天的时间
	 *
	 * @param srcDate
	 * @param differdays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getFirstDayOfWeek(Date srcDate, int differdays) {
		Date date = new Date(srcDate.getTime());
		date.setDate(date.getDate() + differdays);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date firstDateOfWeek; // 得到当天是这周的第几天
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 减去dayOfWeek,得到第一天的日期，因为Calendar用０－６代表一周七天，所以要减一
		calendar.add(Calendar.DAY_OF_WEEK, -(dayOfWeek - 1));
		firstDateOfWeek = calendar.getTime(); // 每周7天，加６，得到最后一天的日子
		return firstDateOfWeek;
	}

	/**
	 * 获取一周中的最后一天
	 *
	 * @param srcDate
	 * @param differdays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getLastDayOfWeek(Date srcDate, int differdays) {
		Date date = new Date(srcDate.getTime());
		date.setDate(date.getDate() + differdays);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date lastDateOfWeek; // 得到当天是这周的第几天
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 减去dayOfWeek,得到第一天的日期，因为Calendar用０－６代表一周七天，所以要减一
		calendar.add(Calendar.DAY_OF_WEEK, -(dayOfWeek - 1));
		calendar.add(Calendar.DAY_OF_WEEK, 6);
		lastDateOfWeek = calendar.getTime();
		return lastDateOfWeek;
	}

	/**
	 * 获取一年中的第一个月
	 *
	 * @param srcDate
	 * @param differmonths
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getFirstMonthOfYear(Date srcDate, int differmonths) {
		Date date = new Date(srcDate.getTime());
		date.setMonth(date.getMonth() + differmonths);
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		date.setDate(cDay1.getActualMinimum(Calendar.DAY_OF_MONTH));
		date.setMonth(cDay1.getActualMinimum(Calendar.MONTH));
		return getFirstSecondOfOneDay(date);
	}

	/**
	 * 获取一年中的最后一月
	 *
	 * @param srcDate
	 * @param differmonths
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getLastMonthOfYear(Date srcDate, int differmonths) {
		Date date = new Date(srcDate.getTime());
		date.setMonth(date.getMonth() + differmonths);
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		date.setDate(cDay1.getActualMaximum(Calendar.DAY_OF_MONTH));
		date.setMonth(cDay1.getActualMaximum(Calendar.MONTH));
		return getLastSecondOfOneDay(date);
	}

	/**
	 * 获取一天中的第一秒时间
	 *
	 * @param srcDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getFirstSecondOfOneDay(Date srcDate) {
		Date date = new Date(srcDate.getTime());
		Calendar c = Calendar.getInstance();
		date.setHours(c.getActualMinimum(Calendar.HOUR_OF_DAY));
		date.setMinutes(c.getActualMinimum(Calendar.MINUTE));
		date.setSeconds(c.getActualMinimum(Calendar.SECOND));
		return date;
	}

	/**
	 * 获取一天中最后一秒时间
	 *
	 * @param srcDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getLastSecondOfOneDay(Date srcDate) {
		Date date = new Date(srcDate.getTime());
		Calendar c = Calendar.getInstance();
		date.setHours(c.getActualMaximum(Calendar.HOUR_OF_DAY));
		date.setMinutes(c.getActualMaximum(Calendar.MINUTE));
		date.setSeconds(c.getActualMaximum(Calendar.SECOND));
		return date;
	}

	/**
	 * 获取指定时区时间
	 * @return
	 */
	public static Date newDate(String timeZone){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone),Locale.CHINESE);
        Calendar day = Calendar.getInstance();
        day.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        day.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        day.set(Calendar.DATE, cal.get(Calendar.DATE));
        day.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        day.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        day.set(Calendar.SECOND, cal.get(Calendar.SECOND));
        return day.getTime();
	}

	public static void main(String[] args) {
		//System.out.println(addDays(new Date(),-3));
		System.out.println(DateUtils.dateToString(new Date(), YYYY_MM_DD_HH_MM_SS));
		System.out.println(DateUtils.dateToString(new Date(), ZH_YYYY_MM_DD));
	}

	public static Date addDays(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	/**
	 * 将开始日转换为含时分秒的日期
	 * @param startDay 开始日(不含时分秒)
	 * @param format 转换格式
	 * @return 结果日期
	 */
	public static Date stringToStartDate(String startDay, String format){
		if (StringUtils.isNotBlank(startDay)) {
			startDay = startDay + START_HHMMSS;
			return DateUtils.stringToDate(startDay, format);
		}
		return null;
	}

	/**
	 * 将结束日转换为含时分秒的日期
	 * @param endDay 结束日(不含时分秒)
	 * @param format 转换格式
	 * @return 结果日期
	 */
	public static Date stringToEndDate(String endDay, String format){
		if (StringUtils.isNotBlank(endDay)) {
			endDay = endDay + END_HHMMSS;
			return DateUtils.stringToDate(endDay, format);
		}
		return null;
	}

	/**
	 * 获取某个时间指定前后某个月份的第一天
	 * @param differmonths 当前时间的前后某个月份间隔数  如1：下个月，-1上个月
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date time, int differmonths){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.MONTH, differmonths);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
		return calendar.getTime();
	}

	/**
	 * 获取某个时间指定前后某个月份的最后一天
	 * @param differmonths 当前时间的前后某个月份间隔数  如1：下个月，-1上个月
	 * @return
	 */
	public static Date getLastDateOfMonth(Date time, int differmonths){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.MONTH, differmonths);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
		return calendar.getTime();
	}

	/**
	 * 计算两个时间相隔的月数  结束时间-开始时间
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public static int getDifferMonths(Date startDate, Date endDate){
		int monthDiff = (endDate.getYear() - startDate.getYear()) * 12 + endDate.getMonth() - startDate.getMonth();
		return monthDiff;
	}

	/**
     * 获取当前日期是周几
     *
     * @param time 当前日期
     * @return 当前日期是周几
     */
    public static String getWeekOfDate(Date time) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

	/**
	 * 获取某个时间指定前后某个年份的第一天
	 * @param differyears 当前时间的前后某个年份间隔数  如1：下一年，-1上一年
	 * @return
	 */
	public static Date getFirstDateOfYear(Date time, int differyears){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.YEAR, differyears);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.getMinimum(Calendar.DATE));
		return calendar.getTime();
	}

	/**
	 * 获取某个时间指定前后某个年份的最后一天
	 * @param differyears 当前时间的前后某个年份间隔数  如1：下一年，-1上一年
	 * @return
	 */
	public static Date getLastDateOfYear(Date time, int differyears){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.YEAR, differyears);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		Date date = calendar.getTime();
		return getLastSecondOfOneDay(date);
	}

	/**
	 * 获取某个时间指定前后某个年份
	 * @param time 当前时间
	 * @param differyears 当前时间的前后某个年份间隔数
	 * @return
	 */
	public static Date getAddYear(Date time, int differyears){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.YEAR, differyears);
		Date date = calendar.getTime();
		return date;
	}
	
	
	/**
	 * 计算与当前时间相隔时间数(大于7天返回该日期，小于7天返回相隔的时间)
	 * @param stime
	 * @return
	 */
	public static String betweenTime(Date stime, String format){
		if (stime != null) {
			Date curr = new Date();
			Calendar beginDate = getCalendar(stime);
			Calendar endDate = getCalendar(curr);
			int day = betweenDays(beginDate, endDate);
			if(day > 7){
				if (StringUtils.isNotBlank(format)) {
					return DateUtils.dateToString(stime, format);
				} else {
					return DateUtils.dateToString(stime, YYYY_MM_DD_HH_MM_SS);
				}
			}else if (0 < day && day <= 7){
				String hour = DateUtils.dateToString(stime, HH_MM);
				return (day == 1 ? " 昨天 " : day == 2 ? " 前天 " : day + "天前") + hour;
			}else{
				long time = curr.getTime() - stime.getTime();
				long hour = (time / (60 * 60 * 1000) - day * 24);
				long min = ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
				long sec = (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
				return hour > 0 ? hour + "小时前" : min > 0 ? min + "分钟前" : sec + "秒前";
			}
		}
		return null;
	}




}