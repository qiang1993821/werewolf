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
  <title>${name}</title>
</head>
<body>
    <div style='margin:0 auto;width:0px;height:0px;overflow:hidden;'>
      <img src="static/images/title.jpg" width='700'>
    </div>
    <input type="hidden" id="gameId" value="${gameId}">
  <div class="page home js_show">
    <div class="page">
      <div class="page__bd">
          <div class="weui-form-preview">
            <div class="weui-form-preview__hd">
              <label class="weui-form-preview__label">房间名称</label>
              <em class="weui-form-preview__value">${name}</em>
            </div>
            <div class="weui-form-preview__bd">
              <div class="weui-form-preview__item">
                <label class="weui-form-preview__label">当前玩家：</label>
              </div>
              <c:forEach items="${member}" var="user">
              <div class="weui-form-preview__item">
                <span class="weui-form-preview__value">${user}</span>
              </div>
              </c:forEach>
            </div>
                <div class="weui-form-preview__ft">
                  <a class="weui-form-preview__btn weui-form-preview__btn_warn" href="javascript:closeGame(${gameId})">取消报名</a>
                  <a class="weui-form-preview__btn weui-form-preview__btn_primary" href="javascript:closeGame(${gameId})">查看身份</a>
                </div>
          </div>
          <br>
      </div>
    </div>
  </div>
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
    var gameId = $("#gameId").val();
    if(!localStorage.wwjoin || localStorage.wwid=='undefined' || localStorage.wwjoin!=gameId){
      location.href = "game?id="+gameId;
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
