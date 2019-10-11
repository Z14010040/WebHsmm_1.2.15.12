<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">
        <title>停止服务</title>
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
				$('#label_ServiceStop').html($.i18n.prop('ServiceStop'));
				$('#label_ServiceStopTips').html($.i18n.prop('ServiceStopTips'));
				$('#label_ServiceStopStopAllServices').html($.i18n.prop('ServiceStopStopAllServices'));
				$('#label_ServiceStopRestartAllServices').html($.i18n.prop('ServiceStopRestartAllServices'));
				document.getElementById("button_stop").value=$.i18n.prop('ServiceStopBtn');
				document.getElementById("button_restart").value=$.i18n.prop('ServiceStopBtnRestart');
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
                    <jsp:param name="step" value="4" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_ServiceStop"></label></div>
                <div class="tobody">
                    <div id="title">${ result }</div>
                    <div><label id="label_ServiceStopTips"></label></div>
                    <div class="blank"></div>
                    <table>
                        <tr>
                            <td width="40%">
                                <form method="post" action="/handle/maintainConfig.jsp?action=serviceStop&isGuide=false">
                                    <input type="submit" id="button_stop" name="Submit" value="立即停止服务"/>
                                </form>  
                                <!--                                
                                <a href="ServiceStopServlet?nStopFlag=1" target="panel">立即停止服务</a>
                                -->
                            </td>
                            <td width="60%"><label id="label_ServiceStopStopAllServices"></label></td>
                        </tr>
                        <!--                       
                        <tr>
                            <td>
                                <form id="restartServ" name="restartServ" method="post" action="ServiceStopServlet">  
                                    <input type="hidden" name="nStopFlag" value="2">
                                    <input type="submit" name="Submit" value="延时停止服务"/>
                                </form> 

                                <a href="ServiceStopServlet?nStopFlag=2" target="panel">延时停止服务</a>

                            </td>
                            <td>不再接收新业务连接，不影响当前正在进行的业务或系统</td>
                        </tr>
                        -->
                        <tr>
                            <td>
                                <form method="post" action="/handle/maintainConfig.jsp?action=serviceRestart&isGuide=false">
                                    <input type="submit" id="button_restart" name="Submit" value="重新启动服务"/>
                                </form>  
                            </td>
                            <td><label id="label_ServiceStopRestartAllServices"></label></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
