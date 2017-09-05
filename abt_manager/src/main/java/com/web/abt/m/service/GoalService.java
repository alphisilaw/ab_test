package com.web.abt.m.service;

import com.web.abt.m.common.ResultBean;



/**
 */
public interface GoalService {

	public ResultBean doSearchGoalByCaseId(int caseId, int projectId);
	
	public ResultBean doSearchGoalByCaseIdAndGoalBuizType(int caseId,
			int projectId, int goalBuizType);
			
	public ResultBean doAddGoal(String gName, int gType, String gSelector, 
			int caseId, int projectId, int gBuizType, int isMaster);
	
	public ResultBean isGoalNotExist(int gTypeId, int caseId, int gBuizType);
	
	public ResultBean isGoalNameExist(String gName, int caseId, int gBuizType);
	
	public ResultBean doUpdateGoal(int goalId, String gName, int gType, 
			String gSelector, int gBuizType, int isMaster);
	
	public ResultBean doDeleteGoal(int goalId, int caseId);
	
	public ResultBean isMasterGoalNotExist(int caseId);
	
	public ResultBean clearMasterGoal(int caseId);
	
	public ResultBean doUpdateMasterGoalStatus(int goalId, int masterStatus);
	
}