<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>Backup Key Wizard - Export Key Component</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_KeyBackupWizard').html($.i18n.prop('KeyBackupWizard'));
				$('#label_KeyBackupStepExportBackupFile').html($.i18n.prop('KeyBackupStepExportBackupFile'));
				$('#label_KeyBackupStepExportBackupFileTips1').html($.i18n.prop('KeyBackupStepExportBackupFileTips1'));
				$('#label_KeyBackupStepExportBackupFileTips2').html($.i18n.prop('KeyBackupStepExportBackupFileTips2'));
				$('#label_KeyBackupStepExportBackupFileTips3').html($.i18n.prop('KeyBackupStepExportBackupFileTips3'));
				$('#label_KeyBackupStepExportBackupFileTips4').html($.i18n.prop('KeyBackupStepExportBackupFileTips4'));
				$('#label_KeyBackupStepExportBackupFileTips5').html($.i18n.prop('KeyBackupStepExportBackupFileTips5'));
				$('#label_KeyBackupStepExportBackupFileDownload').html($.i18n.prop('KeyBackupStepExportBackupFileDownload'));
				document.getElementById("button_next").value=$.i18n.prop('GuideNext');
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
            Object result = request.getAttribute("result");
            boolean isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
        %>
        <div class="totalInfo">
           <div class="guideInfo">
                <jsp:include page="/guide/backup.jsp">
                    <jsp:param name="step" value="3" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_KeyBackupWizard"></label></div>
                <div class="tobody">
                    3、<label id="label_KeyBackupStepExportBackupFile"></label>.<br>
                    <br>
                    <label id="label_KeyBackupStepExportBackupFileTips1"></label>.<br>
          <strong><label id="label_KeyBackupStepExportBackupFileTips2"></label></strong>
		  <label id="label_KeyBackupStepExportBackupFileTips3"></label>“<strong>
			  <label id="label_KeyBackupStepExportBackupFileTips4"></label>...</strong>”，
			  <label id="label_KeyBackupStepExportBackupFileTips5"></label>.<br>
                    <br>
                    <a href="/handle/download.jsp"><label id="label_KeyBackupWizard"></label>[swhsmbak.dat]</a><br>
                    <br>
                    <form method="post" name="form2" action="/handle/maintainKeyBackup.jsp?action=showFinish&isGuide=<%= isGuide %>">
                        <div>
                            <input type="submit" id="button_next" name="Submit" value="next" />
                        </div>                      
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
