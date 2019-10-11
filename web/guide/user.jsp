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
		$('#label_GuidePrivilegesManagement').html($.i18n.prop('GuidePrivilegesManagement'));
		$('#label_GuidePrivilegesUserLogin').html($.i18n.prop('GuidePrivilegesUserLogin'));
		$('#label_GuidePrivilegesUserManagement').html($.i18n.prop('GuidePrivilegesUserManagement'));
		$('#label_GuidePrivilegesModifyICPIN').html($.i18n.prop('GuidePrivilegesModifyICPIN'));
		$('#label_GuidePrivilegesShowTable').html($.i18n.prop('GuidePrivilegesShowTable'));
	}
</script>
<ul>
    <li style="width:80px"><label id="label_GuidePrivilegesManagement"></label>:</li>
    <% if (step == 1) {%>
    <li><a href="/handle/maintainUser.jsp?action=showUserLogin&isGuide=false" target="panel" style="width:60px;color:#007DC9"><b><label id="label_GuidePrivilegesUserLogin"></label></b></a></li>
    <% } else {%>
    <li><a href="/handle/maintainUser.jsp?action=showUserLogin&isGuide=false" target="panel" style="width:60px"><label id="label_GuidePrivilegesUserLogin"></label></a></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == 2) {%>
    <li><a href="/handle/maintainUser.jsp?action=showUserManagement&isGuide=false" target="panel" style="width:60px;color:#007DC9"><b><label id="label_GuidePrivilegesUserManagement"></label></b></a></li>
    <% } else {%>
    <li><a href="/handle/maintainUser.jsp?action=showUserManagement&isGuide=false" target="panel" style="width:60px;"><label id="label_GuidePrivilegesUserManagement"></label></a></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == 3) {%>
    <li><a href="/user/changePin.jsp" target="panel" style="width:100px;color:#007DC9"><b><label id="label_GuidePrivilegesModifyICPIN"></label></b></a></li>
    <% } else {%>
    <li><a href="/user/changePin.jsp" target="panel" style="width:100px"><label id="label_GuidePrivilegesModifyICPIN"></label></a></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == 4) {%>
    <li><a href="/user/changeloginPin.jsp" target="panel" style="width:120px;color:#007DC9"><b><label id="changeloginUserPIN">Web Password</label></b></a></li>
    <% } else {%>
    <li><a href="/user/changeloginPin.jsp" target="panel" style="width:100px"><label id="changeloginUserPIN">Web Password</label></a></li>
    <% }%>
    
    <!--
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) {%>
    <li><a href="/handle/maintainUser.jsp?action=showPrivilegeTable&isGuide=false" target="panel" style="width:75px;color:#007DC9"><b><label id="label_GuidePrivilegesShowTable"></label></b></a></li>
    <% } else {%>
    <li><a href="/handle/maintainUser.jsp?action=showPrivilegeTable&isGuide=false" target="panel" style="width:75px"><label id="label_GuidePrivilegesShowTable"></label></a></li>
    <% }%>
    -->
</ul>

