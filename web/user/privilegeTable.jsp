<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>用户登录</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/styles.css"/>
    <script language="JavaScript" type="text/javascript">
        function check() {
            var sPin = document.getElementById("sPin");
            if (sPin.value == "") {
                alert("请输入PIN口令!");
                return false;
            }
            return true;
        }
    </script>
    <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="js/language.js"></script>
    <script type="text/javascript">
        function display() {
            $('#label_PrivilegeTableHeader').html($.i18n.prop('PrivilegeTableHeader'));

            $('#label_PrivilegeTableSystemHeader').html($.i18n.prop('PrivilegeTableSystemHeader'));
            $('#label_PrivilegeTableSystemType').html($.i18n.prop('PrivilegeTableType'));
            $('#label_PrivilegeTableSystemDetail').html($.i18n.prop('PrivilegeTableDetail'));
            $('#label_PrivilegeTableSystemPrivilege').html($.i18n.prop('PrivilegeTablePrivilege'));
            $('#label_PrivilegeTableSystemDevice').html($.i18n.prop('PrivilegeTableSystemDevice'));
            $('#label_PrivilegeTableSystemDeviceViewBasic').html($.i18n.prop('PrivilegeTableSystemDeviceViewBasic'));
            $('#label_PrivilegeTableSystemDeviceViewStatus').html($.i18n.prop('PrivilegeTableSystemDeviceViewStatus'));
            $('#label_PrivilegeTableSystemDeviceViewMaintain').html($.i18n.prop('PrivilegeTableSystemDeviceViewMaintain'));
            $('#label_PrivilegeTableSystemDeviceModifyMaintain').html($.i18n.prop('PrivilegeTableSystemDeviceModifyMaintain'));
            $('#label_PrivilegeTableSystemSystem').html($.i18n.prop('PrivilegeTableSystemSystem'));
            $('#label_PrivilegeTableSystemSystemChangePassword').html($.i18n.prop('PrivilegeTableSystemSystemChangePassword'));
            $('#label_PrivilegeTableSystemService').html($.i18n.prop('PrivilegeTableSystemService'));
            $('#label_PrivilegeTableSystemServiceStart').html($.i18n.prop('PrivilegeTableSystemServiceStart'));
            $('#label_PrivilegeTableSystemServiceStop').html($.i18n.prop('PrivilegeTableSystemServiceStop'));
            $('#label_PrivilegeTableSystemServiceModifyConfig').html($.i18n.prop('PrivilegeTableSystemServiceModifyConfig'));
            $('#label_PrivilegeTableSystemNetwork').html($.i18n.prop('PrivilegeTableSystemNetwork'));
            $('#label_PrivilegeTableSystemNetworkRestart').html($.i18n.prop('PrivilegeTableSystemNetworkRestart'));
            $('#label_PrivilegeTableSystemNetworkModifyConfig').html($.i18n.prop('PrivilegeTableSystemNetworkModifyConfig'));

            $('#label_PrivilegeTablePrivilegeHeader').html($.i18n.prop('PrivilegeTablePrivilegeHeader'));
            $('#label_PrivilegeTablePrivilegeType').html($.i18n.prop('PrivilegeTableType'));
            $('#label_PrivilegeTablePrivilegeDetail').html($.i18n.prop('PrivilegeTableDetail'));
            $('#label_PrivilegeTablePrivilegePrivilege').html($.i18n.prop('PrivilegeTablePrivilege'));
            $('#label_PrivilegeTablePrivilegePrivilegeManagement').html($.i18n.prop('PrivilegeTablePrivilegePrivilegeManagement'));
            $('#label_PrivilegeTablePrivilegePrivilegeViewLogin').html($.i18n.prop('PrivilegeTablePrivilegePrivilegeViewLogin'));
            $('#label_PrivilegeTablePrivilegePrivilegeViewPrivilege').html($.i18n.prop('PrivilegeTablePrivilegePrivilegeViewPrivilege'));
            $('#label_PrivilegeTablePrivilegeManager').html($.i18n.prop('PrivilegeTablePrivilegeManager'));
            $('#label_PrivilegeTablePrivilegeManagerAddFirst').html($.i18n.prop('PrivilegeTablePrivilegeManagerAddFirst'));
            $('#label_PrivilegeTablePrivilegeManagerAdd').html($.i18n.prop('PrivilegeTablePrivilegeManagerAdd'));
            $('#label_PrivilegeTablePrivilegeManagerDelete').html($.i18n.prop('PrivilegeTablePrivilegeManagerDelete'));
            $('#label_PrivilegeTablePrivilegeOperator').html($.i18n.prop('PrivilegeTablePrivilegeOperator'));
            $('#label_PrivilegeTablePrivilegeOperatorAdd').html($.i18n.prop('PrivilegeTablePrivilegeOperatorAdd'));
            $('#label_PrivilegeTablePrivilegeOperatorDelete').html($.i18n.prop('PrivilegeTablePrivilegeOperatorDelete'));

            $('#label_PrivilegeTableKeyHeader').html($.i18n.prop('PrivilegeTableKeyHeader'));
            $('#label_PrivilegeTableKeyType').html($.i18n.prop('PrivilegeTableType'));
            $('#label_PrivilegeTableKeyDetail').html($.i18n.prop('PrivilegeTableDetail'));
            $('#label_PrivilegeTableKeyPrivilege').html($.i18n.prop('PrivilegeTablePrivilege'));
            $('#label_PrivilegeTableKeyAsymm').html($.i18n.prop('PrivilegeTableKeyAsymm'));
            $('#label_PrivilegeTableKeyAsymmView').html($.i18n.prop('PrivilegeTableKeyAsymmView'));
            $('#label_PrivilegeTableKeyAsymmGenerate').html($.i18n.prop('PrivilegeTableKeyAsymmGenerate'));
            $('#label_PrivilegeTableKeyAsymmDelete').html($.i18n.prop('PrivilegeTableKeyAsymmDelete'));
            $('#label_PrivilegeTableKeyAsymmImport').html($.i18n.prop('PrivilegeTableKeyAsymmImport'));
            $('#label_PrivilegeTableKeyAsymmSetAccess').html($.i18n.prop('PrivilegeTableKeyAsymmSetAccess'));
            $('#label_PrivilegeTableKeyBackupRestore').html($.i18n.prop('PrivilegeTableKeyBackupRestore'));
            $('#label_PrivilegeTableKeyBackup').html($.i18n.prop('PrivilegeTableKeyBackup'));
            $('#label_PrivilegeTableKeyRestore').html($.i18n.prop('PrivilegeTableKeyRestore'));
            $('#label_PrivilegeTablePrivilegeOperatorDelete').html($.i18n.prop('PrivilegeTablePrivilegeOperatorDelete'));

            $('#label_PrivilegeTableFileHeader').html($.i18n.prop('PrivilegeTableFileHeader'));
            $('#label_PrivilegeTableFileType').html($.i18n.prop('PrivilegeTableType'));
            $('#label_PrivilegeTableFileDetail').html($.i18n.prop('PrivilegeTableDetail'));
            $('#label_PrivilegeTableFilePrivilege').html($.i18n.prop('PrivilegeTablePrivilege'));
            $('#label_PrivilegeTableFileLog').html($.i18n.prop('PrivilegeTableFileLog'));
            $('#label_PrivilegeTableFileLogView').html($.i18n.prop('PrivilegeTableFileLogView'));
            $('#label_PrivilegeTableFileLogAudit').html($.i18n.prop('PrivilegeTableFileLogAudit'));
        }

        function load() {
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
            <jsp:param name="step" value="4"/>
        </jsp:include>
    </div>
    <div class="blank"></div>
    <div class="Info">
        <div class="toheader"><label id="label_PrivilegeTableHeader"></label></div>
        <div class="tobody">
            <div class="inheader"><label id="label_PrivilegeTableSystemHeader"></label></div>
            <div class="inbody">
                <table>
                    <tr>
                        <td width="25%"><label id="label_PrivilegeTableSystemType"></label></td>
                        <td width="45%"><label id="label_PrivilegeTableSystemDetail"></label></td>
                        <td width="30%"><label id="label_PrivilegeTableSystemPrivilege"></label></td>
                    </tr>
                    　
                    <tr>
                        <td rowspan="4"><label id="label_PrivilegeTableSystemDevice"></label></td>
                        <td><label id="label_PrivilegeTableSystemDeviceViewBasic"></label></td>
                        <td>${ showDeviceInfo }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableSystemDeviceViewStatus"></label></td>
                        <td>${ showRunStatus }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableSystemDeviceViewMaintain"></label></td>
                        <td>${ showMaintainInfo }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableSystemDeviceModifyMaintain"></label></td>
                        <td>${ modifyMaintainInfo }</td>
                    </tr>
                    <tr>
                        <td rowspan="1"><label id="label_PrivilegeTableSystemSystem"></label></td>
                        <td><label id="label_PrivilegeTableSystemSystemChangePassword"></label></td>
                        <td>${ changeSystemPwd }</td>
                    </tr>
                    <tr>
                        <td rowspan="3"><label id="label_PrivilegeTableSystemService"></label></td>
                        <td><label id="label_PrivilegeTableSystemServiceStart"></label></td>
                        <td>${ startService }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableSystemServiceStop"></label></td>
                        <td>${ stopService }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableSystemServiceModifyConfig"></label></td>
                        <td>${ modifyServiceConfig }</td>
                    </tr>
                    <tr>
                        <td rowspan="2"><label id="label_PrivilegeTableSystemNetwork"></label></td>
                        <td><label id="label_PrivilegeTableSystemNetworkRestart"></label></td>
                        <td>${ resetNetwork }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableSystemNetworkModifyConfig"></label></td>
                        <td>${ modifyNetworkConfig }</td>
                    </tr>
                </table>
            </div>
            <div class="blank"></div>
            <div class="inheader"><label id="label_PrivilegeTablePrivilegeHeader"></label></div>
            <div class="inbody">
                <table>
                    <tr>
                        <td width="25%"><label id="label_PrivilegeTablePrivilegeType"></label></td>
                        <td width="45%"><label id="label_PrivilegeTablePrivilegeDetail"></label></td>
                        <td width="30%"><label id="label_PrivilegeTablePrivilegePrivilege"></label></td>
                    </tr>
                    　
                    <tr>
                        <td rowspan="2"><label id="label_PrivilegeTablePrivilegePrivilegeManagement"></label></td>
                        <td><label id="label_PrivilegeTablePrivilegePrivilegeViewLogin"></label></td>
                        <td>${ showLoginStatus }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTablePrivilegePrivilegeViewPrivilege"></label></td>
                        <td>${ shwoPrivilegeTable }</td>
                    </tr>
                    <tr>
                        <td rowspan="3"><label id="label_PrivilegeTablePrivilegeManager"></label></td>
                        <td><label id="label_PrivilegeTablePrivilegeManagerAddFirst"></label></td>
                        <td>${ initManager }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTablePrivilegeManagerAdd"></label></td>
                        <td>${ addManager }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTablePrivilegeManagerDelete"></label></td>
                        <td>${ delManager }</td>
                    </tr>
                    <tr>
                        <td rowspan="2"><label id="label_PrivilegeTablePrivilegeOperator"></label></td>
                        <td><label id="label_PrivilegeTablePrivilegeOperatorAdd"></label></td>
                        <td>${ addOperator }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTablePrivilegeOperatorDelete"></label></td>
                        <td>${ delOperator }</td>
                    </tr>
                </table>
            </div>
            <div class="blank"></div>
            <div class="inheader"><label id="label_PrivilegeTableKeyHeader"></label></div>
            <div class="inbody">
                <table>
                    <tr>
                        <td width="25%"><label id="label_PrivilegeTableKeyType"></label></td>
                        <td width="45%"><label id="label_PrivilegeTableKeyDetail"></label></td>
                        <td width="30%"><label id="label_PrivilegeTableKeyPrivilege"></label></td>
                    </tr>
                    　
                    <tr>
                        <td rowspan="5"><label id="label_PrivilegeTableKeyAsymm"></label></td>
                        <td><label id="label_PrivilegeTableKeyAsymmView"></label></td>
                        <td>${ showKeyStatus }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableKeyAsymmGenerate"></label></td>
                        <td>${ generateKey }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableKeyAsymmDelete"></label></td>
                        <td>${ delKey }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableKeyAsymmImport"></label></td>
                        <td>${ importKey }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableKeyAsymmSetAccess"></label></td>
                        <td>${ setAccessPin }</td>
                    </tr>
                    <tr>
                        <td rowspan="2"><label id="label_PrivilegeTableKeyBackupRestore"></label></td>
                        <td><label id="label_PrivilegeTableKeyBackup"></label></td>
                        <td>${ keyBackup}</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableKeyRestore"></label></td>
                        <td>${ keyRestore }</td>
                    </tr>
                </table>
            </div>
            <div class="blank"></div>
            <div class="inheader"><label id="label_PrivilegeTableFileHeader"></label></div>
            <div class="inbody">
                <table>
                    <tr>
                        <td width="25%"><label id="label_PrivilegeTableFileType"></label></td>
                        <td width="45%"><label id="label_PrivilegeTableFileDetail"></label></td>
                        <td width="30%"><label id="label_PrivilegeTableFilePrivilege"></label></td>
                    </tr>
                    　
                    <tr>
                        <td rowspan="2"><label id="label_PrivilegeTableFileLog"></label></td>
                        <td><label id="label_PrivilegeTableFileLogView"></label></td>
                        <td>${ showlog }</td>
                    </tr>
                    <tr>
                        <td><label id="label_PrivilegeTableFileLogAudit"></label></td>
                        <td>${ auditlog }</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
