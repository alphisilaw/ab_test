package com.zhenai.channel_dispatcher.controller;

import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhenai.channel_dispatcher.service.ChannelVersionService;
import com.zhenai.channel_dispatcher.util.CommonUtil;

@Controller
public class CacheController extends BaseController {

	private static final Logger logger = Logger.getLogger(CacheController.class);

	@Resource
	private ChannelVersionService versionService;

	@RequestMapping(value = "clearTestChannelCacheByUrl")
	@ResponseBody
	private String clearTestChannelCache(String url) {
		try {
			Integer channelId = null;
			Integer subChannelId = null;
			Map<String, String> param = CommonUtil.URLRequest(URLDecoder.decode(url, "utf-8"));
			String channelStr = param.get("channelId");
			String subChannelStr = param.get("subChannelId");
			if (NumberUtils.isNumber(channelStr)) {
				channelId = NumberUtils.toInt(channelStr);
			}
			if (NumberUtils.isNumber(subChannelStr)) {
				subChannelId = NumberUtils.toInt(subChannelStr);
			}
			if (channelId != null) {
				versionService.clearTestChannelCache(channelId, subChannelId);
			}
			return SUCCESS(null);
		} catch (Exception e) {
			logger.error("清空实验缓存出错!", e);
			return FAIL("清空实验缓存出错!");
		}
	}

	@RequestMapping(value = "clearTestChannelCacheById")
	@ResponseBody
	public String clearTestChannelCache(Integer channelId, Integer subChannelId) {
		try {
			if (channelId != null) {
				versionService.clearTestChannelCache(channelId, subChannelId);
			}
			return SUCCESS(null);
		} catch (Exception e) {
			logger.error("清空实验缓存出错!", e);
			return FAIL("清空实验缓存出错!");
		}
		
	}

	@RequestMapping(value = "clearSpecialListCache")
	@ResponseBody
	public String clearSpecialListCache(Integer channelId) {
		try {
			if (channelId != null) {
				versionService.clearSpecialListCache(channelId);
			}
			return SUCCESS(null);
		} catch (Exception e) {
			logger.error("清空特殊渠道列表缓存出错!", e);
			return FAIL("清空特殊渠道列表缓存出错!");
		}
	}

	@RequestMapping(value = "clearStrategyCache")
	@ResponseBody
	public String clearStrategyCache(Integer channelId, Integer subChannelId) {
		try {
			if (channelId != null) {
				boolean inSpeceialList = versionService.existsInSpecialList(channelId);
				if (inSpeceialList) {
					subChannelId = null;
				}
				versionService.clearStrategyCache(channelId, subChannelId);
			}
			return SUCCESS(null);
		} catch (Exception e) {
			logger.error("清空渠道分配策略缓存出错!", e);
			return FAIL("清空渠道分配策略缓存出错!");
		}
	}
	
}
