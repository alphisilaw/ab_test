package com.zhenai.channel_manager.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zhenai.channel_manager.context.Config;
import com.zhenai.channel_manager.util.HttpsclientUtil;

@Service("channelCacheService")
public class ChannelCacheServiceImpl implements ChannelCacheService {
	
	private static final Logger logger = Logger.getLogger(ChannelCacheServiceImpl.class);

	@Override
	public boolean clearTestChannelCache(Integer channelId, Integer subChannelId) {
		try {
			StringBuilder url = new StringBuilder()
				.append(Config.getDisparchHost())
				.append(Config.getClearTestChannelCacheUrl())
				.append("?channelId=")
				.append(channelId);
			if (subChannelId != null) {
				url.append("&subChannelId=").append(subChannelId);
			}
			String text = HttpsclientUtil.get(url.toString());
			JSONObject json = JSONObject.parseObject(text);
			int result = json.getIntValue("result");
			if (result == 1) {
				return true;
			}
		} catch (Exception e) {
			logger.error("clearTestChannelCache error", e);
		}
		return false;
	}

	@Override
	public boolean clearSpecialListCache(Integer channelId) {
		try {
			String text = HttpsclientUtil.get(Config.getDisparchHost() 
					+ String.format(Config.getClearSpecialListCacheUrl(), channelId));
			JSONObject json = JSONObject.parseObject(text);
			int result = json.getIntValue("result");
			if (result == 1) {
				return true;
			}
		} catch (Exception e) {
			logger.error("clearSpecialListCache error", e);
		}
		return false;
	}

	@Override
	public boolean clearStrategyCache(Integer channelId, Integer subChannelId) {
		try {

			StringBuilder url = new StringBuilder()
				.append(Config.getDisparchHost())
				.append(Config.getClearStrategyCacheUrl())
				.append("?channelId=")
				.append(channelId);
			if (subChannelId != null) {
				url.append("&subChannelId=").append(subChannelId);
			}
			String text = HttpsclientUtil.get(url.toString());
			JSONObject json = JSONObject.parseObject(text);
			int result = json.getIntValue("result");
			if (result == 1) {
				return true;
			}
		} catch (Exception e) {
			logger.error("clearStrategyCache error", e);
		}
		return false;
	}

}
