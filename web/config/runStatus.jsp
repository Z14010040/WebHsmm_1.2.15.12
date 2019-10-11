<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>查看设备运行信息</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script type="text/javascript">
            function refresh(){
                var sub=document.getElementById("modify");
                sub.action = "/handle/maintainConfig.jsp?action=showServiceRunStatus&isGuide=false";
                sub.method = "post";
                sub.submit();
            }
        </script>

		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_RunStatusInformation').html($.i18n.prop('RunStatusInformation'));
				$('#label_RunStatusService').html($.i18n.prop('RunStatusService'));
				$('#label_RunStatusConcurrent').html($.i18n.prop('RunStatusConcurrent'));
				$('#label_RunStatusMemory').html($.i18n.prop('RunStatusMemory'));
				document.getElementById("button_refresh").value=$.i18n.prop('ButtonRefresh');
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
                <jsp:include page="/guide/service.jsp">
                    <jsp:param name="step" value="1" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_RunStatusInformation"></label></div>
                <div class="tobody">
                    <table>
                        <tr><td width="50%"><label id="label_RunStatusService"></label></td><td width="50%">${ status.serviceStatus }</td></tr>
                        <tr><td><label id="label_RunStatusConcurrent"></label></td><td>${ status.currentCount }</td></tr>
                        <tr><td><label id="label_RunStatusMemory"></label></td><td>${ status.usedMemoryPercent }</td></tr>　
                    </table>
                    <div id="title">${ result }</div>
                    <form name="modify" id="modify" method="post" action="">
                        <div class="button">
                            <input type="button" id="button_refresh" name="reload" value="刷新" onclick="refresh()"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
