<%@page import="com.sansec.hsm.bean.Config" %>
<%@ include file="/common.jsp" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>密钥初始化</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/styles.css"/>
    <script language="JavaScript" type="text/javascript">
        function check() {
            return confirm($.i18n.prop('InitializeDeviceConfirm'));
        }
    </script>

    <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="js/language.js"></script>
    <script type="text/javascript">
        function display() {
            $('#label_InitializeDevice').html($.i18n.prop('InitializeDevice'));
            $('#label_InitializeDeviceTips1').html($.i18n.prop('InitializeDeviceTips1'));
            $('#label_InitializeDeviceTips2').html($.i18n.prop('InitializeDeviceTips2'));
            document.getElementById("button_initialize").value = $.i18n.prop('InitializeDeviceBtn');
            $('#label_Next').html($.i18n.prop('GuideNext'));
            $('#label_Return').html($.i18n.prop('GuideReturn'));
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
<%
    boolean isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
%>
<div class="totalInfo">
    <div class="guideInfo">
        <%
            if (isGuide) {
        %>
        <jsp:include page="/guide/guide.jsp">
            <jsp:param name="step" value="1"/>
        </jsp:include>
        <%
        } else {
        %>
        <% if (Config.isECCSupport()) {%>
        <jsp:include page="/guide/key.jsp">
            <jsp:param name="step" value="4"/>
        </jsp:include>
        <% } else { %>
        <jsp:include page="/guide/key.jsp">
            <jsp:param name="step" value="3"/>
        </jsp:include>
        <% } %>
        <%
            }
        %>
    </div>
    <div class="blank"></div>
    <div class="Info">
        <div class="toheader"><span><label id="label_InitializeDevice"></label></span></div>
        <div class="tobody">
            <br>
            <font color="red"><label id="label_InitializeDeviceTips1"></label>.</font><br>
            <form method="post" onsubmit="return check()"
                  action="/handle/maintainKey.jsp?action=init&isGuide=<%= isGuide %>">
                <br>
                <label id="label_InitializeDeviceTips2"></label>.<br>
                <br>
                <div class="button">
                    <input type="submit" id="button_initialize" value="初始化密码机"/>
                </div>
            </form>

            <div id="title">
                <br>
                ${ result }
            </div>

        </div>
        <%
            if (isGuide) {
        %>
        <div class="blank"></div>
        <div class="tobottom">
            <ul>
                <li><a href="/user/managerAdd.jsp?isGuide=true" target="panel" style="width:60px"><label
                        id="label_Next"></label></a></li>
                <li><a href="/welcome.jsp" target="panel" style="width:60px"><label id="label_Return"></label></a></li>
            </ul>
        </div>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
