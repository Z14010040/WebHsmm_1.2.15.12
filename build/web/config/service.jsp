
<%@ page import="com.sansec.hsm.bean.Language"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	//language
	String strServiceStartAuto = Language.get("ServiceConfigStartAuto");
	String strServiceStartNo = Language.get("ServiceConfigStartNo");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>服务配置信息</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script type="text/javascript">
            function modify(isGuide){
                var port = document.getElementById("port").value;
                if(port == ""){
                    alert($.i18n.prop('ServiceConfigPortNull'));
                    return false;
                }else if(port < 1024){
                    alert($.i18n.prop('ServiceConfigPort1024'));
                    return false;
                }

                var sessionTimeout = document.getElementById("sessionTimeout").value;
                if(sessionTimeout == ""){
                    alert($.i18n.prop('ServiceConfigTimeoutNull'));
                    return false;
                }else if(sessionTimeout > 65535){
                    alert($.i18n.prop('ServiceConfigTimeoutMax'));
                    return false;
                }

                var maxThread = document.getElementById("maxThread").value;
                if(maxThread == ""){
                    alert($.i18n.prop('ServiceConfigConnection'));
                    return false;
                }else if(maxThread > 65535){
                    alert($.i18n.prop('ServiceConfigConnectionMax'));
                    return false;
                }

                var connectPwd = document.getElementById("connectPwd").value;
                if(connectPwd == ""){
                    alert($.i18n.prop('ServiceConfigPasswordNull'));
                    return false;
                }else if(connectPwd.length < 8){
                    alert($.i18n.prop('ServiceConfigPasswordLength'));
                    return false;
                }
                
                var serviceStartPwd = document.getElementById("serviceStartPwd").value;
                if(serviceStartPwd == ""){
                    alert($.i18n.prop('ServiceConfigStartupPassword'));
                    return false;
                }else if(serviceStartPwd.length < 8){
                    alert($.i18n.prop('ServiceConfigStartupPasswordLength'));
                    return false;
                }

				var sub=document.getElementById("form1");
                sub.action = "/handle/maintainConfig.jsp?action=modifyServiceInfo&isGuide="+isGuide;
                sub.method = "post";
                sub.submit();
                return true;
            }

            function refresh(isGuide){
                var sub=document.getElementById("form1");
                sub.action = "/handle/maintainConfig.jsp?action=showServiceInfo&isGuide="+isGuide;
                sub.method = "post";
                sub.submit();
            }
        </script>
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_ServiceConfigInfo').html($.i18n.prop('ServiceConfigInfo'));
				$('#label_ServiceConfigTips1').html($.i18n.prop('ServiceConfigTips1'));
				$('#label_ServiceConfigTips2').html($.i18n.prop('ServiceConfigTips2'));
				$('#label_ServiceConfigPort').html($.i18n.prop('ServiceConfigPort'));
				$('#label_ServiceConfigAutoStart').html($.i18n.prop('ServiceConfigAutoStart'));
				$('#label_ServiceConfigSessionTimeout').html($.i18n.prop('ServiceConfigSessionTimeout'));
				$('#label_ServiceConfigMaxConcurrent').html($.i18n.prop('ServiceConfigMaxConcurrent'));
				$('#label_ServiceConfigConnectPassword').html($.i18n.prop('ServiceConfigConnectPassword'));
				$('#label_ServiceConfigStartPIN').html($.i18n.prop('ServiceConfigStartPIN'));
				document.getElementById("button_refresh").value=$.i18n.prop('ButtonRefresh');
				document.getElementById("button_save").value=$.i18n.prop('ButtonSave');
				$('#label_Next').html($.i18n.prop('GuideNext'));
				$('#label_Previous').html($.i18n.prop('GuidePrevious'));
				//set selected
				var boot="${server.onBoot}";
				onBoot = document.getElementById("onBoot");
				for(i=0;i<onBoot.length;i++){
					if(onBoot[i].value==boot)
						onBoot[i].selected = true;
				}

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
				<%
					if (isGuide) {
				%>
                <jsp:include page="/guide/guide.jsp">
                    <jsp:param name="step" value="6" />
                </jsp:include>
				<%		} else {
				%>
                <jsp:include page="/guide/service.jsp">
                    <jsp:param name="step" value="2" />
                </jsp:include>
				<%			}
				%>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_ServiceConfigInfo"></label></div>
                <div class="tobody">
                    <label id="label_ServiceConfigTips1"></label><br>
                    <font color="blue"><label id="label_ServiceConfigTips2"></label></font><br>
                    <form id="form1" method="post" action="">
                        <table>
                            <tr>
                                <td width="50%"><label id="label_ServiceConfigPort"></label></td>
                                <td width="50%"><input type="text" name="port" id="port" value="${ server.port }"/></td>
                            </tr>　
                            <tr>
                                <td><label id="label_ServiceConfigAutoStart"></label></td>
                                <td>
                                    <select name="onBoot" id="onBoot">
                                        <option selected value="0" >
                                            <%=strServiceStartNo%>
                                        </option>
										<option value="1" >
											<%=strServiceStartAuto%>
                                        </option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><label id="label_ServiceConfigSessionTimeout"></label>(0~65535)</td>
                                <td><input type="text" name="sessionTimeout" id="sessionTimeout" value="${ server.sessionTimeout }"/></td>
                            </tr>
                            <tr>
                                <td><label id="label_ServiceConfigMaxConcurrent"></label>(0~65535)</td>
                                <td><input type="text" name="maxThread" id="maxThread" value="${ server.maxThread }"/></td>
                            </tr>
                            <tr>
                                <td><label id="label_ServiceConfigConnectPassword"></label></td>
                                <td><input type="password" name="connectPwd" id="connectPwd" value="${ server.connectPwd }" autocomplete="off"/></td>
                            </tr>
                            <tr>
                                <td><label id="label_ServiceConfigStartPIN"></label></td>
                                <td><input type="password" name="serviceStartPwd" id="serviceStartPwd" value="${ server.serviceStartPwd }" autocomplete="off"/></td>
                            </tr>
                        </table>
                        <br>
                        <div id="title">${ (result == null ? "" : result) }</div>
                        <div class="button">
                            <input type="button" id="button_refresh" value="刷新" onclick="refresh(<%= isGuide%>)" />
                            <input type="button" id="button_save" value="保存" onclick="return modify(<%= isGuide%>)"/>
                        </div>
                    </form>
                </div>
                <%
					if (isGuide) {
                %>
                <div class="tobottom">
					<ul>
                        <li><a href="/handle/maintainConfig.jsp?action=showWhiteHostInfo&isGuide=true" target="panel" style="width:60px"><label id="label_Next"></label></a></li>
                         <!-- modify for visahsm 17/11/09 -->  
                        <li><a href="/handle/maintainKey.jsp?action=kekManagement&isGuide=true" target="panel" style="width:60px"><label id="label_Previous"></label></a></li>
					</ul>
                </div>
                <%					}
                %>                    
            </div>
        </div>
    </body>
</html>
