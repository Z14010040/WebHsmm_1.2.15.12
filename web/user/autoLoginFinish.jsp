<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String enable = request.getParameter("enable");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>自动登陆</title>
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
				$('#label_AutoLogin').html($.i18n.prop('AutoLogin'));
				$('#label_AutoLoginFinish').html($.i18n.prop('AutoLoginFinish'));
//				$('#label_Previous').html($.i18n.prop('GuidePrevious'));
//				$('#label_Return').html($.i18n.prop('GuideReturn'));
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
                <jsp:include page="/guide/autoLogin.jsp">
                    <jsp:param name="step" value="2" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"> <label id="label_AutoLogin">AutoLogin</label></div>
                <div class="tobody">
                    <%if("true".equals(enable)){%>
                        <label id="label_AutoLoginFinish">Congratulations! You have completed the setting of AutoLogin.</label><br>
                    <%}%>
                    <%if("false".equals(enable)){%>
                        <label id="label_AutoLoginFinish">Congratulations! You have canceled the setting of AutoLogin.</label><br>
                    <%}%>
                </div>
<!--                <div class="tobottom">
                    <ul>
                        <li><a href="/welcome.jsp" target="panel" style="width:45px"><label id="label_Return"></label></a></li>
                        <li><a href="/handle/maintainKeyBackup.jsp?action=showTips&isGuide=true" target="panel" style="width:45px"><label id="label_Previous"></label></a></li>
                    </ul>
                </div>       -->
            </div>
        </div>
    </body>
</html>
