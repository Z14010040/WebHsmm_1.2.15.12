<%@page import="com.sansec.hsm.bean.Language"%>
    <%@page import="com.sansec.hsm.util.OperationLogUtil"%>
        <%@page import="com.sansec.hsm.bean.GlobalData"%>
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
            <%@page import="com.sansec.hsm.bean.Config"%>
                <%@page import="com.sansec.hsm.util.IPUtil"%>
                    <%@page import="com.sansec.hsm.bean.login.UserList"%>
                        <%@ page language="java" pageEncoding="UTF-8"%>
                            <%!	UserList userlist = UserList.getInstance();

%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	//language
	String strMultiWarning = Language.get("LoginMultiWarning");
	String strOnlineUser = Language.get("LoginOnlineUser");
%>

                                    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
                                    <html>

                                    <head>
                                        <base href="<%=basePath%>">
                                        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                                        <title>用户登录</title>
                                        <meta http-equiv="pragma" content="no-cache">
                                        <meta http-equiv="cache-control" content="no-cache">
                                        <meta http-equiv="expires" content="0">
                                        <link rel="icon" href="../images/favicon.ico" type="image/x-icon" />
                                        <link rel="shortcut icon" href="../images/favicon.ico" type="image/x-icon" />
                                        <link rel="stylesheet" type="text/css" href="css/styles.css" />
                                        <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
                                        <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
                                        <script type="text/javascript" src="js/language.js"></script>
                                        <script type="text/javascript">
                                            function display() {
                                                //alert("in display");
                                                //alert($.i18n.prop('Login'));
                                                $('#label_welcome').html($.i18n.prop('Welcome'));
                                                $('#label_networkHSM').html($.i18n.prop('NetworkHSM'));
                                                $('#label_username').html($.i18n.prop('UserName'));
                                                $('#label_password').html($.i18n.prop('Password'));
                                                $('#label_language').html($.i18n.prop('Language'));
                                                //footer
                                                $('#label_copyright').html($.i18n.prop('Copyright'));
                                                $('#label_simpleFactoryName').html($.i18n.prop('SimpleFactoryName'));
                                                $('#label_homePage').html($.i18n.prop('HomePage'));
                                                document.getElementById("button_login").value = $.i18n.prop('Login');
                                                window.document.title = $.i18n.prop('LoginTitle');
                                            }

                                            function load() {
                                                getLanguage(display);
                                                window.languageCallback = display;
                                            }
                                            window.onload = load;
                                        </script>

                                        <script type="text/javascript">
                                            
//                                            function checkF() {
//                                                var user = document.getElementById("user").value;
//                                                var pwd = document.getElementById("pwd").value;
//                                                if (user == null || user == "") {
//                                                    alert($.i18n.prop('LoginInvaildUsername'));
//                                                    return false;
//                                                }
//                                                if (pwd == null || pwd == "") {
//                                                    alert($.i18n.prop('LoginInvalidPassword'));
//                                                    return false;
//                                                }
//                                                return true;
//                                            }
                                           
                                        </script>
                                        <script language="javascript">
                                            if (window.top != window.self) {
                                                window.top.location = window.location.href;
                                            }
                                        </script>
                                    </head>

                                    <body>
                                        <%
								// get all user
								String[] users = userlist.getAllUsers();
								String warning = "";
								String result = "";
								if (users.length >= 1) {
									warning = strMultiWarning;
									result += strOnlineUser;
									for (int i = 0; i < users.length; i++) {
										result += users[i];
										result += "<br>";
									}
								}

//								Config.initialize();
								String deviceType = Config.getDeviceType();
								String simpleFactoryName = Config.getSimpleFactoryName();
								String homePageLink = Config.getHomePageLink();
								boolean isShowHomePageLink = Config.isShowHomePageLink();
								String version = GlobalData.VERSION;


							%>

                                            <div id="loginPageInfo">
                                                <div id="loginLogo">
                                                    <!-- <font size="4"><a href="javascript:setLanguage()"><label id="label_language"></label></a> -->
                                                    </font>
                                                    <p>&nbsp;</p>
                                                    <p>&nbsp;</p>
                                                    <p>&nbsp;</p>
                                                    <div id="row"><label id="label_welcome" /></div>
                                                    <%=deviceType%>&nbsp;
                                                        <div id="row"><label id="label_networkHSM" /></div>
                                                </div>
                                                <div id="loginLogU">
                                                    <form action="/handle/maintainLogin.jsp?action=login" method="post">
                                                        <div id="loginArea">
                                                            <table style="width:280px;">
                                                                <tr>
                                                                    <td class="left"><label id="label_username"></label>:</td>
                                                                    <td class="right"><input type="text" name="user" autocomplete="off" id="user" /></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="left"><label id="label_password"></label>:</td>
                                                                    <td class="right"><input type="password" autocomplete="off" name="pwd" id="pwd" /></td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2" class="warning" id="warning">${ (error != null ? error : "") }</td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2" class="oneline" id="tabletitle">
                                                                        <%= result%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2" class="warning" id="warning">
                                                                        <%= warning%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2" class="oneline">
                                                                        <input type="submit" id="button_login" onClick="return checkF()" style="background: #50c0f0; margin-right: auto;margin-left:auto; " />
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </form>
                                                </div>
                                                <div id="footer">
                                                    <%=version%>
                                                </div>
                                            </div>
                                    </body>

                                    </html>