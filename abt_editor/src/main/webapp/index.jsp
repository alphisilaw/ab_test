<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="common/config.jsp"%>
<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="keywords" content="" />
        <meta name="description" content="" />
        <title>实验编辑器</title>
        <link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/all.css?v=<%=UpdateVer %>" />
    </head>
    <body>
        <%@include file="common/header.jsp"%>
        <div class="content index medialab-abtest-popmenu">
            <div class="ctl_bar">
                <div class="proj_info">
                    <div class="nav fl clearfix _zq-panel-wrapper">
                        <a href="#" id="proj_nav_name" class="item">项目名称</a>
                        <span class="item sp"> &gt; </span>
                        <div class="item lname">
                            <div class="read"><span>实验名称</span><i></i></div>
                            <div class="write"><input type="text"></div>
                        </div>
                    </div>
                    <div class="fr clearfix _zq-panel-wrapper _zq-funcs">
                        <div class="lab_ctls _zq-exprm-ctrl">
                            <div id="_zq-goals-count">0</div>
                            <div class="btn" id="lab_goal_setting">
                                <div class="con">目标管理</div>
                            </div>
                        </div>
                        <div class="lab_ctls _zq-exprm-ctrl">
                            <div class="btn" id="lab_uvflow_setting">
                                <div class="con">分流设置</div>
                            </div>
                        </div>

                        <div class="lab_ctls _zq-exprm-ctrl">
                            <div class="btn" id="lab_setting">
                                <div class="con">更多<i></i></div>
                                <div class="drop_box">
                                    <div t="preview" class="item">查看配置</div>
                                    <div class="line"></div>
                                    <div t="code" class="item">实验代码</div>
                                </div>
                            </div>
                        </div>

                        <div class="lab_ctls _zq-exprm-ctrl _zq-exprm-status" status='draft'>
                            <div class="btn green _zq-op" id="start_lab" c='start' style="display:none;">
                                <div class="con">启动试验</div>
                            </div>
                            <div class="btn _zq-op" id="_zq-pause-exprm" c='pause' style="display:none;">
                                <div class="con"></div>
                            </div>
                            <div class="btn _zq-status" id="_zq-status-started" style="display:none;">
                                <div class="con">运行中</div>
                            </div>
                            <div class="btn _zq-status" id="_zq-status-paused" style="display:none;">
                                <div class="con">已暂停</div>
                            </div>
                            <div class="btn" id="_zq-archive-exprm" c='archive' style="display:none;">
                                <div class="con">归档</div>
                            </div>
                            <div class="btn _zq-status" id="_zq-status-archived" style="display:none;">
                                <div class="con">已归档</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="clearfix _zq-editnav-wrapper">
                    <div class="version_wrap clearfix">

                    </div>
                    <div class="version_add">
                        <span class="txt"></span>
                        <div class="drop_box">
                            <div class="item" t="edit">在线编辑版本</div>
                            <div class="item" t="url">URL分离版本</div>
                        </div>
                    </div>
                    <div class="fr clearfix _zq-editnav">

                        <div class="goal_ctl_chk clearfix">
                            <input type="checkbox">
                            <span>高亮跟踪</span>
                        </div>
                        <div class="edit_ctls">
                            <span t="undo" class="btn undo disabled"></span>
                            <span t="redo" class="btn redo disabled"></span>
                            <span t="save" class="btn save disabled"></span>
                        </div>
                        <div class="view_ctls">
                            <div id="_zq-view-mode">
                                <span t="edit" class="btn on">编辑</span>
                                <span t="review" class="btn x">预览</span>
                                <!-- <span t="navigate" class="btn">浏览</span> -->
                                <!-- <span t="refresh" class="btn">刷新</span> -->
                                <span t='current-url' class='btn x' current-url=''>当前URL</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="page_top_tip" id="edit_top_tip-2" style="display:none;">您好，当前实验已经开始测试，为了保证数据的准确性，您所做的一切修改均不能保存。如果有变更的需求，请退出编辑器后新建或拷贝实验</div>
            <div class="page_top_tip" id="edit_top_tip--1" style="display:none;">您好，当前实验已经结束，此页面仅可查看，编辑无效。如果有变更的需求，请退出编辑器后新建或拷贝实验</div>
            <div class="page_editor" id="base_editor">
                <iframe frameborder="0" class="html_loader" id="_zq-ifr-page-editor" name="_zq-ifr-page-editor"></iframe>
            </div>
            <div class="page_loading">
                <div class="loader_wrap">
                    <div class="loader"></div>
                    <div class="txt">正在加载页面</div>
                </div>
            </div>
            <div class="edit edit-box"  id="drop_menu"  style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title">编辑目标 </span>
                    <span class="close">&times;</span> 
                </div> 
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
                <div class="dialog_title">添加页面类目标<span class="_zq-closeme"></span></div>
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
                        <div class="item">
                            <div class="l"></div>
                            <div class="r">
                                <input type="checkbox" id="_zq-set_as_main" style="vertical-align:middle;display:inline">
                                <label>作为主目标（会替换已经设置的主目标）？</label>
                            </div>
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
            <div class="edit edit-url"  style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title">编辑URL </span>
                    <span class="close">&times;</span> 
                </div> 
                <ul class="edit-ul"> 
                    <li>链接文字<input class="tx-1 edit-url-name" type="text" /></li> 
                    <li>链接地址<input class="tx-2 edit-url-link" type="text" /></li> 
                    <!--<li class="pl-70">
                        <div class="ck-bg" data-ck="1">
                            √
                        </div>在新窗口中打开</li>--> 
                    <li class="pl-70 mt-10"><button class="btn-ok">确定</button><button class="btn-cancel">取消</button></li> 
                </ul> 
            </div>

            <div class="edit edit-move"  style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title">移动 </span>
                    <span class="close">&times;</span> 
                </div> 
                <div class="edit-tx-1">
                    拖动选区可改变位置
                </div>
                <div class="btn-c">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button>
                </div>  
            </div> 

            <!-- <div class="edit  edit-htm"  style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title"> 编辑HTML </span>
                    <span class="close">&times;</span> 
                </div> 
                <div class="edit-tx">
                    <script id="container" name="content" type="text/plain">
                        这里写你的初始化内容
                    </script>
                </div>
                <div class="btn-c">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button>
                </div>  
            </div>  -->
            <div class="edit edit-htm"    style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title"> 编辑文本 </span>
                    <span class="close">&times;</span> 
                </div> 
                <div class="edit-tx">
                    <textarea class="editor"></textarea>
                </div>
                <div class="btn-c">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button>
                </div>
            </div>

            <div class="edit edit-text"    style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title"> 编辑文本 </span>
                    <span class="close">&times;</span> 
                </div> 
                <div class="edit-tx">
                    <textarea class="editor"></textarea>
                </div>
                <div class="btn-c">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button>
                </div>  
            </div>

            <div class="edit edit-drag"  style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title">调整大小 </span>
                    <span class="close">&times;</span> 
                </div> 
                <div class="edit-tx-1">
                    拖拽边角可调整大小
                </div> 
                <div class="btn-c">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button> 
                </div> 
            </div> 
            <!-- 编辑图片 -->
            <div class="edit edit-img"  style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title">编辑图片 </span>
                    <span class="close">&times;</span> 
                </div> 
                <div class="edit-ads">
                    输入图片地址<a class="upload-img" href="javascript:;">上传图片<input type="file"></a>
                    <br />
                    <input class="tx-3" type="text" />
                </div> 
                <div class="btn-group">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button>
                </div> 
            </div>
            <!-- 编辑样式 -->
            <div class="edit edit-sty"  style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title">编辑样式 </span>
                    <span class="close">&times;</span> 
                </div> 
                <div class="sty-con scroll scrollB" id="scroll2">
                    <div class="sBar">
                        <span></span>
                    </div>
                    <div class="s_main">
                        <div class="sty-con cont"> 
                            <ul class="ls" id="sty-ls"> 
                                <li class="tx act"><div class="t-0">文字</div>
                                    <ul class="item">
                                        <li><span>font-family</span><input type="text"  value="宋体" class="sty-font-family"/></li>
                                        <li><span>font-size</span><input type="text"  value="12px" class="sty-font-size" /></li>
                                        <li><span>font-weight</span><input type="text"  value="bold" class="sty-font-weight" /></li>
                                        <li><span>font-style</span><input type="text"  value="italic" class="sty-font-style" /></li>
                                        <li><span>color</span>
                                            <div class="color-tx">
                                                <input type="color" value="#ff6699" id="bgcolor" class="bgcolor sty-color" oninput="changeBackground(this.value,this)">
                                                <input type="text" class="sty-color"/>
                                            </div>
                                        </li>
                                        <li><span>text-decoration</span><input type="text"  value="" class="sty-text-decoration" /></li>
                                        <li><span>text-align</span><input type="text"  value="" class="sty-text-align" /></li>
                                        <li><span>line-height</span><input type="text"  value="" class="sty-line-height" /></li>
                                    </ul>
                                </li> 
                                <li class="tx"><div class="t-0">颜色&amp;背景</div>
                                    <ul class="item">
                                        <li><span>background-image</span><input type="text"  value="" class="sty-background-image" /></li>
                                        <li><span>background-color</span>
                                            <div class="color-tx">
                                                <input type="color" class="bgcolor sty-background-color" oninput="changeBackground(this.value,this)">
                                                <input type="text" class="sty-background-color" />
                                            </div>
                                        </li>
                                        <li><span>background-repeat</span><input type="text"  value="" class="sty-background-repeat" /></li>
                                        <li><span>background-position</span><input type="text"  value="" class="sty-background-position" /></li>
                                        <li><span>background-size</span><input type="text"  value="" class="sty-background-size" /></li>
                                        <li><span>background-attachment</span><input type="text"  value="" class="sty-background-attachment" /></li>
                                        <li><span>opacity</span><input type="text"  value="" class="sty-opacity" /></li>	
                                    </ul>
                                </li> 
                                <li class="tx"><div class="t-0">盒模型</div>
                                    <ul class="item">
                                        <li><span>width</span><input type="text"  value="" class="sty-width" /></li>
                                        <li><span>height</span><input type="text"  value="" class="sty-height" /></li>
                                        <li><span>left</span><input type="text"  value="" class="sty-left" /></li>
                                        <li><span>right</span><input type="text"  value="" class="sty-right" /></li>
                                        <li><span>top</span><input type="text"  value="" class="sty-top" /></li>
                                        <li><span>bottom</span><input type="text"  value="" class="sty-bottom" /></li>
                                        <li><span>margin-left</span><input type="text"  value="" class="sty-margin-left" /></li>
                                        <li><span>margin-right</span><input type="text"  value="" class="sty-margin-right" /></li>
                                        <li><span>margin-top</span><input type="text"  value="" class="sty-margin-top" /></li>
                                        <li><span>margin-bottom</span><input type="text"  value="" class="sty-margin-bottom" /></li>
                                        <li><span>padding-left</span><input type="text"  value="" class="sty-padding-left" /></li>
                                        <li><span>padding-right</span><input type="text"  value="" class="sty-padding-right" /></li>
                                        <li><span>padding-top</span><input type="text"  value="" class="sty-padding-top" /></li>
                                        <li><span>padding-bottom</span><input type="text"  value="" class="sty-padding-bottom" /></li>
                                        <li><span>box-shadow</span><input type="text"  value="" class="sty-box-shadow" /></li>
                                        <li><span>box-sizing</span><input type="text"  value="" class="sty-box-sizing" /></li>
                                    </ul>
                                </li> 
                                <li class="tx"><div class="t-0">边框</div>
                                    <ul class="item">
                                        <li><span>border-left-width</span><input type="text"  value="" class="sty-border-left-width" /></li>
                                        <li><span>border-left-color</span><input type="text"  value="" class="sty-border-left-color" /></li>
                                        <li><span>border-left-style</span><input type="text"  value="" class="sty-border-left-style" /></li>
                                        <li><span>border-top-width</span><input type="text"  value="" class="sty-border-top-width" /></li>
                                        <li><span>border-top-color</span><input type="text"  value="" class="sty-border-top-color" /></li>
                                        <li><span>border-top-style</span><input type="text"  value="" class="sty-border-top-style" /></li>
                                        <li><span>border-right-width</span><input type="text"  value="" class="sty-border-right-width" /></li>
                                        <li><span>border-right-color</span><input type="text"  value="" class="sty-border-right-color" /></li>
                                        <li><span>border-right-style</span><input type="text"  value="" class="sty-border-right-style" /></li>
                                        <li><span>border-bottom-width</span><input type="text"  value="" class="sty-border-bottom-width" /></li>
                                        <li><span>border-bottom-color</span><input type="text"  value="" class="sty-border-bottom-color" /></li>
                                        <li><span>border-bottom-style</span><input type="text"  value="" class="sty-border-bottom-style" /></li>
                                        <li><span>border-radius</span><input type="text"  value="" class="sty-border-radius" /></li>
                                    </ul>
                                </li> 
                                <li class="tx"><div class="t-0">定位</div>
                                    <ul class="item">
                                        <li><span>position</span><input type="text"  value=""  class="sty-position"/></li>
                                        <li><span>display</span><input type="text"  value=""  class="sty-display"/></li>
                                        <li><span>visibility</span><input type="text"  value="" class="sty-visibility" /></li>
                                        <li><span>z-index</span><input type="text"  value=""  class="sty-z-index"/></li>
                                        <li><span>overflow-x</span><input type="text"  value=""  class="sty-overflow-x"/></li>
                                        <li><span>overflow-y</span><input type="text"  value="" class="sty-overflow-y"/></li>
                                        <li><span>white-space</span><input type="text"  value=""  class="sty-white-space"/></li>
                                        <li><span>clip</span><input type="text"  value=""  class="sty-clip"/></li>
                                        <li><span>float</span><input type="text"  value=""  class="sty-float"/></li>
                                        <li><span>clear</span><input type="text"  value="" class="sty-clear" /></li>
                                    </ul>
                                </li> 
                                <li class="tx"><div class="t-0">其他</div>
                                    <ul class="item">
                                        <li><span>cursor</span><input type="text"  value=""  class="sty-cursor"/></li>
                                        <li><span>list-style-image</span><input type="text"  value=""  class="sty-list-style-image"/></li>
                                        <li><span>list-style-position</span><input type="text"  value="" class="sty-list-style-position" /></li>
                                        <li><span>list-style-type</span><input type="text"  value=""  class="sty-list-style-type"/></li>
                                        <li><span>marker-offset</span><input type="text"  value="" class="sty-marker-offset" /></li>
                                    </ul>
                                </li> 
                                <!--<li class="tx"><div class="t-0">自定义</div>
                                    <ul class="item">
                                        <li class="cus"><input type="text"  value="" /><input type="text"  value="" /><span class="del"></span></li>
                                        <li class="cus" id="add-btn"><div class="add-btn">添加</div></li>
                                    </ul>
                                </li>--> 
                            </ul> 
                        </div> 
                    </div> 
                </div> 
                <div class="btn-group">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button>
                </div> 
            </div>
            <!-- 常用编辑 -->
            <div class="edit edit-common"  style="display:none;"> 
                <div class="tit dialog_title">
                    <span class="title">常用修改 </span>
                    <span class="close">&times;</span> 
                </div> 
                <div class="sty-con scroll scrollB" id="scroll2">
                    <div class="sBar">
                        <span></span>
                    </div>
                    <div class="s_main">
                        <div class="sty-con cont">
                            <ul class="ls" id="sty-ls">
                                <li class="tx act">
                                    <ul class="item">
                                        <li>
                                            <span>字体大小</span>
                                            <input type="text"  value="12px" class="com-font-size" />
                                        </li>
                                        <li>
                                            <span>字体颜色</span>
                                            <div class="color-tx">
                                                <input type="color" class="bgcolor com-color" oninput="changeBackground(this.value,this)">
                                                <input type="text" class="com-color"/>
                                            </div>
                                        </li>
                                        <li>
                                            <span>背景颜色</span>
                                            <div class="color-tx">
                                                <input type="color" class="bgcolor com-background-color" oninput="changeBackground(this.value,this)">
                                                <input type="text" class="com-background-color"  />
                                            </div>
                                        </li>
                                        <li>
                                            <span>背景图片</span>
                                            <input type="text" class="com-background-image" />
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="btn-group">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button>
                </div>
            </div>
            <!-- 居中 -->
            <div class="edit edit-center"  style="display:none;">
                <div class="tit dialog_title">
                    <span class="title">元素居中 </span>
                    <span class="close">&times;</span>
                </div>
                <div class="edit-tx-1">
                    点击"确定"按钮使元素居中
                </div>
                <div class="btn-c">
                    <button class="btn-ok">确认</button>
                    <button class="btn-cancel">取消</button>
                </div>
            </div>
        </div>
        <%@include file="common/footer.jsp"%>
        <%@include file="common/common_js.jsp"%>
        <link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/js/codemirror/codemirror.css?v=<%=UpdateVer %>" />
        <link rel="stylesheet" type="text/css" href="<%=BasePath%>/assets/css/g_edit.css?v=<%=UpdateVer %>" />

        <script type="text/javascript" src="<%=BasePath%>/assets/js/jquery-1.7.1.min.js?v=<%=UpdateVer %>"></script>
        <script type="text/javascript" src="<%=BasePath%>/assets/js/base.js?v=<%=UpdateVer %>"></script>
        <script type="text/javascript" src="<%=BasePath%>/assets/js/codemirror/codemirror.js?v=<%=UpdateVer %>"></script>
        <script type="text/javascript" src="<%=BasePath%>/assets/js/highcharts.js?v=<%=UpdateVer %>"></script>
        <script type="text/javascript" src="<%=BasePath%>/assets/js/highcharts-more.js?v=<%=UpdateVer %>"></script>
        <script type="text/javascript" src="<%=BasePath%>/assets/js/editor.js?v=<%=UpdateVer %>"></script>
        <script type="text/javascript" src="<%=BasePath%>/assets/js/jquery.zclip.min.js?v=<%=UpdateVer %>"></script>
        <script type="text/javascript" src="<%=MainUrl%>/assets/js/jquery.qrcode.min.js?v=<%=UpdateVer %>"></script>
        <script src="<%=BasePath%>/assets/js/edit_drag.js"></script>
        <script src="<%=BasePath%>/assets/js/scrollBar.js"></script>
        <script src="<%=BasePath%>/assets/js/ReSize.js"></script>
        <script src="<%=BasePath%>/assets/js/ueditor.config.js"></script>
        <script src="<%=BasePath%>/assets/js/ueditor.all.js"></script>
        <script src="<%=BasePath%>/assets/js/lang/zh-cn/zh-cn.js"></script>
        <script>
            var tmp_id;
            var tmp_sel;
            $(function () {
                /*选择编辑项*/
//                $("#edit-ls").find("div").click(function () {
//                    var _tmp = $(this).attr("id");
//                    $("." + $(this).attr("id")).show();
//                    $(".edit-box").hide();
//                })

                /*编辑url里的check状态切换*/
                /*$(".ck-bg").live("click",function () {
                    if ($(this).data("ck") == "1") {
                        $(this).css("color", "#fff");
                        $(this).data("ck", "0");
                    } else {
                        $(this).css("color", "#32c689");
                        $(this).data("ck", "1");
                    }
                })*/

                /*编辑样式*/
                $(".sty-con .tx").click(function () {
                    $(this).siblings().removeClass("act");
                    $(this).addClass("act");
                    var oDiv1 = document.getElementById('sty-ls');
                    var oDiv2 = document.getElementById('scroll2');
                    if (oDiv1.offsetHeight < 490) {
                        $(".sBar").find("span").css("height", 0).css("top", 0);
                        scrollBar(oDiv2, '0');
                    } else {
                        scrollBar(oDiv2, 't');
                    }
                })
				
                /*修改颜色*/
                $(".bgcolor").next().blur(function () {
                    var _tmp = $(this).val();
                    $(this).siblings().val(_tmp);
                });

                /*remove用户自定义属性*/
                $(".cus .del").click(function () {
                    $(this).parent().remove();
                })
            })

            function changeBackground(colorValue, _tmp) {
                document.body.style.bakcgroundColor = colorValue;
                $(_tmp).attr("value", colorValue);
                $(_tmp).next().attr("value", colorValue);
            }

            $(function () {
                /*add用户自定义属性*/
                /*$("#add-btn").click(function () {
                    var tmp = '<li class="cus"><input type="text"  value="" /><input type="text"  value="" /><div class="del"></div></li>';
                    $(this).before(tmp);
                })*/
                /*remove用户自定义属性*/
                /*$(document).on('click', ".del", (function () {
                    $(this).parent().remove();
                }))*/


            })


            // 文本编辑器
            UE.getEditor('container', {
                //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
                toolbars: [['Source','|','fontfamily', 'fontsize', '|', 'forecolor', 'backcolor', 'bold', 'italic', 'underline', 'strikethrough', '|', 'justifyleft', 'justifyright', 'justifycenter', '|', 'undo', 'redo']],
                //focus时自动清空初始化时的内容
                autoClearinitialContent: true,
                //关闭字数统计
                wordCount: false,
                //关闭elementPath
                elementPathEnabled: false,
                //默认的编辑区域高度
                initialFrameHeight: 300
                        //更多其他参数，请参考ueditor.config.js中的配置项
            })
        </script>
    </body>
</html>








