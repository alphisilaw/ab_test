package com.zhenai.channel_dispatcher.moudel;

import java.io.Serializable;


/**
 * Entity for table 'channel_zhenai.ChannelTemp*' generated by sborm
 * 
 * @author sborm
 * @date 2016-06-24 14:57:22
 */
public class ChannelVersion implements Serializable {

	public static final long serialVersionUID = 1900173572700224693L;

	/**
	 * 字段属性
	 */
	private Integer versionId;
	private Integer testId;
	private Double per;
	private Integer tempId;
	
	public Integer getVersionId() {
		return versionId;
	}
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	public Integer getTestId() {
		return testId;
	}
	public void setTestId(Integer testId) {
		this.testId = testId;
	}
	public void setPer (Double per) {
		this.per = per;
	}
	public Double getPer () {
		return this.per;
	}
	public void setTempId (Integer tempId) {
		this.tempId = tempId;
	}
	public Integer getTempId () {
		return this.tempId;
	}
}