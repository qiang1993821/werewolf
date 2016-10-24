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
  <title>${gameName}</title>
</head>
<body class="login">
<div style='margin:0 auto;width:0px;height:0px;overflow:hidden;'>
  <img src="static/images/title.jpg" width='700'>
</div>
<div style="height: 20%;">
  <input type="hidden" id="gameId" value="${gameId}">
  <input type="hidden" id="owner" value="${owner}">
</div>
<div style="height: 10%;">
<span class="name"><font color="#32cd32">输入昵称后点击报名</font></span>
</div>
<br>
<hr>
<div class="username"><input type="text" placeholder="请输入昵称" id="username"></div>
<br>
<a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="saveName()">报名</a>
<div class="weui_dialog_alert" hidden="hidden">
  <div class="weui_mask"></div>
  <div class="weui_dialog">
    <div class="weui_dialog_hd"><strong class="weui_dialog_title">弹窗标题</strong></div>
    <div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div>
    <div class="weui_dialog_ft">
      <a href="#" class="weui_btn_dialog primary" id="url">确定</a>
    </div>
  </div>
</div>
<script type="text/javascript">
  $(function() {
    var owner = $("#owner").val();
    var gameId = $("#gameId").val();
    if(localStorage.wwid && owner && localStorage.wwid==owner){
      $(".weui_dialog_title").html("您是房主");
      $(".weui_dialog_bd").html("转发本页给好友，即可报名参与本场游戏！");
      $('#url').attr('href',"javascript:closeDialog()");
      $(".weui_dialog_alert").removeAttr("hidden");
    }
    if(localStorage.wwjoin && localStorage.wwjoin==gameId){
      location.href = "start?gameId="+gameId;
    }
  });
  //关闭对话框
  function closeDialog(){
    $(".weui_dialog_alert").attr("hidden","hidden");
  }
</script>
</body>
</html>
