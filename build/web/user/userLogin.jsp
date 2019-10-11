<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>用户登录</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script language="JavaScript" type="text/javascript">
            function check(){
                var sPin = document.getElementById("pin").value;
                if(sPin == ""){
                    alert($.i18n.prop('ICPINCheckNull'));
                    return false;
                }else if(sPin.length != 8){
                    alert($.i18n.prop('ICPINCheckLength'));
                    return false;
                }
                $("#button_userLogin").attr("disabled", "disabled");
                return true;
            }
        </script>
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_UserLoginTitle').html($.i18n.prop('UserLoginTitle'));
				$('#label_UserLoginTips').html($.i18n.prop('UserLoginTips'));
				$('#label_UserLoginInputTips').html($.i18n.prop('UserLoginInputTips'));
				$('#label_UserLoginStatusTitle').html($.i18n.prop('UserLoginStatusTitle'));
				$('#label_UserLoginPrivilege').html($.i18n.prop('UserLoginPrivilege'));
				$('#label_UserLoginManagerCount').html($.i18n.prop('UserLoginManagerCount'));
				$('#label_UserLoginLoginManager').html($.i18n.prop('UserLoginLoginManager'));
				$('#label_UserOperatorCount').html($.i18n.prop('UserOperatorCount'));
				$('#label_UserLoginLogOffAllManager').html($.i18n.prop('UserLoginLogOffAllManager'));
				$('#label_UserLoginOperatorStatus').html($.i18n.prop('UserLoginOperatorStatus'));
				$('#label_UserLoginLogOffAllOperator').html($.i18n.prop('UserLoginLogOffAllOperator'));
				document.getElementById("button_userLogin").value=$.i18n.prop('UserLoginBtn');
			}

			function load(){
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
                    <jsp:param name="step" value="1" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_UserLoginTitle"></label></div>
                <div class="tobody"> 
                    <label id="label_UserLoginTips"></label><br>
                    <form onsubmit="return check()" method="post" action="/handle/maintainUser.jsp?action=login&isGuide=false">
                        <div>
                            <br>
                            <label id="label_UserLoginInputTips"></label>:&nbsp;<input type="password" id="pin" name="pin" autocomplete="off"/>
                            <input type="submit" id="button_userLogin" value="用户登录"/>
                        </div>
                    </form>
                    <div id="title">${ result }</div>
                </div>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_UserLoginStatusTitle"></label></div>
                <div class="tobody">
                    <table>
                        <tr>
                            <td width="30%"><label id="label_UserLoginPrivilege"></label></td>
                            <td width="40%">${ privilegeInfo }</td>
                            <td width="30%"></td>
                        </tr>　
                        <tr>
                            <td><label id="label_UserLoginManagerCount"></label></td>
                            <td>${ managerCount }</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><label id="label_UserOperatorCount"></label></td>
                            <td>${ operatorCount }</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><label id="label_UserLoginLoginManager"></label></td>
                            <td>${ onlineManagerInfo }</td>
                            <td><a href="/handle/maintainUser.jsp?action=logoutAllManager&isGuide=false"><label id="label_UserLoginLogOffAllManager"></label></a></td>
                        </tr>
                        <tr>
                            <td><label id="label_UserLoginOperatorStatus"></label></td>
                            <td>${ operatorStatus }</td>
                            <td><a href="/handle/maintainUser.jsp?action=logoutAllOperator&isGuide=false"><label id="label_UserLoginLogOffAllOperator"></label></a></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
