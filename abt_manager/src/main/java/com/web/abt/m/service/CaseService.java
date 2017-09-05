package com.web.abt.m.service;

import com.web.abt.m.common.ResultBean;



/**
 */
public interface CaseService {

	// case related
	
	public ResultBean doSearchAvailCasePageByProjectId(int projectId, int pageSize, int pageNum,String expriType,String orderBy, String url);
	
	public ResultBean doSearchDeleteCasePageByProjectId(int projectId, int pageSize, int pageNum);
	
	public ResultBean doSearchRunningCaseListByProjectId(int projectId);
	
	public ResultBean doAddCase(int projectId, String cName, String cUrl, int buizType, int isMobile);
	
	public ResultBean isCaseNameNotExisted(int projectId, String cName);
	
	public ResultBean getProjectByCaseId(int caseId);
	
	public ResultBean doUpdateCaseStatus(int caseId, int statusId);
	
	public ResultBean doRunCase(int caseId);
	
	public ResultBean doCopyCase(int caseId);
	
	public ResultBean findCaseByCaseId(int caseId);
	
	// version related
	
	public ResultBean doSearchAvailableVersionListByCaseId(int caseId, int projectId);
	
	public ResultBean doSearchVersionByCaseId(int caseId, int projectId);
	
	public ResultBean doSearchCaseByCaseId(int caseId);
	
	public ResultBean doAddCaseVersionJscode(String versionName, int caseId, 
			int projectId, String jsCode, double percent, int versionType,
			String forwardUrl);
	
	public ResultBean doUpdateCaseVersionJscode(int versionId, String jsCode, double percent,String versionName);
	
	public ResultBean doUpdateCaseVersionStatus(int versionId, int statusId);
	
	public ResultBean doDeleteVersion(int versionId, int caseId);
	
	public ResultBean findVersionByVersionId(int versionId);
	
	public ResultBean findAvailableVersionByVersionId(int versionId);
	
	public ResultBean doUpdateCaseName(int caseId,String caseName);
	
	public ResultBean isVersionNameNotExisted(int caseId,String versionName,int versionId);

}