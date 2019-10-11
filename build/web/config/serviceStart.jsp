<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>启动服务</title>
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
				$('#label_ServiceStart').html($.i18n.prop('ServiceStart'));
				$('#label_ServiceStartTips').html($.i18n.prop('ServiceStartTips'));
				document.getElementById("button_start").value=$.i18n.prop('ServiceStartBtn');
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
        %>
        <div class="totalInfo">
            <div class="guideInfo">
                <jsp:include page="/guide/service.jsp">
                    <jsp:param name="step" value="4" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_ServiceStart"></label></div>
                <div class="tobody">
                    <div id="title"><%= (result == null ? "" : result)%></div>
                    <label id="label_ServiceStartTips"></label><br>
                    <br>
                    <form method="post" action="/handle/maintainConfig.jsp?action=serviceStart&isGuide=false">
                        <div class="button">
                            <input type="submit" id="button_start" name="submit" value="启动密码服务"/>
                        </div>
                    </form>                 
                </div>
            </div>
        </div>
    </body>
</html>
