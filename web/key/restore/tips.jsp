<%@page import="com.sansec.hsm.bean.Config"%>
<%@ include file="/common.jsp" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>Key Recovery Wizard - ready</title>
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
				$('#label_KeyRestoreWizard').html($.i18n.prop('KeyRestoreWizard'));
				$('#label_KeyRestoreStepPrepare').html($.i18n.prop('KeyRestoreStepPrepare'));
				$('#label_KeyRestoreStepPrepareTips1').html($.i18n.prop('KeyRestoreStepPrepareTips1'));
				$('#label_KeyRestoreStepPrepareTips2').html($.i18n.prop('KeyRestoreStepPrepareTips2'));
				$('#label_KeyRestoreStepPrepareTips3').html($.i18n.prop('KeyRestoreStepPrepareTips3'));
				$('#label_KeyRestoreStepPreparePrivilege').html($.i18n.prop('KeyRestoreStepPreparePrivilege'));
				document.getElementById("button_beginRecovery").value=$.i18n.prop('KeyRestoreStepPrepareBtn');
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
        <div class="totalInfo">
            <div class="guideInfo">
                <jsp:include page="/guide/restore.jsp">
                    <jsp:param name="step" value="1" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_KeyRestoreWizard"></label></div>
                <div class="tobody">
                    1、<label id="label_KeyRestoreStepPrepare"></label>.<br>
                    <br>
                    <label id="label_KeyRestoreStepPrepareTips1"></label>
					&nbsp;<%= (Config.getMaxManangerCount()/2 + 1) %>&nbsp;
					<label id="label_KeyRestoreStepPrepareTips2"></label>.<br>
                    <br>
                    <font color="#0000FF"><label id="label_KeyRestoreStepPrepareTips3"></label>.</font><br>
                    <br>
                    <a href="/handle/maintainUser.jsp?action=showUserLogin&isGuide=false"><label id="label_KeyRestoreStepPreparePrivilege"></label></a>
                    <br>
                    <div id="title">${ result }</div>
                    <form method="post" action="/handle/maintainKeyRestore.jsp?action=showUploadFile">
                        <div class="button">
                            <input type="submit" id="button_beginRecovery" value="start to restore" />
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
