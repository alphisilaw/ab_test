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
	<div class='channel-manage-wrap'>
		<div class='manage-scroll'>
			<div class='manage-content'>
				<div id='_zq-exprmdata-top-panel' class='_zq-righttop-panel clrfix'>
					<div id='_zq-exprmdata-nav' class='fl info'>
						<div class='_zq-title'>
							<span id="_zq-top_proj_name">${userProject.projectName}</span>
							<span id="_zq-top_exprm_name">${userCase.caseName}</span>
						</div>
					</div>
				</div>
				<div id='_zq-experiment-data' class='_zq-main-panel'>
					<div id='_zq-expri-header' class='clrfix'>
						<div id="tabs_nav" class="item_wrap clearfix">
							<div t="summary" class="item on">概览</div>
						</div>
					</div>
					<div class='_zq-tab_main' nav-by='tabs_nav'>
						<div class="tab clrfix" t="summary" id="_zq-expri-stat-summary" style="display:;">
							
							<!-- 添加表格 -->
							<div class="kpi-total-wrap">
								<div class="channel-manage" style="line-height: 44px;">
									<input type="text" value="${begin}" class="date" id="dateBegn">
									<input type="text" value="${end}" class="date" id="dateEnd">
									<input id="queryBtn" type="button" value="查看" class='btn' />
								</div>
								<table class="_zq-table">
									<thead>
										<tr>
											<th>日期</th>
											<th>版本</th>
											<th>版本占比</th>
											<th>pv</th>
											<th>uv</th>
											<th>点击数</th>
											<th>注册人数</th>
											<th>注册/uv</th>
											<th>号码验证数</th>
											<th>号码验证率</th>
											<th>收入指标</th>
											<th>自助收入</th>
											<th>线下收入</th>
											<th>电销收入</th>
											<th>总收入</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${kpiDatas}" var="entry" varStatus="i">
											<c:set var="kpiData" value="${entry.value}"/>
											<tr>
												<c:if test="${i.index == 0}">
												<td rowspan="${fn:length(kpiDatas)*3}">${begin} ~ ${end}</td>
												</c:if>
												<td rowspan="3">${kpiData.version.versionName}</td>
												<td rowspan="3">${kpiData.version.percent}%</td>
												<td rowspan="3">${kpiData.pv}</td>
												<td rowspan="3">${kpiData.uv}</td>
												<td rowspan="3">${kpiData.click}</td>
												<td rowspan="3">${kpiData.regitCount}</td>
												<td rowspan="3">${kpiData.regitPercent}</td>
												<td rowspan="3">${kpiData.verpassCount}</td>
												<td rowspan="3">${kpiData.verpassPercent}</td>
												<td>总量</td>
												<td>${kpiData.selfSevcIncome}</td>
												<td>${kpiData.offlineIncome}</td>
												<td>${kpiData.telsellIncome}</td>
												<td rowspan="3">${kpiData.totalIncome}</td>
											</tr>
											<tr>
												<td>平均</td>
												<td>${kpiData.selfPercent}</td>
												<td>${kpiData.offlinePercent}</td>
												<td>${kpiData.telsellPercent}</td>
											</tr>
											<tr>
												<td>人数</td>
												<td>${kpiData.selfSevcUv}</td>
												<td>${kpiData.offlineUv}</td>
												<td>${kpiData.telsellUv}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<!-- END添加表格 -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="common/footer.jsp"%>
	<%@include file="common/common_js.jsp"%>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/jquery-1.9.1.min.js?v=<%=UpdateVer%>"></script>
	<link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/jquery-ui-1.10.2.date.css?v=<%=UpdateVer%>">
	<script type="text/javascript" src="<%=BasePath%>/assets/js/jquery-ui-1.10.2.date.js?v=<%=UpdateVer%>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/base.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript">
		$(function() {
		 	$.Login.needLogin();
		 	
			$( ".date" ).datepicker({
			    inline: true,
			    dateFormat: 'yy-mm-dd'
			});

			$('#queryBtn').on('click', function() {
				var loc = MAIN_URL + '/v/exprmdata-simple?pid=${pid}&cid=${cid}&begin='+$('#dateBegn').val()+'&end='+$('#dateEnd').val();
				location.href = loc;
			});
		})
	</script>
</body>
</html>
