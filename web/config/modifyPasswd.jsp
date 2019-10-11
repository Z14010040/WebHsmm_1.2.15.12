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

        <title>修改系统登录口令</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script language="JavaScript" type="text/javascript">
            function check(){
                var oldPwd = document.getElementById("oldPwd");
                var newPwd = document.getElementById("newPwd");
                var renPwd = document.getElementById("renPwd");
                if(oldPwd.value == ""){
                    alert($.i18n.prop('PasswordCheckNull'));
                    return false;
                }
                if(newPwd.value == ""){
                    alert($.i18n.prop('PasswordCheckNull'));
                    return false;
                }else if(newPwd.value.length < 6){
                    alert($.i18n.prop('PasswordCheckLength'));
                    return false;
                }else if(newPwd.value.length >8){
                    alert($.i18n.prop('PasswordCheckLength'));
                    return false;
                }else if(renPwd.value == ""){
                    alert($.i18n.prop('PasswordCheckConsistent'));
                    return false;
                }else if(newPwd.value != renPwd.value){
                    alert($.i18n.prop('PasswordCheckConsistent'));
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
				$('#label_PasswordTitle').html($.i18n.prop('PasswordTitle'));
				$('#label_PasswordTips').html($.i18n.prop('PasswordTips'));
				$('#label_PasswordOriginal').html($.i18n.prop('PasswordOriginal'));
				$('#label_PasswordNew').html($.i18n.prop('PasswordNew'));
				$('#label_PasswordConfirm').html($.i18n.prop('PasswordConfirm'));
				document.getElementById("button_changePassword").value=$.i18n.prop('PasswordChangeBtn');
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
                    <jsp:param name="step" value="4" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_PasswordTitle"></label></div>
                <div class="tobody">
                    <label id="label_PasswordTips"></label><br>
                    <form id="changeUp" name="changeUp" method="post" action="/handle/maintainLogin.jsp?action=modifyPasswd">
                        <table>
                            <tr>
                                <td width="50%"><label id="label_PasswordOriginal"></label></td>
                                <td width="50%"><input type="password" name="oldPwd" id="oldPwd" autocomplete="off"/></td>
                            </tr>　
                            <tr>
                                <td><label id="label_PasswordNew"></label></td>
                                <td><input type="password" name="newPwd" id="newPwd" autocomplete="off"/></td>
                            </tr>
                            <tr>
                                <td><label id="label_PasswordConfirm"></label></td>
                                <td><input type="password" name="newPwd2" id="renPwd" autocomplete="off"/></td>
                            </tr>
                        </table>
                        <div id="title">${ result }</div>
                        <div class="button">
                            <input type="submit" id="button_changePassword" name="Submit" value="changePassword" onclick="return check()"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
