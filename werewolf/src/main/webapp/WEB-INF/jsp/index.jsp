<!DOCTYPE html >
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta name="wap-font-scale" content="no">
<link rel="stylesheet" href="/static/css/weui.min.css"/>
<link rel="stylesheet" href="/static/css/reset.css">
<link rel="stylesheet" href="/static/css/index.css">
<link rel="stylesheet" href="/static/css/example.css">
<title>首夜辅助</title>
</head>
<body class="common">
<div id="qyp"></div>
    <div class="bd spacing">
        <a href="addGame" class="weui_btn weui_btn_primary">
            新建房间
        </a>
        <a href="javascript:myGame()" class="weui_btn weui_btn_default">
            我的房间
        </a>
        <a href="javascript:logout()" class="weui_btn weui_btn_warn">
            退出登录
        </a>
    </div>
    <!-- 搜索结束 -->
    <script src="/static/js/zepto.min.js"></script>
    <script src="/static/js/router.min.js"></script>
    <script type="text/javascript">
        $(function() {
            if(!localStorage.wwid || localStorage.wwid=='undefined'){
                location.href = "login";
            }
        });
        //登出
        function logout(){
            localStorage.clear();
            location.href = "login";
        }
        //我的房间
        function myGame(){
            location.href = "myGame?uid="+localStorage.wwid;
        }
    </script>
    <style type="text/css">
        #qyp{
            height:70%;
            background:url(/static/images/head.jpg) center top no-repeat;
            background-size:100%;
        }
    </style>
</body>
</html>





