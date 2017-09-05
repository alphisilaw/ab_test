package com.web.abt.m.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.context.Config;
import com.web.abt.m.model.UserProjectCaseModel;
import com.web.abt.m.service.ServiceFactory;
import com.web.abt.m.util.DateFormatUtil;
import com.web.abt.m.util.HttpToolkit;

@Controller
public class DataViewController extends BaseController {

    private static Log logger = LogFactory.getLog(DataViewController.class);
    
    @RequestMapping(method = RequestMethod.GET, value = "doSearchDataViewSummary")
    @ResponseBody
    public String doSearchDataViewSummary(HttpServletRequest request,
            Integer caseId, String starttime, String endtime, String dataType) {
        if (this.isUserLogin(request)) {
            if (StringUtils.isBlank(dataType)) {
                return FAIL(ResultConstant.INVALID_DATA_TYPE);
            }

            if (caseId == null || caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }

            if (StringUtils.isBlank(starttime)) {
                ResultBean casebean = ServiceFactory.getCaseService().findCaseByCaseId(caseId);
                if (casebean.isSuccess()) {
                    UserProjectCaseModel caseModel = (UserProjectCaseModel) casebean.getBean();
                    //如果未填写fromDate, 则取实验开始时间
                    if (caseModel.getStartRunTime() != null) {
                        starttime = DateFormatUtil.dateToString(caseModel.getStartRunTime(), "yyyy-MM-dd");
                    }
                } else {
                    return FAIL(ResultConstant.EMPTY_IN_CASE_SEARCH);
                }
            }

            if (StringUtils.isBlank(starttime)) {
                return FAIL(ResultConstant.CASE_NOT_RUN);
            }
            //如果未填写toDate, 则取当前时间
            if (StringUtils.isBlank(endtime)) {
                endtime = DateFormatUtil.dateToString(new Date(), "yyyy-MM-dd");
            }

            StringBuffer url = new StringBuffer(Config.getDataHost()).append("/abt/abtdata/").append(caseId)
                    .append("?stattype=").append(dataType)
                    .append("&starttime=").append(starttime)
                    .append("&endtime=").append(endtime);
            logger.info("URL::" + url.toString());
            String result = HttpToolkit.doGet(url.toString(), null, "utf-8", true);
            return result;
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "getCasePersonViewByProjectId")
    @ResponseBody
    public String getCasePersonViewByProjectId(HttpServletRequest request, Integer pid) {
        if (this.isUserLogin(request)) {
            if (pid == null || pid <= 0) {
                return FAIL(ResultConstant.INVALID_PROJECT_ID);
            }
            StringBuffer url = new StringBuffer(Config.getDataHost())
                    .append("/abt/abtdata/get_case_pv_by_project_id/").append(pid);
            logger.info("URL::" + url.toString());
            String result = HttpToolkit.doGet(url.toString(), null, "utf-8", true);
            return result;
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "doSearchDataViewDetail")
    @ResponseBody
    public String doSearchDataViewDetail(HttpServletRequest request,
            Integer caseId, String starttime, String endtime, String dataType) {
        if (this.isUserLogin(request)) {
            if (StringUtils.isBlank(dataType)) {
                return FAIL(ResultConstant.INVALID_DATA_TYPE);
            }

            if (caseId == null || caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }

            if (StringUtils.isBlank(starttime)) {
                ResultBean casebean = ServiceFactory.getCaseService().findCaseByCaseId(caseId);
                if (casebean.isSuccess()) {
                    UserProjectCaseModel caseModel = (UserProjectCaseModel) casebean.getBean();
                    //如果未填写fromDate, 则取实验开始时间
                    if (caseModel.getStartRunTime() != null) {
                        starttime = DateFormatUtil.dateToString(caseModel.getStartRunTime(), "yyyy-MM-dd");
                    }
                } else {
                    return FAIL(ResultConstant.EMPTY_IN_CASE_SEARCH);
                }
            }

            if (StringUtils.isBlank(starttime)) {
                return FAIL(ResultConstant.CASE_NOT_RUN);
            }
            //如果未填写toDate, 则取当前时间
            if (StringUtils.isBlank(endtime)) {
                endtime = DateFormatUtil.dateToString(new Date(), "yyyy-MM-dd");
            }

            StringBuffer url = new StringBuffer(Config.getDataHost()).append("/abt/abtdata/detail/").append(caseId)
                    .append("?stattype=").append(dataType)
                    .append("&starttime=").append(starttime)
                    .append("&endtime=").append(endtime);
            logger.info("URL::" + url.toString());
            String result = HttpToolkit.doGet(url.toString(), null, "utf-8", true);
            return result;
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "doSearchDataViewMeta")
    @ResponseBody
    public String doSearchDataViewMeta(HttpServletRequest request,
            Integer caseId) {
        if (this.isUserLogin(request)) {
            if (caseId == null || caseId <= 0) {
                return FAIL(ResultConstant.INVALID_CASE_ID);
            }

            StringBuffer url = new StringBuffer(Config.getDataHost()).append("/abt/abtdata/get_meta/").append(caseId);
            logger.info("URL::" + url.toString());
            String result = HttpToolkit.doGet(url.toString(), null, "utf-8", true);
            return result;
        } else {
            return FAIL(ResultConstant.USER_NOT_LOGIN);
        }
    }
}
