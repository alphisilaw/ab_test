<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="common/config.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head> 
        <meta charset="UTF-8" /> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" /> 
        <title>Betterlly--产品介绍</title> 
        <link rel="stylesheet" href="<%=BasePath%>/assets/css/g_better.css" />
    </head> 
    <body> 
        <div class="b-top"> 
            <div class="b-con"> 
                <a href="/">
                    <img src="<%=BasePath%>/assets/images/logo.png" /> 
                </a>
                <ul> 
                    <li class="li-1"><a href="<%=BasePath%>/help.jsp">帮助中心</a></li> 
                    <li><a href="<%=BasePath%>/help.jsp#aboutUs">联系我们</a></li> 
                    <li><a href="http://blog.96333.com">博客</a></li> 
                    <li><a href="<%=BasePath%>/login.jsp">登录</a></li> 
                </ul> 
            </div> 
        </div> 
        <div class="item-1"> 
            <div class="c-con video-1"> 
                <div class="tx fl-l al-1"> 
                    <div class="tx-1">科学、简单的A/B测试平台 </div> 
                    <p> 我们帮你优化用户体验 <br />提升产品活跃度 <br />增加产品收益 </p> 
                    <a class="a-1" href="<%=BasePath%>/demoLogin">查看演示</a> 
                </div> 
                <div class="video-bg al-r"> 
                    <div class="video-box"> 
                        <video  id="video1" width="528" height="297"  poster="<%=BasePath%>/assets/images/init.png">
                            <source src="<%=BasePath%>/assets/video/Better-0428.mp4?v=<%=UpdateVer%>" type="video/mp4" />
                            <object data="<%=BasePath%>/assets/video/Better-0428.mp4?v=<%=UpdateVer%>" width="528" height="297">
                                <embed width="528" height="297" src="/i/movie.swf?v=<%=UpdateVer%>" />
                            </object>
                        </video>
                    </div>
                </div> 
            </div> 
        </div> 
        <div class="item-2"> 
            <div class="c-con"> 
                <div class="tx fl-r al-r"> 
                    <div class="tx-1  p-1">
                        帮你节约成本 
                    </div> 
                    <p class="p-1"> 拥有可视化编辑页面 ,无需技术基础，自由设置<br /> 自动将人群引至转化率最高的版本，省去新旧版本切换人工过程 </p> 
                    <a class="a-2" href="<%=BasePath%>/login.jsp">立即体验 &gt;</a> 
                </div> 
                <div class="img-1 al-l"> 
                    <img src="<%=BasePath%>/assets/images/screen-2.png" /> 
                </div> 
            </div> 
        </div> 
        <div class="item-3"> 
            <div class="c-con"> 
                <div class="tx fl-l al-1"> 
                    <div class="tx-1">
                        帮你分析用户行为数据 
                    </div> 
                    <p> 统计用户点击、下单、付费等行为数据<br /> 精确分析不同版本对当前页面转化率及整个产品收益的影响 </p> 
                    <a class="a-3" href="<%=BasePath%>/login.jsp">立即体验 &gt;</a> 
                </div> 
                <div class="img-1 al-r"> 
                    <img src="<%=BasePath%>/assets/images/screen-3.png" /> 
                </div> 
            </div> 
        </div> 
        <div class="item-4"> 
            <div class="c-con"> 
                <div class="tx fl-r al-r"> 
                    <div class="tx-1">
                        帮你决策 
                    </div> 
                    <p> 实时呈现各个版本的明细报表和真实数据 ，为决策提供依据<br /> 当多个测试同时运行时，智能分析交叉影响，给出整体最佳组合方案 </p> 
                    <a class="a-4" href="<%=BasePath%>/login.jsp">立即体验 &gt;</a> 
                </div> 
                <div class="img-1 al-l"> 
                    <img src="<%=BasePath%>/assets/images/screen-4.png" /> 
                </div> 
            </div> 
        </div> 
        <div class="item-5"> 
            <div class="c-con"> 
                <span>Betterlly帮你优化用户体验、提升产品活跃度、增加产品收益</span> 
                <a class="a-5 fl-r" href="<%=BasePath%>/login.jsp">立即体验</a> 
            </div> 
        </div> 
        <div class="b-bottom"> 
            <div class="c-con"> 
                <div> 
                    <img src="<%=BasePath%>/assets/images/logo.png" /> 
                </div> 
                <p class="inf-lst">地址：深圳市南山区高新技术产业园北区朗山路7号中航工业南航大厦3楼<br />邮编：518057<br />联系电话：13537522920<br />意见反馈：xia.xie@zhenai.com<br />QQ群：311784469</p> 
                <p class="wx"> 微信扫一扫<br /> <img src="<%=BasePath%>/assets/images/wx.png" /> </p> 
                <div class="sys">
                    Copyright &copy; 2014-2015 All Rights Reserved Betterlly 
                </div> 
            </div> 
        </div>
        <a title="返回顶部" id="to-top" href="javascript:void(0);" onclick="javascript:window.scrollTo(0, 0);">&lt;</a>
        <%@include file="common/common_js.jsp"%>
        <script src="<%=BasePath%>/assets/js/jquery-1.9.1.min.js?v=<%=UpdateVer%>"></script>
        <script type="text/javascript">
            $(function () {
                /*返回顶部*/
                window.onscroll = function () {
                    var winScroll = document.documentElement.scrollTop || document.body.scrollTop;
                    if (winScroll > 0) {
                        $("#to-top").show();
                    } else if (winScroll == 0) {
                        $("#to-top").hide();
                    }
                }

                /*视频播放控制*/
                var _flag = false;
                $(".video-box").click(function () {
                    if (!_flag) {
                        $('video').attr("controls", "controls");
                        $('video').trigger('play');
                        _flag = true;
                    } else {
                        $('video').trigger('pause');
                        _flag = false;
                    }
                })
                var myVid = document.getElementById("video1");
                myVid.addEventListener("ended",
                        function () {
                            _flag = false;
                        },
                        false);

            })
        </script>
    </body>
</html>