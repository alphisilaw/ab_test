package com.web.abt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.web.abt.moudel.UserProjectCaseModel;

@Repository
public class UserProjectCaseDao extends BaseDao<UserProjectCaseModel> {

	@Override
	protected String getKey() {
		return "caseId";
	}

	public List<UserProjectCaseModel> findList(Integer projectId, String referer) {
		String sql = "select caseId,buizType,caseStatus,url from user_project_case where projectId=? and caseStatus=2 and find_in_set(url,?)";
		return mainNamedTemplate.getJdbcOperations().query(sql, rowMapper, projectId, referer);
	}

}
