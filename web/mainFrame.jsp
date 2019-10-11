<%@page import="com.sansec.hsm.bean.GlobalData" %>
<%@ include file="/common.jsp" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@page import="com.sansec.hsm.bean.Config" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%
        //String deviceType = Config.getDeviceType();
        String deviceType = "";
        String simpleFactoryName = Config.getSimpleFactoryName();
        String homePageLink = Config.getHomePageLink();
        boolean isShowHomePageLink = Config.isShowHomePageLink();
        String version = GlobalData.VERSION;
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>密码设备</title>
    <base href="<%=basePath%>"/>
    <link rel="icon" href="../images/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="../images/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="css/main.css" type="text/css"/>
    <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
    <script type="text/javascript" src="js/menu.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var pageWidth = window.innerWidth;
            var pageHeight = window.innerHeight;
            if (typeof pageWidth != "number") {
                if (document.compatMode == "number") {
                    pageWidth = document.documentElement.clientWidth;
                    pageHeight = document.documentElement.clientHeight;
                } else {
                    pageWidth = document.body.clientWidth;
                    pageHeight = document.body.clientHeight;
                }
            }
            //alert(pageHeight);
            //$('#wait').height($(window).innerHeight() *0.9);
            //$('#wait').width($(window).innerWidth()*0.8);
            //$('.leftmenu').height($(window).innerHeight() *0.8);
            //$('.center').height($(window).innerHeight() *0.8);
            $('#wait').height(pageHeight * 0.9);
            $('#wait').width(pageWidth * 0.8);
            $('.leftmenu').height(pageHeight * 0.8);
            $('.center').height(pageHeight * 0.8);
        });
    </script>
    <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="js/language.js"></script>
    <script type="text/javascript">
        function display() {
            //alert("in display");
            //alert($.i18n.prop('Login'));
            $('#label_language').html($.i18n.prop('Language'));
            $('#label_welcome').html($.i18n.prop('Welcome'));
            $('#label_networkHSM').html($.i18n.prop('NetworkHSM'));
            $('#label_logout').html($.i18n.prop('Logout'));
            //footer
            $('#label_copyright').html($.i18n.prop('Copyright'));
            $('#label_simpleFactoryName').html($.i18n.prop('SimpleFactoryName'));
            $('#label_homePage').html($.i18n.prop('HomePage'));
            window.document.title = $.i18n.prop('MainTitle');
            //MENU
            $('#menu_setupGuide').html($.i18n.prop('MenuSetupGuide'));
            $('#menu_login').html($.i18n.prop('MenuUserLogin'));
            $('#menu_autoLogin').html($.i18n.prop('MenuAutoLogin'));
            //SYSTEM
            $('#menu_systemManagement').html($.i18n.prop('MenuSystemManagement'));
            $('#menu_systemInfo').html($.i18n.prop('MenuDeviceInfo'));
            $('#menu_systemMaintainInfo').html($.i18n.prop('MenuDeviceMaintainInfo'));
            $('#menu_networkConfiguration').html($.i18n.prop('MenuNetworkConfiguration'));
            $('#menu_changePassword').html($.i18n.prop('MenuChangePassword'));
            //Privilege
            $('#menu_privilegeManagement').html($.i18n.prop('MenuPrivilegeManagement'));
            $('#menu_login2').html($.i18n.prop('MenuUserLogin'));
            $('#menu_userManagement').html($.i18n.prop('MenuUserManagement'));
            $('#menu_changeICCardPIN').html($.i18n.prop('MenuChangeICCardPIN'));
            $('#menu_privilegeSettingTable').html($.i18n.prop('MenuPrivilegeTable'));
            //KEY
            $('#menu_keyManagement').html($.i18n.prop('MenuKeyManagement'));
            $('#menu_RSAKeyManagement').html($.i18n.prop('MenuRSAKeyManagement'));
            $('#menu_ECCKeyManagement').html($.i18n.prop('MenuECCKeyManagement'));
            $('#menu_KEKManagement').html($.i18n.prop('MenuKEKManagement'));
            $('#menu_destoryKey').html($.i18n.prop('MenuDestroyKey'));
            //Service
            $('#menu_serviceManagement').html($.i18n.prop('MenuServiceManagement'));
            $('#menu_serviceStatus').html($.i18n.prop('MenuServiceStatus'));
            $('#menu_serviceConfiguration').html($.i18n.prop('MenuServiceConfig'));
            $('#menu_whiteListManagement').html($.i18n.prop('MenuWhiteListManagement'));
            $('#menu_startStopService').html($.i18n.prop('MenuServiceOperate'));
            //Backup Restore
            $('#menu_BackupRestore').html($.i18n.prop('MenuBackupRestore'));
            $('#menu_backup').html($.i18n.prop('MenuBackup'));
            $('#menu_restore').html($.i18n.prop('MenuRestore'));
            //LOG
            $('#menu_logManagement').html($.i18n.prop('MenuLogManagement'));
            $('#menu_viewLog').html($.i18n.prop('MenuViewLog'));
            $('#label_GuidePrivilegesUserLogin').html($.i18n.prop('GuidePrivilegesUserLogin'));
            $('#label_GuidePrivilegesUserManagement').html($.i18n.prop('GuidePrivilegesUserManagement'));
            $('#label_GuidePrivilegesModifyICPIN').html($.i18n.prop('GuidePrivilegesModifyICPIN'));
            $('#label_GuidePrivilegesShowTable').html($.i18n.prop('GuidePrivilegesShowTable'));
        }

        function load() {
            getLanguage(display);
            window.languageCallback = display;
        }

        window.onload = function () {
            load();
            menuFix();
        }
    </script>
</head>
<body>
<div class="content">
    <!--top-->
    <div id="header">
        <div id="row">
            <!--  <font size="4"><a href="javascript:setLanguage()"><label id="label_language"></label></a> -->
            </font>
        </div>
        </p>
        <font size="4">
            <div id="row"><label id="label_welcome"/></div>
        </font> &nbsp;<%=deviceType%>&nbsp;<div id="row"><label id="label_networkHSM"/></div>
        <a href="/handle/maintainLogin.jsp?action=logout" style="color:#007DC9; font-size: 14px; text-align: right;">
            <div id="row"><label id="label_logout"/></div>
        </a>
    </div>
    <!--mid-->
    <div id="main">
        <div class="leftmenu">
            <ul id="nav">
                <li>
                    <a href="/key/initialize.jsp?isGuide=true" target="panel"><label id="menu_setupGuide"/></a>
                </li>
                <li class="tree"><a href="/handle/maintainUser.jsp?action=showUserLogin&isGuide=false"
                                    target="panel"><label id="menu_login"/></a>
                    <ul>
                        <li><a href="/handle/maintainUser.jsp?action=showUserLogin&isGuide=false" target="panel"><label
                                id="label_GuidePrivilegesUserLogin"/></a></li>
                        <li><a href="/handle/maintainUser.jsp?action=showUserManagement&isGuide=false"
                               target="panel"><label id="label_GuidePrivilegesUserManagement"/></a></li>
                        <li><a href="/user/changePin.jsp" target="panel"><label id="label_GuidePrivilegesModifyICPIN"/></a>
                        </li>
                        <li><a href="/user/changeloginPin.jsp" target="panel"><label id="changeloginUserPIN">Web
                            Password</label></a></li>
                    </ul>
                </li>
                <li class="tree"><a href="/key/backup/tips.jsp?isGuide=false" target="panel"><label
                        id="menu_BackupRestore"/></a>
                    <ul>
                        <li><a href="/key/backup/tips.jsp?isGuide=false" target="panel"><label id="menu_backup"/></a>
                        </li>
                        <li><a href="/key/restore/tips.jsp?isGuide=false" target="panel"><label id="menu_restore"/></a>
                        </li>
                    </ul>
                </li>
                <li>
                    <a href="/handle/maintainAutoLogin.jsp?action=autoLogin&isGuide=false" target="panel"><label
                            id="menu_autoLogin"/></a>
                </li>

            </ul>
        </div>
        <div class="center">
            <iframe src="/handle/maintainUser.jsp?action=showUserLogin&isGuide=false" frameBorder="0" name="panel"
                    id="panel" width="100%" height="100%"></iframe>
        </div>
    </div>
    <!--bottom-->
    <div id="footer">
        <%=version%>
    </div>
</div>
</body>
</html>
