package com.zhenai.channel_manager.controller.moudel;


public class ChannelParam {
	private Integer pageNum = 0;
	private Integer pageSize = 10;
	private String channelId;
	private String subChannelId; 
	private String channelName;
	private String staple1;
	private Integer tempId = -1;
	
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getSubChannelId() {
		return subChannelId;
	}
	public void setSubChannelId(String subChannelId) {
		this.subChannelId = subChannelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getStaple1() {
		return staple1;
	}
	public void setStaple1(String staple1) {
		this.staple1 = staple1;
	}
	public Integer getTempId() {
		return tempId;
	}
	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}
}
