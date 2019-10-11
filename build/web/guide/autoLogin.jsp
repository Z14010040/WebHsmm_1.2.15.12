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
		$('#label_AutoLogin').html($.i18n.prop('GuideAutoLogin'));
		$('#label_AutoLoginStart').html($.i18n.prop('GuideAutoLoginSetting'));
		$('#label_AutoLoginFinish').html($.i18n.prop('GuideAutoLoginFinish'));
	}
</script>

<ul>
    <li style="width:80px"><label id="label_AutoLogin"></label>:</li>
    <% if (step == 1) {%>
    <li style="width:130px;color:#007DC9"><b><label id="label_AutoLoginStart"></label></b></li>
    <% } else {%>
    <li style="width:130px"><label id="label_AutoLoginStart"></label></li>
    <% }%>
    <li style="width:20px">&gt;</li>
    <% if (step == 2) {%>
    <li style="width:60px;color:#007DC9"><b><label id="label_AutoLoginFinish"></label></b></li>
    <% } else {%>  
    <li style="width:60px"><label id="label_AutoLoginFinish"></label></li>
    <% }%>
</ul>
