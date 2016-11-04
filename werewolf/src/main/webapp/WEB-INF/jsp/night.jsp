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
    <a href="#" class="weui-btn weui-btn_plain-primary btn" onclick="death()">点击用药</a>
  </div>
  <div id="witchBtn" hidden="hidden">
    <div class="weui_cell">
      <div class="weui_cell_hd"><label class="weui_label">昨夜死的人：</label></div>
      <div class="weui_cell_bd weui_cell_primary">
        <select class="weui_select" id="diedMan"></select>
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
    <div id="alertList">
      <div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div>
    </div>
    <div class="weui_dialog_ft">
      <a href="#" class="weui_btn_dialog primary" id="url">确定</a>
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
  //开启异常对话框
  function openDialog(btn){
    $(".weui_dialog_title").html("网络异常");
    $(".weui_dialog_bd").html("服务器被海王类劫持了！");
    $('#url').attr('href',"javascript:closeDialog(0)");
    $(".weui_dialog_alert").removeAttr("hidden");
    $(btn).removeAttr("hidden");
  }

  function werewolf(){
    $("#werewolf").attr("hidden","hidden");
    var kill = $("#kill").val();
    $.ajax({
      url: 'game/werewolf',
      type: 'POST',
      data:{
        kill:kill,
        uid:localStorage.wwUid
      },
      dataType: 'json',
      error: function () {
        openDialog("#werewolf");
      },
      success: function (data) {
        if(data.code == 1){
          location.reload();
        }else{
          openDialog("#werewolf");
        }
      }
    });
  }

  function prophet(){
    $("#prophet").attr("hidden","hidden");
    var guess = $("#guess").val();
    $.ajax({
      url: 'game/prophet',
      type: 'POST',
      data:{
        guess:guess,
        uid:localStorage.wwUid
      },
      dataType: 'json',
      error: function () {
        openDialog("#prophet");
      },
      success: function (data) {
        if(data.code == 1){
          $(".weui_dialog_title").html("验人结果(1/3)");
          $(".weui_dialog_bd").html(data.msg);
          $('#url').attr('href',"javascript:prophetTimes(\""+data.msg+"\",1)");
          $('#url').html("我记住了");
          $(".weui_dialog_alert").removeAttr("hidden");
        }else{
          openDialog("#prophet");
        }
      }
    });
  }

  function prophetTimes(result,resultTimes){
    $(".weui_dialog_alert").attr("hidden","hidden");
    if(resultTimes == 1){
      $(".weui_dialog_title").html("验人结果(2/3)");
      $(".weui_dialog_bd").html(result);
      $('#url').attr('href',"javascript:prophetTimes(\""+result+"\",2)");
      $('#url').html("我记住了");
      $(".weui_dialog_alert").removeAttr("hidden");
    }else if(resultTimes == 2){
      $(".weui_dialog_title").html("验人结果(3/3)");
      $(".weui_dialog_bd").html(result);
      $('#url').attr('href',"javascript:prophetTimes(\""+result+"\",3)");
      $('#url').html("我记住了");
      $(".weui_dialog_alert").removeAttr("hidden");
    }
  }

  function guard(){
    $("#guard").attr("hidden","hidden");
    var protect = $("#protect").val();
    $.ajax({
      url: 'game/guard',
      type: 'POST',
      data:{
        protect:protect,
        uid:localStorage.wwUid
      },
      dataType: 'json',
      error: function () {
        openDialog("#guard");
      },
      success: function (data) {
        if(data.code == 1){
          location.reload();
        }else{
          openDialog("#guard");
        }
      }
    });
  }

  function death(){
    $("#death").attr("hidden","hidden");
    $.ajax({
      url: 'game/killByWolf',
      type: 'POST',
      data:{
        gameId:gameId
      },
      dataType: 'json',
      error: function () {
        openDialog("#death");
      },
      success: function (data) {
        if(data.code == 1){
          $("#diedMan").append('<option value='+data.uid+'>'+data.name+'</option>');
          $("#witchBtn").removeAttr("hidden");
        }else{
          openDialog("#death");
        }
      }
    });
  }

  function witch(){
    $("#witch").attr("hidden","hidden");
    var diedMan = $("#diedMan").val();
    var save = $("#save").val();
    var poison = $("#poison").val();
    if((diedMan == null || diedMan == 0) && save == 1){
      $(".weui_dialog_title").html("平安夜不需要救人");
      $(".weui_dialog_bd").html("");
      $('#url').attr('href',"javascript:closeDialog(1)");
      $(".weui_dialog_alert").removeAttr("hidden");
    }else if(save == 1 && poison > 0){
      $(".weui_dialog_title").html("不能同时用药");
      $(".weui_dialog_bd").html("请选择救人或者毒人，不能同时选择！");
      $('#url').attr('href',"javascript:closeDialog(1)");
      $(".weui_dialog_alert").removeAttr("hidden");
    }else if(save == 1 && diedMan == localStorage.wwUid){
      $(".weui_dialog_title").html("女巫不能自救");
      $(".weui_dialog_bd").html("");
      $('#url').attr('href',"javascript:closeDialog(1)");
      $(".weui_dialog_alert").removeAttr("hidden");
    }else{
      var uid = save==1?diedMan:poison;
      $.ajax({
        url: 'game/witch',
        type: 'POST',
        data:{
          uid:uid,
          save:save,
          witch:localStorage.wwUid
        },
        dataType: 'json',
        error: function () {
          openDialog("#witch");
        },
        success: function (data) {
          if(data.code == 1){
            location.reload();
          }else{
            openDialog("#witch");
          }
        }
      });
    }
  }

  function cupid(code){
    $("#cupid").attr("hidden","hidden");
    var lover1 = $("#lover1").val();
    var lover2 = $("#lover2").val();
    if(code == 1 && lover1 == lover2){
      $(".weui_dialog_title").html("不能连同一人");
      $(".weui_dialog_bd").html("");
      $('#url').attr('href',"javascript:closeDialog(1)");
      $(".weui_dialog_alert").removeAttr("hidden");
    }else{
      $.ajax({
        url: 'game/cupid',
        type: 'POST',
        data:{
          uid:localStorage.wwUid,
          code:code,
          lover1:lover1,
          lover2:lover2
        },
        dataType: 'json',
        error: function () {
          openDialog("#cupid");
        },
        success: function (data) {
          if(data.code == 1){
            location.reload();
          }else{
            openDialog("#cupid");
          }
        }
      });
    }
  }

  function lover(){
    $("#lover").attr("hidden","hidden");
    $.ajax({
      url: 'game/lover',
      type: 'POST',
      data:{
        uid:localStorage.wwUid
      },
      dataType: 'json',
      error: function () {
        openDialog("#lover");
      },
      success: function (data) {
        if(data.code == 1){
          $(".weui_dialog_title").html(data.title);
          $(".weui_dialog_bd").html(data.msg);
          $('#url').attr('href',"javascript:closeDialog(0)");
          $(".weui_dialog_alert").removeAttr("hidden");
          $("#lover").removeAttr("hidden");
        }else{
          openDialog("#lover");
        }
      }
    });
  }

  function killed(){
    $("#killed").attr("hidden","hidden");
    $(".weui_dialog_title").html("查看昨夜结果");
    $(".weui_dialog_bd").html("查看前应先竞选警长");
    $('#url').attr('href',"javascript:showResult()");
    $('#url').html("我知道了");
    $(".weui_dialog_alert").removeAttr("hidden");
  }

  function showResult(){
    $(".weui_dialog_alert").attr("hidden","hidden");
    $.ajax({
      url: 'game/showResult',
      type: 'POST',
      data:{
        gameId:gameId
      },
      dataType: 'json',
      error: function () {
        openDialog("#killed");
      },
      success: function (data) {
        if(data.code == 1){
          $(".weui_dialog_title").html(data.title);
          $(".weui_dialog_bd").html(data.msg);
          $('#url').attr('href',"nightDetails?gameId="+gameId);
          $('#url').html("查看");
          $(".weui_dialog_ft").append('<a href="javascript:closeDialog(1)" class="weui_btn_dialog default" id="cancel">取消</a>');
          $(".weui_dialog_alert").removeAttr("hidden");
          $("#killed").removeAttr("hidden");
        }else{
          openDialog("#killed");
        }
      }
    });
  }
</script>
</body>
</html>
