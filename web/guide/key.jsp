<%-- any content can be specified here e.g.: --%>
<%@page import="com.sansec.hsm.bean.Config"%>
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
		$('#label_GuideKeyManagement').html($.i18n.prop('GuideKeyManagement'));
		$('#label_GuideRSAKeyManagement').html($.i18n.prop('GuideRSAKeyManagement'));
		$('#label_GuideECCKeyManagement').html($.i18n.prop('GuideECCKeyManagement'));
		$('#label_GuideSymmetricKeyManagement').html($.i18n.prop('GuideSymmetricKeyManagement'));
		$('#label_GuideDeleteKey').html($.i18n.prop('GuideDeleteKey'));
	}
</script>
<ul>
    <li style="width:80px"><label id="label_GuideKeyManagement"></label>:</li>
    <% if (step == ii++) { %>
    <li><a href="/handle/maintainKey.jsp?action=RSAManagement&isGuide=false" target="panel" style="width:90px;color:#007DC9"><b><label id="label_GuideRSAKeyManagement"></label></b></a></li>
    <% } else { %>  
    <li><a href="/handle/maintainKey.jsp?action=RSAManagement&isGuide=false" target="panel" style="width:90px"><label id="label_GuideRSAKeyManagement"></label></a></li>
    <% } %>
    <% if (Config.isECCSupport()) { %>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) { %>
    <li><a href="/handle/maintainKey.jsp?action=ECCManagement&isGuide=false" target="panel" style="width:90px;color:#007DC9"><b><label id="label_GuideECCKeyManagement"></label></b></a></li>
    <% } else { %>  
    <li><a href="/handle/maintainKey.jsp?action=ECCManagement&isGuide=false" target="panel" style="width:90px"><label id="label_GuideECCKeyManagement"></label></a></li>
    <% } %>
    <% } %>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) { %>
    <li><a href="/handle/maintainKey.jsp?action=kekManagement&isGuide=false" target="panel" style="width:90px;color:#007DC9"><b><label id="label_GuideSymmetricKeyManagement"></label></b></a></li>
    <% } else { %>  
    <li><a href="/handle/maintainKey.jsp?action=kekManagement&isGuide=false" target="panel" style="width:90px;"><label id="label_GuideSymmetricKeyManagement"></label></a></li>
    <% } %>
    <li style="width:20px">&gt;</li>
    <% if (step == ii++) { %>
    <li><a href="/key/initialize.jsp?isGuide=false" target="panel" style="width:60px;color:#007DC9"><b><label id="label_GuideDeleteKey"></label></b></a></li>
    <% } else { %>  
    <li><a href="/key/initialize.jsp?isGuide=false" target="panel" style="width:60px;"><label id="label_GuideDeleteKey"></label></a></li>
    <% } %>
</ul>
