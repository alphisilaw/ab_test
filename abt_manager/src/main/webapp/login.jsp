<%@page language="java" contentType="text/html; charset=UTF-8" 
        pageEncoding="UTF-8"%>
<%@page import="com.web.abt.m.util.CookieUtil"%>
<%@include file="common/config.jsp"%>
<%    

    Cookie[] cookies = request.getCookies();
    if (null != cookies) {
        for (Cookie cookie : cookies) {
            if ("loginUid".equals(cookie.getName()) && cookie.getValue() != null) {
                response.setStatus(302);
                response.setHeader("Location", MainUrl + "/experiment.jsp");
            }
        }
    }
    TopRnav = "";
%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head> 
        <meta charset="UTF-8" /> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" /> 
        <title>登录</title> 
        <link rel="stylesheet" href="<%=BasePath%>/assets/css/g_better.css" /> 
    </head> 
    <body> 
        <div class="login-top"> 
            <div class="login-c"> 
                <a href="/">
                    <img src="<%=BasePath%>/assets/images/logo.png" /> 
                </a>
                <span>登录</span> 
            </div> 
        </div> 
        <div class="login-con"> 
            <div class="login-img"> 
                <img src="<%=BasePath%>/assets/images/login.png" /> 
            </div> 
            <div class="login-tx">
                <ul> 
                    <li class="li-1">登录</li> 
                    <li class="li-2 email"><input type="email" id="email" placeholder="用户名/邮箱" /></li> 
                    <li class="li-3"><input type="password" id="pwd" placeholder="密码" /></li> 
                    <li class="li-4 err-msg"></li> 
                    <li class="li-5"><input type="button" id="login" value="登录" /></li> 
                    <li class="li-6"><!--<a href="#">忘记密码</a>--><a class="ty" href="<%=BasePath%>/demoLogin">直接体验</a></li> 
                </ul> 
            </div> 
        </div> 
        <div class="login-bottom"> 
            <ul class="op-lst"> 
                <li class="li-1"><a href="/">产品介绍</a></li> 
                <li><a href="/help.jsp">帮助中心</a></li> 
                <li><a href="/help.jsp#aboutUs">联系我们</a></li> 
            </ul> 
            <div class="sys">
                Copyright &copy; 2014-2015 All Rights Reserved Betterlly 
            </div> 
        </div> 
        <%@include file="common/common_js.jsp"%>
        <script> var tip = '<%=request.getAttribute("activeResult")%>';</script>
        <script src="<%=BasePath%>/assets/js/jquery-1.9.1.min.js?v=<%=UpdateVer%>"></script>
        <script type="text/javascript" src="<%=BasePath%>/assets/js/base.js?v=<%=UpdateVer%>"></script>
        <script type="text/javascript" src="<%=BasePath%>/assets/js/login.js?v=<%=UpdateVer%>"></script>
        <script type="text/javascript">
            $(function () {
                $(".login-tx .err-bor").click(function () {
                    $(this).removeClass("err-bor");
                    $(".err-msg").hide();
                })
            })
        </script>
    </body>
</html>