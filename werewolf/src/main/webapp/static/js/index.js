var page = 0;
$(function () {
    //登录拦截
    if(!localStorage.gyid){
        location.href = "/login";
    }
    var uid = localStorage.gyid;
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
                var content = $(this).val();
                if (content) {
                    $.ajax({
                        url: 'activity/fuzzyQueryAC?name=' + content,
                        type: 'POST',
                        dataType: 'json',
                        error: function () {
                            $searchShow.empty();
                            $searchShow.append('<div class="weui_cell"> <div class="weui_cell_bd weui_cell_primary"> <p>无相关结果</p> </div> </div>')
                        },
                        success: function (data) {
                            if(data&&data.nameList&&data.nameList.length>0){
                                $searchShow.empty();
                                for(var i in data.nameList){
                                    $searchShow.append('<div class="weui_cell"> <div class="weui_cell_bd weui_cell_primary"> <p onclick="javascript:queryByName(\''+data.nameList[i]+'\')">'+data.nameList[i]+'</p> </div> </div>')
                                }
                            }else{
                                $searchShow.empty();
                                $searchShow.append('<div class="weui_cell"> <div class="weui_cell_bd weui_cell_primary"> <p>无相关结果</p> </div> </div>')
                            }
                        }
                    });
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
    //加载前五个活动
    $("#result_show").empty();
    queryByPage();
    $('#activity').on('click',function(){
        $('#search_bar').show();
        $("#result_show").empty();
        page = 0;
        queryByPage();
    })
    $('#me').on('click',function(){
        $('#search_bar').hide();
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
//刷新使js生效,放在这个位置第一次访问js才生效，原因不详
    if(!localStorage.needRefresh || localStorage.needRefresh==1) {
        localStorage.needRefresh = 0;
        location.reload();
    }
    //查询活跃时间
    $.ajax({
        url: 'user/current?uid=' + uid,
        type: 'POST',
        dataType: 'json',
        error: function () {
        },
        success: function (data) {
            if(data.code==1){
                $('#current').addClass('current');
            }
        }
    });
});
//根据名字精确查询
function queryByName(name){
    $('#loadMore').hide();
    var $searchShow = $("#search_show");
    var $resultShow = $("#result_show");
    $searchShow.hide();
    $resultShow.empty();
    $.ajax({
        url: 'activity/queryByName?name=' + name,
        type: 'POST',
        dataType: 'json',
        error: function () {
            $resultShow.append('<div class="weui_media_box weui_media_text"><h4 class="weui_media_title">无相关结果</h4></div>');
        },
        success: function (data) {
            if(data&&data.activityList&&data.activityList.length>0){
                for(var i in data.activityList) {
                    var activity = data.activityList[i];
                    $resultShow.append('<a class="weui_cell" href="/activityInfo?activityId='+activity.id+'"><div class="weui_media_box weui_media_text">' +
                    '<h4 class="weui_media_title">'+activity.name+'</h4><p class="weui_media_desc">'+activity.hour+'工时|'+activity.status+'</p></div></a>');
                }
            }else{
                $resultShow.append('<div class="weui_media_box weui_media_text"><h4 class="weui_media_title">无相关结果</h4></div>');
            }
        }
    });
}
//分页查询
function queryByPage(){
    var $resultShow = $("#result_show");
    $.ajax({
        url: 'activity/searchAC?page=' + page,
        type: 'POST',
        dataType: 'json',
        error: function () {
            $resultShow.append('<div class="weui_media_box weui_media_text"><h4 class="weui_media_title">无相关结果</h4></div>');
        },
        success: function (data) {
            if(data&&data.activityList&&data.activityList.length>0){
                for(var i in data.activityList) {
                    var activity = data.activityList[i];
                    $resultShow.append('<a class="weui_cell" href="/activityInfo?activityId='+activity.id+'"><div class="weui_media_box weui_media_text">' +
                    '<h4 class="weui_media_title">'+activity.name+'</h4><p class="weui_media_desc">'+activity.hour+'工时|'+activity.status+'</p></div></a>');
                }
                if(data.activityList.length==5){
                    $("#loadMore").show();
                }else{
                    $("#loadMore").hide();
                }
            }else{
                $("#loadMore").hide();
                $resultShow.append('<div class="weui_media_box weui_media_text"><h4 class="weui_media_title">无相关结果</h4></div>');
            }
        }
    });
    page = page+1;
}
//登出
function logout(){
    localStorage.clear();
    location.href = "/login";
}