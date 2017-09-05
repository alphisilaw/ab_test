<%@page import="org.apache.taglibs.standard.tag.common.core.ForEachSupport"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.web.abt.m.model.UserProjectCaseVersionModel"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="org.apache.commons.lang3.time.DateFormatUtils"%>
<%@page import="java.text.ParseException"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.apache.commons.lang3.math.NumberUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.web.abt.m.model.UserProjectModel"%>
<%@page import="com.web.abt.m.service.ProjectService"%>
<%@page import="com.web.abt.m.model.UserProjectCaseModel"%>
<%@page import="com.web.abt.m.common.ResultBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.Integer"%>
<%@page import="java.lang.String"%>
<%@page import="com.web.abt.m.service.DataViewService"%>
<%@page import="com.web.abt.m.service.ServiceFactory"%>
<%@page import="com.web.abt.m.service.CaseService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
	String begin = request.getParameter("begin");
	String end = request.getParameter("end");
	String cid = request.getParameter("cid");
	String pid = request.getParameter("pid");

	if( (null==cid) || cid.trim().equals("") )
	{
		response.setStatus(302);
		response.setHeader("Location", MainUrl+"/v/experiment?pid="+new String(request.getParameter("pid")+""));
	}

	Integer caseId=Integer.parseInt(cid);
	Integer projectId=Integer.parseInt(pid);
	Date beginDate = null;
	Date endDate = null;
	if (StringUtils.isBlank(end)) {
		endDate = DateUtils.addDays(new Date(), -1);
	} else {
		try {
			endDate = DateUtils.parseDate(end, "yyyy-MM-dd");
		} catch (ParseException e) {
		}
	}
	if (StringUtils.isBlank(begin)) {
		beginDate = endDate;
	} else {
		try {
			beginDate = DateUtils.parseDate(begin, "yyyy-MM-dd");
		} catch (ParseException e) {
		}
	}
	
	CaseService caseService = ServiceFactory.getCaseService();
	DataViewService service = ServiceFactory.getDataViewService();
	

	List<UserProjectCaseVersionModel> versions = (List<UserProjectCaseVersionModel>) caseService.doSearchVersionByCaseId(caseId, projectId).getBean();
	Map<Integer, Map<String, Object>> kpiDatas = service.getKPIData(caseId, beginDate, endDate);
	
	List<Integer> dirtyVersion = new ArrayList<Integer>();
	for (Entry<Integer, Map<String, Object>> entry : kpiDatas.entrySet()) {
		Integer versionId = entry.getKey();
		boolean contains = false;
		for (UserProjectCaseVersionModel version : versions) {
			if (versionId != null && versionId.equals(version.getVersionId())) {
				entry.getValue().put("version", version);
				contains = true;
				break;
			}
		}
		if (!contains) {
			dirtyVersion.add(versionId);
		}
	}
	for (Integer dirty : dirtyVersion) {
		kpiDatas.remove(dirty);
	}
	
	ResultBean result = caseService.doSearchCaseByCaseId(caseId);
	UserProjectCaseModel userCase = (UserProjectCaseModel) result.getBean();
	
	result = caseService.getProjectByCaseId(caseId);
	UserProjectModel userProject = (UserProjectModel) result.getBean();
	
	request.setAttribute("kpiDatas", kpiDatas);
	request.setAttribute("userCase", userCase);
	request.setAttribute("userProject", userProject);

	request.setAttribute("cid", caseId);
	request.setAttribute("pid", projectId);
	request.setAttribute("begin", DateFormatUtils.format(beginDate, "yyyy-MM-dd"));
	request.setAttribute("end", DateFormatUtils.format(endDate, "yyyy-MM-dd"));
%>