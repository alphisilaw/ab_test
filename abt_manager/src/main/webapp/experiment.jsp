<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="common/config.jsp"%>
<%
	String cid=request.getParameter("cid");
	if( null != cid )
	{
		if( ! cid.trim().equals("") ){
			response.setStatus(302);
			response.setHeader("Location", MainUrl+"/v/exprmdata-simple?cid="+cid+"&pid="+new String(request.getParameter("pid")+""));
		}
	}
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String today = formatter.format(new Date());
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>**-实验列表--A/B Test工具</title>
	<link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/_zq-all.css?v=<%=UpdateVer %>" />
</head>
<body>
	<div id='_zq-fullpagemask' style='background:#FFF;position: fixed;height:100%;width:100%;z-index:10001;'></div>
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
                    <li class="li-1"><a href="<%=ChannelUrl%>">渠道测试</a></li>
                    <li class="on"><a href="<%=MainUrl%>/experiment.jsp#valid">页面测试</a></li>
                </ul>
                <div class="fr rnav" id="top_user_info">
					<a href="#">${user.realname}</a>
					<a href="logout">退出</a>
				</div>
            </div>
        </div>
	</div>
	<div id='_zq-leftcol'>
		<div class='proj_list_wrap' style=''>
			<div class="proj_list" id="proj_list">
				<div class="plist">
				</div>
			</div>
		</div>
		<div class="bot">
			<span t="add" class="btn">添加项目</span>
		</div>
	</div>
	<div class='channel-manage-wrap'>
		<div class='manage-scroll'>
			<div class='manage-content'>
				<div class='_zq-righttop-panel clrfix'>
					<div id='_zq-proj-info' class='fl info'>
						<div class='_zq-title'>
							<span id="_zq-top_proj_name">项目名称</span>
						</div>
						<div class='_zq-content'>
							<span id="_zq-top_proj_dscrp" class='_zq-main'>项目描述</span>
							<span id="_zq-top_proj_crtime" class='_zq-spplmt'>创建时间</span>
						</div>
					</div>
					<div id='_zq-proj-ctrl' class='fr ctrl'>
						<input type='button' value='编辑项目' class='btn' t='edit'/>
						<input type='button' value='删除项目' class='btn' t='delproject' style='display: none;'/>
						<input type='button' value='项目代码' class='btn' t='proj-code'/>
					</div>
				</div>
				<div id='_zq-experiment-list' class='_zq-main-panel'>
					<div id='_zq-expri-header' class='clrfix'>
						<div id="tabs_nav" class="item_wrap clearfix">
							<div t="valid" class="item on">有效实验<i>0</i></div>
							<div class="sp"></div>
							<div t="archive" class="item">归档实验<i>0</i></div>
						</div>
						<div id='_zq-expri-ctrlpaenl'>
							<input type="text" id='query_txt' style='padding: 7px 8px;margin-bottom: 10px;border-radius: 5px;border: 1px solid #ccc;'/>
							<input type="button" value="查询" class='btn' id='query_lab'/>
							<input type="button" value="添加实验" class='btn' id='add_lab'/>
						</div>
					</div>
					
					<div class='_zq-tab_main' nav-by='tabs_nav'>
						<div class="tab" t="valid" id="_zq-expri-valid">
							<table class='_zq-expri-table'>
								<thead><tr>
									<th class='_zq-field-status' title='点击按照状态排序'>状态</th>	
									<th class='_zq-field-name'>名称</th>
									<th class='_zq-field-buiz'>所属业务</th>
									<!-- <th class='_zq-field-dcount'>测试天数</th> -->
									<th class='_zq-field-pcount'>测试人数</th>
									<th class='_zq-field-vcount'>版本数</th>
									<th class='_zq-field-gcount'>统计目标</th>
									<th class='_zq-field-crtime' title='点击按照创建时间排序'>创建时间</th>
									<th class='_zq-field-ctrl'>操作</th>
								</tr></thead>
								<tbody>
								</tbody>
							</table>
						</div>
						<div class="tab" t="archive" id="_zq-expri-archive" style="display:none;">
							<table class='_zq-expri-table'>
								<thead><tr>
									<th class='_zq-field-status'>状态</th>	
									<th class='_zq-field-name'>名称</th>
									<th class='_zq-field-buiz'>所属业务</th>
									<!-- <th class='_zq-field-dcount'>测试天数</th> -->
									<th class='_zq-field-pcount'>测试人数</th>
									<th class='_zq-field-vcount'>版本数</th>
									<th class='_zq-field-gcount'>统计目标</th>
									<th class='_zq-field-crtime'>创建时间</th>
									<th class='_zq-field-ctrl'>操作</th>
								</tr></thead>
								<tbody>
									
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="page_loading">
		<div class="loader_wrap">
			<div class="loader"></div>
			<div class="txt">正在加载页面</div>
		</div>
	</div>
	<%@include file="common/footer.jsp"%>
	<%@include file="common/common_js.jsp"%>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/jquery-1.9.1.min.js?v=<%=UpdateVer %>"></script>
	<link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/jquery-ui-1.10.2.date.css?v=<%=UpdateVer %>" />
	<script type="text/javascript" src="<%=BasePath%>/assets/js/jquery-ui-1.10.2.date.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/highcharts.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/highcharts-more.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/base.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/project.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/jquery.zclip.min.js?v=<%=UpdateVer %>"></script>
</body>
</html>
