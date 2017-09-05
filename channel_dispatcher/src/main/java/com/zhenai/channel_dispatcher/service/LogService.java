package com.zhenai.channel_dispatcher.service;


public interface LogService {

	void uploadLog(String sessionId, Integer versionId, Integer testId, String remoteAddr);

}
