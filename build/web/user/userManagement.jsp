<%@page import="com.sansec.hsm.bean.Language" %>
<%@page import="debug.log.LogUtil" %>
<%@page import="java.util.List" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    //Language
    String strManagerAdd = Language.get("ButtonAdd");
    String strManagerDelete = Language.get("ButtonDelete");
    String strStatusValid = Language.get("StatusValid");
    String strUserManagementManagerName = Language.get("UserManagementManagerName");
    String strUserManagementManagerNamePrefix = Language.get("UserManagementManagerNamePrefix");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>User Management</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/styles.css"/>
    <script language="JavaScript" type="text/javascript">
        function check() {
            var sPin = document.getElementById("pin").value;
            if (sPin == "") {
                alert($.i18n.prop('ICPINCheckNull'));
                return false;
            } else if (sPin.length != 8) {
                alert($.i18n.prop('ICPINCheckLength'));
                return false;
            }
            $("#button_confirm").attr("disabled", "disabled");
            return true;
        }

        function doSubmit() {
            var input = document.getElementById("button_resetOperator");
            input.disabled = 'disabled';
            return true;
        }
    </script>

    <script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="js/language.js"></script>
    <script type="text/javascript">
        function display() {
            $('#label_UserManagementManger').html($.i18n.prop('UserManagementManger'));
            $('#label_UserManagementMangerNo').html($.i18n.prop('UserManagementMangerNo'));
            $('#label_UserManagementMangerStatus').html($.i18n.prop('UserManagementMangerStatus'));
            $('#label_UserManagementMangerOperate').html($.i18n.prop('UserManagementMangerOperate'));
            $('#label_UserManagementOperator').html($.i18n.prop('UserManagementOperator'));
            $('#label_UserManagementOperatorAdd').html($.i18n.prop('UserManagementOperatorAdd'));
            $('#label_UserManagementOperatorAddTips').html($.i18n.prop('UserManagementOperatorAddTips'));
            $('#label_UserManagementOperatorAddInput').html($.i18n.prop('UserManagementOperatorAddInput'));
            $('#label_UserManagementDeleteOperator').html($.i18n.prop('UserManagementDeleteOperator'));
            $('#label_UserManagementDeleteOperatorTips').html($.i18n.prop('UserManagementDeleteOperatorTips'));
            document.getElementById("button_confirm").value = $.i18n.prop('ButtonConfirm');
            document.getElementById("button_resetOperator").value = $.i18n.prop('UserManagementResetOperatorBtn');
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
<%
    List<Integer> listManager = (List<Integer>) request.getAttribute("allManager");
    LogUtil.println("userManagement.jsp  -> list: " + listManager);
%>
<div class="totalInfo">
    <div class="guideInfo">
        <jsp:include page="/guide/user.jsp">
            <jsp:param name="step" value="2"/>
        </jsp:include>
    </div>
    <div class="blank"></div>
    <div class="Info">
        <div class="toheader"><label id="label_UserManagementManger"></label></div>
        <div class="tobody">
            <table>
                <tr>
                    <td width="35%"><label id="label_UserManagementMangerNo"></label></td>
                    <td width="35%"><label id="label_UserManagementMangerStatus"></label></td>
                    <td width="30%"><label id="label_UserManagementMangerOperate"></label></td>
                </tr>
                <%
                    int bFirstNotExistManager = 1;
                    for (int i = 0; i < listManager.size(); i++) {
                %>
                <tr>
                    <td><%=strUserManagementManagerNamePrefix%><%= i + 1%><%=strUserManagementManagerName%>
                    </td>
                    <%
                        if (listManager.get(i) == 0) {
                    %>
                    <td>-</td>
                    <%
                        //判断是否是首个不存在的管理员
                        if (bFirstNotExistManager == 1) {
                    %>
                    <td><a href="/user/managerAdd.jsp?isGuide=false"><%=strManagerAdd%>
                    </a></td>
                    <%
                        bFirstNotExistManager = 0;
                    } else {
                    %>
                    <td>-</td>
                    <% }
                        //if(bFirstNotExistManager == 1) {
                    %>
                    <%
                    } else {
                    %>
                    <td><%=strStatusValid%>
                    </td>
                    <td>
                        <a href="/handle/maintainUser.jsp?action=delManager&isGuide=false&id=<%= i + 1%>"><%=strManagerDelete%>
                        </a></td>
                    <%
                        }
                        //if(listManager.get(i) == 0) {
                    %>

                </tr>
                <%
                    }
                %>
            </table>
        </div>
    </div>
    <div class="blank"></div>
    <div class="Info">
        <div class="toheader"><label id="label_UserManagementOperator"></label></div>
        <div class="tobody">
            <div class="inheader"><label id="label_UserManagementOperatorAdd"></label></div>
            <div class="inbody">
                <table>
                    <tr>
                        <td>
                            <br>
                            <label id="label_UserManagementOperatorAddTips"></label>.
                            <form method="post" onsubmit="return check()"
                                  action="/handle/maintainUser.jsp?action=addOperator&isGuide=false">
                                <div>
                                    <br>
                                    <label id="label_UserManagementOperatorAddInput"></label>:&nbsp;<input
                                        type="password" name="pin" id="pin" autocomplete="off"/>
                                    <input type="submit" id="button_confirm" name="Submit" value="submit"/>
                                </div>
                            </form>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="blank"></div>
            <div class="inheader"><label id="label_UserManagementDeleteOperator"></label></div>
            <div class="inbody">
                <table>
                    <tr>
                        <td>
                            <br>
                            <font color="blue"><label id="label_UserManagementDeleteOperatorTips"></label>.</font><br>
                            <br>
                            <form method="post" onsubmit="return doSubmit()"
                                  action="/handle/maintainUser.jsp?action=delOperator&isGuide=false">
                                <div class="button">
                                    <input type="submit" id="button_resetOperator" value="Reset operator password"/>
                                </div>
                            </form>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div id="title">${ result }</div>
    <div class="blank"></div>
</div>
</body>
</html>
