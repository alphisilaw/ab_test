package com.web.abt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.web.abt.dao.UserProjectCaseDao;
import com.web.abt.dao.UserProjectCaseGoalDao;
import com.web.abt.dao.UserProjectCaseVersionDao;
import com.web.abt.moudel.UserProjectCaseGoalModel;
import com.web.abt.moudel.UserProjectCaseModel;
import com.web.abt.moudel.UserProjectCaseVersionModel;
import com.web.abt.redis.RedisCache;

@Service
public class UserProjectServiceImpl implements UserProjectService {
	
	@Resource
	private UserProjectCaseDao userProjectCaseDao;
	@Resource
	private UserProjectCaseGoalDao userProjectCaseGoalDao;
	@Resource
	private UserProjectCaseVersionDao userProjectCaseVersionDao;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;  
	
	@Override
	@Cacheable(value="caseConfig", key="'caseConfig'+#projectId+'@@'+#referer")
	public Map<String, Object> getConfig(Integer projectId, String referer) {
		Map<String, UserProjectCaseModel> caseMap = getCaseMap(projectId, referer);
		Map<String, Map<String, UserProjectCaseVersionModel>> caseVersionMap = getVersionMap(projectId, referer);
		Map<String, Map<String, UserProjectCaseGoalModel>> caseGoalMap = getGoalMap(projectId, referer);
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("cases", caseMap);
		config.put("versions", caseVersionMap);
		config.put("goals", caseGoalMap);
		return config;
	}

	@Override
	public void clearConfigCache() {
		redisTemplate.delete(RedisCache.HASH);
	}

	private Map<String, Map<String, UserProjectCaseGoalModel>> getGoalMap(Integer projectId, String referer) {
		List<UserProjectCaseGoalModel> goals = userProjectCaseGoalDao.findListByProjectId(projectId, referer);
		Map<String, Map<String, UserProjectCaseGoalModel>> caseGoalMap = new HashMap<String, Map<String,UserProjectCaseGoalModel>>();
		for (UserProjectCaseGoalModel userProjectCaseGoalModel : goals) {
			userProjectCaseGoalModel.setGoalSelector(userProjectCaseGoalModel.getGoalSelector().replaceAll("\\\"", "\\'"));
			String caseId = String.valueOf(userProjectCaseGoalModel.getCaseId());
			String goalId = String.valueOf(userProjectCaseGoalModel.getGoalId());
			Map<String, UserProjectCaseGoalModel> goalMap = null;
			if (!caseGoalMap.containsKey(caseId)) {
				goalMap = new HashMap<String, UserProjectCaseGoalModel>();
				caseGoalMap.put(caseId, goalMap);
			} else {
				goalMap = caseGoalMap.get(caseId);
			}
			goalMap.put(goalId, userProjectCaseGoalModel);
		}
		return caseGoalMap;
	}

	private Map<String, Map<String, UserProjectCaseVersionModel>> getVersionMap(
			Integer projectId, String referer) {
		List<UserProjectCaseVersionModel> versions = userProjectCaseVersionDao.findListByProjectId(projectId, referer);
		Map<String, Map<String, UserProjectCaseVersionModel>> caseVersionMap = new HashMap<String, Map<String, UserProjectCaseVersionModel>>();
		for (UserProjectCaseVersionModel userProjectCaseVersionModel : versions) {
			userProjectCaseVersionModel.setJsCode(userProjectCaseVersionModel.getJsCode().replaceAll("\\\"", "\\'"));
			String caseId = String.valueOf(userProjectCaseVersionModel.getCaseId());
			String versionId = String.valueOf(userProjectCaseVersionModel.getVersionId());
			Map<String, UserProjectCaseVersionModel> versionMap = null;
			if (!caseVersionMap.containsKey(caseId)) {
				versionMap = new HashMap<String, UserProjectCaseVersionModel>();
				caseVersionMap.put(caseId, versionMap);
			} else {
				versionMap = caseVersionMap.get(caseId);
			}
			versionMap.put(versionId, userProjectCaseVersionModel);
		}
		return caseVersionMap;
	}

	private Map<String, UserProjectCaseModel> getCaseMap(Integer projectId, String referer) {
		List<UserProjectCaseModel> cases = userProjectCaseDao.findList(projectId, referer);
		Map<String, UserProjectCaseModel> caseMap = new HashMap<String, UserProjectCaseModel>();
		for (UserProjectCaseModel userProjectCaseModel : cases) {
			caseMap.put(String.valueOf(userProjectCaseModel.getCaseId()), userProjectCaseModel);
		}
		return caseMap;
	}
}
