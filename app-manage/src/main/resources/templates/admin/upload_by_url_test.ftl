<#include "/macro/frame.ftl">
<#setting number_format="#">
<@html>
    <@head title="E管理系统">

    </@head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>附件上传</h5>
                </div>
                <div class="ibox-content">
                    <div id="closeDiv" class="form-group">
                        <div >
                            <button class="btn btn-success" onclick="dialogClose();">关闭</button>
                        </div>
                    </div>
                    <form class="form-horizontal m-t" id="frm" enctype="multipart/form-data" method="post" action="${ctx!}/test/upload" >

                        <div class="form-group">
                            <label class="col-sm-3 control-label">File URL：</label>
                            <div class="col-sm-8">
                                <input id="fileUrl" name="fileUrl" value="" class="form-control" type="text">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-8 col-sm-offset-3">
                                <button class="btn btn-success" type="submit">上传</button>
                            </div>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

<!-- 自定义js -->
<script src="${static}/jia/js/jquery.min.js?v=2.1.4"></script>
<script src="${static}/jia/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${static}/jia/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="${static}/jia/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="${static}/jia/js/plugins/layer/layer.min.js"></script>
<!-- layerDate plugin javascript -->
<script src="${static}/jia/js/plugins/layer/laydate/laydate.js"></script>
<script src="${static}/jia/js/content.js?v=1.0.0"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${static}/jia/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${static}/jia/js/plugins/validate/messages_zh.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#frm").validate({
            rules: {
            },
            messages: {},
            submitHandler:function(form){
                var loadIndex = layer.load(1);
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: "${ctx!}/test/upload/url",
                    /*data: $(form).serialize(),*/
                    data: new FormData(form),
                    contentType: false,
                    processData: false,
                    success: function(msg){
                        if(msg.code == 0) {
                            layer.msg(msg.msg, {time: 2000}/*,function(){
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index);
                            }*/);
                        } else {
                            layer.msg(msg.msg, {time: 2000});
                        }
                    },
                    complete: function (XMLHttpRequest, textStatus) {
                        layer.close(loadIndex);
                    }
                });
            }
        });
    });

</script>

</body>

</html>
</@html>