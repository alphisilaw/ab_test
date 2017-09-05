package com.zhenai.channel_manager.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhenai.channel_manager.dao.ChannelDao;
import com.zhenai.channel_manager.dao.ChannelVersionDao;
import com.zhenai.channel_manager.moudel.ChannelTest;
import com.zhenai.channel_manager.moudel.ChannelVersion;

@Service("versionService")
public class ChannelVersionServiceImpl implements ChannelVersionService {
	
	@Resource
	private ChannelTestService testService;
	
	@Resource
	private ChannelDao channelDao;
	
	@Resource
	private ChannelVersionDao channelVersionDao;
	
	@Resource
	private ChannelCacheService channelCacheService;

	@Override
	public void modifyVersion(List<ChannelVersion> versions, Integer channelId, Integer subChannelId) {

		ChannelTest channelTest = testService.findOrSetByChannelId(channelId, subChannelId);
		channelVersionDao.setStatus(channelTest.getTestId(), 0);
		Date now = new Date();
		for (ChannelVersion version : versions) {
			version.setTestId(channelTest.getTestId());
			version.setCreatetime(now);
			version.setStatus(1);
		}
		channelVersionDao.save(versions.toArray(new ChannelVersion[versions.size()]));
		channelCacheService.clearStrategyCache(channelId, subChannelId);
		channelCacheService.clearTestChannelCache(channelId, subChannelId);
		channelCacheService.clearSpecialListCache(channelId);
	}

	@Override
	public void batchModifyVersion(List<ChannelVersion> versions, Integer channelId) {
		List<Integer> subChannelIds = channelDao.getSubChannelIdsByChannelId(channelId);
		for (Integer subChannelId : subChannelIds) {
			modifyVersion(versions, channelId, subChannelId);
		}
	}
}
