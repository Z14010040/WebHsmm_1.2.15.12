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

        <title>Change Web Password</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script language="JavaScript" type="text/javascript">
            function check(){
                var oldPin = $("#oldPin").val();
                var newPin = $("#newPin").val();
                var renPin = $("#renPin").val();
                if(oldPin == ""){
                    alert("Password can not be blank.");
                    return false;
                }

                var flag = passwordCheck(newPin);
                if(!flag){
                    return false;
                }
                if(renPin == ""){
                    alert("Repeat Password can not be blank.");
                    return false;
                }else if(newPin != renPin){
                    alert("Repeat Password is not corrected.");
                    return false;
                }else if(oldPin == newPin ){
                    alert("New password and old password cannot be the same.");
                    return false;
                }
                return true;
            }
            function passwordCheck(newPwd){
                var num = 0;
                var rule1 = /\d+/;
                var rule2 = /[a-z]+/;
                var rule3 = /[A-Z]+/;
                var rule4 = /[._-]+/;
                var rule5 = /^.{8,64}$/;
                var flag1 = rule1.test(newPwd);
                var flag2 = rule2.test(newPwd);
                var flag3 = rule3.test(newPwd);
                var flag4 = rule4.test(newPwd);
                var flag5 = rule5.test(newPwd);
                if (flag1){
                    num = num + 1;
                }
                if (flag2){
                    num = num + 1;
                }
                if (flag3){
                    num = num + 1;
                }
                if (flag4){
                    num = num + 1;
                }
                if(!(num >2&&flag5)){
                    alert("New password must be a combination of 8-64 characters. At least three of numbers, uppercase and lowercase letters, and special symbols : '_' , '-' , '.'");
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
				$('#label_ChangeICPIN').html('Change Login Password');
				$('#label_ChangeICPINTips').html('Please enter an old password to change new password');
				$('#label_ChangeICPINOldPassword').html('Please input old password');
				$('#label_ChangeICPINNewPassword').html('Please input new password');
				$('#label_ChangeICPINComfirmPassword').html('Repeat new password');
				document.getElementById("button_changePIN").value='Change Password';
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
                    <jsp:param name="step" value="4" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_ChangeICPIN"></label></div>
                <div class="tobody">
                    <label id="label_ChangeICPINTips"></label>.<br>
                    <form id="changeUp" name="changeUp" method="post" action="/handle/maintainUser.jsp?action=changeloginPin&isGuide=false">
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
