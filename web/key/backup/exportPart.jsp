<%@page import="debug.log.LogUtil"%>
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
        <link rel="stylesheet" type="text/css" href="css/styles.css" /> 
        <script language="JavaScript" type="text/javascript">
            function check(){
                var sPin = document.getElementById("pin1");
                var len = sPin.value.length;
                if(len != 8){
                    alert($.i18n.prop('ICPINCheckLength'));
                    return false;
                }
                $("#button_confirm").attr("disabled", "disabled");
                return true;
            }
        </script>
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_KeyBackupWizard').html($.i18n.prop('KeyBackupWizard'));
				$('#label_KeyBackupStepExportComponent').html($.i18n.prop('KeyBackupStepExportComponent'));
				$('#label_KeyBackupStepExportComponentTips1').html($.i18n.prop('KeyBackupStepExportComponentTips1'));
				$('#label_KeyBackupStepExportComponentTips2').html($.i18n.prop('KeyBackupStepExportComponentTips2'));
				$('#label_KeyBackupStepExportComponentTips3').html($.i18n.prop('KeyBackupStepExportComponentTips3'));
				$('#label_KeyBackupStepExportComponentInputPIN').html($.i18n.prop('KeyBackupStepExportComponentInputPIN'));
				document.getElementById("button_confirm").value=$.i18n.prop('ButtonConfirm');
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
            int part = 1;
            try {
                part = (Integer)request.getAttribute("part");
            } catch(Exception ex) {
                LogUtil.println("exportPart.jsp -> exception: "+ex);
            }
            
            boolean isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
        %>
        <div class="totalInfo">
            <div class="guideInfo">
                <jsp:include page="/guide/backup.jsp">
                    <jsp:param name="step" value="2" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_KeyBackupWizard"></label></div>
                <div class="tobody">
                    <!--<div id="title">${ result }</div>-->
                    2、<label id="label_KeyBackupStepExportComponent"></label>[<%= part %>].<br>
                    <br>
                    <label id="label_KeyBackupStepExportComponentTips1"></label><%= part %><label id="label_KeyBackupStepExportComponentTips2"></label>.<br>
                    <label id="label_KeyBackupStepExportComponentTips3"></label>.<br>
                    <br>
                    <form onsubmit="return check()" method="post" action="/handle/maintainKeyBackup.jsp?action=exportPart&isGuide=<%= isGuide %>">
                        <div>
                            <label id="label_KeyBackupStepExportComponentInputPIN"></label>：<input type="password" name="pin" id="pin" value="" autocomplete="off"/>
                            <input type="submit" id="button_confirm" value="submit" />
                        </div>
                        <input type="hidden" value="<%= part %>" name="part">
                    </form>
                    <script>
                        $("form").submit(function() {
                          $("#button_confirm").attr("disabled", "disabled");
                        });
                    </script>
                </div>
            </div>
        </div>
    </body>
</html>
