package com.water.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CommonValidator {

	/**
	 * 检测邮箱地址是否合法
	 *
	 * @param email
	 * @return true合法 false不合法
	 */
	public static boolean checkEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		Pattern p = Pattern
				.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证手机号码
	 *
	 * @param mobile
	 * @return true合法 false不合法
	 */
	public static boolean checkMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[8-9])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 *
	 * @param cardNo
	 *            银行卡卡号
	 * @return true合法 false不合法
	 */
	public static boolean checkBankCardNo(String cardNo) {
		if (StringUtils.isEmpty(cardNo)) {
			return false;
		}
		String nonCheckCodeCardId = cardNo.substring(0, cardNo.length() - 1);
		if (nonCheckCodeCardId == null
				|| nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			// 如果传的不是数据返回false
			return false;
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		char bit = (luhmSum % 10 == 0) ? '0'
				: (char) ((10 - luhmSum % 10) + '0');
		return cardNo.charAt(cardNo.length() - 1) == bit;
	}

	/**
	 * 验证QQ号码
	 *
	 * @param qq
	 * @return true合法 false不合法
	 */
	public static boolean checkQQ(String qq) {
		if (StringUtils.isEmpty(qq)) {
			return false;
		}
		Pattern p = Pattern.compile("^[1-9][0-9]{4,18}$");
		Matcher m = p.matcher(qq);
		return m.matches();
	}

	/**
	 * 验证数字
	 * @param num
	 * @return
	 */
	public static boolean checkNumber(String num){
		if (StringUtils.isEmpty(num)) {
			return false;
		}
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(num);
		return m.matches();
	}

	/**
	 * 验证金额
	 * @param digit 位数
	 * @author zhangtao
	 * @date Aug 27, 2016 3:19:44 PM
	 */
	public static boolean isMoney(String str, int digit){
		Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,"+digit+"})?$");
		Matcher match = pattern.matcher(str);
		if(match.matches() == false){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 验证小数
	 * @param digit 位数
	 * @author zhangtao
	 * @date Aug 27, 2016 3:19:44 PM
	 */
	public static boolean isDecimal(String str, int digit){
		Pattern pattern = Pattern.compile("^([0]{1})(\\.(\\d){0,"+digit+"})?$");
		Matcher match = pattern.matcher(str);
		if(match.matches() == false){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 验证是否为数字，包含小数
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			Double.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 验证是否为数字或字母
	 * @param str 验证的字符
	 * @param start 最小位数
	 * @param end 最大位数
	 * @return
	 */
	public static boolean isNumOrLetter(String str, int start, int end) {
		String regex = "^[0-9A-Za-z]{" + start + "," + end +"}$";
		return match(regex, str);
	}

	/**
	 * 正则匹配
	 * @param regex 正则表达式
	 * @param str 验证的字符
	 * @return
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(str);
		return match.matches();
	}

	/**
	 * 验证是否为正整数
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		try {
			int val=Integer.valueOf(str);
			if (val > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
