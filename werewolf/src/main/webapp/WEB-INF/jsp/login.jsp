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
<link rel="stylesheet" href="static/css/reset.css">
<link rel="stylesheet" href="static/css/login.css">
<script src="static/js/zepto.min.js"></script>
<title>登录</title>
</head>
<body class="login">
	<div class="userPhoto"><img src="static/images/head.jpg" alt=""></div>
	<span class="name">狼人杀首夜上帝</span>
	<div class="username"><input type="text" placeholder="请填写邮箱" id="username"></div>
	<div class="pwd"><input type="password" placeholder="请填写密码" id="pwd"></div>
	<div id="btn">
		<a href="#" class="weui_btn weui_btn_primary btn" onclick="login()">登录</a>
		<a href="#" class="weui_btn weui_btn_warn btn" onclick="forget()">忘记密码</a>
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
		localStorage.needRefresh = 1;
		$(function() {
			if(localStorage.wwid){
				location.href = "/index?uid="+localStorage.wwid;
			}else{
				$(".weui_dialog_title").html("注册指南");
				$(".weui_dialog_bd").html("输入正确的邮箱，设置密码后点击登录按钮，您的邮箱将收到激活链接，激活后即可登录！");
				$('#url').attr('href',"javascript:closeDialog()");
				$(".weui_dialog_alert").removeAttr("hidden");
				$("#btn").removeAttr("hidden");
			}
		});
		function login(){
			$("#btn").attr("hidden","hidden");
			var name = $("#username").val();
			var pwd = $("#pwd").val();
			var msg = null;
			//验证邮箱
			var reg=/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
			if(name==null||pwd==null||name==""||pwd=="")
				msg = "用户名或密码不能为空！";
			else if(reg.test(name.trim() ) == false)
				msg = "请检查邮箱格式！";
			if(msg == null){
				$.ajax({
					url: 'user/login?username=' + name + '&pwd=' + pwd,
					type: 'POST',
					dataType: 'json',
					error: function () {
						$(".weui_dialog_title").html("登录失败");
						$(".weui_dialog_bd").html("服务器被海王类劫持了！");
						$('#url').attr('href',"javascript:closeDialog()");
						$(".weui_dialog_alert").removeAttr("hidden");
						$("#btn").removeAttr("hidden");
					},
					success: function (data) {
						if(data.code==1){
							$(".weui_dialog_title").html("登录成功");
							$(".weui_dialog_bd").html("");
							$('#url').attr('href',"/index?uid="+data.result);
							localStorage.wwid = data.result;
						}else{
							$(".weui_dialog_title").html("提示");
							$(".weui_dialog_bd").html(data.result);
							$('#url').attr('href',"javascript:closeDialog()");
						}
						$("#btn").removeAttr("hidden");
						$(".weui_dialog_alert").removeAttr("hidden");
					}
				});
			}else{
				$(".weui_dialog_title").html("登录失败");
				$(".weui_dialog_bd").html(msg);
				$('#url').attr('href',"javascript:closeDialog()");
				$(".weui_dialog_alert").removeAttr("hidden");
				$("#btn").removeAttr("hidden");
			}
		}
		function forget(){
			$("#btn").attr("hidden","hidden");
			var name = $("#username").val();
			var msg = null;
			//验证邮箱
			var reg=/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
			if(name==null||name=="")
				msg = "请输入邮箱！";
			else if(reg.test(name.trim() ) == false)
				msg = "请检查邮箱格式！";
			if(msg == null){
				$.ajax({
					url: 'mail/forget?mail=' + name,
					type: 'POST',
					dataType: 'json',
					error: function () {
						$(".weui_dialog_title").html("操作失败");
						$(".weui_dialog_bd").html("服务器被海王类劫持了！");
						$('#url').attr('href',"javascript:closeDialog()");
						$(".weui_dialog_alert").removeAttr("hidden");
						$("#btn").removeAttr("hidden");
					},
					success: function (data) {
						if(data.code==1){
							$(".weui_dialog_title").html("提示");
							$(".weui_dialog_bd").html("已向该邮箱发送提醒邮件");
							$('#url').attr('href',"javascript:closeDialog()");
						}else{
							$(".weui_dialog_title").html("提示");
							$(".weui_dialog_bd").html(data.result);
							$('#url').attr('href',"javascript:closeDialog()");
						}
						$("#btn").removeAttr("hidden");
						$(".weui_dialog_alert").removeAttr("hidden");
					}
				});
			}else{
				$(".weui_dialog_title").html("注意");
				$(".weui_dialog_bd").html(msg);
				$('#url').attr('href',"javascript:closeDialog()");
				$(".weui_dialog_alert").removeAttr("hidden");
				$("#btn").removeAttr("hidden");
			}
		}
		//关闭对话框
		function closeDialog(){
			$(".weui_dialog_alert").attr("hidden","hidden");
		}
	</script>
</body>
</html>