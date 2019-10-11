<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>Key Recovery Wizard - Upload Backup File</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script type="text/javascript">
            //获取文件名称  
            function getFileName(path) {  
                var pos1 = path.lastIndexOf('/');  
                var pos2 = path.lastIndexOf('\\');  
                var pos = Math.max(pos1, pos2);  
                if (pos < 0) {
                    return path;
                }
                else {
                    return path.substring(pos + 1);
                }
            }
            function check() {
                var str = document.getElementById("uploadfile").value;
                var fileName = getFileName(str);
                var name = fileName.substring(0,fileName.lastIndexOf('.'));

                if(name == null || name == "") {
                    alert("Please select a file!");
                    return false;
                } 
//                else if(name != "swhsmbak.dat"){
//                    alert("please check file name: swhsmbak.dat");
//                    return false;
//                } 
                else{
                    return true;
                }
            }
        </script>
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_KeyRestoreWizard').html($.i18n.prop('KeyRestoreWizard'));
				$('#label_KeyRestoreStepUploadFile').html($.i18n.prop('KeyRestoreStepUploadFile'));
				$('#label_KeyRestoreStepUploadFileTips').html($.i18n.prop('KeyRestoreStepUploadFileTips'));
				$('#label_KeyRestoreStepUploadFileAlready').html($.i18n.prop('KeyRestoreStepUploadFileAlready'));
				$('#label_KeyRestoreStepUploadFileNext').html($.i18n.prop('KeyRestoreStepUploadFileNext'));
				document.getElementById("button_upload").value=$.i18n.prop('ButtonUpload');
				document.getElementById("uploadfile").value=$.i18n.prop('ButtonBrowse');
			}

			function load(){
				//alert(window.languageCallback);
				getLanguage(display);
				getLanguage(showGuide);
			}
			window.onload = load;
		</script>
    </head>
    <body>
        <%
            Object url = request.getAttribute("url");
            Object result = request.getAttribute("result");
        %>
        <div class="totalInfo">
            <div class="guideInfo">
                <jsp:include page="/guide/restore.jsp">
                    <jsp:param name="step" value="2" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_KeyRestoreWizard"></label></div>
                <div class="tobody">
                    2、<label id="label_KeyRestoreStepUploadFile"></label>.<br>
                    <br>
                    <label id="label_KeyRestoreStepUploadFileTips"></label>.<br>
                    <br>
                    <form method="post" enctype="multipart/form-data" name="form1" id="up" action="/handle/maintainKeyRestore.jsp?action=uploadFile" onsubmit="return check()">
                        <div>
                            <input type="file" name="file1" id="uploadfile" value="Browse"/>
                            <input type="submit" id="button_upload" value="upload" onclick="return check()"/>
                        </div>
                    </form>
                    <script>
                        $("form").submit(function() {
                          $("#button_upload").attr("disabled", "disabled");
                        });
                    </script>
<!--                    <br>
                    <label id="label_KeyRestoreStepUploadFileAlready"></label><a href="/handle/maintainKeyRestore.jsp?action=checkBackupFile">&lt;
						<label id="label_KeyRestoreStepUploadFileNext"></label>&gt;</a>-->
                </div>
            </div>
        </div>
    </body>
</html>
