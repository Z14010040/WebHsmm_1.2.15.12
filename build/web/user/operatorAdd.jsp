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

        <title>增加操作员</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script language="JavaScript" type="text/javascript">
            var time;
            function check(){
                var pin=document.getElementById("pin").value;
                var enable=document.getElementById("enable").value;
                if(pin == ""){
                    alert($.i18n.prop('ICPINCheckNull'));
                    return false;
                } else if (pin.length != 8) {
                    alert($.i18n.prop('ICPINCheckLength'));
                    return false;
                } 
//                else {
//                    $("#button_operatorAdd").attr("disabled", "disabled");
//                    return true;
//                }
                if("1"==time){
//                    alert("经过第一次判断");
                    return ture;
                }else{
//                    alert("经过第二次判断");
//                    alert("true"==enable);
//                    alert(pin);
                    if("true"==enable){
                        return confirm('This USBKey has already been registered! Would you like to use it any more!');
                    }else if("false"==enable){
                        return true;
                    }
                }
//                 return ture; 
            }
        </script>

		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				document.getElementById("button_operatorAdd").value=$.i18n.prop('UserOperatorAddBtn');
				$('#label_Next').html($.i18n.prop('GuideNext'));
				$('#label_Previous').html($.i18n.prop('GuidePrevious'));
			}

			function load(){
				//alert(window.languageCallback);
				getLanguage(display);
//				getLanguage(showGuide);
                                time = document.getElementById("time").value;
                                console.log("time的值是：");
                                console.log(time);
                                if("2"==time){
                                    document.getElementById("button_operatorAdd").click();
                //                     alert(2);
                                }else{
                                    time = "1";
                                    document.getElementById("time").value="1";
                //                    alert(3);
                                }
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
                <ul>
                    <%
                        if( isGuide ) {
                    %>
                                <li style="width:80px"><label id="label_GuideInstallWizard">Wizard</label>:</li>
                                <li><a href="/key/initialize.jsp?isGuide=true" target="panel" style="width:75px"><label id="label_GuideInstallDeviceInit">Initialize</label></a></li>
                                <li><a href="/user/managerAdd.jsp?isGuide=true" target="panel" style="width:60px"><label id="label_GuideInstallAddManager">Manager</label></a></li>
                                <li style="width:10px">&gt;</li>
                                <li><a href="/user/operatorAdd.jsp?isGuide=true" target="panel" style="width:60px;color:#007DC9"><b><label id="label_GuideInstallAddOperator">Operator</label></b></a></li>
                                <li style="width:10px">&gt;</li>
                                <li><a href="/key/backup/tips.jsp?isGuide=true" target="panel" style="width:60px;"><label id="label_GuideInstallBackupKey">Backup</label></a></li>
                                <li style="width:10px">&gt;</li>
                                <li><a href="/finish.jsp" target="panel" style="width:75px;"><label id="label_GuideInstallFinish">Finish</label></a></li>
                              
                    <%
                        } else {
                    %>
                            <li style="width:80px"><label id="label_GuidePrivilegesManagement">Privilege</label>:</li>
                            <li><a href="/handle/maintainUser.jsp?action=showUserLogin&isGuide=false" target="panel" style="width:60px"><label id="label_GuidePrivilegesUserLogin">Login</label></a></li>
                            <li style="width:20px">&gt;</li>
                            <li><a href="/handle/maintainUser.jsp?action=showUserManagement&isGuide=false" target="panel" style="width:60px;color:#007DC9"><b><label id="label_GuidePrivilegesUserManagement">User</label></b></a></li>
                            <li style="width:20px">&gt;</li>
                            <li><a href="/user/changePin.jsp" target="panel" style="width:100px"><label id="label_GuidePrivilegesModifyICPIN">USB Key</label></a></li>
                            <li style="width:20px">&gt;</li>
                            <li><a href="/user/changeloginPin.jsp" target="panel" style="width:100px"><label id="changeloginUserPIN">Web Password</label></a></li>
                           
                    <%
                        }
                    %>
                </ul>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_UserOperatorAdd">Add operator</label></div>
                <div class="tobody">
                    <div>
                        <label id="label_UserOperatorAddTips">Please insert operator USB Key into device and then enter the USB Key security code</label>.<br><br>
                        <form method="post" onsubmit="return check()" action="/handle/maintainUser.jsp?action=addOperator">
                            PIN&nbsp;<label id="label_UserOperatorAddPIN">code</label>:&nbsp;&nbsp;
                            <input type="password" name="pin" id="pin" autocomplete="off" value="${ pin }"/>&nbsp;&nbsp;
                            <input type="text" name="time" id="time" autocomplete="off" hidden="true" value="${time}">
                            <input type="text" name="enable" id="enable" autocomplete="off" hidden="true" value="${enable}">
                            <input type="text" name="isGuide" id="isGuide" autocomplete="off" hidden="true" value="${isGuide}">
                            <input type="submit" id="button_operatorAdd" name="Submit" value="Add operator"/>
                            <br>
                        </form>               
                    </div>
                    <div id="title">${(result == null ? "" : result) }</div>
                </div>
                <%
                    if( isGuide ) {
                %>
                <div class="tobottom">
                    <ul>
                        <li><a href="/key/backup/tips.jsp?isGuide=false" target="panel" style="width:60px"><label id="label_Next"></label></a></li>
                        <li><a href="/user/managerAdd.jsp?isGuide=true" target="panel" style="width:60px"><label id="label_Previous"></label></a></li>
                <%
                    } else {
                %>
                        <li><a href="/handle/maintainUser.jsp?action=showManagement&isGuide=false" target="panel" style="width:45px"><label id="label_Return">Return</label></a></li>
                    </ul>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </body>
</html>
