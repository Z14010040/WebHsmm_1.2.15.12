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

        <title>北京三未信安密码设备</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
                <link rel="shortcut icon" href="../images/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="css/main.css" type="text/css" />
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_welcome').html($.i18n.prop('Welcome'));
				$('#label_simpleFactoryName').html($.i18n.prop('SimpleFactoryName'));
				$('#label_networkHSM').html($.i18n.prop('NetworkHSM'));
				$('#label_WelcomeTxt1').html($.i18n.prop('WelcomeTxt1'));
				$('#label_WelcomeTxt2').html($.i18n.prop('WelcomeTxt2'));
				$('#label_WelcomeTxt3').html($.i18n.prop('WelcomeTxt3'));
				$('#label_FactoryName').html($.i18n.prop('FactoryName'));
			}

			function load(){
				//alert(window.languageCallback);
				getLanguage(display);
			}
			window.onload = load;
		</script>
    </head>
    <body style="text-align: center">
        <%
			String deviceType = "";
			String simpleFactoryName = Config.getSimpleFactoryName();
			String factoryName = Config.getFactoryName();
        %>            
        <div style="width: 90%; text-align: left">
			<div id="row1"><label id="label_welcome"></label></div>
			<div id="row1"><label id="label_networkHSM"></label></div>
            <div style="font-size:16px; line-height:18px; letter-spacing:0px">
                <span class="txt">
                    <p>
                        &nbsp;&nbsp;&nbsp;&nbsp; <label id="label_WelcomeTxt1"></label>&nbsp;<label id="label_FactoryName"></label>&nbsp;<label id="label_WelcomeTxt2"></label>
                    </p>
                    <p>
                        &nbsp;&nbsp;&nbsp;&nbsp;<label id="label_WelcomeTxt3"></label>
                    </p>
                </span>                    
            </div>
        </div>
    </body>
</html>
