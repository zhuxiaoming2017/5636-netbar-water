package com.water.common.utils;


public class Constants {

	/**
	 * 汉字  时
	 */
	public static final String ZH_HOUR = "时";


	public static final String MESSAGE = "msg";

	public static final String RESULT_STATUS = "status";

	public static final String RESULT_STATUS_SUCCESS = "success";

	public static final String RESULT_STATUS_WARNING = "warning";

	public static final String RESULT_STATUS_ERROR = "error";

	public static final String DATA = "data";

	public static final String ERROR_MSG = "提交失败，请检查信息填写是否完整!";
	public static final String PARAM_ERROR_MSG = "参数传递错误!";
	public static final String SYS_ERROR_MSG = "系统繁忙!";
	public static final String SUCCESS_MSG_ADD = "新增成功!";
	public static final String SUCCESS_MSG_UPDATE = "更新成功!";
	public static final String SUCCESS_MSG_UPLOAD = "上传成功!";
	public static final String SUCCESS_MSG_DELETE = "删除成功!";
	public static final String ERROR_MSG_LOGIN = "请登录!";

	public final static String CONTENT_CHARSET_UTF8 = "utf-8";

	/**
	 * UTF-8编码
	 */
	public static final String UTF8 = "UTF-8";
    /**
	 * 同步接口执行成功的返回编码标示
	 */
	public final static String SYNINTERFACE_RETURNCODE_SUCCESS = "10001";
	/**
	 * 
	 */
	public final static String FRONT_LOGIN_USER_BEAN_SESSION_KEY = "frontLoginUserBean";//前台登陆session
	public static final Integer PLATFORM_TYPE_H5 = 1;

	public static final Integer REGISTER_TYPE_PHONENUM = 1; //手机号注册
	public static final Integer REGISTER_TYPE_UNAME = 2; //账号注册

	public static final Integer MSG_CODE_VALID_TIME = 10*60; //验证码有效时长 10min

	/**
	 * 收入数据显示时间
	 */
	public final static String SHOW_DATA_TIME = " 13:15:00";

	// 报表
	public static final String YESTERDAY = "y";
	public static final String THIS_WEEK = "tw";
	public static final String LAST_WEEK = "lw";
	public static final String THIS_MONTH = "tm";
	public static final String LAST_MONTH = "lm";
	public static final String ALL = "all";

	public static final String STRING_ZERO_DECIMALS = "0.000";
	public static final String STRING_ZERO_NUMBER = "0";
	public static final String TIME_SPLIT_STR = " 至 ";

	public static final String STRING_FORMAT_NUMBER = "###,##0";
	public static final String STRING_FORMAT_MONEY = "￥###,##0.000";
	
	public static final Integer CLOUD_ISSHOW_YES = 1;
	
    public static final Integer CLOUD_ISSHOW_NO = 0;
    
    public static final Integer CLOUD_AMOUNT_DEFAULT = 100;
    
    public static final Integer CLOUD_MONTH_DEFAULT = 12;
    
    
    /**
	 * 用户信息 前缀 redis
	 */
	public static final String USER_KEY = "uk_bbs_";


}