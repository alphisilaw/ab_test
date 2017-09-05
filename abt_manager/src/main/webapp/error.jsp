<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="common/config.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>错误 - abtest</title>
	<link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/all.css?v=<%=UpdateVer %>" />
</head>
<body class="index_bg">
	<%@include file="common/header.jsp"%>
	<div class="content">
		<div class="layout">
			<div class="login_wrap">
				<div class="login_success">
					<div class="tl">错误信息</div>
					<div class="sub"><a href="<%=MainUrl%>">返回首页</a></div>
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
