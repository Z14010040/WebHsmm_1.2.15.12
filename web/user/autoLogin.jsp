<%@page import="com.sansec.hsm.bean.usermgr.Manager"%>
<%@ include file="/common.jsp" %>
<%@page import="com.sansec.hsm.bean.Config"%>
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
				$('#label_AutoLogin').html($.i18n.prop('AutoLogin'));
				$('#label_AutoLoginInputTips').html($.i18n.prop('AutoLoginInputTips'));
				$('#label_AutoLoginRadio').html($.i18n.prop('AutoLoginRadio'));
				document.getElementById("button_AutoLogin").value=$.i18n.prop('AutoLoginButton');
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
            String result = request.getParameter("result");
            Manager mgr = new Manager();
            String value = mgr.readIniFile(0, "enable");
        %>
        <div class="totalInfo">
            <div class="guideInfo">
                <jsp:include page="/guide/autoLogin.jsp">
                    <jsp:param name="step" value="1" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_AutoLogin">AutoLogin</label></div>
                <div class="tobody">
                    <br>
                    <form method="post" action="/handle/maintainAutoLogin.jsp?action=check&isGuide=false">
                        <div>
                            <br>
                            <label id="label_AutoLoginInputTips"></label>:&nbsp;<input type="password" id="pin" name="pin" autocomplete="off"/>
                            <%if("The pin code is not correct or the operator key is not inserted into the machine !".equals(result)){%>
                            <label id="title" >The pin code is not correct or the operator key is not inserted into the machine !</label>
                            <%}else if("Key type error ! Please ensure the key type is operator !".equals(result)){%>
                            <label id="title" >Key type error ! Please ensure the key type is operator !</label>
                            <%}%>
                            <br>
                            <br>
                            <br>
                            <label id="enable">Enable?</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <%if("true".equals(value)){%>
                                <input type="radio" name="enable" id="yes" value="true" checked="ture"/>Yes
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="enable" id="no" value="false" />No
                            <%}else{%>
                                <input type="radio" name="enable" id="yes" value="true"/>Yes
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="enable" id="no" value="false" checked="ture"/>No
                            <%}%>
                            <br>
                            <br>
                            <div class="button">
                                <input type="submit" id="button_AutoLogin" value="确认"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
