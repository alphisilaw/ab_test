package com.zhenai.channel_dispatcher.service;

import java.util.List;

import com.zhenai.channel_dispatcher.moudel.ChannelVersion;

public interface ChannelVersionService {
	
	ChannelVersion findTempIdByChannelId(Integer channelId, Integer subChannelId);

	List<Integer> findStrategyByChannelId(Integer channelId, Integer subChannelId);

	ChannelVersion findVersionById(Integer versionId);

	boolean existsInSpecialList(Integer channelId);

	boolean isTestChannel(Integer channelId, Integer subChannelId);

	void clearStrategyCache(Integer channelId, Integer subChannelId);

	void clearVersionCache(Integer versionId);

	void clearSpecialListCache(Integer channelId);

	void clearTestChannelCache(Integer channelId, Integer subChannelId);

	int getTemplate(Integer channelId, Integer subid, String sessionId,
			Integer tempId, String sremoteAddr);

}
