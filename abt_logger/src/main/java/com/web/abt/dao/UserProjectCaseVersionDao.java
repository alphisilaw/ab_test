package com.web.abt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.web.abt.moudel.UserProjectCaseVersionModel;

@Repository
public class UserProjectCaseVersionDao extends BaseDao<UserProjectCaseVersionModel> {

	@Override
	protected String getKey() {
		return "versionId";
	}
	
	public List<UserProjectCaseVersionModel> findListByProjectId(Integer projectId, String referer) {
		String sql = "select cv.versionId,cv.versionType,cv.versionStatus,cv.caseId,cv.jsCode,cv.forwardUrl,cv.percent "
				+ "from user_project_case_version cv join user_project_case c on cv.caseId=c.caseId where cv.projectId=? and c.caseStatus=2 and find_in_set(c.url,?)";
		return mainNamedTemplate.getJdbcOperations().query(sql, rowMapper, projectId, referer);
	}

}
