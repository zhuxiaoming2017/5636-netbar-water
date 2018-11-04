package com.water.modules.model;

import java.io.Serializable;


/**
 * AskQuestion
 * @author jiangdan
 */
public class AskQuestion implements Serializable {

	private static final long serialVersionUID = 1239392485394374036L;
	/**
     * 
     */
	private int id;
	/**
     * 标题
     */
	private String qtitle;	
	/**
     * 内容
     */
	private String qcontent;	
	/**
     * 悬赏分数,0为非悬赏问题
     */
	private int qreward;	
	/**
     * 是否关闭
     */
	private String qclose;	
	/**
     * 是否推荐
     */
	private String qcommend;	
	/**
     * 提问时间
     */
	private int qtime;

	/**
	 * 提问时间
	 */
	private String qtimeStr;
	/**
     * 结贴时间
     */
	private int qfinishtime;	
	/**
     * 最佳回答的评价
     */
	private String qestimate;	
	/**
     * 
     */
	private int cid;
	/**
     * 用户ID
     */
	private int uid;	
	/**
     * 问题审核状态，R:审核中，N：没有通过，Y：审核通过
     */
	private String qstatus;
	/**
	 * 是否解决：0、为解决，1、已解决
	 */
	private int isresolve;
	// 问题类型名
	private String cname;
	// 提问用户名
	private String uname;
	// 用户头像
	private String upicture;
	//是否是新：1-是， 0-否
	private int isnew;
    //回复数
	private int answerNum;
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public int getIsresolve() {
		return isresolve;
	}

	public void setIsresolve(int isresolve) {
		this.isresolve = isresolve;
	}

	public void setQfinishtime(int qfinishtime) {
		this.qfinishtime = qfinishtime;
	}
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	public String getQtitle() {
		return qtitle;
	}
	public void setQtitle(String qtitle) {
		this.qtitle = qtitle;
	}
	public String getQcontent() {
		return qcontent;
	}
	public void setQcontent(String qcontent) {
		this.qcontent = qcontent;
	}
	public int getQreward() {
		return qreward;
	}
	public void setQreward(int qreward) {
		this.qreward = qreward;
	}
	public String getQclose() {
		return qclose;
	}
	public void setQclose(String qclose) {
		this.qclose = qclose;
	}
	public String getQcommend() {
		return qcommend;
	}
	public void setQcommend(String qcommend) {
		this.qcommend = qcommend;
	}
	public int getQtime() {
		return qtime;
	}
	public void setQtime(int qtime) {
		this.qtime = qtime;
	}

	public String getQtimeStr() {
		return qtimeStr;
	}

	public void setQtimeStr(String qtimeStr) {
		this.qtimeStr = qtimeStr;
	}

	public int getQfinishtime() {
		return qfinishtime;
	}
	public void setQfinishTime(int qfinishtime) {
		this.qfinishtime = qfinishtime;
	}
	public String getQestimate() {
		return qestimate;
	}
	public void setQestimate(String qestimate) {
		this.qestimate = qestimate;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getQstatus() {
		return qstatus;
	}
	public void setQstatus(String qstatus) {
		this.qstatus = qstatus;
	}

	public String getUpicture() {
		return upicture;
	}

	public void setUpicture(String upicture) {
		this.upicture = upicture;
	}

	public int getIsnew() {
		return isnew;
	}

	public void setIsnew(int isnew) {
		this.isnew = isnew;
	}

	public int getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
	}
}