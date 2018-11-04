package com.water.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 * @author xiaoxiao
 *
 */
@SuppressWarnings("serial")
public  class PageInfo<T> implements Serializable {

	public final static String PAGE = "pageInfo";
	public final static String PAGENO = "pageNo";
	public final static String PAGESIZE = "pageSize";
	public final static int PAGE_SIZE =10;
	private int pageSize=10;//每页显示记录数
	private int firstResult=0;//当页第一条记录号
	private int totalCount;//总记录数
	private int totalPage;//总页码
	private int pageNo=0;//当前页码
	private List<?> sumData;//此集合可用来保存 合计数据
	private List<T> data;//查询结果
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getFirstResult() {
		firstResult=pageSize * (pageNo -1);
		return firstResult;
	}
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		totalPage = this.totalCount/pageSize;
		if (totalPage == 0 || totalCount % pageSize != 0) {
			totalPage++;
		}
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public List<?> getData() {
		return data;
	}
	
	public void setData(List<T> data) {
		this.data = data;
	}
	
	/**
	 * 是否第一页
	 */
	public boolean isFirstPage() {
		return pageNo <= 1;
	}
	
	/**
	 * 是否最后一页
	 */
	public boolean isLastPage() {
		return pageNo >= getTotalPage();
	}
	
	/**
	 * 下一页页码
	 */
	public int getNextPage() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}

	/**
	 * 上一页页码
	 */
	public int getPrePage() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}
	
	public PageInfo(){}
	
	public PageInfo(int pageNo){
		this.pageNo=pageNo;
	}
	
	public PageInfo(int pageNo, int pageSize){
		this.pageNo=pageNo;
		this.pageSize = pageSize;
	}
	
	public List<?> getSumData() {
		return sumData;
	}
	public void setSumData(List<?> sumData) {
		this.sumData = sumData;
	}
	
}
