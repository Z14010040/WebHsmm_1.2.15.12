<%@ include file="/common.jsp" %>
<%@page import="com.sansec.hsm.bean.Config"%>
<%@ page import="com.sansec.util.FileTools" %>
<%@ page import="com.sansec.hsm.bean.GlobalData" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>Backup Key Wizard - Ready</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />

		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_KeyBackupWizard').html($.i18n.prop('KeyBackupWizard'));
				$('#label_KeyBackupStepPrepare').html($.i18n.prop('KeyBackupStepPrepare'));
				$('#label_KeyBackupStepPrepareTips1').html($.i18n.prop('KeyBackupStepPrepareTips1'));
				$('#label_KeyBackupStepPrepareTips2').html($.i18n.prop('KeyBackupStepPrepareTips2'));
				$('#label_KeyBackupStepPrepareTips3').html($.i18n.prop('KeyBackupStepPrepareTips3'));
				$('#label_KeyBackupViewLoginStatus').html($.i18n.prop('KeyBackupViewLoginStatus'));
				document.getElementById("button_startBackup").value=$.i18n.prop('KeyBackupStepPrepareBtn');
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
            boolean isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
            String createTime = FileTools.getModifiedTime(GlobalData.DOWNLOAD_BACKUP_FILE);
        %>
        <div class="totalInfo">
            <div class="guideInfo">
                <jsp:include page="/guide/backup.jsp">
                    <jsp:param name="step" value="1" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_KeyBackupWizard"></label></div>
                <div class="tobody">
                    1、<label id="label_KeyBackupStepPrepare"></label>.<br>
                    <br>
                    <label id="label_KeyBackupStepPrepareTips1"></label>,<br>
                    <label id="label_KeyBackupStepPrepareTips2"></label>&nbsp;<label id="label_KeyBackupStepPrepareTips3"></label>.<br>
                    <br>
                    <a href="/handle/maintainUser.jsp?action=showUserLogin&isGuide=false"><label id="label_KeyBackupViewLoginStatus"></label></a><br>
                    <br>
                    <%
                        // 跳转
                        if(StringUtils.isNotBlank(createTime)) {
                    %>
                        <label id="test">The last back up file has been displayed in the table. Click the file name to download it. If you want to re-backup, you can click the start backup button to begin and the previous backup file will be overwritten.</label><br>
                        <table width="50%">
                            <tr>
                                <td width="50%"><label >FileName</label></td>
                                <td width="50%"><label >BackupTime</label></td>
                            </tr>　
                            <tr>
                                <td><a href="/handle/download.jsp"><label id="FileName">swhsmbak.dat</label></a></td>
                                <td><label id="BackupTime"><%=createTime%></label></td>
                            </tr>
                        </table>
                        <br>
                    <%
                        }
                    %>
                    <div id="title">${ result }</div>
                    <form method="post" action="/handle/maintainKeyBackup.jsp?action=showExportPart&isGuide=<%= isGuide %>">
                        <div class="button">
                            <input type="submit" id="button_startBackup" value="开始备份" />
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
