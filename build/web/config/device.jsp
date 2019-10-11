<%@page import="java.io.PrintWriter"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>查看设备基本信息</title>
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
				$('#label_DeviceInfoTitle').html($.i18n.prop('DeviceInfoTitle'));
				$('#label_DeviceInfoManufacturer').html($.i18n.prop('DeviceInfoManufacturer'));
				$('#label_DeviceInfoModel').html($.i18n.prop('DeviceInfoModel'));
				$('#label_DeviceInfoProductNumber').html($.i18n.prop('DeviceInfoProductNumber'));
				$('#label_DeviceInfoSN').html($.i18n.prop('DeviceInfoSN'));
				$('#label_DeviceInfoVersion').html($.i18n.prop('DeviceInfoVersion'));
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
                <jsp:include page="/guide/system.jsp">
                    <jsp:param name="step" value="1" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_DeviceInfoTitle"></label></div>
                <div class="tobody">
                    <table>
                        <tr>
                            <td width="50%"><label id="label_DeviceInfoManufacturer"></label></td>
                            <td width="50%">${ device.manufacture}</td>
                        </tr>　
                        <tr>
                            <td><label id="label_DeviceInfoModel"></label></td>
                            <td>${ device.deviceType }</td>
                        </tr>
                        <tr>
                            <td><label id="label_DeviceInfoProductNumber"></label></td>
                            <td>${ device.productType }</td>
                        </tr>
                        <tr>
                            <td><label id="label_DeviceInfoSN"></label></td>
                            <td>${ device.serialNumber}</td>
                        </tr>
                        <tr>
                            <td><label id="label_DeviceInfoVersion"></label></td>
                            <td>${ device.version }</td>
                        </tr>
                    </table>
                    <div id="title">${ result }</div>
                </div>
            </div>
        </div>
    </body>
</html>
