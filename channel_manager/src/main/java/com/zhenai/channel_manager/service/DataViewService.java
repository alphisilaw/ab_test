package com.zhenai.channel_manager.service;

import java.util.Date;
import java.util.Map;

public interface DataViewService {

	Map<Integer, Map<String, Object>> getKPIData(Integer caseId, Date begin, Date end);

	Integer getUvByVersionId(Integer versionId);
}
