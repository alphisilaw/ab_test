package com.web.abt.service;

import java.util.Map;

import com.web.abt.moudel.UserProjectCaseModel;
import com.web.abt.moudel.UserProjectCaseVersionModel;

public interface CaseVersionDispartchService {
	
	UserProjectCaseVersionModel dispartch(
			Map<String, UserProjectCaseModel> cases, 
			Map<String, Map<String, UserProjectCaseVersionModel>> versions, 
			String urlReferrer);
	
}
