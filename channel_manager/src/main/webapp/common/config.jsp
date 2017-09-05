<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String BasePath = request.getContextPath();
	String Host = request.getServerName();
	Host = "http://"+Host;
	String Port = String.valueOf(request.getServerPort());
	if(!Port.equals("80")){
		Host = Host+":"+Port;
	}
	
	String EditorUrl = Host + "/abt_editor";
	String SpiderUrl = Host + "/abt_spier";
	String MainUrl = Host + "/abt_manager";
	String ChannelUrl = Host+"/channel_manager";

	String UpdateVer = "2.2";
%>