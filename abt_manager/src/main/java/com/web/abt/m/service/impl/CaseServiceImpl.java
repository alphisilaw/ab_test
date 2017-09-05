package com.web.abt.m.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.coola.jutil.data.DataPage;
import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.context.Config;
import com.web.abt.m.dao.UserProjectCaseDao;
import com.web.abt.m.dao.UserProjectCaseGoalDao;
import com.web.abt.m.dao.UserProjectCaseVersionDao;
import com.web.abt.m.model.UserProjectCaseGoalModel;
import com.web.abt.m.model.UserProjectCaseModel;
import com.web.abt.m.model.UserProjectCaseVersionModel;
import com.web.abt.m.model.UserProjectModel;
import com.web.abt.m.service.CaseService;
import com.web.abt.m.util.HttpToolkit;

/**
 */
public class CaseServiceImpl extends BaseServiceImpl implements CaseService {

	@Override
	public ResultBean doSearchAvailCasePageByProjectId(int projectId, int pageSize,
			int pageNum, String expriType, String orderBy, String url) {

		DataPage<UserProjectCaseModel> dataPage = UserProjectCaseDao.getInstance().getAvailCasePageByProjectId(projectId, pageSize, pageNum, expriType, orderBy, url);
		if(dataPage != null && dataPage.getRecord() != null){
			return Result(dataPage);
		}else{
			return Result(ResultConstant.EMPTY_IN_CASE_SEARCH);
		}
	}

	@Override
	public ResultBean doSearchDeleteCasePageByProjectId(int projectId,
			int pageSize, int pageNum) {

		DataPage<UserProjectCaseModel> dataPage = UserProjectCaseDao.getInstance().getDeleteCasePageByProjectId(projectId, pageSize, pageNum);
		if(dataPage != null && dataPage.getRecord() != null){
			return Result(dataPage);
		}else{
			return Result(ResultConstant.EMPTY_IN_CASE_SEARCH);
		}
	}

	@Override
	public ResultBean doSearchRunningCaseListByProjectId(int projectId) {

		List<UserProjectCaseModel> list = UserProjectCaseDao.getInstance().doSearchRunningListByProjectId(projectId);
		if(list != null){
			return Result(list);
		}else{
			return Result(ResultConstant.EMPTY_IN_CASE_SEARCH);
		}
	}

	@Override
	public ResultBean doAddCase(int projectId, String cName, String cUrl, int buizType, int isMobile) {

		UserProjectCaseModel model = new UserProjectCaseModel();
		model.setCaseName(cName);
		model.setCaseStatus(0);
		model.setCreateTime(new Date());
		model.setMaxGoalCount(10);
		model.setProjectId(projectId);
		model.setUpdateTime(new Date());
		model.setBuizType(buizType);
		model.setUrl(cUrl);
		model.setIsMobile(isMobile);
		UserProjectCaseModel caseModel = UserProjectCaseDao.getInstance().insert(model);
		if(caseModel != null)
			return Result(caseModel);
		else
			return Result(ResultConstant.EXCEPTION_IN_CASE_ADD);
	}

	@Override
	public ResultBean isCaseNameNotExisted(int projectId, String cName) {
		boolean result = UserProjectCaseDao.getInstance().isSameCaseNameExisted(projectId, cName);
		if(!result){
			return Result();
		}else{
			return Result(ResultConstant.CASE_NAME_EXISTED_ALREADY);
		}
	}

	@Override
	public ResultBean doUpdateCaseStatus(int caseId, int statusId) {

		UserProjectCaseModel caseModel = UserProjectCaseDao.getInstance().findByPrimaryKey(caseId);
		if(caseModel != null){
			boolean isSuccess = UserProjectCaseDao.getInstance().updateCaseStatus(caseId, statusId);
			if(isSuccess)
				return clearCache(caseModel);
			else
				return Result(ResultConstant.EXCEPTION_IN_CASE_STATUS_UPDATE);
		}else
			return Result(ResultConstant.EMPTY_IN_CASE_SEARCH);

	}
	@Override
	public ResultBean doUpdateCaseName(int caseId, String caseName) {

		UserProjectCaseModel caseModel = UserProjectCaseDao.getInstance().findByPrimaryKey(caseId);
		if(caseModel != null){
			boolean isSuccess = UserProjectCaseDao.getInstance().updateCaseName(caseId, caseName);
			if(isSuccess)
				return Result();
			else
				return Result(ResultConstant.EXCEPTION_IN_CASE_NAME_UPDATE);
		}else
			return Result(ResultConstant.EMPTY_IN_CASE_SEARCH);

	}

	@Override
	public ResultBean doRunCase(int caseId) {

		UserProjectCaseModel caseModel = UserProjectCaseDao.getInstance().findByPrimaryKey(caseId);
		if(caseModel != null){
			boolean isSameCaseUrlRunning = UserProjectCaseDao.getInstance().isSameCaseUrlRunning(caseModel.getUrl());
			if (!isSameCaseUrlRunning) {
				boolean isSuccess = UserProjectCaseDao.getInstance().runCase(caseId, caseModel.getStartRunTime());
				if(isSuccess) {
					return clearCache(caseModel);
				} else {
					return Result(ResultConstant.EXCEPTION_IN_CASE_STATUS_UPDATE);
				}
			} else
				return Result(ResultConstant.CASE_URL_EXISTED_ALREADY);
		}else
			return Result(ResultConstant.EMPTY_IN_CASE_SEARCH);

	}

	private ResultBean clearCache(UserProjectCaseModel caseModel) {
		ResultBean jsBean = clearJs();
		if (jsBean.isSuccess()) {
			ResultBean testBean = clearTestChannel(caseModel.getUrl());
			return testBean;
		} else {
			return jsBean;
		}
	}

	private ResultBean clearJs() {
		String url = Config.getLogHost() + Config.getClearTestCacheUrl();
		String result = HttpToolkit.doGet(url, null, "utf-8", true);
		JSONObject json = JSONObject.fromObject(result);
		int flag = json.optInt("result");
		if (flag == 1) {
			return Result();
		} else {
			return Result(ResultConstant.CLEAR_CACHE_EXCEPTION);
		}
	}

	private ResultBean clearTestChannel(String channelUrl) {
		try {
			String url = Config.getDispatchHost() + String.format(Config.getClearChannelCacheUrl(), URLEncoder.encode(channelUrl, "utf-8"));
			String result = HttpToolkit.doGet(url, null, "utf-8", true);
			JSONObject json = JSONObject.fromObject(result);
			int flag = json.optInt("result");
			if (flag == 1) {
				return Result();
			} else {
				return Result(ResultConstant.CLEAR_CACHE_EXCEPTION);
			}
		} catch (UnsupportedEncodingException e) {
			return Result(ResultConstant.URL_ENCODE_EXCEPTION);
		}
	}

	@Override
	public ResultBean doCopyCase(int caseId) {

		UserProjectCaseModel oldcase = UserProjectCaseDao.getInstance().findByPrimaryKey(caseId);
		if(oldcase != null){
			// copy case
			int newCopySN=UserProjectCaseDao.getInstance().getCopySN(caseId);
			UserProjectCaseModel newcase = new UserProjectCaseModel().copyModel(oldcase,newCopySN,caseId);
			newcase = UserProjectCaseDao.getInstance().insert(newcase);
			if(newcase.getCaseId() > 0){

				// copy case_version
				List<UserProjectCaseVersionModel> newversionList = new ArrayList<UserProjectCaseVersionModel>();  
				List<UserProjectCaseVersionModel> oldversionList = UserProjectCaseVersionDao.getInstance().doSearchListByCaseId(oldcase.getCaseId(), oldcase.getProjectId());
				if(oldversionList != null){
					for(UserProjectCaseVersionModel oldversion : oldversionList){
						UserProjectCaseVersionModel newversion = new UserProjectCaseVersionModel().copyModel(oldversion, newcase.getCaseId(), newcase.getProjectId());
						newversion = UserProjectCaseVersionDao.getInstance().insert(newversion);
						if(newversion.getVersionId() > 0){
							newversionList.add(newversion);
						}else{
							return Result(ResultConstant.EXCEPTION_IN_CASE_COPY);
						}
					}
				}else{
					return Result(ResultConstant.EXCEPTION_IN_CASE_COPY);
				}

				// copy case_goal
				List<UserProjectCaseGoalModel> oldgoalList = UserProjectCaseGoalDao.getInstance().doSearchListByCaseId(oldcase.getCaseId(), oldcase.getProjectId());
				if(oldgoalList != null){
					for(UserProjectCaseGoalModel oldgoal : oldgoalList){
						UserProjectCaseGoalModel newgoal = new UserProjectCaseGoalModel().copyModel(oldgoal, newcase.getCaseId(), newcase.getProjectId());
						newgoal = UserProjectCaseGoalDao.getInstance().insert(newgoal);
					}
				}else{
					return Result(ResultConstant.EXCEPTION_IN_CASE_COPY);
				}
			}else{
				return Result(ResultConstant.EXCEPTION_IN_CASE_COPY);
			}
		}else{
			return Result(ResultConstant.EXCEPTION_IN_CASE_COPY);
		}

		return Result();
	}

	@Override
	public ResultBean findCaseByCaseId(int caseId) {

		UserProjectCaseModel model = UserProjectCaseDao.getInstance().findByPrimaryKey(caseId);
		if(model != null){
			return Result(model);
		}else{
			return Result(ResultConstant.EMPTY_IN_CASE_SEARCH);
		}
	}

	@Override
	public ResultBean doSearchVersionByCaseId(int caseId, int projectId) {

		List<UserProjectCaseVersionModel> list = UserProjectCaseVersionDao.getInstance().doSearchListByCaseId(caseId, projectId);
		if(list != null){
			return Result(list);
		}else{
			return Result(ResultConstant.EMPTY_IN_VERSION_SEARCH);
		}
	}

	@Override
	public ResultBean doSearchCaseByCaseId(int caseId) {

		UserProjectCaseModel model = UserProjectCaseDao.getInstance().findByPrimaryKey(caseId);
		if(model != null){
			return Result(model);
		}else{
			return Result(ResultConstant.EMPTY_IN_CASE_SEARCH);
		}
	}

	@Override
	public ResultBean doAddCaseVersionJscode(String versionName, int caseId, int projectId,
			String jsCode, double percent, int versionType, String forwardUrl) {

		UserProjectCaseVersionModel model = new UserProjectCaseVersionModel();
		model.setCaseId(caseId);
		model.setCreateTime(new Date());
		model.setJsCode(jsCode);
		model.setProjectId(projectId);
		model.setUpdateTime(new Date());
		model.setVersionName(versionName);
		model.setVersionStatus(1);
		model.setPercent(percent);
		model.setVersionType(versionType);
		model.setForwardUrl(forwardUrl);
		UserProjectCaseVersionModel version = UserProjectCaseVersionDao.getInstance().insert(model);
		if(version != null){
			UserProjectCaseDao.getInstance().addVersionCount(caseId);
			return Result(version);
		}
		else
			return Result(ResultConstant.EXCEPTION_IN_VERSION_ADD);
	}

	@Override
	public ResultBean doUpdateCaseVersionJscode(int versionId, String jsCode, double percent,String versionName) {

		boolean isSuccess = UserProjectCaseVersionDao.getInstance().updateVersionJscode(versionId, jsCode, percent,versionName);
		if(isSuccess)
			return Result();
		else
			return Result(ResultConstant.EXCEPTION_IN_VERSION_UPDATE);
	}

	@Override
	public ResultBean doUpdateCaseVersionStatus(int versionId, int statusId) {

		boolean isSuccess = UserProjectCaseVersionDao.getInstance().updateVersionStatus(versionId, statusId);
		if(isSuccess)
			return Result();
		else
			return Result(ResultConstant.EXCEPTION_IN_VERSION_STATUS_UPDATE);
	}

	@Override
	public ResultBean doDeleteVersion(int versionId, int caseId) {

		boolean isSuccess = UserProjectCaseVersionDao.getInstance().deleteByPrimaryKey(versionId);
		if(isSuccess){
			UserProjectCaseDao.getInstance().minusVersionCount(caseId);
			return Result();
		}
		else
			return Result(ResultConstant.EXCEPTION_IN_VERSION_DELETING);
	}

	@Override
	public ResultBean doSearchAvailableVersionListByCaseId(int caseId, int projectId) {

		List<UserProjectCaseVersionModel> list = UserProjectCaseVersionDao.getInstance().doSearchAvailableListByCaseId(caseId, projectId);
		if(list != null){
			return Result(list);
		}else{
			return Result(ResultConstant.EMPTY_IN_VERSION_SEARCH);
		}
	}

	@Override
	public ResultBean findVersionByVersionId(int versionId) {

		UserProjectCaseVersionModel model = UserProjectCaseVersionDao.getInstance().findByPrimaryKey(versionId);
		if(model != null){
			return Result(model);
		}else{
			return Result(ResultConstant.EMPTY_IN_VERSION_SEARCH);
		}
	}

	@Override
	public ResultBean findAvailableVersionByVersionId(int versionId) {

		UserProjectCaseVersionModel model = UserProjectCaseVersionDao.getInstance().findByPrimaryKey(versionId);
		if(model != null){
			return Result(model);
		}else{
			return Result(ResultConstant.EMPTY_IN_VERSION_SEARCH);
		}
	}

	@Override
	public ResultBean getProjectByCaseId(int caseId){
		UserProjectModel model=UserProjectCaseDao.getInstance().getProjectByCaseId(caseId);
		if(model != null){
			return Result(model);
		}else{
			return Result(ResultConstant.EXCEPTION_IN_PROJECT_SEARCH);
		}
	}

	@Override
	public ResultBean isVersionNameNotExisted(int caseId,String versionName,int versionId) {

		boolean result = UserProjectCaseVersionDao.getInstance().isVersionNameExist(caseId, versionName, versionId);
		if( ! result){
			return Result();
		}else{
			return Result(ResultConstant.VERSION_NAME_EXISTED_ALREADY+"【重复版本名："+versionName+"】");
		}
	}

}
