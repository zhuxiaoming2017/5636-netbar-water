package com.water.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {

	/**
	 * 将数字格式化为金额
	 * @param formatStr 金额格式
	 * @param money 要格式化的数字
	 * @return 格式化结果
	 */
	public static String formatMoney(String formatStr, double money) {
		DecimalFormat dft = new DecimalFormat(formatStr);
		return dft.format(money);
	}

	/**
	 * 获取一天24小时
	 * @return
	 */
	public static String[] getHourLine(){
		String[] hourLine = new String[24];
		for (int i = 0; i < 24; i++) {
			String hour = "";
			if (i < 10) {
				hour = "0";
			}
			hour += i + Constants.ZH_HOUR;
			hourLine[i] = hour;
		}
		return hourLine;
	}

	/**
	 * 获取日期(yyyy-MM-dd)
	 * @param days
	 * @param startDate
	 * @return
	 */
	public static String[] getDayLine(int days, Date startDate) {
		String[] dayLine = new String[days];
		for (int i = 1; i <= days; i++) {
			String day = DateUtils.dateToString(DateUtils.newDate(startDate, i), DateUtils.YYYY_MM_DD);
			dayLine[i-1] = day;
		}
		return dayLine;
	}
	/**
	 * 获取当前月的所有日
	 * @return
	 */
	public static String[] getCurDayOfMonth(){
		Calendar c = Calendar.getInstance();
		int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		String[] dataLine = new String[maxDay];
		for(int i = 1; i <= maxDay; i++){
			dataLine[i-1] = i < 10 ? "0"+i :String.valueOf(i);
		}
		return dataLine;
	}

	 /**
	  * 将数字转化为大写
	  * @param num
	  * @return
	  */
    public static String numToUpper(int num) {
        // String u[] = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        String u[] = { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + u[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }

    /**
	   * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	   *
	   * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	   * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	   *
	   * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
	   * 192.168.1.100
	   *
	   * 用户真实IP为： 192.168.1.110
	   *
	   * @param request
	   * @return
	   */
	  public static String getIpAddress(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getRemoteAddr();
	    }
	    return ip;
	  }

	  /**
	   * 字符串转型
	 * @param num
	 * @return
	 */
	public static Long stringConvertLong(String num){
		  try{
			  return Long.valueOf(num);
		  }catch(Exception e){
			  return new Long(0);
		  }
	  }

	/**
	 * 字符串转化int
	 * @param num
	 * @return
	 */
	public static Integer stringConvertInt(String num){
		try{
			return Integer.valueOf(num);
		}catch(Exception e){
			return new Integer(0);
		}
	}


	/**
	 * 转化double
	 * @param num
	 * @return
	 */
	public static Double stringConvertDouble(String num){
		try{
			return Double.valueOf(num);
		}catch(Exception e){
			return new Double(0);
		}
	}

	/**
	 * 通过Map 的 key 排序
	 * @param map
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, String> sortMap = new TreeMap<String, String>(
				new  Comparator<String>(){
					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
		 });
		sortMap.putAll(map);
		return sortMap;
	}

	public static String formatString(String text) {
		return (text == null) ? "" : text.trim();
	}

	/**
	 * 判断是否包含特殊字符
	 * @param string
	 * @return
	 */
	public static boolean isConSpeCharacters(String string){
		if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*","").length()==0 && !isContainChinese(string)){
			//不包含特殊字符
			return false;
		}

		return true;
	}

	/**
	 * 判断是否包含汉字
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static boolean isValidIp(String ip){
		if (StringUtils.isBlank(ip)) return false;

		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
}
