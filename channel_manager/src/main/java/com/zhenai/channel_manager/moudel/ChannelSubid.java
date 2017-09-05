package com.zhenai.channel_manager.moudel;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity for table 'channel_zhenai.ChannelSubid*' generated by sborm
 * 
 * @author sborm
 * @date 2016-06-24 14:57:07
 */
public class ChannelSubid implements Serializable {

	public static final long serialVersionUID = 2297820568142934533L;

	/**
	 * 字段属性
	 */
	private Integer channelId;
	private Integer subid;
	private String name;
	private Date createTime;

	public ChannelSubid() {
	}
	public void setChannelId (Integer channelId) {
		this.channelId = channelId;
	}
	public Integer getChannelId () {
		return this.channelId;
	}
	public void setSubid (Integer subid) {
		this.subid = subid;
	}
	public Integer getSubid () {
		return this.subid;
	}
	public void setName (String name) {
		this.name = name;
	}
	public String getName () {
		return this.name;
	}
	public void setCreateTime (Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateTime () {
		return this.createTime;
	}
}