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
import com.web.abt.m.model.UserProjectCaseGoalModel;
import com.web.abt.m.model.UserProjectCaseModel;
import com.web.abt.m.model.UserProjectCaseVersionModel;
import com.web.abt.m.model.UserProjectModel;
import com.web.abt.m.service.ServiceFactory;
import com.web.abt.m.util.CommonUtil;

@Controller
public class CaseController extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "doSearchAvailCaseByProjectId")
    @ResponseBody
    public String doSearchAvailCaseByProjectId(HttpServletRequest request, int projectId, int pageSize, int pageNum,
            String expriType, String orderBy, String url) {
        if (this.isUserLogin(request)) {
            if (projectId <= 0) {
                return FAIL(ResultConstant.INVALID_PROJECT_ID);
            }
            ResultBean bean = ServiceFactory.getCaseService().doSearchAvailCasePageByProjectId(projectId, pageSize, pageNum, expriType, orderBy, url);
            if (bean.isSuccess()) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

                DataPage<UserProjectCaseModel> dataPage = (DataPage<UserProjectCaseModel>) bean.getBean();
                for (UserProjectCaseModel model : dataPage.getRecord()) {
                    list.add(CommonUtil.getUserProjectCaseMap(model));
                }

                JSONObject json = new JSONObject();
                json.put(this.RESULT, this.RESULT_SUCCESS);
                json.put("data", list);
                json.put("pageNum", pageNum);
                json.put("pageTotal", dataPage.getPageCount());
                json.put("dataTotal", dataPage.getRecordCount());
                return json.toString();
            } else {
                return FAIL(bean.getErrCode());
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "doSearchDeleteCaseByProjectId")
    @ResponseBody
    public String doSearchDeleteCaseByProjectId(HttpServletRequest request, int projectId, int pageSize, int pageNum) {
        if (this.isUserLogin(request)) {
            if (projectId <= 0) {
                return FAIL(ResultConstant.INVALID_PROJECT_ID);
            }

            ResultBean bean = ServiceFactory.getCaseService().doSearchDeleteCasePageByProjectId(projectId, pageSize, pageNum);
            if (bean.isSuccess()) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

                DataPage<UserProjectCaseModel> dataPage = (DataPage<UserProjectCaseModel>) bean.getBean();
                for (UserProjectCaseModel model : dataPage.getRecord()) {
                    list.add(CommonUtil.getUserProjectCaseMap(model));
                }

                JSONObject json = new JSONObject();
                json.put(this.RESULT, this.RESULT_SUCCESS);
                json.put("data", list);
                json.put("pageNum", pageNum);
                json.put("pageTotal", dataPage.getPageCount());
                json.put("dataTotal", dataPage.getRecordCount());
                return json.toString();
            } else {
                return FAIL(bean.getErrCode());
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "doAddCase")
    @ResponseBody
    public String doAddCase(HttpServletRequest request, Integer projectId,
            String cName, String cUrl, Integer buizType, Integer isMobile) {
        if (this.isUserLogin(request)) {
            //体验账号
            int loginUid = this.getLoginUid(request);
            if (loginUid < 100) {
                return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
            }
            //
            if (projectId == null || projectId <= 0) {
                return FAIL(ResultConstant.INVALID_PROJECT_ID);
            }
            if (StringUtils.isBlank(cName)) {
                return FAIL(ResultConstant.CASE_NAME_EMPTY);
            }
            if (StringUtils.isBlank(cUrl)) {
                return FAIL(ResultConstant.CASE_URL_EMPTY);
            }
            if (buizType == null || buizType <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_BUIZ_TYPE);
            }
            if (isMobile == null || !(isMobile == 0 || isMobile == 1)) {
                return FAIL(ResultConstant.INVALID_IS_MOBILE);
            }
            if (cName.matches("-copy\\(\\d+\\)")) { //?
                return FAIL(ResultConstant.SYSTEM_CASE_NAME_CANNOT_USE);
            }

            ResultBean cNameExistedBean = ServiceFactory.getCaseService().isCaseNameNotExisted(projectId, cName);
            if (!cNameExistedBean.isSuccess()) {
                return FAIL(cNameExistedBean.getErrCode());
            } else {
                ResultBean bean = ServiceFactory.getCaseService().doAddCase(projectId, cName, cUrl, buizType, isMobile);
                if (bean.isSuccess()) {
                    UserProjectCaseModel c = (UserProjectCaseModel) bean.getBean();
                    // 固定新增一个控制版本
                    ResultBean verbean = ServiceFactory.getCaseService().doAddCaseVersionJscode("控制版本", c.getCaseId(), projectId, "", 100, 1, "");
                    if (verbean.isSuccess()) {
                        return SUCCESS();
                    } else {
                        return FAIL(bean.getErrCode());
                    }
                } else {
                    return FAIL(bean.getErrCode());
                }
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "doDeleteCase")
    @ResponseBody
    public String doDeleteCase(HttpServletRequest request, int caseId, boolean real) {
        if (this.isUserLogin(request)) {
            //体验账号
            int loginUid = this.getLoginUid(request);
            if (loginUid < 100) {
                return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
            }
            //
            if (caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }
            int caseStatus = -1;
            if (real) {
                caseStatus = -2;
            }
            ResultBean currentCaseBean = ServiceFactory.getCaseService().findCaseByCaseId(caseId);
            if (!currentCaseBean.isSuccess()) {
                return currentCaseBean.getErrCode();
            } else {

                UserProjectCaseModel model = (UserProjectCaseModel) currentCaseBean.getBean();
                Map<String, Object> caseInfo = CommonUtil.getUserProjectCaseMap(model);
                int primaryCaseStatus = (Integer) caseInfo.get("caseStatus");
                if ((primaryCaseStatus > 0) && (caseStatus == -2)) {
                    return FAIL("无法彻底删除非草稿状态的实验!");
                }
            }
            ResultBean bean = ServiceFactory.getCaseService().doUpdateCaseStatus(caseId, caseStatus);
            if (bean.isSuccess()) {
                return SUCCESS();
            } else {
                return FAIL(bean.getErrCode());
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "getCaseInfo")
    @ResponseBody
    public String getCaseInfo(HttpServletRequest request, int caseId) {
        if (this.isUserLogin(request)) {
            if (caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }

            ResultBean bean = ServiceFactory.getCaseService().findCaseByCaseId(caseId);
            if (bean.isSuccess()) {
                UserProjectCaseModel model = (UserProjectCaseModel) bean.getBean();

                JSONObject json = new JSONObject();
                json.put(this.RESULT, this.RESULT_SUCCESS);
                json.put("info", CommonUtil.getUserProjectCaseMap(model));
                return json.toString();
            } else {
                return FAIL(bean.getErrCode());
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "runCase")
    @ResponseBody
    public String runCase(HttpServletRequest request, int projectId, int caseId) {
        if (this.isUserLogin(request)) {
            //体验账号
            int loginUid = this.getLoginUid(request);
            if (loginUid < 100) {
                return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
            }
            //
            if (projectId <= 0) {
                return FAIL(ResultConstant.INVALID_PROJECT_ID);
            }
            if (caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }

            ResultBean updatebean = ServiceFactory.getCaseService().doRunCase(caseId);
            if (updatebean.isSuccess()) {
                return SUCCESS();
            } else {
                return FAIL(updatebean.getErrCode());
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "stopCase")
    @ResponseBody
    public String stopCase(HttpServletRequest request, int projectId, int caseId) {
        if (this.isUserLogin(request)) {
            //体验账号
            int loginUid = this.getLoginUid(request);
            if (loginUid < 100) {
                return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
            }
            //
            if (projectId <= 0) {
                return FAIL(ResultConstant.INVALID_PROJECT_ID);
            }
            if (caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }
            ResultBean updatebean = ServiceFactory.getCaseService().doUpdateCaseStatus(caseId, 1);
            if (updatebean.isSuccess()) {
                return SUCCESS();
            } else {
                return FAIL(updatebean.getErrCode());
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "doCopyCase")
    @ResponseBody
    public String doCopyCase(HttpServletRequest request, int caseId) {
        if (this.isUserLogin(request)) {
            //体验账号
            int loginUid = this.getLoginUid(request);
            if (loginUid < 100) {
                return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
            }
            //
            if (caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }

            ResultBean bean = ServiceFactory.getCaseService().doCopyCase(caseId);
            if (bean.isSuccess()) {
                return SUCCESS();
            } else {
                return FAIL(bean.getErrCode());
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "doSearchConfigInfoByCaseId")
    @ResponseBody
    public String doSearchConfigInfoByCaseId(HttpServletRequest request,
            int caseId, int projectId) {
        if (caseId <= 0) {
            return FAIL(ResultConstant.INVALID_CASE_ID);
        }
        if (projectId <= 0) {
            return FAIL(ResultConstant.INVALID_PROJECT_ID);
        }
        JSONObject json = new JSONObject();

        // 基本信息 
        ResultBean casebean = ServiceFactory.getCaseService().doSearchCaseByCaseId(caseId);
        if (casebean.isSuccess()) {
            UserProjectCaseModel caseModel = (UserProjectCaseModel) casebean.getBean();
            json.put("caseInfo", CommonUtil.getUserProjectCaseMap(caseModel));
        }

        // 追踪目标
        ResultBean goalbean = ServiceFactory.getGoalService().doSearchGoalByCaseId(caseId, projectId);
        if (goalbean.isSuccess()) {
            List<UserProjectCaseGoalModel> goalList = (List<UserProjectCaseGoalModel>) goalbean.getBean();
            List<Map<String, Object>> nGoalList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> kpiGoalList = new ArrayList<Map<String, Object>>();
            for (UserProjectCaseGoalModel goal : goalList) {
                if (goal.getGoalBuizType() == 1) {
                    nGoalList.add(CommonUtil.getUserProjectCaseGoalMap(goal));
                } else if (goal.getGoalBuizType() == 2) {
                    kpiGoalList.add(CommonUtil.getUserProjectCaseGoalMap(goal));
                }
            }
            json.put("pageGoal", nGoalList);
            json.put("kpiGoal", kpiGoalList);
        }

        // 流量分配
        ResultBean verbean = ServiceFactory.getCaseService().doSearchAvailableVersionListByCaseId(caseId, projectId);
        if (verbean.isSuccess()) {
            List<UserProjectCaseVersionModel> verList = (List<UserProjectCaseVersionModel>) verbean.getBean();
            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
            for (UserProjectCaseVersionModel version : verList) {
                datas.add(CommonUtil.getUserProjectCaseVersionMap(version));
            }
            json.put("percents", datas);
        }
        json.put(this.RESULT, this.RESULT_SUCCESS);
        return json.toString();
    }

    @RequestMapping(method = RequestMethod.POST, value = "updateCaseName")
    @ResponseBody
    public String updateCaseName(HttpServletRequest request, int caseId, String caseName) {
        if (this.isUserLogin(request)) {
            //体验账号
            int loginUid = this.getLoginUid(request);
            if (loginUid < 100) {
                return FAIL(ResultConstant.DEMO_USER_NOT_AUTH);
            }
            //
            if (StringUtils.isBlank(caseName)) {
                return FAIL(ResultConstant.CASE_NAME_EMPTY);
            }
            if (caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }
            if (caseName.matches("-copy\\(\\d+\\)")) {
                return FAIL(ResultConstant.SYSTEM_CASE_NAME_CANNOT_USE);
            }
            ResultBean projectBean = ServiceFactory.getCaseService().getProjectByCaseId(caseId);
            if (projectBean.isSuccess()) {
                int projectId = ((UserProjectModel) projectBean.getBean()).getProjectId();
                ResultBean cNameExistedBean = ServiceFactory.getCaseService().isCaseNameNotExisted(projectId, caseName);
                if (!cNameExistedBean.isSuccess()) {
                    return FAIL(cNameExistedBean.getErrCode());
                }
            }
            ResultBean bean = ServiceFactory.getCaseService().doUpdateCaseName(caseId, caseName);
            if (bean.isSuccess()) {
                return SUCCESS();
            } else {
                return FAIL(bean.getErrCode());
            }
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }
}
