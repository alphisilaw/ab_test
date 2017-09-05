package com.web.abt.service;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.web.abt.moudel.UserProjectCaseModel;
import com.web.abt.moudel.UserProjectCaseVersionModel;

@Service
public class CaseVersionDispartchServiceImpl implements CaseVersionDispartchService {

	@Override
	public UserProjectCaseVersionModel dispartch(
			Map<String, UserProjectCaseModel> cases,
			Map<String, Map<String, UserProjectCaseVersionModel>> caseVersions, 
			String urlReferrer) {
		
		UserProjectCaseVersionModel versionModel = null;
		
		Map<String, UserProjectCaseVersionModel> versionMap = getVersionMap(cases, caseVersions, urlReferrer);
		
		if (versionMap != null) {
			Collection<UserProjectCaseVersionModel> versions = versionMap.values();
			versionModel = selectVersionModel(versions);
		}
		return versionModel;
	}
	
	private UserProjectCaseVersionModel selectVersionModel(Collection<UserProjectCaseVersionModel> versions) {
		double ran = Math.random() * 100;
		double per = 0;
		for (UserProjectCaseVersionModel version : versions) {
			double percent = version.getPercent();
			if (ran > per && ran <= percent + per) {
				return version;
			} else {
				per += percent;
			}
		}
		return null;
	}

	private Map<String, UserProjectCaseVersionModel> getVersionMap(
			Map<String, UserProjectCaseModel> cases, 
			Map<String, Map<String, UserProjectCaseVersionModel>> caseVersions,
			String urlReferrer) {
		for (Entry<String, Map<String, UserProjectCaseVersionModel>> entry : caseVersions.entrySet()) {
			String caseId = entry.getKey();
			UserProjectCaseModel caseModel = cases.get(caseId);
			String url = caseModel.getUrl();
			if (url != null && urlReferrer.contains(url)) {
				return entry.getValue();
			}
		}
		return null;
	}

}
