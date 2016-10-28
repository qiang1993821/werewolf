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
<link rel="stylesheet" href="static/css/index.css">
<link rel="stylesheet" href="static/css/example.css">
<title>添加房间</title>
</head>
<body class="activity">
	<div class="container" id="container">
		<div class="weui_cells weui_cells_form">
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">房间名</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" placeholder="请输入房间名" value="${name}" id="name">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">平民数量</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<select class="weui_select" id="civilian">
						<c:forEach var="item" varStatus="status" begin="2" end="10">
							<option value="${status.index}">${status.index}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">狼人数量</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<select class="weui_select" id="werewolf">
						<c:forEach var="item" varStatus="status" begin="2" end="10">
							<option value="${status.index}">${status.index}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="weui_cell weui_cell_switch">
			<div class="weui_cell_hd weui_cell_primary">预言家</div>
			<div class="weui_cell_ft">
				<input class="weui_switch" type="checkbox" id="prophet">
			</div>
		</div>
		<div class="weui_cell weui_cell_switch">
			<div class="weui_cell_hd weui_cell_primary">守卫</div>
			<div class="weui_cell_ft">
				<input class="weui_switch" type="checkbox" id="guard">
			</div>
		</div>
		<div class="weui_cell weui_cell_switch">
			<div class="weui_cell_hd weui_cell_primary">女巫</div>
			<div class="weui_cell_ft">
				<input class="weui_switch" type="checkbox" id="witch">
			</div>
		</div>
		<div class="weui_cell weui_cell_switch">
			<div class="weui_cell_hd weui_cell_primary">猎人</div>
			<div class="weui_cell_ft">
				<input class="weui_switch" type="checkbox" id="hunter">
			</div>
		</div>
		<div class="weui_cell weui_cell_switch">
			<div class="weui_cell_hd weui_cell_primary">白痴</div>
			<div class="weui_cell_ft">
				<input class="weui_switch" type="checkbox" id="idiot">
			</div>
		</div>
		<div class="weui_cell weui_cell_switch">
			<div class="weui_cell_hd weui_cell_primary">丘比特</div>
			<div class="weui_cell_ft">
				<input class="weui_switch" type="checkbox" id="cupid">
			</div>
		</div>
		<div id="btn">
			<div class="weui_cells weui_cells_form">
				<div class="bd spacing">
					<a href="javascript:addGame()" class="weui_btn weui_btn_primary">确认创建</a>
				</div>
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
	<script src="static/js/zepto.min.js"></script>
    <script src="static/js/router.min.js"></script>
	<script type="text/javascript">
		$(function() {
			if(!localStorage.wwid || localStorage.wwid=='undefined'){
				location.href = "login";
			}
		});
		function addGame(){
			$("#btn").attr("hidden","hidden");
			if(!localStorage.wwid){
				location.href = "/login";
			}
			var num = 0;
			var role = "";
			var name = $("#name").val();
			var civilian = $("#civilian").val();
			num += parseInt(civilian);
			for(var i=0;i<civilian;i++){
				role += "平民-";
			}
			var werewolf = $("#werewolf").val();
			num += parseInt(werewolf);
			for(var i=0;i<werewolf;i++){
				role += "狼人-";
			}
			if($("#prophet").attr("checked")){
				role += "预言家-";
				num++;
			}

			if($("#guard").attr("checked")){
				role += "守卫-";
				num++;
			}
			if($("#witch").attr("checked")){
				role += "女巫-";
				num++;
			}
			if($("#hunter").attr("checked")){
				role += "猎人-";
				num++;
			}
			if($("#idiot").attr("checked")){
				role += "白痴-";
				num++;
			}
			if($("#cupid").attr("checked")){
				role += "丘比特-";
				num++;
			}
			if(name && civilian && werewolf){
				$.ajax({
					url: 'game/addGame',
					type: 'POST',
					data:{
						uid:localStorage.wwid,
						name:name,
						num:num,
						role:role,
						status:0
					},
					dataType: 'json',
					error: function () {
						$(".weui_dialog_title").html("创建失败");
						$(".weui_dialog_bd").html("服务器被海王类劫持了！");
						$('#url').attr('href',"javascript:closeDialog(0)");
						$(".weui_dialog_alert").removeAttr("hidden");
						$("#btn").removeAttr("hidden");
					},
					success: function (data) {
						if(data.code==1){
							$(".weui_dialog_title").html("创建成功");
							$(".weui_dialog_bd").html("");
							$('#url').attr('href',"javascript:closeDialog(1)");
						}else{
							$(".weui_dialog_title").html("创建失败");
							$(".weui_dialog_bd").html(data.msg);
							$('#url').attr('href',"javascript:closeDialog(0)");
							$("#btn").removeAttr("hidden");
						}
						$(".weui_dialog_alert").removeAttr("hidden");
					}
				});
			}else{
				$(".weui_dialog_title").html("创建失败");
				$(".weui_dialog_bd").html("请检查前三项是否完善！");
				$('#url').attr('href',"javascript:closeDialog(0)");
				$(".weui_dialog_alert").removeAttr("hidden");
				$("#btn").removeAttr("hidden");
			}
		}
		//关闭对话框
		function closeDialog(code){
			if(code==1) {
				location.href = "/index";
			}else if(code==0){
				$(".weui_dialog_alert").attr("hidden","hidden");
			}
		}
	</script>
</body>
</html>