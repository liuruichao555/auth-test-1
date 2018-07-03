<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人中心</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <script src="/static/jquery-3.3.1.min.js"></script>
    <script src="/static/jquery.cookie.js"></script>
</head>
<body>
    username: <span id="username"></span>
    <br/>
    <br/>
    <br/>
    <br/>
    播放记录：
    <div id="resource1Div" style="border: 1px solid black;min-height: 200px;">
    </div>

    <br/>
    <br/>
    <br/>
    <br/>

    VIP会员：
    <div id="resource2Div" style="border: 1px solid black;min-height: 200px;">
    </div>

    <br/>
    <br/>
    <br/>
    <br/>

    基本信息：
    <div id="resource3Div" style="border: 1px solid black;min-height: 200px;">
    </div>
    <script type="application/javascript">
        var accessToken = $.cookie("accessToken");
        $(function() {
            var token = "Bearer " + accessToken;
            $('#username').html($.cookie('username'));
            $.ajax({
                url: "/resource/history/list",
                type: "GET",
                headers: {
                    'Authorization': token
                },
                success: function(data) {
                    var html = '';
                    for (var i = 0; i < data.length; i++) {
                        var item = data[i];
                        html += item.name + ', ' + item.time + '<br/>';
                    }
                    $('#resource1Div').html(html);
                },
                error: function(err) {
                    $('#resource1Div').html(data);
                }
            });
            $.ajax({
                url: "/resource/vip/info",
                type: "GET",
                headers: {
                    'Authorization': token
                },
                success: function(data) {
                    $('#resource2Div').html(data);
                },
                error: function(data) {
                    $('#resource2Div').html(data.responseJSON.error_description);
                }
            });
            $.ajax({
                url: "/resource/basic/info",
                type: "GET",
                headers: {
                    'Authorization': token
                },
                success: function(data) {
                    $('#resource3Div').html(data);
                },
                error: function(data) {
                    $('#resource3Div').html(data.responseJSON.error_description);
                }
            })
        });
        /*$('#loginId').click(function() {
            var username = $('#username').val();
            var password = $('#password').val();
            if (!username) {
                alert('username不能为空');
                return;
            }
            if (!password) {
                alert("password不能为空");
                return;
            }
            $.ajax({
                url: "/user",
                method: "POST",
                data: "username=" + username + "&password=" + password,
                success: function(data) {
                    if (data && data.access_token) {
                        accessToken = data.access_token;
                        $.cookie("accessToken", accessToken);
                    }
                },
                error: function(data) {
                    alert(data.error_description);
                }
            })
        })*/
    </script>
</body>
</html>
