package com.zhenai.channel_manager.service;

import com.zhenai.channel_manager.moudel.ChannelTest;

public interface ChannelTestService {

	ChannelTest findOrSetByChannelId(Integer channelId, Integer subChannelId);

}
