package com.web.abt.m.service.impl;

import java.util.Date;
import java.util.List;

import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.dao.UserProjectCaseDao;
import com.web.abt.m.dao.UserProjectCaseGoalDao;
import com.web.abt.m.model.UserProjectCaseGoalModel;
import com.web.abt.m.service.GoalService;

/**
 */
public class GoalServiceImpl extends BaseServiceImpl implements GoalService {

	@Override
	public ResultBean doSearchGoalByCaseId(int caseId, int projectId) {
		
		List<UserProjectCaseGoalModel> list = UserProjectCaseGoalDao.getInstance().doSearchListByCaseId(caseId, projectId);
		if(list != null){
			return Result(list);
		}else{
			return Result(ResultConstant.EMPTY_IN_GOAL_SEARCH);
		}
	}
	
	@Override
	public ResultBean doSearchGoalByCaseIdAndGoalBuizType(int caseId,
			int projectId, int goalBuizType) {
		
		List<UserProjectCaseGoalModel> list = UserProjectCaseGoalDao.getInstance().doSearchListByCaseIdAndBuizType(caseId, projectId, goalBuizType);
		if(list != null){
			return Result(list);
		}else{
			return Result(ResultConstant.EMPTY_IN_GOAL_SEARCH);
		}
	}

	@Override
	public ResultBean doAddGoal(String gName, int gType, String gSelector,
			int caseId, int projectId, int gBuizType, int isMaster) {
		
		UserProjectCaseGoalModel model = new UserProjectCaseGoalModel();
		model.setCaseId(caseId);
		model.setCreateTime(new Date());
		model.setGoalName(gName);
		model.setGoalSelector(gSelector);
		model.setGoalTypeId(gType);
		model.setProjectId(projectId);
		model.setUpdateTime(new Date());
		model.setGoalBuizType(gBuizType);
		model.setIsMaster(isMaster);
		UserProjectCaseGoalModel goal = UserProjectCaseGoalDao.getInstance().insert(model);
		if(goal != null){
			UserProjectCaseDao.getInstance().addCurGoalCount(caseId);
			return Result(goal);
		}
		else
			return Result(ResultConstant.EXCEPTION_IN_GOAL_ADD);
	}

	@Override
	public ResultBean doUpdateGoal(int goalId, String gName, int gType,
			String gSelector, int gBuizType, int isMaster) {
		
		boolean isSuccess = UserProjectCaseGoalDao.getInstance().updateGoal(goalId, gName, gType, gSelector, gBuizType, isMaster);
		if(isSuccess)
			return Result();
		else
			return Result(ResultConstant.EXCEPTION_IN_GOAL_UPDATE);
	}

	@Override
	public ResultBean doDeleteGoal(int goalId, int caseId) {
		
		boolean isSuccess = UserProjectCaseGoalDao.getInstance().deleteByPrimaryKey(goalId);
		if(isSuccess){
			UserProjectCaseDao.getInstance().minusCurGoalCount(caseId);
			return Result();
		}
		else
			return Result(ResultConstant.EXCEPTION_IN_GOAL_DELETE);
	}

	@Override
	public ResultBean isMasterGoalNotExist(int caseId) {
		
		boolean isNotExist = UserProjectCaseGoalDao.getInstance().isMasterGoalNotExistByCaseId(caseId);
		if(isNotExist){
			return Result();
		}else{
			return Result(ResultConstant.MASTER_GOAL_EXIST_ALREADY);
		}
	}

	@Override
	public ResultBean clearMasterGoal(int caseId) {
		
		boolean isSuccess = UserProjectCaseGoalDao.getInstance().updateMasterGoalStatusByCaseId(caseId, 0);
		if(isSuccess){
			return Result();
		}else{
			return Result(ResultConstant.EXCEPTION_IN_SET_MASTER_GOAL);
		}
	}

	@Override
	public ResultBean doUpdateMasterGoalStatus(int goalId, int masterStatus) {
		
		boolean result = UserProjectCaseGoalDao.getInstance().updateMasterGoalStatus(goalId, masterStatus);
		if(result){
			return Result();
		}else{
			return Result(ResultConstant.EXCEPTION_IN_SET_MASTER_GOAL);
		}
	}

	@Override
	public ResultBean isGoalNotExist(int gTypeId, int caseId, int gBuizType) {
		
		boolean isNotExist = UserProjectCaseGoalDao.getInstance().isGoalNotExistByCaseId(gTypeId, gBuizType, caseId);
		if(isNotExist){
			return Result();
		}else{
			return Result(ResultConstant.KPI_GOAL_EXIST_ALREADY);
		}
	}

	@Override
	public ResultBean isGoalNameExist(String gName, int caseId, int gBuizType) {
		
		boolean isNotExist = UserProjectCaseGoalDao.getInstance().isGoalNameNotExistByCaseId(gName, gBuizType, caseId);
		if(isNotExist){
			return Result();
		}else{
			return Result(ResultConstant.COM_GOAL_NAME_EXIST_ALREADY);
		}
	}
	
}
