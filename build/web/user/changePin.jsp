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

        <title>修改USB key保护口令</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script language="JavaScript" type="text/javascript">
            function check(){
                var oldPin = document.getElementById("oldPin");
                var newPin = document.getElementById("newPin");
                var renPin = document.getElementById("renPin");
               if(oldPin.value == ""){
                    alert("Password length is can not be blank.");
                    return false;
               }
               if(newPin.value == ""){
                    alert("Password length is can not be blank.");
                    return false;
               }else if(newPin.value.length != 8){
                    alert("Password length is must be 8.");
                    return false;
               }else if(renPin.value == ""){
                    alert("Password length is can not be blank.");
                    return false;
               }else if(newPin.value != renPin.value){
                    alert("Repeat Password is not corrected.");
                    return false;
               }else if(oldPin.value == newPin.value ){
                    alert("New password and old password cannot be the same.");
                    return false;
               }
               return true;
            }
        </script>

		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_ChangeICPIN').html($.i18n.prop('ChangeICPIN'));
				$('#label_ChangeICPINTips').html($.i18n.prop('ChangeICPINTips'));
				$('#label_ChangeICPINOldPassword').html($.i18n.prop('ChangeICPINOldPassword'));
				$('#label_ChangeICPINNewPassword').html($.i18n.prop('ChangeICPINNewPassword'));
				$('#label_ChangeICPINComfirmPassword').html($.i18n.prop('ChangeICPINComfirmPassword'));
				document.getElementById("button_changePIN").value=$.i18n.prop('ChangeICPINBtn');
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
                <jsp:include page="/guide/user.jsp">
                    <jsp:param name="step" value="3" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_ChangeICPIN"></label></div>
                <div class="tobody">
                    <label id="label_ChangeICPINTips"></label>.<br>
                    <form id="changeUp" name="changeUp" method="post" action="/handle/maintainUser.jsp?action=changePin&isGuide=false">
                        <table>
                            <tr>
                                <td width="50%"><label id="label_ChangeICPINOldPassword"></label></td>
                                <td width="50%"><input type="password" name="oldPin" id="oldPin" autocomplete="off"/></td>
                            </tr>　
                            <tr>
                                <td><label id="label_ChangeICPINNewPassword"></label></td>
                                <td><input type="password" name="newPin" id="newPin" autocomplete="off"/></td>
                            </tr>
                            <tr>
                                <td><label id="label_ChangeICPINComfirmPassword"></label></td>
                                <td><input type="password" name="renPin" id="renPin" autocomplete="off"/></td>
                            </tr>
                        </table>
                        <div id="title">${ result }</div>
                        <div class="button">
                            <input type="submit" id="button_changePIN" name="Submit" value="changePassword" onclick="return check()"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
