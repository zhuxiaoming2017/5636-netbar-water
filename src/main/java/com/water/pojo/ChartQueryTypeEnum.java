package com.water.pojo;

public enum ChartQueryTypeEnum {
	CUR_WEEK(1, "本周"),
	CUR_MONTH(2, "本月"),
	CUR_YEAR(3,"本年");

	int value;
	String msg;

	private ChartQueryTypeEnum(int value, String msg)
	{
		this.value = value;
		this.msg = msg;
	}
	public int getValue()
	{
		return this.value;
	}
	public String getMsg()
	{
		return this.msg;
	}
	
}
