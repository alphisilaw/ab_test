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
<link rel="stylesheet" type="text/css" href="<%=MainUrl%>/assets/css/_zq-all.css?v=<%=UpdateVer%>" />
<!-- <link rel="stylesheet" type="text/css" href="<%=MainUrl%>/assets/css/jquery-ui.min.css?v=<%=UpdateVer%>" /> -->
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
			<div class="manage-content">
				<div class="channel-manage" style="line-height: 44px;">
					<input type="text" value="${begin}" class="date" id="dateBegn">
					<input type="text" value="${end}" class="date" id="dateEnd">
					<input id="queryBtn" type="button" value="查看" class='btn' />
					<input id="downloadBtn" type="button" value="导出数据" class='btn' />
				</div>

				<table class='_zq-expri-table'>
					<thead>
						<tr>
							<th>日期</th>
							<th>渠道名称</th>
							<th>渠道号</th>
							<th>子渠道号</th>
							<th>模板名称</th>
							<th>pv</th>
							<th>uv</th>
							<th>注册人数</th>
							<th>注册/uv</th>
							<th>号码验证数</th>
							<th>号码验证率</th>
							<th>自助收入</th>
							<th>线下收入</th>
							<th>电销收入</th>
							<th>总收入</th>
							<th>Arpu</th>
						</tr>
					</thead>
					<tbody id="channelList">
						<c:forEach items="${resultLst}" var="result">
						<c:set var="size" value="${fn:length(result.kpiDatas)}" />
						<c:if test="${size == 0}">
							<tr>
								<td>${result.date}</td>
								<td>${result.ChannelName}</td>
								<td>${result.channelId}</td>
								<td>${result.subid}</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</c:if>
						<c:if test="${size > 0}">
							<c:forEach items="${result.kpiDatas}" var="kpiData" varStatus="i">
								<tr>
									<c:if test="${i.index == 0}">
										<td rowspan="${size}">${result.date}</td>
										<td rowspan="${size}">${result.ChannelName}</td>
										<td rowspan="${size}">${result.channelId}</td>
										<td rowspan="${size}">${result.subid}</td>
									</c:if>
									<td>${kpiData.tempName}</td>
									<td>${kpiData.pv}</td>
									<td>${kpiData.uv}</td>
									<td>${kpiData.regitCount}</td>
									<td>${kpiData.regitPercent}</td>
									<td>${kpiData.verpassCount}</td>
									<td>${kpiData.verpassPercent}</td>
									<td>${kpiData.selfSevcIncome}</td>
									<td>${kpiData.offlineIncome}</td>
									<td>${kpiData.telsellIncome}</td>
									<td>${kpiData.totalIncome}</td>
									<td>${kpiData.totalPercent}</td>
								</tr>
							</c:forEach>
						</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<%@include file="common/footer.jsp"%>
	<%@include file="common/common_js.jsp"%>
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/jquery-1.9.1.min.js?v=<%=UpdateVer%>"></script>
	<link rel="stylesheet" type="text/css" href="<%=MainUrl%>/assets/css/jquery-ui-1.10.2.date.css?v=<%=UpdateVer%>">
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/jquery-ui-1.10.2.date.js?v=<%=UpdateVer%>"></script>
	<script type="text/javascript" src="<%=MainUrl%>/assets/js/base.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript">
		$( ".date" ).datepicker({
		    inline: true,
		    dateFormat: 'yy-mm-dd'
		});

		$('#queryBtn').on('click', function() {
			var loc = CHANNEL_URL + '/channel-data?channelId=${channelId}&subChannelId=${subChannelId}&begin='+$('#dateBegn').val()+'&end='+$('#dateEnd').val();

			location.href = loc;
		});

		$('#downloadBtn').on('click', function() {
			var loc = CHANNEL_URL + '/channel-data-excel?channelId=${channelId}&subChannelId=${subChannelId}&begin='+$('#dateBegn').val()+'&end='+$('#dateEnd').val();

			location.href = loc;
		});
	</script>

</body>
</html>
