package com.web.abt.m.service;

import com.web.abt.m.common.ResultBean;



/**
 */
public interface ProjectService {

	public ResultBean doSearchProjectPage(int pageSize, int pageNum);
	
	public ResultBean doSaveProjectByProjectId(int projectId, String pName, String pDesc);
	
	public ResultBean doAddProjectByUid(int uid, String pName, String pDesc);
	
	public ResultBean doDeleteProject(int projectId);
	
}