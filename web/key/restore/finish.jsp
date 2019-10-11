<%@ page import="com.sansec.hsm.bean.Backup" %>
<%@ page language="java" pageEncoding="UTF-8" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    Backup.deleteFile();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>Key Recovery Wizard - Import Key Component</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/styles.css"/>

    <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="js/language.js"></script>
    <script type="text/javascript">
        function display() {
            $('#label_KeyRestoreWizard').html($.i18n.prop('KeyRestoreWizard'));
            $('#label_KeyRestoreStepFinish').html($.i18n.prop('KeyRestoreStepFinish'));
            $('#label_KeyRestoreStepFinishTips1').html($.i18n.prop('KeyRestoreStepFinishTips1'));
            $('#label_KeyRestoreStepFinishTips2').html($.i18n.prop('KeyRestoreStepFinishTips2'));
            $('#label_Return').html($.i18n.prop('GuideReturn'));
        }

        function load() {
            //alert(window.languageCallback);
            getLanguage(display);
            getLanguage(showGuide);
        }

        window.onload = load;
    </script>
</head>
<body>

<div class="totalInfo">
    <div class="guideInfo">
        <jsp:include page="/guide/restore.jsp">
            <jsp:param name="step" value="4"/>
        </jsp:include>
    </div>
    <div class="blank"></div>
    <div class="Info">
        <div class="toheader"><label id="label_KeyRestoreWizard"></label></div>
        <div class="tobody">
            4、<label id="label_KeyRestoreStepFinish"></label>.<br>
            <br>
            <label id="label_KeyRestoreStepFinishTips1"></label>.<br>
            <label id="label_KeyRestoreStepFinishTips2"></label>.
        </div>
        <div class="tobottom">
            <ul>
                <li><a href="/welcome.jsp" target="panel" style="width:45px"><label id="label_Return"></label></a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
