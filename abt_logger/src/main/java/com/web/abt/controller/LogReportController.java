package com.web.abt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.abt.context.Config;
import com.zhenai.clogger.client.OpEventBuilder;
import com.zhenai.clogger.client.entity.OpEvent;

@Controller
public class LogReportController  {
	
	@RequestMapping(method = RequestMethod.GET, value = "/log/zhenai")
	@ResponseBody
	public void doResponseUrl(HttpServletRequest request,
			String op,
			String userid,
			String sessionid,
			String testversionid,
			String testid,
			String guid,
			String testsessiondt,
			String insertdt,
			String sdevice,
			String sbrowser,
			String smobileos,
			String clickid){

		OpEvent e = OpEventBuilder.create(Config.getUploadChannel());
		e.setOpIp(request.getRemoteAddr());
		e.set("userid", userid)
		.set("sessionid", sessionid)
		.set("testversionid", testversionid)
		.set("testid", testid)
		.set("guid", guid)
		.set("testsessiondt", testsessiondt)
		.set("insertdt", insertdt)
		.set("sdevice", sdevice)
		.set("sbrowser", sbrowser)
		.set("smobileos", smobileos);
		if (clickid != null && clickid != "") {
			e.set("clickid", clickid);
		}
		OpEventBuilder.cache(e);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/before_log")
	@ResponseBody
	public void doResponseUrl() {}
}
