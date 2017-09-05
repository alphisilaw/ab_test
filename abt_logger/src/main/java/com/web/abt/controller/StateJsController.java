package com.web.abt.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.web.abt.moudel.UserProjectCaseModel;
import com.web.abt.moudel.UserProjectCaseVersionModel;
import com.web.abt.service.CaseVersionDispartchService;
import com.web.abt.service.UserProjectService;

@Controller
public class StateJsController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(StateJsController.class);
	
	@Resource
	private UserProjectService userProjectService;
	
	@Resource
	private CaseVersionDispartchService caseVersionDispartchService;
	
	public static String initJs = new StringBuilder()
	.append("(zabtest_start={")
	.append("config:zabtest_config,whetherDoTest:true,")
	.append("hideBody:function hideBody(){var head=document.getElementsByTagName(\"head\").item(0);var style=document.createElement(\"style\");head.appendChild(style);style.setAttribute(\"type\",\"text/css\");style.setAttribute(\"id\",\"_hide-body-forabtest\");var css=\"body{opacity:0 !important;filter:alpha(opacity=0) !important;}\";if(style.styleSheet){style.styleSheet.cssText=css}else{style.appendChild(document.createTextNode(css))}},showBody:function showBody(){var style=document.getElementById(\"_hide-body-forabtest\");if(style){style.parentNode.removeChild(style)}},stop:function(){zabtest_start.showBody();zabtest_start.whetherDoTest=false},")
	.append("loadJs:function(jsfile){var head=document.getElementsByTagName(\"head\").item(0);var script=document.createElement(\"script\");script.setAttribute(\"type\",\"text/javascript\");head.appendChild(script);script.setAttribute(\"src\",jsfile);script.onerror=function(){zabtest_start.showBody();return null};return script},timerHandler:0,")
	.append("init:function(){zabtest_start.timerHandler=setTimeout(\"zabtest_start.stop()\",zabtest_start.config.ToleranceTime);")
	.append("zabtest_start.hideBody();},")
	.append("zabtest_setCookie:function(key,value,options)")
	.append("{")
	.append("if(!options)")
	.append("{")
	.append("options={};")
	.append("}")
	.append("if(options.expires)")
	.append("{")
	.append("var expires=options.expires;")
	.append("var unit=expires.toString().substr(expires.toString().length-1);")
	.append("expires=parseInt(expires,10);")
	.append("if('h'===unit)")
	.append("{")
	.append("expires=3600000*expires;")
	.append("}")
	.append("else if('m'===unit)")
	.append("{")
	.append("expires=60000*expires;")
	.append("}")
	.append("else if('s'===unit)")
	.append("{")
	.append("expires=1000*expires;")
	.append("}")
	.append("options.expires=new Date(+(new Date())+expires).toUTCString();")
	.append("}")
	.append("return document.cookie=[")
	.append("key,'=',value,")
	.append("options.expires ? (';expires=' + options.expires):'',")
	.append("options.path    ? ('; path=' + options.path) : ';path=/',")
	.append("options.domain  ? ('; domain=' + options.domain) : '',")
	.append("options.secure  ? ('; secure') : ''")
	.append("].join('');")
	.append("},")
	.append("zabtest_getCookie:function(key)")
	.append("{")
	.append("var re=new RegExp('(?:\\\\b|;)\\\\s*'+key+'=([^;]+)');")
	.append("var match=re.exec(document.cookie);")
	.append("if(match instanceof Array)")
	.append("{")
	.append("return match[1];")
	.append("}")
	.append("},")
	.append("zabtest_getLocaleDateTime:function()")
	.append("{")
	.append("var d=new Date();")
	.append("var year=d.getFullYear();")
	.append("var month=d.getMonth()+1;")
	.append("var date=d.getDate();")
	.append("var hour=d.getHours();")
	.append("var minute=d.getMinutes();")
	.append("var second=d.getSeconds();")
	.append("return [year,zabtest_start.twoNumberFormat(month),zabtest_start.twoNumberFormat(date)].join('-')")
	.append("+ ' ' +")
	.append("[zabtest_start.twoNumberFormat(hour),zabtest_start.twoNumberFormat(minute),zabtest_start.twoNumberFormat(second)].join(':');")
	.append("},")
	.append("twoNumberFormat:function(number)")
	.append("{")
	.append("return (number<10)?('0'+number.toString(10)):number.toString(10);")
	.append("},")
	.append("getGUID:function()")
	.append("{")
	.append("var GUID;")
	.append("if((!(GUID=zabtest_start.zabtest_getCookie('GUID')))&&(!(GUID=zabtest_start.zabtest_getCookie('s'))))")
	.append("{")
	.append("GUID=Math.floor((Math.random()*999999999)).toString(36);")
	.append("}")
	.append("GUID=GUID.toLowerCase();")
	.append("zabtest_start.zabtest_setCookie('GUID',GUID,{expires:'8760h'});")
	.append("return GUID;")
	.append("},")
	.append("getUserID:function()")
	.append("{")
	.append("if(!(userID=zabtest_start.zabtest_getCookie('userID')))")
	.append("{")
	.append("userID=0-Math.floor((Math.random()*999999999));")
	.append("zabtest_start.zabtest_setCookie('userID',userID,{expires:'8760h'});")
	.append("}")
	.append("return userID;")
	.append("},")
	.append("buildLogRecord:function()")
	.append("{")
	.append("var now=zabtest_start.zabtest_getLocaleDateTime();")
	.append("return {")
	.append("userid:		zabtest_start.getUserID(),")
	.append("sessionid:		zabtest_start.zabtest_getCookie('s')?zabtest_start.zabtest_getCookie('s'):('FAKE_'+zabtest_start.getGUID()),")
	.append("testversionid:	zabtest_start.zabtest_getCookie('testversionID'),")
	.append("testid:		zabtest_start.zabtest_getCookie('TestID'),")
	.append("guid:			zabtest_start.zabtest_getCookie('GUID'),")
	.append("testsessiondt:	now,")
	.append("insertdt:		now,")
	.append("sdevice:		navigator.platform,")
	.append("sbrowser:		navigator.userAgent,")
	.append("smobileos:		navigator.userAgent,")
	.append("clickid:        0")
	.append("}")
	.append("},")
	.append("uploadLogs:function(record)")
	.append("{")
	.append("var queries=[];")
	.append("for(var k in record)")
	.append("{")
	.append("queries.push(k+'='+encodeURIComponent(record[k]));")
	.append("}")
	.append("if(queries.length)")
	.append("{")
	.append("var uploadTo='http://abt.96333.com:8021/abt_logger/log/zhenai?op=t_fw_00014';")
	.append("new Image().src=uploadTo+'&'+ queries.join('&');")
	.append("}")
	.append("}")
	.append("}).init();").toString();
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/code/{projectId}/stat.js")
	public void stateJs(@PathVariable Integer projectId, HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		String js =	"";
		try {
			String referer = request.getHeader("Referer");
			if (StringUtils.isNoneBlank(referer)) {
				
				referer = rewriteReferer(referer);
				
				Map<String, Object> config = userProjectService.getConfig(projectId, referer);
				Map<String, UserProjectCaseModel> cases = (Map<String, UserProjectCaseModel>) config.get("cases");
				Map<String, Map<String, UserProjectCaseVersionModel>> versions = (Map<String, Map<String, UserProjectCaseVersionModel>>) config.get("versions");
				UserProjectCaseVersionModel versionModel = caseVersionDispartchService.dispartch(cases, versions, referer);
				
				StringBuilder jsBuilder = new StringBuilder();
				jsBuilder.append("var zabtest_config={ProjectID:").append(projectId).append(",ToleranceTime:3000};");
				jsBuilder.append(initJs);
				
				if (config != null) {
					String confStr = JSONObject.toJSONString(config).replaceAll("\\'", "\\\\\'");
					jsBuilder.append("var raw_abtest_config='").append(confStr).append("';");
				}
				
				if (versionModel != null) {
					jsBuilder.append("(function(){");
					
					jsBuilder.append("if(!zabtest_start.zabtest_getCookie('TestID')){")
					.append("zabtest_start.zabtest_setCookie('TestID',")
					.append(versionModel.getCaseId())
					.append(",{expires:'8760h'});")
					.append("zabtest_start.zabtest_setCookie('testversionID',")
					.append(versionModel.getVersionId())
					.append(",{expires:'8760h'});}")
					.append("var record = zabtest_start.buildLogRecord();")
					.append("zabtest_start.uploadLogs(record);")
					.append("zabtest_start.loadJs('http://imagescrm.zhenai.com/edition18810/abtest/abt-main.js'); ");
					
					jsBuilder.append("})();");
				}
				
				js = jsBuilder.toString();
			}
		} catch (Exception e) {
			LOGGER.error("生成js出错!projectId="+projectId, e);
		}
		response.setContentType("Content-Type:text/javascript");
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(js);
	}

	private String rewriteReferer(String referer) {
		if (referer.contains("http://m.zhenai.com/from.do")) {
			String[] subStr = referer.split("&");
			if (subStr.length == 1) {
				if (!subStr[0].contains("channelId")) {
					referer = referer.split("?")[0];
				} else {
					referer = subStr[0];
				}
			} else if (subStr.length > 1) {
				if (!subStr[0].contains("channelId")) {
					referer = referer.split("?")[0];
				} else if (!subStr[1].contains("subChannelId")) {
					referer = subStr[0];
				} else {
					referer = subStr[0] + "&" + subStr[1];
				}
			}
		}
		return referer;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/code/clearConfigCache")
	@ResponseBody
	public String clearJsCache() {
		try {
			userProjectService.clearConfigCache();
		} catch (Exception e) {
			LOGGER.error("清空实验缓存出错!", e);
			return FAIL("清空实验缓存出错!");
		}
		return SUCCESS(null);
	}

}
