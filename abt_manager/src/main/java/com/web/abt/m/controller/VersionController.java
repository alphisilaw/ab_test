package com.web.abt.m.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.model.UserProjectCaseVersionModel;
import com.web.abt.m.service.ServiceFactory;
import com.web.abt.m.util.CommonUtil;

@Controller
public class VersionController extends BaseController {
	
	@RequestMapping(method = RequestMethod.GET, value = "doSearchVersionByCaseId")
	@ResponseBody
	public String doSearchVersionByCaseId(HttpServletRequest request, int caseId, int projectId){
		if(this.isUserLogin(request)){
			if(caseId <= 0){
				return FAIL(ResultConstant.INVALID_CASE_ID);
			}
			if(projectId <= 0){
				return FAIL(ResultConstant.INVALID_PROJECT_ID);
			}
			
			// 已配置的版本内容
			ResultBean bean = ServiceFactory.getCaseService().doSearchVersionByCaseId(caseId, projectId);
			if(bean.isSuccess()){
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				
				List<UserProjectCaseVersionModel> datas = (List<UserProjectCaseVersionModel>) bean.getBean();
				for(UserProjectCaseVersionModel model : datas){
					list.add(CommonUtil.getUserProjectCaseVersionMap(model));
				}
				
				JSONObject json = new JSONObject();
				json.put(this.RESULT, this.RESULT_SUCCESS);
				json.put("data", list);
				return json.toString();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doSaveVersion")
	@ResponseBody
	public String doSaveVersion(HttpServletRequest request, Integer versionId, String versionName,
			Integer caseId, Integer projectId, String jsCode, Double percent, Integer versionType,
			String forwardUrl){
		if(this.isUserLogin(request)){
			//体验账号
			int loginUid = this.getLoginUid(request);
			if( loginUid<100 ){
				return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
			}
			//
			if( (caseId==null) || (caseId<=0) ){
				return FAIL(ResultConstant.CASE_ID_EMPTY);
			}
			if( (versionId == null) || (versionId<=0) ){
				versionId=0;
			}
			ResultBean isVersionNameNotExistedBean = ServiceFactory.getCaseService().isVersionNameNotExisted(caseId, versionName, versionId);
			if( ! isVersionNameNotExistedBean.isSuccess() ){
				return FAIL(isVersionNameNotExistedBean.getErrCode());
			}
			if(versionId != null && versionId > 0){
				// 更改版本操作
				ResultBean bean = ServiceFactory.getCaseService().doUpdateCaseVersionJscode(versionId, jsCode, percent,versionName);
				if(bean.isSuccess()){
					return SUCCESS();
				}else{
					return FAIL(bean.getErrCode());
				}
			}else{
				// 新增版本操作
				ResultBean bean = ServiceFactory.getCaseService().doAddCaseVersionJscode(versionName, caseId, projectId, jsCode, percent, versionType, forwardUrl);
				if(bean.isSuccess()){
					return SUCCESS();
				}else{
					return FAIL(bean.getErrCode());
				}
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doPauseVersion")
	@ResponseBody
	public String doPauseVersion(HttpServletRequest request, int versionId){
		if(this.isUserLogin(request)){
			if(versionId <= 0){
				return FAIL(ResultConstant.INVALID_VERSION_ID);
			}
			
			ResultBean bean = ServiceFactory.getCaseService().doUpdateCaseVersionStatus(versionId, 0);
			if(bean.isSuccess()){
				return SUCCESS();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doCancelPauseVersion")
	@ResponseBody
	public String doCancelPauseVersion(HttpServletRequest request, int versionId){
		if(this.isUserLogin(request)){
			if(versionId <= 0){
				return FAIL(ResultConstant.INVALID_VERSION_ID);
			}
			
			ResultBean bean = ServiceFactory.getCaseService().doUpdateCaseVersionStatus(versionId, 1);
			if(bean.isSuccess()){
				return SUCCESS();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doDeleteVersion")
	@ResponseBody
	public String doDeleteVersion(HttpServletRequest request, int versionId, int caseId){
		if(this.isUserLogin(request)){
			//体验账号
			int loginUid = this.getLoginUid(request);
			if( loginUid<100 ){
				return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
			}
			//
			if(versionId <= 0){
				return FAIL(ResultConstant.INVALID_VERSION_ID);
			}
			
			ResultBean bean = ServiceFactory.getCaseService().doDeleteVersion(versionId, caseId);
			if(bean.isSuccess()){
				return SUCCESS();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
}
