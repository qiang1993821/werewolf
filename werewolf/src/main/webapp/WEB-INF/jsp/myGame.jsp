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
  <link rel="stylesheet" href="/static/css/weui.css"/>
  <link rel="stylesheet" href="/static/css/reset.css">
  <link rel="stylesheet" href="/static/css/index.css">
  <link rel="stylesheet" href="/static/css/example.css">
  <script src="/static/js/zepto.min.js"></script>
  <title>我的房间</title>
</head>
<body>
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
<a href="index" class="weui_btn weui_btn_warn">返回</a>
<c:choose>
  <c:when test="${not empty gameList}">
  <div class="page home js_show">
    <div class="page">
      <div class="page__bd">
      <c:forEach items="${gameList}" var="game">
        <div class="weui-form-preview">
          <div class="weui-form-preview__hd">
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">房间名称</label>
              <em class="weui-form-preview__value">${game.name}</em>
            </div>
          </div>
          <div class="weui-form-preview__bd">
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">平民</label>
              <span class="weui-form-preview__value">${game.cNum}个</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">狼人</label>
              <span class="weui-form-preview__value">${game.wNum}个</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">神</label>
              <span class="weui-form-preview__value">${game.godNum}个</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">神种类</label>
              <span class="weui-form-preview__value">${game.god}</span>
            </div>
          </div>
          <c:choose>
            <c:when test="${game.start == 0}">
              <div class="weui-form-preview__ft">
                <a class="weui-form-preview__btn weui-form-preview__btn_primary" href="openGame?id=${game.id}">开始游戏</a>
              </div>
            </c:when>
            <c:otherwise>
              <div class="weui-form-preview__ft">
                <a class="weui-form-preview__btn weui-form-preview__btn_warn" href="javascript:closeGame(${game.id})">结束游戏</a>
                <a class="weui-form-preview__btn weui-form-preview__btn_primary" href="game?id=${game.id}">进入游戏</a>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
        <br>
      </c:forEach>
      </div>
    </div>
  </div>
  </c:when>
  <c:otherwise>
    <script type="text/javascript">
      $(".weui_dialog_title").html("提示");
      $(".weui_dialog_bd").html("您还未创建房间");
      $('#url').attr('href',"javascript:closeDialog()");
      $(".weui_dialog_alert").removeAttr("hidden");
    </script>
  </c:otherwise>
</c:choose>
<script type="text/javascript">
  $(function() {
    if(!localStorage.wwid || localStorage.wwid=='undefined'){
      location.href = "login";
    }
  });
  function closeDialog(){
    location.href = "index";
  }
  function closeGame(gameId){
    $.ajax({
      url: 'game/closeGame?gameId='+gameId,
      type: 'GET',
      dataType: 'json',
      error: function () {
        $(".weui_dialog_title").html("关闭失败");
        $(".weui_dialog_bd").html("服务器被海王类劫持了！");
        $('#url').attr('href',"javascript:closeDialog()");
        $(".weui_dialog_alert").removeAttr("hidden");
      },
      success: function (data) {
        if(data.code==1){
          $(".weui_dialog_title").html("关闭成功");
          $(".weui_dialog_bd").html("点击\"开始游戏\"进入新的一局吧~");
          $('#url').attr('href',"javascript:closeDialog()");
        }else{
          $(".weui_dialog_title").html("关闭失败");
          $(".weui_dialog_bd").html("");
          $('#url').attr('href',"javascript:closeDialog()");
        }
        $(".weui_dialog_alert").removeAttr("hidden");
      }
    });
  }
</script>
</body>
</html>
