<%@page import="com.sansec.hsm.bean.Config"%>
<%@ page import="com.sansec.hsm.bean.keymgr.AsymmetricKeyStatus"%>
<%@ page import="com.sansec.hsm.bean.Language"%>
<%@ page import="java.util.*"%>
<%@ page language="java" pageEncoding="UTF-8"%>

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

        <title>查看RSA密钥状态</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script language="JavaScript" type="text/javascript">
            function submit1() {
                var index = document.getElementById("index").value;
                if(index == ""){
                    alert($.i18n.prop('InputCheckNull'));
                    return false;
                }else{
					document.getElementById("submitbtn").style.display = "none";
					document.getElementById("hidemsg").style.display = "block";
					document.getElementById("title").style.display = "none";
                    return true;
                }
                
                
            }
        </script>
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_RSAKeyManagement').html($.i18n.prop('RSAKeyManagement'));
				$('#label_RSAKeyGenerateKeyPair').html($.i18n.prop('RSAKeyGenerateKeyPair'));
				$('#label_RSAKeyGenerateTips').html($.i18n.prop('RSAKeyGenerateTips'));
				$('#label_KeyUsage').html($.i18n.prop('KeyUsage'));
				$('#label_RSAKeyBits').html($.i18n.prop('RSAKeyBits'));
				$('#label_Next').html($.i18n.prop('GuideNext'));
				$('#label_Previous').html($.i18n.prop('GuidePrevious'));
				document.getElementById("button_generateKeyPair").value=$.i18n.prop('KeyGenerateKeyPair');
			}

			function load(){
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
                    <jsp:param name="step" value="4" />
                </jsp:include>
                <% } else {%>
                <jsp:include page="/guide/key.jsp">
                    <jsp:param name="step" value="1" />
                </jsp:include>
                <% }%>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader" style="text-align: left; "><label id="label_RSAKeyManagement"></label></div>
                <div class="tobody">
                    <div class="inheader" align="left"><label id="label_RSAKeyGenerateKeyPair"></label></div>
                    <div class="inbody">
                        <form method="post" onsubmit="return checkGe()" action="/handle/maintainKey.jsp?action=generate_RSA&isGuide=<%=isGuide%>">
                            <table>
                                <tr>
                                    <td width="50%">
                                        <label id="label_RSAKeyGenerateTips"></label>
                                    </td>
                                    <td width="50%"><input type="text" name="index" id="index"/></td>
                                </tr>
                                <tr>
                                    <td><label id="label_KeyUsage"></label></td>
                                    <td>
                                        <select id ="usage" name="usage">
                                            <option value='1'><%=strKeyUsageSign%></option>
                                            <option value='2'><%=strKeyUsageEncrypt%></option>
                                            <option value='3'><%=strKeyUsageSignEncrypt%></option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label id="label_RSAKeyBits"></label>(bits)</td>
                                    <td>
                                        <select name="length">
                                            <option value='1024'>1024</option>
                                            <option value='2048'>2048</option>
                                            <option value='3072'>3072</option>
                                            <option value='4096'>4096</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="text-align: center">
                                        <input type="submit" name="submit" id="button_generateKeyPair" value="生成密钥对" onclick="return submit1()" style="display:inline"/>
                                        <div id="hidemsg" style="display:none; text-align:center">
                                            <font color="#0000FF"><%=strGenerateTips%></font>
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
                            <% if (Config.isECCSupport()) {%>                            
                            <li><a href="/handle/maintainKey.jsp?action=ECCManagement&isGuide=true" target="panel" style="width:60px"><label id="label_Next"></label></a></li>
                            <% } else {%>                            
                            <li><a href="/handle/maintainKey.jsp?action=kekManagement&isGuide=true" target="panel" style="width:60px"><label id="label_Next"></label></a></li>
                            <% }%>                          
                            <li><a href="/user/operatorAdd.jsp?isGuide=true" target="panel" style="width:60px"><label id="label_Previous"></label></a></li>
                        </ul>
                    </div>
                    <% }%>
                    <jsp:include page="/key/asymmtricKeyStatus.jsp">
                        <jsp:param name="algrithm" value="RSA" />
                        <jsp:param name="isGuide" value="<%= isGuide%>" />
                    </jsp:include>
                </div>
            </div>
        </div>
    </body>
</html>
