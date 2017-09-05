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

import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.model.UserProjectCaseGoalModel;
import com.web.abt.m.model.UserProjectCaseModel;
import com.web.abt.m.service.ServiceFactory;
import com.web.abt.m.util.CommonUtil;

@Controller
public class GoalController extends BaseController {
	
	@RequestMapping(method = RequestMethod.GET, value = "doSearchGoalByCaseId")
	@ResponseBody
	public String doSearchGoalByCaseId(HttpServletRequest request, 
			int caseId, int projectId){
		// 未登陆状态下也可以调用本接口
		if(caseId <= 0){
			return FAIL(ResultConstant.INVALID_CASE_ID);
		}
		if(projectId <= 0){
			return FAIL(ResultConstant.INVALID_PROJECT_ID);
		}

		JSONObject json = new JSONObject();
		
		// 页面类目标
		ResultBean nGoalBbean = ServiceFactory.getGoalService().doSearchGoalByCaseIdAndGoalBuizType(caseId, projectId, 1);
		if(nGoalBbean.isSuccess()){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			List<UserProjectCaseGoalModel> datas = (List<UserProjectCaseGoalModel>) nGoalBbean.getBean();
			for(UserProjectCaseGoalModel model : datas){
				list.add(CommonUtil.getUserProjectCaseGoalMap(model));
			}
			
			json.put("normalGoalData", list);
		}
		
		// kpi类目标
		ResultBean kpiGoalBean = ServiceFactory.getGoalService().doSearchGoalByCaseIdAndGoalBuizType(caseId, projectId, 2);
		if(kpiGoalBean.isSuccess()){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			List<UserProjectCaseGoalModel> datas = (List<UserProjectCaseGoalModel>) kpiGoalBean.getBean();
			for(UserProjectCaseGoalModel model : datas){
				list.add(CommonUtil.getUserProjectCaseGoalMap(model));
			}
			
			json.put("kpiGoalData", list);
		}
		
		json.put(this.RESULT, this.RESULT_SUCCESS);
		return json.toString();
		
	
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doSaveGoal")
	@ResponseBody
	public String doSaveGoal(HttpServletRequest request, Integer goalId, String gName, 
			Integer gTypeId, String gSelector, Integer caseId, Integer projectId,
			Integer gBuizType,Integer isMaster){
		if(this.isUserLogin(request)){
			//体验账号
			int loginUid = this.getLoginUid(request);
			if( loginUid<100 ){
				return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
			}
			//
			if(StringUtils.isNotBlank(gName))
				gName = gName.trim();
			if( isMaster == null ){
				isMaster = 0;
			}
			if(goalId != null && goalId > 0){
				// 清除已经设置的主追踪目标 
				if( isMaster == 1 ){
					ServiceFactory.getGoalService().clearMasterGoal(caseId);
				}
				// 更改goal操作
				ResultBean bean = ServiceFactory.getGoalService().doUpdateGoal(goalId, gName, gTypeId, 
						gSelector, gBuizType, isMaster);
				if(bean.isSuccess()){
					return SUCCESS();
				}else{
					return FAIL(bean.getErrCode());
				}
			}else{
				// 验证目标是否已满
				ResultBean casebean = ServiceFactory.getCaseService().findCaseByCaseId(caseId);
				if(casebean.isSuccess()){
					UserProjectCaseModel caseModel = (UserProjectCaseModel) casebean.getBean();
					if(caseModel.getCurGoalCount() >= 10){
						return FAIL(ResultConstant.CURR_GOAL_IS_FULL);
					}
				}
				// 新增goal操作
				// 验证是否KPI目标并且不能重复
				boolean canDo = true;
				if(gBuizType == 2){
					ResultBean goalNotExistBean = ServiceFactory.getGoalService().isGoalNotExist(gTypeId, caseId, gBuizType);
					if(!goalNotExistBean.isSuccess()){
						canDo = false;
						return FAIL(goalNotExistBean.getErrCode());
					}
				}
				
				// 验证目标名称不能重复
				if(gBuizType == 1){
					ResultBean goalNotExistBean = ServiceFactory.getGoalService().isGoalNameExist(gName, caseId, gBuizType);
					if(!goalNotExistBean.isSuccess()){
						canDo = false;
						return FAIL(goalNotExistBean.getErrCode());
					}
				}
				
				if(canDo){
					// 清除已经设置的主追踪目标 
					if( isMaster == 1 ){
						ServiceFactory.getGoalService().clearMasterGoal(caseId);
					}
					ResultBean bean = ServiceFactory.getGoalService().doAddGoal(gName, gTypeId, 
							gSelector, caseId, projectId, gBuizType, isMaster);
					if(bean.isSuccess()){
						return SUCCESS();
					}else{
						return FAIL(ResultConstant.EXCEPTION_IN_GOAL_ADD);
					}
				}
				return FAIL(ResultConstant.EXCEPTION_IN_GOAL_ADD);
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doDeleteGoal")
	@ResponseBody
	public String doDeleteGoal(HttpServletRequest request, int goalId, int caseId){
		if(this.isUserLogin(request)){
			//体验账号
			int loginUid = this.getLoginUid(request);
			if( loginUid<100 ){
				return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
			}
			//
			if(goalId <= 0){
				return FAIL(ResultConstant.INVALID_GOAL_ID);
			}
			if(caseId <= 0){
				return FAIL(ResultConstant.INVALID_CASE_ID);
			}
			
			ResultBean bean = ServiceFactory.getGoalService().doDeleteGoal(goalId, caseId);
			if(bean.isSuccess()){
				return SUCCESS();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doSetMasterGoal")
	@ResponseBody
	public String doSetMasterGoal(HttpServletRequest request, int goalId, int caseId){
		if(this.isUserLogin(request)){
			if(goalId <= 0){
				return FAIL(ResultConstant.INVALID_GOAL_ID);
			}
			if(caseId <= 0){
				return FAIL(ResultConstant.INVALID_CASE_ID);
			}
			
			// 清除已经设置的主追踪目标 
			ServiceFactory.getGoalService().clearMasterGoal(caseId);
			// 设置主追踪目标
			ResultBean bean = ServiceFactory.getGoalService().doUpdateMasterGoalStatus(goalId, 1);
			if(bean.isSuccess()){
				return SUCCESS();
			}else{
				return FAIL(bean.getErrCode());
			}
		}else{
			return FAIL(ResultConstant.USER_NOT_LOGIN);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doCancelMasterGoal")
	@ResponseBody
	public String doCancelMasterGoal(HttpServletRequest request, int goalId){
		if(this.isUserLogin(request)){
			if(goalId <= 0){
				return FAIL(ResultConstant.INVALID_GOAL_ID);
			}
			
			ResultBean bean = ServiceFactory.getGoalService().doUpdateMasterGoalStatus(goalId, 0);
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
