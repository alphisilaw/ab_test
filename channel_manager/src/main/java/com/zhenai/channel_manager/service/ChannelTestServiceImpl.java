package com.zhenai.channel_manager.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhenai.channel_manager.dao.AbtCaseDao;
import com.zhenai.channel_manager.dao.ChannelTestDao;
import com.zhenai.channel_manager.moudel.ChannelTest;

@Service("testService")
public class ChannelTestServiceImpl implements ChannelTestService {
	
	@Resource
	private ChannelTestDao channelTestDao;
	
	@Resource
	private AbtCaseDao abtCaseDao;

	@Override
	public ChannelTest findOrSetByChannelId(Integer channelId, Integer subChannelId) {
		ChannelTest channelTest = channelTestDao.findByChannelId(channelId, subChannelId);
		if (channelTest == null) {
			channelTest = new ChannelTest();
			channelTest.setChannelId(channelId);
			channelTest.setSubChannelId(subChannelId);
			channelTestDao.save(channelTest);
			abtCaseDao.saveInAbtCase(channelTest);
		}
		return channelTest;
	}
}
