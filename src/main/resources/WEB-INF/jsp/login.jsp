<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <script src="/static/jquery-3.3.1.min.js"></script>
    <script src="/static/jquery.cookie.js"></script>
</head>
<body>
    <form action="/user" method="post">
        用户名：<input id="username" type="text" name="username"><br/>
        密 码：<input id="password" type="password" name="password"><br/>
        <input id="loginId" type="button" value="登录">
    </form>
    <script type="application/javascript">
        var accessToken = null;
        $('#loginId').click(function() {
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
                        $.cookie("username", username);
                        location.href = '/user/profile';
                    }
                },
                error: function(data) {
                    alert(data.responseJSON.error_description);
                }
            })
        })
    </script>
</body>
</html>
