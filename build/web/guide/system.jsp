<%-- any content can be specified here e.g.: --%>
<%@ page pageEncoding="UTF-8" %>
<%
    int ii = 1;
    int step = -1;
    try {
        step = Integer.parseInt(request.getParameter("step"));
    } catch(Exception ex) {
        //ex.printStackTrace();
        //LogUtil.println("guide.jsp -> step: "+step);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<script type="text/javascript">
	function showGuide(){
		$('#label_GuideSystemManagement').html($.i18n.prop('GuideSystemManagement'));
//		$('#label_GuideSystemShowDeviceInfo').html($.i18n.prop('GuideSystemShowDeviceInfo'));
//		$('#label_GuideSystemMaintenanceInfo').html($.i18n.prop('GuideSystemMaintenanceInfo'));
//		$('#label_GuideSystemNetworkConfiguration').html($.i18n.prop('GuideSystemNetworkConfiguration'));
		$('#label_GuideSystemModifyLoginPassword').html($.i18n.prop('GuideSystemModifyLoginPassword'));
	}
</script>
<ul>
    <li style="width:80px"><label id="label_GuideSystemManagement"></label>:</li>
    <% if (step == ii++) {%>
    <li><a href="/handle/maintainConfig.jsp?action=showDeviceInfo&isGuide=false" target="panel" style="width:90px;color:#007DC9"><b><label id="label_GuideSystemShowDeviceInfo"></label></b></a></li>
    <% } else {%>
    <li><a href="/handle/maintainConfig.jsp?action=showDeviceInfo&isGuide=false" target="panel" style="width:90px"><label id="label_GuideSystemShowDeviceInfo"></label></a></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) {%>
    <li><a href="/handle/maintainConfig.jsp?action=showMaintainInfo&isGuide=false" target="panel" style="width:90px;color:#007DC9"><b><label id="label_GuideSystemMaintenanceInfo"></label></b></a></li>
    <% } else {%>
    <li><a href="/handle/maintainConfig.jsp?action=showMaintainInfo&isGuide=false" target="panel" style="width:90px"><label id="label_GuideSystemMaintenanceInfo"></label></a></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) {%>
    <li><a href="/handle/maintainConfig.jsp?action=showNetworkInfo&isGuide=false" target="panel" style="width:90px;color:#007DC9"><b><label id="label_GuideSystemNetworkConfiguration"></label></b></a></li>
    <% } else {%>
    <li><a href="/handle/maintainConfig.jsp?action=showNetworkInfo&isGuide=false" target="panel" style="width:90px"><label id="label_GuideSystemNetworkConfiguration"></label></a></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) {%>
    <li><a href="/handle/maintainLogin.jsp?action=modifyPasswd" target="panel" style="width:90px;color:#007DC9"><b><label id="label_GuideSystemModifyLoginPassword"></label></b></a></li>
    <% } else {%>
    <li><a href="/config/modifyPasswd.jsp" target="panel" style="width:90px"><label id="label_GuideSystemModifyLoginPassword"></label></a></li>
    <% }%>
</ul>
