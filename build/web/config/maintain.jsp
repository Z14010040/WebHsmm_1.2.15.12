<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>配置设备维护信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/styles.css"/>
    <script type="text/javascript">
        function submitModify() {
            var sub = document.getElementById("modify");
            sub.action = "/handle/maintainConfig.jsp?action=modifyMaintainInfo&isGuide=false";
            sub.method = "post";
            sub.submit();
        }

        function submitRefresh() {
            var sub = document.getElementById("modify");
            sub.action = "/handle/maintainConfig.jsp?action=showMaintainInfo&isGuide=false";
            sub.method = "post";
            sub.submit();
        }

        function changeEdit() {
            document.getElementById("hide").style.display = "block";
            document.getElementById("hide2").style.display = "none";
        }
    </script>

    <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="js/language.js"></script>
    <script type="text/javascript">
        function display() {
            $('#label_DeviceMaintainInfo').html($.i18n.prop('DeviceMaintainInfo'));
            $('#label_DeviceMaintainInfoSystem').html($.i18n.prop('DeviceMaintainInfoSystem'));
            $('#label_DeviceMaintainInfoCompany').html($.i18n.prop('DeviceMaintainInfoCompany'));
            $('#label_DeviceMaintainInfoDepartment').html($.i18n.prop('DeviceMaintainInfoDepartment'));
            $('#label_DeviceMaintainInfoContact').html($.i18n.prop('DeviceMaintainInfoContact'));
            $('#label_DeviceMaintainInfoTel').html($.i18n.prop('DeviceMaintainInfoTel'));
            $('#label_DeviceMaintainInfoPhone').html($.i18n.prop('DeviceMaintainInfoPhone'));
            $('#label_DeviceMaintainInfoMail').html($.i18n.prop('DeviceMaintainInfoMail'));
            document.getElementById("button_refresh").value = $.i18n.prop('ButtonRefresh');
            document.getElementById("button_save").value = $.i18n.prop('ButtonSave');
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
        <jsp:include page="/guide/system.jsp">
            <jsp:param name="step" value="2"/>
        </jsp:include>
    </div>
    <div class="blank"></div>
    <div class="Info">
        <div class="toheader"><label id="label_DeviceMaintainInfo"></label></div>
        <div class="tobody">
            <form name="modify" id="modify" method="post" action="">
                <table>　
                    <tr>
                        <td width=50%><label id="label_DeviceMaintainInfoSystem"></label></td>
                        <td width=50%>
                            <input type="text" name="systemName" id="systemName" value="${ maintain.systemName }"/>
                        </td>
                    </tr>
                    　
                    <tr>
                        <td><label id="label_DeviceMaintainInfoCompany"></label></td>
                        <td>
                            <input type="text" name="company" id="company" value="${ maintain.company }"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label id="label_DeviceMaintainInfoDepartment"></label></td>
                        <td>
                            <input type="text" name="department" id="department" value="${ maintain.department }"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label id="label_DeviceMaintainInfoContact"></label></td>
                        <td>
                            <input type="text" name="contact" id="contact" value="${ maintain.contact }"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label id="label_DeviceMaintainInfoTel"></label></td>
                        <td>
                            <input type="text" name="telephone" id="telephone" value="${ maintain.telephone }"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label id="label_DeviceMaintainInfoPhone"></label></td>
                        <td>
                            <input type="text" name="mobile" id="mobile" value="${ maintain.mobile }"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label id="label_DeviceMaintainInfoMail"></label></td>
                        <td>
                            <input type="text" name="mail" id="mail" value="${ maintain.mail }"/>
                        </td>
                    </tr>
                </table>
                <div id="title">${ result }</div>
                <div class="button">
                    <input type="button" id="button_refresh" value="刷新" onclick="submitRefresh()"/>
                    <input type="button" id="button_save" value="修改" onclick="submitModify()"/>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
