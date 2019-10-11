<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>设置私钥保护权限</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/styles.css"/>
    <script language="JavaScript" type="text/javascript">
        function check() {
            //var initPin = document.getElementById("initPin");
            //var newPin = document.getElementById("newPin");
            //var renPin = document.getElementById("renPin");
            var newPin = document.getElementById("password");
            var renPin = document.getElementById("password2");
            if (newPin.value == "") {
                alert($.i18n.prop('KeyAccessPasswordCheckNull'));
                return false;
            } else if (newPin.value.length != 8) {
                alert($.i18n.prop('KeyAccessPasswordCheckLength'));
                return false;
            } else if (renPin.value == "") {
                alert($.i18n.prop('KeyAccessPassword2CheckNull'));
                return false;
            } else if (renPin.value.length != 8) {
                alert($.i18n.prop('KeyAccessPassword2CheckLength'));
                return false;
            } else if (newPin.value != renPin.value) {
                alert($.i18n.prop('KeyAccessPasswordCheckConsistent'));
                return false;
            } else {
                return true;
            }
        }
    </script>

    <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="js/language.js"></script>
    <script type="text/javascript">
        function display() {
            $('#label_KeySetAccessPassword').html($.i18n.prop('KeySetAccessPassword'));
            $('#label_KeySetAccessPasswordTips').html($.i18n.prop('KeySetAccessPasswordTips'));
            $('#label_KeySetAccessPasswordPIN1').html($.i18n.prop('KeySetAccessPasswordPIN1'));
            $('#label_KeySetAccessPasswordPIN2').html($.i18n.prop('KeySetAccessPasswordPIN2'));
            document.getElementById("button_confirm").value = $.i18n.prop('ButtonConfirm');
            document.getElementById("button_return").value = $.i18n.prop('GuideReturn');
        }

        function load() {
            getLanguage(display);
            getLanguage(showGuide);
        }

        window.onload = load;
    </script>
</head>

<body>
<%
    boolean isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
    String sKeyType = (String) request.getAttribute("algrithm");
    String step = "";
    if ("ECC".equals(sKeyType)) {
        step = "2";
    } else {
        step = "1";
    }
%>
<div class="totalInfo">
    <div class="guideInfo">
        <%
            if (isGuide) {
        %>
        <jsp:include page="/guide/guide.jsp">
            <jsp:param name="step" value="4"/>
        </jsp:include>
        <%
        } else {
        %>
        <jsp:include page="/guide/key.jsp">
            <jsp:param name="step" value="<%=step%>"/>
        </jsp:include>
        <%
            }
        %>
    </div>
    <div class="blank"></div>
    <div class="Info">
        <div class="toheader"><label id="label_KeySetAccessPassword"></label></div>
        <div class="tobody">
            <label id="label_KeySetAccessPasswordTips"></label>.<br>
            <br>
            <form onsubmit="return check()" method="post"
                  action="/handle/maintainKey.jsp?action=modifyPrivateKeyAccessPassword&isGuide=<%= isGuide %>">
                <input type="hidden" name="keyIndex" value="${ keyIndex }">
                <input type="hidden" name="algrithm" value="${ algrithm }">
                <table>
                    <tr>
                        <td width="50%"><label id="label_KeySetAccessPasswordPIN1"></label></td>
                        <td width="50%"><input type="password" name="password" id="password" autocomplete="off"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label id="label_KeySetAccessPasswordPIN2"></label></td>
                        <td><input type="password" name="password2" id="password2" autocomplete="off"/>
                        </td>
                    </tr>
                </table>
                <div id="title">${ result }</div>
                <div class="button">
                    <input type="submit" id="button_confirm" name="Submit" value="确定"/>
                    <input type="button" id="button_return" onclick="history.go(-1)" value="返回"/>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
