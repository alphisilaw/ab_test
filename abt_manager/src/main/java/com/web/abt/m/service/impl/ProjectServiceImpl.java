package com.web.abt.m.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.coola.jutil.data.DataPage;
import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.dao.UserProjectDao;
import com.web.abt.m.model.UserProjectModel;
import com.web.abt.m.service.ProjectService;

/**
 */
public class ProjectServiceImpl extends BaseServiceImpl implements ProjectService {

	@Override
	public ResultBean doSearchProjectPage(int pageSize, int pageNum) {
		
		DataPage<UserProjectModel> dataPage = UserProjectDao.getInstance().getUserProjectPage(pageSize, pageNum);
		if(dataPage != null && dataPage.getRecord() != null){
			return Result(dataPage);
		}else{
			return Result(ResultConstant.EMPTY_IN_PROJECT_SEARCH);
		}
	}

	@Override
	public ResultBean doSaveProjectByProjectId(int projectId, String pName,
			String pDesc) {
		
		if(StringUtils.isBlank(pDesc))
			pDesc = "";
		boolean isSuccess = UserProjectDao.getInstance().updateProjectByProjectID(projectId, pName, pDesc);
		if(isSuccess)
			return Result();
		else
			return Result(ResultConstant.EXCEPTION_IN_PROJECT_EDIT);
	}

	@Override
	public ResultBean doAddProjectByUid(int uid, String pName, String pDesc) {
		
		UserProjectModel model = new UserProjectModel();
//		model.setCodeFile();
		model.setDescription(pDesc);
		model.setPlatform("web");
		model.setProjectName(pName);
		model.setOwnerId(uid);
		model.setUpdateTime(new Date());
		model.setCreateTime(new Date());
		UserProjectModel user = UserProjectDao.getInstance().insert(model);
		if(user != null)
			return Result(user);
		else
			return Result(ResultConstant.EXCEPTION_IN_PROJECT_ADD);
	}

	@Override
	public ResultBean doDeleteProject(int projectId) {
		
		
		/*UserProjectCaseDao.getInstance().deleteByProjectId(projectId);
		UserProjectCaseGoalDao.getInstance().deleteByProjectId(projectId);
		UserProjectCaseGoalStatisticDao.getInstance().deleteByProjectId(projectId);
		UserProjectCaseVersionDao.getInstance().deleteByProjectId(projectId);
		UserProjectStatisticDao.getInstance().deleteByProjectId(projectId);*/
		if( ! UserProjectDao.getInstance().isEmpty(projectId) ){
			return Result(ResultConstant.EXCEPTION_DEL_NOEMPTY_PROJECT); 
		}
		boolean isSuccess = UserProjectDao.getInstance().deleteByPrimaryKey(projectId);
		if(isSuccess)
			return Result();
		else
			return Result(ResultConstant.EXCEPTION_IN_PROJECT_DEL);
	}

}
