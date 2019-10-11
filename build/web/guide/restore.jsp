<%-- any content can be specified here e.g.: --%>
<%@ page pageEncoding="UTF-8" %>
<%
    int ii = 1;
    int step = -1;
    try {
        step = Integer.parseInt(request.getParameter("step"));
    } catch (Exception ex) {
        //ex.printStackTrace();
        //LogUtil.println("guide.jsp -> step: "+step);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<script type="text/javascript">
    function showGuide() {
        $('#label_GuideRestoreKey').html($.i18n.prop('GuideRestoreKey'));
        $('#label_GuideRestoreKeyPrepare').html($.i18n.prop('GuideRestoreKeyPrepare'));
        $('#label_GuideRestoreKeyUploadBackupFile').html($.i18n.prop('GuideRestoreKeyUploadBackupFile'));
        $('#label_GuideRestoreKeyImportKeyComponent').html($.i18n.prop('GuideRestoreKeyImportKeyComponent'));
        $('#label_GuideRestoreKeyCompleted').html($.i18n.prop('GuideRestoreKeyCompleted'));
    }
</script>
<ul>
    <li style="width:80px"><label id="label_GuideRestoreKey"></label>:</li>
    <% if (step == ii++) {%>
    <li style="width:60px;color:#007DC9"><b><label id="label_GuideRestoreKeyPrepare"></label></b></li>
    <% } else {%>
    <li style="width:60px"><label id="label_GuideRestoreKeyPrepare"></label></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) {%>
    <li style="width:90px;color:#007DC9"><b><label id="label_GuideRestoreKeyUploadBackupFile"></label></b></li>
    <% } else {%>
    <li style="width:90px"><label id="label_GuideRestoreKeyUploadBackupFile"></label></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) {%>
    <li style="width:90px;color:#007DC9"><b><label id="label_GuideRestoreKeyImportKeyComponent"></label></b></li>
    <% } else {%>
    <li style="width:90px"><label id="label_GuideRestoreKeyImportKeyComponent"></label></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) {%>
    <li style="width:50px;color:#007DC9"><b><label id="label_GuideRestoreKeyCompleted"></label></b></li>
    <% } else {%>
    <li style="width:50px"><label id="label_GuideRestoreKeyCompleted"></label></li>
    <% }%>
</ul>
