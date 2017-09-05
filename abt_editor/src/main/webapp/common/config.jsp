<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String BasePath = request.getContextPath();

	String Host = request.getServerName();
	Host = "http://" + Host;
	String Port = String.valueOf(request.getServerPort());
	if (!Port.equals("80")) {
		Host = Host + ":" + Port;
	}

	String EditorUrl = Host + "/abt_editor";
	String SpiderUrl = Host + "/abt_spier";
	String MainUrl = Host + "/abt_manager";
	String ChannelUrl = Host+"/channel_manager";
	String DataHost = "http://abt.96333.com";

	String UpdateVer = "2.2";
	String TopRnav = "<a href=\"\">登录</a>";
%>