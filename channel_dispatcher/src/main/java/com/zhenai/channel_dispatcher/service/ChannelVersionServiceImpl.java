package com.zhenai.channel_dispatcher.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zhenai.channel_dispatcher.dao.AbtCaseDao;
import com.zhenai.channel_dispatcher.dao.ChannelVersionDao;
import com.zhenai.channel_dispatcher.dao.SpecialChannelDao;
import com.zhenai.channel_dispatcher.moudel.ChannelVersion;

@Service("versionService")
public class ChannelVersionServiceImpl implements ChannelVersionService {

	private static final Logger logger = Logger.getLogger(ChannelVersionServiceImpl.class);
	
	@Resource
	private ChannelVersionDao channelVersionDao;
	
	@Resource
	private SpecialChannelDao specialChannelDao;
	
	@Resource
	private AbtCaseDao abtCaseDao;
	
	@Resource
	private LogService logService;
	
	@Resource
	private ChannelVersionService versionService;//aop需要通过接口调用方法
	
	@Override
	public int getTemplate(Integer channelId, Integer subid,
			String sessionId, Integer tempId, String remoteAddr) {
		int rtempId = -1;
		try {
			ChannelVersion version = versionService.findTempIdByChannelId(channelId, subid);
			if (tempId != null && tempId > 0) {
				rtempId = tempId;
			} else if (version != null) {
				rtempId = version.getTempId();
			} else {
				rtempId = -1;
			}
			if (rtempId > 0 && version != null) {
				Integer versionId = version.getVersionId();
				Integer testId = version.getTestId();
				logService.uploadLog(sessionId, versionId, testId, remoteAddr);
			}
		} catch (Exception e) {
			String error = new StringBuilder("method:getTemplate Exception,")
			.append("param:channelId=").append(channelId)
			.append("&subid=").append(subid)
			.append("&sessionId=").append(sessionId)
			.append("&tempId=").append(tempId)
			.toString();
			logger.error(error, e);
			rtempId = -1;
		}
		return rtempId;
	}
	
	@Override
	public ChannelVersion findTempIdByChannelId(Integer channelId, Integer subChannelId) {
		boolean inSpecialList = versionService.existsInSpecialList(channelId);
		if (inSpecialList) {
			subChannelId = null;
		}
		List<Integer> versionPool = versionService.findStrategyByChannelId(channelId, subChannelId);
		if (versionPool.isEmpty()) {
			return null;
		}
		Integer versionId = versionPool.get(RandomUtils.nextInt(0, versionPool.size()-1));
		if (versionId == null) {
			return null;
		}
		return versionService.findVersionById(versionId);
	}
	
	@Override
	@Cacheable(value="zhenaiChannel", key="'channelStrategy'+#channelId+'@@'+#subChannelId")
	public List<Integer> findStrategyByChannelId(Integer channelId, Integer subChannelId) {
		List<ChannelVersion> temps = channelVersionDao.findLstByChannelId(channelId, subChannelId);
		List<Integer> versionPool = new ArrayList<Integer>(100);
		for (ChannelVersion version : temps) {
			int per = (int) (version.getPer() * 100);
			for (int i = 0; i < per; i++) {
				versionPool.add(version.getVersionId());
			}
		}
		Collections.shuffle(versionPool);
		return versionPool;
	}
	
	@Override
	@Cacheable(value="zhenaiChannel", key="'channelVersion'+'@@'+#versionId")
	public ChannelVersion findVersionById(Integer versionId) {
		return channelVersionDao.findVersionById(versionId);
	}

	@Override
	@Cacheable(value="zhenaiChannel", key="'inSpecialList'+'@@'+#channelId")
	public boolean existsInSpecialList(Integer channelId) {
		return specialChannelDao.existsInSpecialList(channelId);
	}
	
	@Override
	@Cacheable(value="zhenaiChannel", key="'isTestChannel'+#channelId+'@@'+#subChannelId")
	public boolean isTestChannel(Integer channelId, Integer subChannelId) {
		return abtCaseDao.isTestChannel(channelId, subChannelId);
	}
	
	@Override
	@CacheEvict(value="zhenaiChannel", key="'channelStrategy'+#channelId+'@@'+#subChannelId")
	public void clearStrategyCache(Integer channelId, Integer subChannelId) {
	}

	
	@Override
	@CacheEvict(value="zhenaiChannel", key="'channelVersion'+'@@'+#versionId")
	public void clearVersionCache(Integer versionId) {
	}

	@Override
	@CacheEvict(value="zhenaiChannel", key="'inSpecialList'+'@@'+#channelId")
	public void clearSpecialListCache(Integer channelId) {
	}
	
	@Override
	@CacheEvict(value="zhenaiChannel", key="'isTestChannel'+#channelId+'@@'+#subChannelId")
	public void clearTestChannelCache(Integer channelId, Integer subChannelId) {
	}
}
