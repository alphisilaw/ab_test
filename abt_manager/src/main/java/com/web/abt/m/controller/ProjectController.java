package com.web.abt.m.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coola.jutil.data.DataPage;
import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.dao.UserProjectDao;
import com.web.abt.m.model.UserProjectModel;
import com.web.abt.m.service.ServiceFactory;
import com.web.abt.m.util.CommonUtil;

@Controller
public class ProjectController extends BaseController {
	
	@RequestMapping(method = RequestMethod.GET, value = "doSearchProjectsByUid")
	@ResponseBody
	public String doSearchProjectsByUid(HttpServletRequest request, int pageSize, int pageNum){
		if(this.isUserLogin(request)){
			int loginUid = this.getLoginUid(request);
			ResultBean bean = ServiceFactory.getProjectService().doSearchProjectPage(pageSize, pageNum);
			if(bean.isSuccess()){
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				
				DataPage<UserProjectModel> dataPage = (DataPage<UserProjectModel>) bean.getBean();
				for(UserProjectModel model : dataPage.getRecord()){
					list.add(CommonUtil.getUserProjectMap(model));
				}
				
				JSONObject json = new JSONObject();
				json.put(this.RESULT, this.RESULT_SUCCESS);
				json.put("data", list);
				json.put("pageNum", pageNum);
				json.put("pageTotal", dataPage.getPageCount());
				json.put("dataTotal", dataPage.getRecordCount());
				return json.toString();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doAddProjectsByUid")
	@ResponseBody
	public String doAddProjectsByUid(HttpServletRequest request, String pName, String pDesc){
		if(this.isUserLogin(request)){
			if(StringUtils.isBlank(pName)){
				return FAIL(ResultConstant.PROJECT_NAME_EMPTY);
			}
			
			int loginUid = this.getLoginUid(request);
			
			//体验账号
			if( loginUid<100 ){
				return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
			}
			//
			
			ResultBean bean = ServiceFactory.getProjectService().doAddProjectByUid(loginUid, pName, pDesc);
			if(bean.isSuccess()){
				return SUCCESS();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doEditProjectsByProjectId")
	@ResponseBody
	public String doEditProjectsByProjectId(HttpServletRequest request, Integer pid, String pName, String pDesc){
		if(this.isUserLogin(request)){
			
			//体验账号
			int loginUid = this.getLoginUid(request);
			if( loginUid<100 ){
				return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
			}
			//
			
			if(pid == null || pid <= 0){
				return FAIL(ResultConstant.INVALID_PROJECT_ID);
			}
			if(StringUtils.isBlank(pName)){
				return FAIL(ResultConstant.PROJECT_NAME_EMPTY);
			}
			
			ResultBean bean = ServiceFactory.getProjectService().doSaveProjectByProjectId(pid, pName, pDesc);
			if(bean.isSuccess()){
				return SUCCESS();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doDeleteProject")
	@ResponseBody
	public String doDeleteProject(HttpServletRequest request, int pid){
		if(this.isUserLogin(request)){
			
			//体验账号
			int loginUid = this.getLoginUid(request);
			if( loginUid<100 ){
				return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
			}
			//
			
			if(pid <= 0){
				return FAIL(ResultConstant.INVALID_PROJECT_ID);
			}
			
			ResultBean bean = ServiceFactory.getProjectService().doDeleteProject(pid);
			if(bean.isSuccess()){
				return SUCCESS();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	@RequestMapping(method = RequestMethod.POST, value = "isEmptyProject")
	@ResponseBody
	public String isEmptyProject(HttpServletRequest request,int pid){
		if(this.isUserLogin(request)){
			return UserProjectDao.getInstance().isEmpty(pid)?"1":"0";
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
}
