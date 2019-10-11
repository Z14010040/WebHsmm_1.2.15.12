<%@ page import="java.util.*" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.sansec.hsm.bean.Language" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    //language
    String strKeyUsageSign = Language.get("KeyUsageSign");
    String strKeyUsageEncrypt = Language.get("KeyUsageEncrypt");
    String strKeyUsageSignEncrypt = Language.get("KeyGenerateUsageSignEncrypt");
    String strGenerateTips = Language.get("KeyGenerateKeyPairTips");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>查看ECC密钥状态</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/styles.css"/>
    <script language="JavaScript" type="text/javascript">
        function submit1() {

            var index = document.getElementById("index").value;
            if (index == "") {
                alert($.i18n.prop('InputCheckNull'));
                return false;
            } else {
                document.getElementById("submitbtn").style.display = "none";
                document.getElementById("hidemsg").style.display = "block";
                document.getElementById("title").style.display = "none";
                return true;
            }


            return true;
        }
    </script>
    <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="js/language.js"></script>
    <script type="text/javascript">
        function display() {
            $('#label_ECCKeyManagement').html($.i18n.prop('ECCKeyManagement'));
            $('#label_ECCKeyGenerateECCKeyPair').html($.i18n.prop('ECCKeyGenerateECCKeyPair'));
            $('#label_ECCKeyGenerateTips').html($.i18n.prop('ECCKeyGenerateTips'));
            $('#label_KeyUsage').html($.i18n.prop('KeyUsage'));
            $('#label_ECCKeyBits').html($.i18n.prop('ECCKeyBits'));
            $('#label_Next').html($.i18n.prop('GuideNext'));
            $('#label_Previous').html($.i18n.prop('GuidePrevious'));
            document.getElementById("button_generateKeyPair").value = $.i18n.prop('KeyGenerateKeyPair');
        }

        function load() {
            getLanguage(display);
            getLanguage(showGuide);
            getLanguage(showKeyStatus);
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
        <% if (isGuide) {%>
        <jsp:include page="/guide/guide.jsp">
            <jsp:param name="step" value="4"/>
        </jsp:include>
        <% } else {%>
        <jsp:include page="/guide/key.jsp">
            <jsp:param name="step" value="2"/>
        </jsp:include>
        <% }%>
    </div>
    <div class="blank"></div>
    <div class="Info">
        <div class="toheader"><label id="label_ECCKeyManagement"></label></div>
        <div class="tobody">
            <div class="inheader" align="left"><label id="label_ECCKeyGenerateECCKeyPair"></label></div>
            <div class="inbody">
                <form method="post" action="/handle/maintainKey.jsp?action=generate_ECC&isGuide=<%=isGuide%>">
                    <table>
                        <tr>
                            <td width="50%">
                                <label id="label_ECCKeyGenerateTips"></label>
                            </td>
                            <td width="50%"><input type="text" name="index" id="index"/></td>
                        </tr>
                        <tr>
                            <td><label id="label_KeyUsage"></label></td>
                            <td>
                                <select id="keyType" name="usage">
                                    <option value='1'><%=strKeyUsageSign%>
                                    </option>
                                    <option value='2'><%=strKeyUsageEncrypt%>
                                    </option>
                                    <option value='3'><%=strKeyUsageSignEncrypt%>
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><label id="label_ECCKeyBits"></label>(bits)</td>
                            <td>
                                <select name="length">
                                    <option value='256'>256</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" style="text-align: center">
                                <input id="button_generateKeyPair" type="submit" name="submit"
                                       onclick="return submit1()" value="生成密钥对"/>
                                <div id="hidemsg" style="display:none; text-align: center">
                                    <font color="#0000FF"><%=strGenerateTips%>
                                    </font>
                                </div>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div id="title">${ result }</div>
            <% if (isGuide) {%>
            <div class="tobottom">
                <ul>
                    <li><a href="/handle/maintainKey.jsp?action=kekManagement&isGuide=true" target="panel"
                           style="width:45px"><label id="label_Next"></label></a></li>
                    <li><a href="/handle/maintainKey.jsp?action=RSAManagement&isGuide=true" target="panel"
                           style="width:45px"><label id="label_Previous"></label></a></li>
                </ul>
            </div>
            <% }%>
            <jsp:include page="/key/asymmtricKeyStatus.jsp">
                <jsp:param name="algrithm" value="ECC"/>
                <jsp:param name="isGuide" value="<%= isGuide%>"/>
            </jsp:include>

        </div>
    </div>
</div>
</body>
</html>
