<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="common/config.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>abtest editor</title>
	<link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/all.css?v=<%=UpdateVer %>" />
</head>
<body>
	<%@include file="common/header.jsp"%>
	<div class="content index">
		<div class="ctl_bar">
			<div class="proj_info">
				<div class="nav">
					<a href="#" id="proj_nav_name" class="item">项目名称</a>
					<span class="item sp"> &gt; </span>
					<div class="item lname">
				location.href = EDITOR_URL+'/v/experiment?pid='+pid;		<div class="read"><span>实验名称</span><i></i></div>
						<div class="write"><input type="text"></div>
					</div>
				</div>
				<div class="fr clearfix">
					<div class="lab_ctls">
						<div class="btn green" id="start_lab" style="display:none;">
							<div class="con">启动试验</div>
						</div>
					</div>
					<div class="lab_ctls">
						<div class="btn" id="lab_goal_setting">
							<div class="con">管理目标<!-- <i></i> --></div>
							<!-- <div class="drop_box">
								<div class="item">管理目标</div>
							</div> -->
						</div>
					</div>
					<div class="lab_ctls">
						<div class="btn" id="lab_setting">
							<div class="con">实验设置<i></i></div>
							<div class="drop_box">
								<div t="assign" class="item">分流设置</div>
								<div t="limit" class="item">限定实验人群</div>
								<div class="line"></div>
								<div t="code" class="item">实验代码</div>
							</div>
						</div>
					</div>
					<div class="edit_ctls">
						<span t="save" class="btn save disabled"></span>
					</div>
				</div>
			</div>
			<div class="clearfix">
				<div class="version_wrap clearfix">
					
				</div>
				<div class="version_add">
					<span class="txt">+</span>
					<div class="drop_box">
						<div class="item" t="edit">在线编辑版本</div>
						<div class="item" t="url">URL分离版本</div>
					</div>
				</div>
				<div class="fr clearfix">
					<div class="lab_review">查看配置信息</div>
					<div class="goal_ctl_chk clearfix">
						<input type="checkbox">
						<span>高亮跟踪元素</span>
					</div>
					<div class="edit_ctls">
						<span t="undo" class="btn undo disabled"></span>
						<span t="redo" class="btn redo disabled"></span>
					</div>
					<div class="view_ctls">
						<div class="tog_btn" style="display:none;">
							<span t="edit" class="btn on">编辑</span>
							<span t="view" class="btn">浏览</span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="page_top_tip" id="edit_top_tip" style="display:none;">您好，当前实验已经开始测试，为了保证数据的准确性，您所做的一切修改均不能保存。如果有变更的需求，请退出编辑器后新建或拷贝实验</div>
		<div class="page_editor" id="base_editor">
			<iframe frameborder="0" class="html_loader"></iframe>
		</div>
		<div class="page_loading">
			<div class="loader_wrap">
				<div class="loader"></div>
				<div class="txt">正在加载页面</div>
			</div>
		</div>
		<div class="drop_menu" id="drop_menu">
			<div class="title">元素div</div>
			<div class="menu_list">
				
			</div>
		</div>
		<div class="dialog" id="code_editer" style="display:none;">
			<div class="dialog_title">title</div>
			<div class="content">
				<div id="code_area" style="width:500px; height:250px;"></div>
			</div>
			<div class="ctl_wrap">
				<span class="btn ok_btn">确定</span>
				<span class="btn cancel_btn">取消</span>
			</div>
		</div>
		<div class="dialog" id="goal_editor" style="display:none;">
			<div class="dialog_title">添加页面类目标</div>
			<div class="content goal_set">
				<div class="s_form">
					<div class="item">
						<div class="l">目标名称：</div>
						<div class="r"><input type="text" id="goal_name" style="width:300px;"></div>
					</div>
					<div class="item">
						<div class="l">目标类型：</div>
						<div class="r"><select name="" id="goal_type"></select></div>
					</div>
					<div class="item">
						<div class="l">目标元素：</div>
						<div class="r"><input type="text" id="goal_slt" style="width:300px;"></div>
					</div>
					<div class="item" id="add_tip">
						<div class="cen tip">建议您直接在页面选取要追踪的元素右键点击管理目标设置</div>
					</div>
				</div>
			</div>
			<div class="ctl_wrap">
				<span class="btn ok_btn">确定</span>
				<span class="btn cancel_btn">取消</span>
			</div>
		</div>
	</div>
	<%@include file="common/footer.jsp"%>
	<%@include file="common/common_js.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/js/codemirror/codemirror.css?v=<%=UpdateVer %>" />

	<script type="text/javascript" src="<%=BasePath%>/assets/js/jquery-1.7.1.min.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/base.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/codemirror/codemirror.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/highcharts.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/highcharts-more.js?v=<%=UpdateVer %>"></script>
	<script type="text/javascript" src="<%=BasePath%>/assets/js/editor.js?v=<%=UpdateVer %>"></script>
</body>
</html>








