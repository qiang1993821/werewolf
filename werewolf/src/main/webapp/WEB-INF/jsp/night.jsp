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
  <title>${game.name}</title>
</head>
<body class="login">
<div style='margin:0 auto;width:0px;height:0px;overflow:hidden;'>
  <img src="static/images/title.jpg" width='700'>
</div>
<div style="height: 10%;">
  <input type="hidden" id="gameId" value="${game.id}">
  <input type="hidden" id="owner" value="${game.uid}">
</div>
</div>
<div style="height: 10%;">
  <span class="name"><font>你的身份是</font></span>
</div>
<div style="height: 10%;">
  <span class="name"><font color="#32cd32" size="50" id="role"></font></span>
</div>
<br>
<hr>
<br>
<div id="werewolf" hidden="hidden">
  <div class="weui_cell">
    <div class="weui_cell_hd"><label class="weui_label">要杀的人：</label></div>
    <div class="weui_cell_bd weui_cell_primary">
      <select class="weui_select" id="kill"></select>
    </div>
  </div>
  <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="werewolf()">确认</a>
</div>
<div id="prophet" hidden="hidden">
  <div class="weui_cell">
    <div class="weui_cell_hd"><label class="weui_label">要验的人：</label></div>
    <div class="weui_cell_bd weui_cell_primary">
      <select class="weui_select" id="guess"></select>
    </div>
  </div>
  <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="prophet()">确认</a>
</div>
<div id="guard" hidden="hidden">
  <div class="weui_cell">
    <div class="weui_cell_hd"><label class="weui_label">要守卫的人：</label></div>
    <div class="weui_cell_bd weui_cell_primary">
      <select class="weui_select" id="protect"></select>
    </div>
  </div>
  <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="guard()">确认</a>
</div>
<div id="witch" hidden="hidden">
  <div id="death">
    <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="death()">是否用药</a>
  </div>
  <div id="witchBtn" hidden="hidden">
    <div class="weui_cell">
      <div class="weui_cell_hd"><label class="weui_label">昨夜死的人：</label></div>
      <div class="weui_cell_bd weui_cell_primary">
        <select class="weui_select" id="diedMan">
            <option value="${diedMan.id}">${diedMan.name}</option>
        </select>
      </div>
    </div>
    <div class="weui_cell">
      <div class="weui_cell_hd"><label class="weui_label">是否救人：</label></div>
      <div class="weui_cell_bd weui_cell_primary">
        <select class="weui_select" id="save">
          <option value="0">不救</option>
          <option value="1">救</option>
        </select>
      </div>
    </div>
    <div class="weui_cell">
      <div class="weui_cell_hd"><label class="weui_label">要毒的人：</label></div>
      <div class="weui_cell_bd weui_cell_primary">
        <select class="weui_select" id="poison"></select>
      </div>
    </div>
    <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="witch()">确认</a>
  </div>
</div>
<div id="cupid" hidden="hidden">
  <div class="weui_cell">
    <div class="weui_cell_hd"><label class="weui_label">情侣1：</label></div>
    <div class="weui_cell_bd weui_cell_primary">
      <select class="weui_select" id="lover1"></select>
    </div>
  </div>
  <div class="weui_cell">
    <div class="weui_cell_hd"><label class="weui_label">情侣2：</label></div>
    <div class="weui_cell_bd weui_cell_primary">
      <select class="weui_select" id="lover2"></select>
    </div>
  </div>
  <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="cupid(1)">确认</a>
  <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="cupid(0)">不连任何情侣</a>
</div>
<div id="lover" hidden="hidden">
  <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="lover()">查看情侣</a>
</div>
<div id="killed" hidden="hidden">
  <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="killed()">查看结果</a>
</div>
<div class="weui_dialog_alert" hidden="hidden">
  <div class="weui_mask"></div>
  <div class="weui_dialog">
    <div class="weui_dialog_hd"><strong class="weui_dialog_title">弹窗标题</strong></div>
    <div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div>
    <div class="weui_dialog_ft">
      <a href="#" class="weui_btn_dialog primary" id="url">查看</a>
      <%--$(".weui_dialog_ft").append('<a href="#" class="weui_btn_dialog default" id="cancel">取消</a>');--%>
    </div>
  </div>
</div>
<script type="text/javascript">
  var gameId = $("#gameId").val();
  $(function() {
    var owner = $("#owner").val();
    if(localStorage.wwjoin && localStorage.wwjoin==gameId && localStorage.wwUid){
      $.ajax({
        url: 'game/hasJoin',
        type: 'POST',
        data:{
          gameId:gameId,
          uid:localStorage.wwUid
        },
        dataType: 'json',
        error: function () {
          $(".weui_dialog_title").html("网络异常");
          $(".weui_dialog_bd").html("服务器被海王类劫持了！");
          $('#url').attr('href',"javascript:closeDialog(1)");
          $(".weui_dialog_alert").removeAttr("hidden");
        },
        success: function (data) {
          if(data.code==1){//已报名跳正常显示
            isShowBtn();
            if(localStorage.wwid && owner && localStorage.wwid == owner){//房主显示结果
              $("#killed").removeAttr("hidden");
            }
          }else{
            $(".weui_dialog_title").html("您不在本场游戏中");
            $(".weui_dialog_bd").html("您可关注公众号“弓一”或在添加公众号中搜索微信号roc_gongyi创建房间");
            $('#url').attr('href',"#");
            $(".weui_dialog_alert").removeAttr("hidden");
          }
        }
      });
    }else{
      $(".weui_dialog_title").html("您不在本场游戏中");
      $(".weui_dialog_bd").html("您可关注公众号“弓一”或在添加公众号中搜索微信号roc_gongyi创建房间");
      $('#url').attr('href',"#");
      $(".weui_dialog_alert").removeAttr("hidden");
    }
  });
  //显示功能
  function isShowBtn(){
    $.ajax({
      url: 'game/showBtn',
      type: 'POST',
      data:{
        gameId:gameId,
        uid:localStorage.wwUid
      },
      dataType: 'json',
      error: function () {
        $(".weui_dialog_title").html("网络异常");
        $(".weui_dialog_bd").html("服务器被海王类劫持了！");
        $('#url').attr('href',"javascript:closeDialog(1)");
        $(".weui_dialog_alert").removeAttr("hidden");
      },
      success: function (data) {
        if(data){
          $("#role").html(data.roleName);
          if(data.hasBtn == 1 && data.isShow == 0){
            if(data.role == "#werewolf"){
              for(var index in data.member){
                $("#kill").append('<option value='+data.member[index].id+'>'+data.member[index].name+'</option>');
              }
            }
            if(data.role == "#prophet"){
              for(var index in data.member){
                $("#guess").append('<option value='+data.member[index].id+'>'+data.member[index].name+'</option>');
              }
            }
            if(data.role == "#guard"){
              for(var index in data.member){
                $("#protect").append('<option value='+data.member[index].id+'>'+data.member[index].name+'</option>');
              }
            }
            if(data.role == "#witch"){
              for(var index in data.member){
                $("#poison").append('<option value='+data.member[index].id+'>'+data.member[index].name+'</option>');
              }
            }
            if(data.role == "#cupid"){
              for(var index in data.member){
                $("#lover1").append('<option value='+data.member[index].id+'>'+data.member[index].name+'</option>');
                $("#lover2").append('<option value='+data.member[index].id+'>'+data.member[index].name+'</option>');
              }
            }
            $(data.role).removeAttr("hidden");
          }
          if(data.lover == 1){
            $("#lover").removeAttr("hidden");
          }
        }else{
          $(".weui_dialog_title").html("数据异常");
          $(".weui_dialog_bd").html("个人数据获取异常");
          $('#url').attr('href',"#");
          $(".weui_dialog_alert").removeAttr("hidden");
        }
      }
    });
  }
  //关闭对话框
  function closeDialog(code){
    if(code == 0)
      $(".weui_dialog_alert").attr("hidden","hidden");
    else
      location.reload();
  }

  function cupid(){

  }
</script>
</body>
</html>
