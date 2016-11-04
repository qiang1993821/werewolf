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
  <link rel="stylesheet" href="static/css/weui.min.css"/>
  <link rel="stylesheet" href="static/css/weui.css"/>
  <link rel="stylesheet" href="static/css/login.css">
  <link rel="stylesheet" href="static/css/example.css">
  <script src="/static/js/zepto.min.js"></script>
  <title>昨夜详情</title>
</head>
<body class="login">
<div style="height: 10%;">
</div>
<c:forEach items="${nightInfo}" var="info">
  <span class="name"><font color="#32cd32" size="6" id="role">${info}</font></span>
</c:forEach>
<a href="game?id=${gameId}" class="weui-btn weui-btn_plain-primary btn">返回</a>
</body>
</html>
