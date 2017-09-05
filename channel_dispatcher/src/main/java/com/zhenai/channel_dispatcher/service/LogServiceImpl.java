package com.zhenai.channel_dispatcher.service;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zhenai.channel_dispatcher.context.Config;
import com.zhenai.clogger.client.OpEventBuilder;
import com.zhenai.clogger.client.entity.OpEvent;

@Service("logService")
public class LogServiceImpl implements LogService {

	private static final Logger logger = Logger.getLogger(LogServiceImpl.class);

	@Override
	public void uploadLog(String sessionId, 
			Integer versionId, 
			Integer testId,
			String remoteAddr) {
		try {
			String now = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");

			OpEvent e = OpEventBuilder.create(Config.getUploadChannel());
			e.setOpIp(remoteAddr);
			e.set("userid", -1)
			.set("sessionid", sessionId)
			.set("testversionid", versionId)
			.set("testid", testId)
			.set("guid", -1)
			.set("clickid", 0)
			.set("testsessiondt", now)
			.set("insertdt", now)
			.set("sdevice", " ")
			.set("sbrowser", " ")
			.set("smobileos", " ");
			OpEventBuilder.cache(e);
		} catch (Exception e) {
			logger.error("uploadLog fail!", e);
		}

	}
}
