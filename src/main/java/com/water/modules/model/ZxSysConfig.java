package com.water.modules.model;

import java.io.Serializable;
import java.util.Date;

/**
 * ZxSysConfigPo Po
 * @author system
 */
public class ZxSysConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7113550972904440749L;
	/**
	 * 主键ID
	 */
	private Integer id;	
	/**
     * 所属系统标识(com表示所有系统共用)
     */
	private String code;	
	/**
     * 参数名
     */
	private String attrkey;	
	/**
     * 参数值
     */
	private String attrvalue;	
	/**
     * 描述
     */
	private String descs;	
	/**
     * 创建人
     */
	private Integer creater;	
	/**
     * 修改人
     */
	private Integer updater;	
	/**
     * 创建时间
     */
	private Date createtime;	
	/**
     * 修改时间
     */
	private Date updatetime;	
    
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public String getCode(){
		return code;
	}
	public void setCode(String code){
		this.code = code;
	}
	public String getAttrkey(){
		return attrkey;
	}
	public void setAttrkey(String attrkey){
		this.attrkey = attrkey;
	}
	public String getAttrvalue(){
		return attrvalue;
	}
	public void setAttrvalue(String attrvalue){
		this.attrvalue = attrvalue;
	}
	public String getDescs(){
		return descs;
	}
	public void setDescs(String descs){
		this.descs = descs;
	}
	public Integer getCreater(){
		return creater;
	}
	public void setCreater(Integer creater){
		this.creater = creater;
	}
	public Integer getUpdater(){
		return updater;
	}
	public void setUpdater(Integer updater){
		this.updater = updater;
	}
	public Date getCreatetime(){
		return createtime;
	}
	public void setCreatetime(Date createtime){
		this.createtime = createtime;
	}
	public Date getUpdatetime(){
		return updatetime;
	}
	public void setUpdatetime(Date updatetime){
		this.updatetime = updatetime;
	}
}