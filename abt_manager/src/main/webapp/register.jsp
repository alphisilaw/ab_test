<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="common/config.jsp"%>
<%	
	TopRnav = "<a href=\""+BasePath+"/v/login"+"\">登录</a><a href=\""+BasePath+"/demoLogin"+"\">体验</a>";
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>注册 - abtest</title>
	<link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/all.css?v=<%=UpdateVer %>" />
</head>
<body class="index_bg">
	<%@include file="common/header.jsp"%>
	<div class="content">
		<div class="layout">
			<div class="login_wrap">
				<div class="s_form">
					<div class="item">
						<div class="l">用户名：</div>
						<div class="r"><input id="name" type="text"></div>
					</div>
					<div class="item">
						<div class="l">邮箱：</div>
						<div class="r"><input id="email" type="text"></div>
					</div>
					<div class="item">
						<div class="l">密码：</div>
						<div class="r"><input id="pwd" type="password"></div>
					</div>
					<div class="item">
						<div class="l">确认密码：</div>
						<div class="r"><input id="pwd2" type="password"></div>
					</div>
					<div class="item">
						<div class="l">手机：</div>
						<div class="r"><input id="phone" type="text"></div>
					</div>
					<div class="item">
						<div class="l">&nbsp;</div>
						<div class="r"><a href="#" id="register" class="btn">注册</a></div>
					</div>
				</div>
				<div class="login_success" style="display:none;">
					<div class="tl">注册成功！</div>
					<div class="sub"></div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="common/footer.jsp"%>
	<%@include file="common/common_js.jsp"%>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/jquery-1.9.1.min.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/base.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/register.js?v=<%=UpdateVer %>"></script>
</body>
</html>
