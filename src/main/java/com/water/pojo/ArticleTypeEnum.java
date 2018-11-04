package com.water.pojo;

public enum ArticleTypeEnum {
	NOTICE(1, "联盟公告"),
	QUESTION(3, "常见问题"),
	NEWS(2,"行业资讯");
	
	int value;
	String msg;
  
	private ArticleTypeEnum(int value, String msg)
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
