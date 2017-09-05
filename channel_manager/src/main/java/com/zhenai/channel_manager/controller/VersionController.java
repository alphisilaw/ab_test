package com.zhenai.channel_manager.controller;

import java.net.URLDecoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhenai.channel_manager.moudel.ChannelVersion;
import com.zhenai.channel_manager.service.ChannelVersionService;
import com.zhenai.channel_manager.service.DataViewService;
import com.zhenai.channel_manager.util.CommonUtil;
import com.zhenai.channel_manager.util.CookieUtils;

@Controller
public class VersionController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(VersionController.class);

	@Resource
	private ChannelVersionService versionService;
	
	@Resource
	private DataViewService dataViewService;

	@RequestMapping(value = "modifyVersion")
	@ResponseBody
	private String modifyVersion(@RequestBody String body, HttpServletRequest request) {
		try {
			JSONObject json = JSONObject.parseObject(URLDecoder.decode(body, "utf8"));
			List<ChannelVersion> versions = JSONArray.parseArray(json.getString("versions"), ChannelVersion.class);
			Integer channelId = json.getInteger("channelId");
			String subChannelStr = json.getString("subid");
			boolean batchModify = json.getBooleanValue("batchModify");
			Integer subChannelId = null;
			if (NumberUtils.isNumber(subChannelStr)) {
				subChannelId = NumberUtils.toInt(subChannelStr);
			}
			if (versions.size() == 0) {
				return FAIL("没有模板，无法保存!");
			}
			if (versions.size() >= 6) {
				return FAIL("模板数不能大于六!");
			}
			double total = 0d;
			Set<Integer> set = new HashSet<Integer>();
			for (ChannelVersion version : versions) {
				if (version.getPer() <= 0d ) {
					return FAIL("分配数要大于0!");
				}
				total += version.getPer();
				if (CommonUtil.evalInt(version.getTempId()) < 0) {
					return FAIL("请选择模板!");
				}
				set.add(version.getTempId());
				
				version.setCreateUser(CookieUtils.getCookie(request, LOGIN_NAME));
			}
			if (set.size() < versions.size()) {
				return FAIL("不能选择相同模板!");
			}
			if (total != 1d) {
				return FAIL("分配之和不等于1!");
			}
			if (batchModify) {
				versionService.batchModifyVersion(versions, channelId);
			} else {
				versionService.modifyVersion(versions, channelId, subChannelId);
			}
			return SUCCESS(null);
		} catch (Exception e) {
			LOGGER.error("获取出错!", e);
			return FAIL("获取出错!");
		}
	}

	@RequestMapping(value = "getVersionUv")
	@ResponseBody
	public String getUvByVersionId(Integer versionId) {
		try {
			Integer uv = dataViewService.getUvByVersionId(versionId);
			return SUCCESS(uv);
		} catch (Exception e) {
			LOGGER.error("获取出错!", e);
			return FAIL("获取出错!");
		}
	}
}
