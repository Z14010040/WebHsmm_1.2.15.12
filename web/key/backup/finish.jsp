<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>Backup Key Wizard - Finish</title>
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
				$('#label_KeyBackupStepFinish').html($.i18n.prop('KeyBackupStepFinish'));
				$('#label_KeyBackupStepFinishTips1').html($.i18n.prop('KeyBackupStepFinishTips1'));
				$('#label_KeyBackupStepFinishTips2').html($.i18n.prop('KeyBackupStepFinishTips2'));
				$('#label_Next').html($.i18n.prop('GuideNext'));
				$('#label_Previous').html($.i18n.prop('GuidePrevious'));
				$('#label_Return').html($.i18n.prop('GuideReturn'));
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
        %>
        <div class="totalInfo">
            <div class="guideInfo">
                <jsp:include page="/guide/backup.jsp">
                    <jsp:param name="step" value="4" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_KeyBackupWizard"></label></div>
                <div class="tobody">
                    4. <label id="label_KeyBackupStepFinish"></label>.<br>
                    <br>
                    <label id="label_KeyBackupStepFinishTips1"></label>.<br>
                    <br>
                    <label id="label_KeyBackupStepFinishTips2"></label>.
                </div>
                <div class="tobottom">
                 <%
                    if (isGuide) {
                %>
                    <ul>
                        <li><a href="/finish.jsp" target="panel" style="width:60px"><label id="label_Next"></label></a></li>
                        <%--<li><a href="/handle/maintainConfig.jsp?action=showWhiteHostInfo&isGuide=true" target="panel" style="width:60px"><label id="label_Previous"></label></a></li>--%>
                    </ul>
                <%                    } else {
                %>
                    <ul>
                        <li><a href="/welcome.jsp" target="panel" style="width:45px"><label id="label_Return"></label></a></li>
                    </ul>
                <%                    }
                %>
                </div>
            </div>
        </div>
    </body>
</html>
