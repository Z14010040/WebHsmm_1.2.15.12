<%-- any content can be specified here e.g.: --%>
<%@ page import="debug.log.LogUtil"%>
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
		$('#label_GuideInstallWizard').html($.i18n.prop('GuideInstallWizard'));
		$('#label_GuideInstallDeviceInit').html($.i18n.prop('GuideInstallDeviceInit'));
		$('#label_GuideInstallAddManager').html($.i18n.prop('GuideInstallAddManager'));
		$('#label_GuideInstallAddOperator').html($.i18n.prop('GuideInstallAddOperator'));
		$('#label_GuideInstallKeyManagement').html($.i18n.prop('GuideInstallKeyManagement'));
		//$('#label_GuideInstallNetworkConfiguration').html($.i18n.prop('GuideInstallNetworkConfiguration'));
		//$('#label_GuideInstallServiceConfiguration').html($.i18n.prop('GuideInstallServiceConfiguration'));
		$('#label_GuideInstallBackupKey').html($.i18n.prop('GuideInstallBackupKey'));
		$('#label_GuideInstallFinish').html($.i18n.prop('GuideInstallFinish'));
	}
</script>
<ul>
    <li style="width:80px"><label id="label_GuideInstallWizard"></label>:</li>
    <% if (step == ii++) { %>
    <li><a href="/key/initialize.jsp?isGuide=true" target="panel" style="width:75px;color:#007DC9"><b><label id="label_GuideInstallDeviceInit"></label></b></a></li>
    <% } else { %>
    <li><a href="/key/initialize.jsp?isGuide=true" target="panel" style="width:75px"><label id="label_GuideInstallDeviceInit"></label></a></li>
    <% } %>
    <li style="width:10px">&gt;</li>
    <% if (step == ii++) { %>
    <li><a href="/user/managerAdd.jsp?isGuide=true" target="panel" style="width:60px;color:#007DC9"><b><label id="label_GuideInstallAddManager"></label></b></a></li>
    <% } else { %>
    <li><a href="/user/managerAdd.jsp?isGuide=true" target="panel" style="width:60px;"><label id="label_GuideInstallAddManager"></label></a></li>
    <% } %>
    <li style="width:10px">&gt;</li>
    <% if (step == ii++) { %>
    <li><a href="/user/operatorAdd.jsp?isGuide=true" target="panel" style="width:60px;color:#007DC9""><b><label id="label_GuideInstallAddOperator"></label></b></a></li>
    <% } else { %>
    <li><a href="/user/operatorAdd.jsp?isGuide=true" target="panel" style="width:60px;"><label id="label_GuideInstallAddOperator"></label></a></li>
    <% } %>
   <li style="width:10px">&gt;</li>
    <% if (step == ii++) { %>
    <li><a href="/key/backup/tips.jsp?isGuide=true" target="panel" style="width:60px;color:#007DC9"><b><label id="label_GuideInstallBackupKey"></label></b></a></li>
    <% } else { %>
    <li><a href="/key/backup/tips.jsp?isGuide=true" target="panel" style="width:60px;"><label id="label_GuideInstallBackupKey"></label></a></li>
    <% } %>
    <li style="width:10px">&gt;</li>
    <% if (step == ii++) { %>
    <li><a href="/finish.jsp" target="panel" style="width:75px;color:#007DC9"><b><label id="label_GuideInstallFinish"></label></b></a></li>
    <% } else { %>
    <li><label id="label_GuideInstallFinish"></label></li>
    <% } %>
</ul>
