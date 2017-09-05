<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="common/config.jsp"%>
<%@include file="kpidata.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>**-实验数据--A/B Test工具</title>
	<link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/_zq-all.css?v=<%=UpdateVer %>" />
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
		<div class='proj_list_wrap'>
			<div class="proj_list">
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
				<div id='_zq-exprmdata-top-panel' class='_zq-righttop-panel clrfix'>
					<div id='_zq-exprmdata-nav' class='fl info'>
						<div class='_zq-title'>
							<span id="_zq-top_proj_name"></span>
							<span id="_zq-top_exprm_name"></span>
						</div>
					</div>
					<div id='_zq-proj-ctrl' class='fr ctrl'>
						<input type='button' value='查看配置' class='btn' t='preview' c='check'/>
					</div>
				</div>
				<div id='_zq-experiment-data' class='_zq-main-panel'>
					<div id='_zq-expri-header' class='clrfix'>
						<div id="tabs_nav" class="item_wrap clearfix">
							<div t="summary" class="item on">概览</div>
							<div class="sp"></div>
							<div t="goals" class="item">指标详情</div>
							<div class="sp"></div>
							<div t="versions" class="item">版本详情</div>
						</div>
						<div id='_zq-expri-ctrlpaenl'>
							
						</div>
						
					</div>
					
					<div class='_zq-tab_main' nav-by='tabs_nav'>
						<div class="tab clrfix" t="summary" id="_zq-expri-stat-summary" style="display:;">
							
							<!-- 添加表格 -->
							<div class="kpi-total-wrap">
								<table class="_zq-table">
									<thead>
										<tr>
											<th>时间</th>
											<th>版本</th>
											<th>注册人数</th>
											<th>注册率</th>
											<th>号码验证率</th>
											<th>总收入</th>
											<th>人均收入</th>
											<th>自助收入</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${kpiData}" var="row" varStatus="i">
										<tr>
											<c:if test="${i.index == 0}" >  
											<c:forEach items="${row.value}" var="cell" end="0">
											<td rowspan=${fn:length(kpiData)}>${cell}</td>
											</c:forEach>
											</c:if>
											<c:forEach items="${row.value}" var="cell" begin="1" end="7">
											<td>${cell}</td>
											</c:forEach>
										</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<!-- END添加表格 -->
	
							<div class='_zq-expri-stat-count'>
								<ul class='clrfix'>
									<li cntf='days' class='days _zq-cntf _zq-first'>
										<span class='cnt-data'>0,000</span>
										<span class='_zq-cntf'>测试天数</span>
									</li>
									<li cntf='persons' class='persons _zq-cntf'>
										<span class='cnt-data'>0,000</span>
										<span class='_zq-cntf'>测试人数</span>
									</li>
									<li cntf='goals' class='goals _zq-cntf'>
										<span class='cnt-data'>0,000</span>
										<span class='_zq-cntf'>指标数</span>
									</li>
									<li cntf='versions' class='versions _zq-cntf _zq-last'>
										<span class='cnt-data'>0,000</span>
										<span class='_zq-cntf'>版本数</span>
									</li>
								</ul>
							</div>
							<div class='_zq-expri-versions-cmpr _zq-chart-wrapper'>
								<div class='_zq-chart-title'>
								</div>
								<div class='_zq-chart-ctrl-panel _zq-sn0 clrfix'>
									<div class='fl' id='_zq-goal-selector'><select name="" id="ld_goal_list"></select></div>
									<div class='fr' style='display:none;' id='_zq-timespan-selector'>
										<div class="fl" id="lb_start_date"><input type="text" value="" class="date"></input></div>
										<div class="fl">&nbsp;</div>
										<div class="fl" id="lb_end_date"><input type="text" value="" class="date"></input></div>
									</div>
									
									<div class='fr' id='_zq-per-userview-selecor'>
										<input type="checkbox" id="_zq-per-userview" value="1">
										<label for='_zq-per-userview'>人均化</label>
									</div>
								</div>
								<div class='_zq-chart-canvas' id='_zq-chart-version-cmpr'>
										
								</div>
							</div>
							
							<div class='_zq-expri-uvcount-trend _zq-chart-wrapper'>
								<div class='_zq-chart-title'>
									
								</div>
								<div class='_zq-chart-ctrl-panel _zq-sn2'>
								</div>
								<div class='_zq-chart-canvas' id='_zq-chart-uv-trend'>
										
								</div>
							</div>
						</div>
						<div class="tab clrfix" t="goals" id="_zq-expri-stat-goals" style="display:;">
							<div class='_zq-expri-versions-cmpr _zq-chart-wrapper'>
								<div class='_zq-chart-title'>
								</div>
								<div class='_zq-chart-ctrl-panel _zq-sn0 clrfix'>
								</div>
								<div class='_zq-chart-canvas' id='_zq-chart-version-trend-cmpr'>
										
								</div>
								
								
							</div>
							<div class='_zq-expri-versions-cmpr _zq-chart-wrapper _zq-table-wrapper clrfix'>
								<div class='_zq-chart-ctrl-panel _zq-sn1 clrfix'>
									<div class='_zq-isbyday' id='_zq-isbyday-selector'>
										<input type="radio" value="theday" class='_zq-isbyday' label='按天'/>
										<input type="radio" value="theaccm" class='_zq-isbyday' label='累计' checked="checked"/>
									</div>
								</div>
								<table class='_zq-table'>
									<thead>
										<tr>
											<th>日期</th>
											<th>版本</th>
											<th class="_zq-expri-goal-name">指标数值</th>
											<th>测试人数</th>
											<th>指标数值/测试人数</th>
											<th>提升率</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>	
						</div>
						<div class="tab clrfix" t="versions" id="_zq-expri-stat-versions" style="display:;">
							<div class='_zq-expri-goals-show _zq-chart-wrapper clrfix'>
								<div class='_zq-chart-title'>
								</div>
								<div class='_zq-chart-ctrl-panel _zq-sn0 clrfix'>
									<div class='fl' id='_zq-version-selector'><select name="" id="ld_version_list"></select></div>
								</div>
								<div class='_zq-chart-canvas fl' id='_zq-chart-moneygoals-show'>
										
								</div>
								
								<div class='_zq-chart-canvas fr' id='_zq-chart-normalgoals-show'>
										
								</div>
								
							</div>
							<div class='_zq-expri-goals-show _zq-chart-wrapper _zq-table-wrapper clrfix'>
								<div class='_zq-chart-ctrl-panel _zq-sn1 clrfix'>
								</div>
								<table class='_zq-table'>
									<thead>
										<tr>
											<th>日期</th>
											<th>指标</th>
											<th>指标数值</th>
											<th>测试人数</th>
											<th>指标数值/测试人数</th>
											<th>提升率</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>	
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
	<div style='display:none'>
		<% /*
			-----------------------------------------------
			ID设为 ld_lab_list 是为了兼容旧的JS代码
			-----------------------------------------------
			*/ 
		%>
		<input id='ld_lab_list' value='<%=cid%>' />
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
