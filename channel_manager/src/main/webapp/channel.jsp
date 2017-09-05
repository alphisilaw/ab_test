<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="common/config.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>**A/B Test工具 渠道管理</title>
	<link rel="stylesheet" type="text/css" href="<%=MainUrl%>/assets/css/_zq-all.css?v=<%=UpdateVer %>" />
</head>
<body>
	<div class="header">
		<div class="logo_wrap">
			<div class="txt"></div>
		</div>
		<div class="b-top">
            <div class="b-con">
                <a href="<%=ChannelUrl%>">
                    <img src="<%=MainUrl%>/assets/images/logo.png">
                </a>
                <ul>
                    <li class="li-1 on"><a href="<%=ChannelUrl%>">渠道测试</a></li>
                    <li><a href="<%=MainUrl%>/experiment.jsp#valid">页面测试</a></li>
                </ul>
                <div class="fr rnav" id="top_user_info">
					<a href="#">${user.realname}</a>
					<a href="logout">退出</a>
				</div>
            </div>
        </div>
	</div>
	<div class='channel-manage-wrap'>
		<div class="manage-scroll">
			<div class="manage-content channel-table">
				<table class="channel-manage">
					<tr>
						<td>
							<label>渠道名称</label>
							<input type="text" id="channelName">
						</td>
						<td>
							<label>渠道号</label>
							<input type="text" id="channelId">
						</td>
						<td>
							<label>子渠道号</label>
							<input type="text" id="subChannelId">
						</td>
						<td style="position: relative;">
							<label>媒介组</label>
							<input id="staple1" type="text" style="width: 200px;" />
							<div class="form-defined-options" style="display: none;">
								<c:forEach items="${staples}" var="staple">
								<a href="javascript:;" title="${staple}" value="${staple}" class="defined-option">${staple}</a>
								</c:forEach>
		                    </div>

							<!-- <datalist id="staplelist">
								<option value="">请选择</option>
								<c:forEach items="${staples}" var="staple">
								<option value="${staple}">${staple}</option>
								</c:forEach>
							</datalist> -->
							<!-- <select id="staple1">
								<option value="">请选择</option>
								<c:forEach items="${staples}" var="staple">
								<option value="${staple}">${staple}</option>
								</c:forEach>
							</select> -->
						</td>
						<td>
							<label>模板</label>
							<select id="tempId">
								<option value="">请选择</option>
								<c:forEach items="${templates}" var="template">
								<option value="${template.key}">${template.value}</option>
								</c:forEach>
							</select>
						</td>
						<td>
							<input id="queryBtn" type="button" value="查询" class='btn'/>
						</td>
					</tr>
				</table>
			
				<table class='_zq-expri-table'>
					<thead><tr>
						<th width="16%">媒介组</th>	
						<th width="20%">渠道名称</th>
						<th width="10%">渠道号</th>
						<th width="8%">子渠道号</th>
						<th width="8%">访客数</th>
						<th width="20%">模板配置情况</th>
						<th width="8%">配置比例</th>
						<th width="10%">操作</th>
					</tr></thead>
					<tbody id="channelList"></tbody>
				</table>
				<div class="pages">
					<div class="pages-wrap">
						<span>共0条</span>
						<span>第0/0页</span>
						<a href="javascript:;" class="chg-pg">首页</a>
						<a href="javascript:;" class="chg-pg">上一页</a>
						<a href="javascript:;" class="chg-pg">下一页</a>
						<a href="javascript:;" class="chg-pg">末页</a>
		                <input type="number">
		                <a href="javascript:;" class="chg-pg go-page">go</a>
		              </div>
				</div>
			</div>
		</div>
	</div>
	<!-- 
	<div class="page_loading define-loading">
		<div class="loader_wrap">
			<div class="loader"></div>
			<div class="txt">正在加载页面</div>
		</div>
	</div> -->
	<%@include file="common/footer.jsp"%>
	<%@include file="common/common_js.jsp"%>
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/jquery-1.9.1.min.js?v=<%=UpdateVer %>"></script>
	<link rel="stylesheet" type="text/css" href="<%=MainUrl%>/assets/css/jquery-ui-1.10.2.date.css?v=<%=UpdateVer %>" />
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/jquery-ui-1.10.2.date.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/base.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/jquery.qrcode.min.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/jquery.zclip.min.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/channel_manage.js?v=<%=UpdateVer %>"></script>
</body>
</html>
