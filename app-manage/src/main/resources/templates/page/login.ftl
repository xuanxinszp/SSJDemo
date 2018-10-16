<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title>E管理系统 - 登录</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="${static}/jia/css/bootstrap.min.css" rel="stylesheet">
    <link href="${static}/jia/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${static}/jia/css/animate.css" rel="stylesheet">
    <link href="${static}/jia/css/style.css" rel="stylesheet">
    <link href="${static}/jia/css/login.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <script>
        if (window.top !== window.self) {
            window.top.location = window.location;
        }
    </script>

</head>

<body class="signin">
<div class="signinpanel">
    <div class="row">
        <div class="col-sm-12">
        <#if errMsg?exists >
            <div class="alert alert-danger">
            ${errMsg!}
            </div>
        </#if>
            <form method="post" action="${ctx!}/login" id="frm">
                <h4 class="no-margins">登录：</h4>
                <p class="m-t-md">登录到E管理平台</p>
                <input type="text" class="form-control uname" maxlength="11" name="username" id="username" placeholder="用户名" />
                <input type="password" class="form-control pword m-b" name="password" id="password"  placeholder="密码" />
                <button class="btn btn-success btn-block">登录</button>
            </form>
        </div>
    </div>
    <div class="signup-footer">
        <div class="pull-left">
            &copy; E管理系统
        </div>
    </div>
</div>
<!-- 全局js -->
<script src="${static}/jia/js/jquery.min.js?v=2.1.4"></script>
<script src="${static}/jia/js/bootstrap.min.js?v=3.3.6"></script>

<!-- 自定义js -->
<script src="${static}/jia/js/content.js?v=1.0.0"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${static}/jia/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${static}/jia/js/plugins/validate/messages_zh.min.js"></script>
<script type="text/javascript">
    $().ready(function() {
        // 在键盘按下并释放及提交后验证提交表单
        $("#frm").validate({
            rules: {
                username: {
                    required: true,
                    minlength: 2
                },
                password: {
                    required: true,
                    minlength: 6
                }
            },
            messages: {
                username: {
                    required: "请输入用户名",
                    minlength: "用户名必需由两个或者以上的字母组成"
                },
                password: {
                    required: "请输入密码",
                    minlength: "密码长度不能小于 6 位数"
                }
            },
            submitHandler:function(form){
                form.submit();
            }
        });
    });

</script>
</body>

</html>
