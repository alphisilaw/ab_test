package com.web.abt.service;

import java.util.Map;

public interface UserProjectService {

	Map<String, Object> getConfig(Integer projectId, String referer);

	void clearConfigCache();

}
