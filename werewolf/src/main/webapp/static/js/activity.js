$(function () {
    //tab切换
    var router = new Router({
        container: '#container',
        enterTimeout: 250,
        leaveTimeout: 250
    });

    var home = {
        url: '/',
        className: 'home',
        render: function () {
            return $('#tpl_home').html();
        },
        bind: function () {
            // searchbar
            $('#container').on('focus', '#search_input', function () {
                var $weuiSearchBar = $('#search_bar');
                $weuiSearchBar.addClass('weui_search_focusing');
            }).on('blur', '#search_input', function () {
                var $weuiSearchBar = $('#search_bar');
                $weuiSearchBar.removeClass('weui_search_focusing');
                if ($(this).val()) {
                    $('#search_text').hide();
                } else {
                    $('#search_text').show();
                }
            }).on('input', '#search_input', function () {
                var $searchShow = $("#search_show");
                if ($(this).val()) {
                    $searchShow.show();
                } else {
                    $searchShow.hide();
                }
            }).on('touchend', '#search_cancel', function () {
                $("#search_show").hide();
                $('#search_input').val('');
            }).on('touchend', '#search_clear', function () {
                $("#search_show").hide();
                $('#search_input').val('');
            });
            // tabbar
            $('#container').on('click', '.weui_navbar_item', function () {
                $(this).addClass('weui_bar_item_on').siblings('.weui_bar_item_on').removeClass('weui_bar_item_on');
            });
        }
    };
   

    router.push(home)
        .setDefault('/')
        .init();


    $('.weui_tabbar a').on('click',function(){ 
        // console.log($(this).index());
        $('.lists>div').hide();
        $('.lists>div').eq($(this).index()).show();
    })


    // .container 设置了 overflow 属性, 导致 Android 手机下输入框获取焦点时, 输入法挡住输入框的 bug
    // 相关 issue: https://github.com/weui/weui/issues/15
    // 解决方法:
    // 0. .container 去掉 overflow 属性, 但此 demo 下会引发别的问题
    // 1. 参考 http://stackoverflow.com/questions/23757345/android-does-not-correctly-scroll-on-input-focus-if-not-body-element
    //    Android 手机下, input 或 textarea 元素聚焦时, 主动滚一把
    if (/Android/gi.test(navigator.userAgent)) {
        window.addEventListener('resize', function () {
            if (document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA') {
                window.setTimeout(function () {
                    document.activeElement.scrollIntoViewIfNeeded();
                }, 0);
            }
        })
    }
    if(!localStorage.gyid){
        location.href = "/login?activityId="+$("#activityId").val();
    }
});
//点击报名活动按钮
function joinAC(activityId){
    if(localStorage.gyid){
        $("#joinBtn").attr("hidden","hidden");
        $.ajax({
            url: 'activity/join?uid=' + localStorage.gyid + '&activityId=' + activityId,
            type: 'POST',
            dataType: 'json',
            error: function () {
                $(".weui_dialog_title").html("报名失败");
                $(".weui_dialog_bd").html("服务器被海王类劫持了！");
                $('#url').attr('href',"javascript:closeDialog(0)");
                $(".weui_dialog_alert").removeAttr("hidden");
                $("#joinBtn").removeAttr("hidden");
            },
            success: function (data) {
                if(data.code==1){
                    $(".weui_dialog_title").html("报名成功");
                    $(".weui_dialog_bd").html("");
                    $('#url').attr('href',"javascript:closeDialog(1)");
                }else{
                    $(".weui_dialog_title").html("报名失败");
                    $(".weui_dialog_bd").html(data.msg);
                    $('#url').attr('href',"javascript:closeDialog(1)");
                    $("#joinBtn").removeAttr("hidden");
                }
                $(".weui_dialog_alert").removeAttr("hidden");
            }
        });
    }else{
        location.href = "/login?activityId="+activityId;
    }
}
//关闭对话框
function closeDialog(code){
    if(code==1) {
        localStorage.needRefresh = 1;
        location.href = "/index?uid=" + localStorage.gyid;
    }else if(code==0){
        $(".weui_dialog_alert").attr("hidden","hidden");
    }
}
//发起活动
function launch(){
    var name = $("#activityName").val();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    var details = $("#details").val();
    var hour = $("#hour").val();
    if(!hour)
        hour = 0;
    if(name&&startTime&&endTime&&details){
        $("#launch").attr("hidden","hidden");
        $.ajax({
            url: 'activity/launch',
            type: 'POST',
            data:{
                sponsor:localStorage.gyid,
                startTime:startTime,
                endTime:endTime,
                hour:hour,
                details:details,
                name:name
            },
            dataType: 'json',
            error: function () {
                $(".weui_dialog_title").html("发起失败");
                $(".weui_dialog_bd").html("服务器被海王类劫持了！");
                $('#url').attr('href',"javascript:closeDialog(0)");
                $(".weui_dialog_alert").removeAttr("hidden");
                $("#launch").removeAttr("hidden");
            },
            success: function (data) {
                if(data.code==1){
                    $(".weui_dialog_title").html("发起成功");
                    $(".weui_dialog_bd").html("");
                    $('#url').attr('href',"javascript:closeDialog(1)");
                }else{
                    $(".weui_dialog_title").html("发起失败");
                    $(".weui_dialog_bd").html(data.msg);
                    $('#url').attr('href',"javascript:closeDialog(0)");
                }
                $(".weui_dialog_alert").removeAttr("hidden");
            }
        });
    }else{
        $(".weui_dialog_title").html("发起失败");
        $(".weui_dialog_bd").html("请仔细检查资料是否填写完善！");
        $('#url').attr('href',"javascript:closeDialog(0)");
        $(".weui_dialog_alert").removeAttr("hidden");
    }
}
//审批邀请
function approve(code){
    $("#approve").attr("hidden","hidden");
    var pendId = $("#pendId").val();
    var activityId = $("#activityId").val();
    var url;
    if(code==1){
        url = "activity/approveUser?id="+pendId+"&uid="+localStorage.gyid+"&activityId="+activityId+"&result=1&type=2";
    }else{
        url = "activity/approveUser?id="+pendId+"&uid="+localStorage.gyid+"&activityId="+activityId+"&result=0&type=2";
    }
    $.ajax({
        url: url,
        type: 'POST',
        dataType: 'json',
        error: function () {
            $(".weui_dialog_title").html("审批失败");
            $(".weui_dialog_bd").html("服务器被海王类劫持了！");
            $('#url').attr('href',"javascript:closeDialog(0)");
            $(".weui_dialog_alert").removeAttr("hidden");
            $("#approve").removeAttr("hidden");
        },
        success: function (data) {
            if(data.code==1){
                $(".weui_dialog_title").html("审批成功");
                $(".weui_dialog_bd").html("");
                $('#url').attr('href',"javascript:closeDialog(1)");
            }else{
                $(".weui_dialog_title").html("审批失败");
                $(".weui_dialog_bd").html(data.msg);
                $('#url').attr('href',"javascript:closeDialog(0)");
                $("#approve").removeAttr("hidden");
            }
            $(".weui_dialog_alert").removeAttr("hidden");
        }
    });
}
//签到
function signIn(activityId){
    $("#signIn").attr("hidden","hidden");
    $.ajax({
        url: 'activity/signIn?activityId='+activityId+"&uid="+localStorage.gyid+"&type=0",
        type: 'POST',
        dataType: 'json',
        error: function () {
            $(".weui_dialog_title").html("签到失败");
            $(".weui_dialog_bd").html("服务器被海王类劫持了！");
            $('#url').attr('href',"javascript:closeDialog(0)");
            $(".weui_dialog_alert").removeAttr("hidden");
            $("#signIn").removeAttr("hidden");
        },
        success: function (data) {
            if(data.code==1){
                $(".weui_dialog_title").html("签到成功");
                $(".weui_dialog_bd").html("");
                $('#url').attr('href',"javascript:closeDialog(1)");
            }else{
                $(".weui_dialog_title").html("签到失败");
                $(".weui_dialog_bd").html(data.msg);
                $('#url').attr('href',"javascript:closeDialog(0)");
                $("#signIn").removeAttr("hidden");
            }
            $(".weui_dialog_alert").removeAttr("hidden");
        }
    });
}
//提交反馈
function feedback(activityId){
    var feedback = $("#feedback").val();
    if(feedback==null||feedback==""){
        $(".weui_dialog_title").html("提交失败");
        $(".weui_dialog_bd").html("反馈不能为！");
        $('#url').attr('href',"javascript:closeDialog(0)");
        $(".weui_dialog_alert").removeAttr("hidden");
    }else{
        $("#feedbackbtn").attr("hidden","hidden");
        $.ajax({
            url: 'activity/feedback',
            type: 'POST',
            data:{
                uid:localStorage.gyid,
                feedback:feedback,
                activityId:activityId
            },
            dataType: 'json',
            error: function () {
                $(".weui_dialog_title").html("提交失败");
                $(".weui_dialog_bd").html("服务器被海王类劫持了！");
                $('#url').attr('href',"javascript:closeDialog(0)");
                $(".weui_dialog_alert").removeAttr("hidden");
                $("#feedbackbtn").removeAttr("hidden");
            },
            success: function (data) {
                if(data.code==1){
                    $(".weui_dialog_title").html("提交成功");
                    $(".weui_dialog_bd").html("");
                    $('#url').attr('href',"javascript:closeDialog(1)");
                }else{
                    $(".weui_dialog_title").html("提交失败");
                    $(".weui_dialog_bd").html(data.msg);
                    $('#url').attr('href',"javascript:closeDialog(0)");
                    $("#feedbackbtn").removeAttr("hidden");
                }
                $(".weui_dialog_alert").removeAttr("hidden");
            }
        });
    }
}

