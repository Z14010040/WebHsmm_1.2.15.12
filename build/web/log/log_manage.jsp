<%@page import="com.sansec.hsm.bean.Language"%>
<%@page import="debug.log.LogUtil.Log"%>
<%@page import="debug.log.LogUtil"%>
<%@page import="com.sansec.hsm.bean.log.LogManage"%>
<%@ page import="java.util.*"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

	String strAudit = Language.get("LogManagementAudit");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <base href="<%=basePath%>" />

        <title>管理日志</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />       
        <link rel="stylesheet" type="text/css" href="css/styles.css" />

		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_LogManagementHeader').html($.i18n.prop('LogManagementHeader'));
				$('#label_LogManagementBatchOperate1').html($.i18n.prop('LogManagementBatchOperate'));
				$('#label_LogManagementSelectAll1').html($.i18n.prop('LogManagementSelectAll'));
				$('#label_LogManagementSelectOther1').html($.i18n.prop('LogManagementSelectOther'));
				$('#label_LogManagementBatchOperate2').html($.i18n.prop('LogManagementBatchOperate'));
				$('#label_LogManagementSelectAll2').html($.i18n.prop('LogManagementSelectAll'));
				$('#label_LogManagementSelectOther2').html($.i18n.prop('LogManagementSelectOther'));

				$('#label_LogManagementLogTime').html($.i18n.prop('LogManagementLogTime'));
				$('#label_LogManagementLogOperator').html($.i18n.prop('LogManagementLogOperator'));
				$('#label_LogManagementLogDetail').html($.i18n.prop('LogManagementLogDetail'));
				$('#label_LogManagementLogAudit').html($.i18n.prop('LogManagementLogAudit'));
			}

			function load(){
				//alert(window.languageCallback);
				getLanguage(display);
			}
			window.onload = load;
		</script>
		<script src="js/jquery.cookie.js" type="text/javascript"></script>
			<script src="js/log_manage.js" type="text/javascript"></script>
    </head>

    <body>
        <div class="totalInfo">
            <div class="Info">
                <div class="toheader"><label id="label_LogManagementHeader"></label></div>
                <div class="tobody">
                    <table>
                        <thead>
                            <tr class="msgArea">                                
                                <td colspan="5">
                                    <div style="float:left;">
                                        <span><label id="label_LogManagementBatchOperate1"></label></span>
                                        <input type="checkbox" class="selectAll" />
                                        <span><label id="label_LogManagementSelectAll1"></label></span>
                                        <input type="checkbox" class="selectInvert" />
                                        <span><label id="label_LogManagementSelectOther1"></label></span>
                                    </div>
                                    <div style="float:right;">
                                        <input type="button" value="<%=strAudit%>" class="toAudit" />
                                    </div>
                                </td>                            
                            </tr>
                            <tr>
                                <th   width="10%">id</th><th  width="20%"><label id="label_LogManagementLogTime"></label></th>
								<th width="15%"><label id="label_LogManagementLogOperator"></label></th>
								<th  width="43%"><label id="label_LogManagementLogDetail"></label></th>
								<th  width="12%"><label id="label_LogManagementLogAudit"></label></th>
                            </tr>
                        </thead>
                        <tfoot>                            
                            <tr class="msgArea">
                                <td colspan="5">
                                    <div style="float:left;">
                                        <span><label id="label_LogManagementBatchOperate2"></label></span>
                                        <input type="checkbox" class="selectAll" />
                                        <span><label id="label_LogManagementSelectAll2"></label></span>
                                        <input type="checkbox" class="selectInvert" />
                                        <span><label id="label_LogManagementSelectOther2"></label></span>
                                    </div>
                                    <div style="float:right;">
                                        <input type="button" value="<%=strAudit%>" class="toAudit" />
                                    </div>
                                </td>
                            </tr>
                        </tfoot>
                        <tbody id="logList">
                            <jsp:include page="/log/log_list.jsp"></jsp:include>
                        </tbody>
                    </table>                    
                </div>
            </div>
        </div>
    </body>
</html>
