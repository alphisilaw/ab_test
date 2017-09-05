package com.zhenai.channel_dispatcher.moudel;

import java.io.Serializable;

public class ChannelTest implements Serializable {
	private static final long serialVersionUID = 2874130195528861005L;
	private Integer testId;
	private Integer channelId;
	private Integer subChannelId;
	public Integer getTestId() {
		return testId;
	}
	public void setTestId(Integer testId) {
		this.testId = testId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public Integer getSubChannelId() {
		return subChannelId;
	}
	public void setSubChannelId(Integer subChannelId) {
		this.subChannelId = subChannelId;
	}
	
}
