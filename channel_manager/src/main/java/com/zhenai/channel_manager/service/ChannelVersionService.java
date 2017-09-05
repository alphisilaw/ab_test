package com.zhenai.channel_manager.service;

import java.util.List;

import com.zhenai.channel_manager.moudel.ChannelVersion;

public interface ChannelVersionService {

	void modifyVersion(List<ChannelVersion> versions, Integer channelId, Integer subChannelId);

	void batchModifyVersion(List<ChannelVersion> versions, Integer channelId);

}
