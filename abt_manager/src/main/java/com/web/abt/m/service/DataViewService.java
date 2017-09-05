package com.web.abt.m.service;

import java.util.Date;
import java.util.Map;

public interface DataViewService {

	Map<Integer, Map<String, Object>> getKPIData(Integer caseId, Date begin, Date end);
}
