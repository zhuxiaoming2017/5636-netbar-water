package com.water.pojo;

public enum LogtypeEnum {
	REGISTER(1, "注册登录"),
	NORMALLOGIN(2, "登录"),
	LOGOUT(3,"登出"),
	NEWNAV(4,"新手导航");

	int value;
	String msg;

	private LogtypeEnum(int value, String msg)
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
