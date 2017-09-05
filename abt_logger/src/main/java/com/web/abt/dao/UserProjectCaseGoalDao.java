package com.web.abt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.web.abt.moudel.UserProjectCaseGoalModel;

@Repository
public class UserProjectCaseGoalDao extends BaseDao<UserProjectCaseGoalModel> {

	@Override
	protected String getKey() {
		return "goalId";
	}
	
	public List<UserProjectCaseGoalModel> findListByProjectId(Integer projectId, String referer) {
		String sql = "select cg.goalId,cg.caseId,cg.goalSelector,cg.isMaster,cg.goalTypeId "
				+ "from user_project_case_goal cg join user_project_case c on cg.caseId=c.caseId "
				+ "where cg.projectId=? and c.caseStatus=2 and cg.goalBuizType=1 and find_in_set(c.url,?)";
		return mainNamedTemplate.getJdbcOperations().query(sql, rowMapper, projectId, referer);
	}

}
