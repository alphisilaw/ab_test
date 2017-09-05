package com.zhenai.channel_manager.service;

public interface ChannelCacheService {

	boolean clearTestChannelCache(Integer channelId, Integer subChannelId);

	boolean clearSpecialListCache(Integer channelId);

	boolean clearStrategyCache(Integer channelId, Integer subChannelId);

}
