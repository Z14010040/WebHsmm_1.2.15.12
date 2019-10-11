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
		$('#label_GuideBackupGuide').html($.i18n.prop('GuideBackupGuide'));
		$('#label_GuideBackupStart').html($.i18n.prop('GuideBackupStart'));
		$('#label_GuideBackupExportKeyComponent').html($.i18n.prop('GuideBackupExportKeyComponent'));
		$('#label_GuideBackupExportBackupFile').html($.i18n.prop('GuideBackupExportBackupFile'));
		$('#label_GuideBackupFinish').html($.i18n.prop('GuideBackupFinish'));
	}
</script>

<ul>
    <li style="width:80px"><label id="label_GuideBackupGuide"></label>:</li>
    <% if (step == 1) {%>
    <li style="width:60px;color:#007DC9"><b><label id="label_GuideBackupStart"></label></b></li>
    <% } else {%>
    <li style="width:60px"><label id="label_GuideBackupStart"></label></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == 2) {%>
    <li style="width:90px;color:#007DC9"><b><label id="label_GuideBackupExportKeyComponent"></label></b></li>
    <% } else {%>  
    <li style="width:90px"><label id="label_GuideBackupExportKeyComponent"></label></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == 3) {%>
    <li style="width:90px;color:#007DC9"><b><label id="label_GuideBackupExportBackupFile"></label></b></li>
    <% } else {%>
    <li style="width:90px"><label id="label_GuideBackupExportBackupFile"></label></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == 4) {%>
    <li style="width:40px;color:#007DC9"><b><label id="label_GuideBackupFinish"></label></b></li>
    <% } else {%>
    <li style="width:40px"><label id="label_GuideBackupFinish"></label></li>
    <% }%>
    
</ul>
