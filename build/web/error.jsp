<%@ include file="/common.jsp" %>
<%@ page contentType="text/html; charset=utf-8" language="java" isErrorPage="true"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><%=name%>-错误</title>
<style type="text/css">
<!--
body {
	border: 1px;
	border-color:  #CCCCCC;
}
.bian {
	border-top-width: 1px;
	border-top-style: solid;
	border-top-color: #969696;
	border-bottom-width: 1px;
	border-bottom-style: solid;
	border-bottom-color: #999999;
}
.f12 {
	font-family: "宋体";
	font-size: 12px;
	color: #000000;
	text-decoration: none;
	line-height: 23px;
}
.bian1 {
	border-right-width: 1px;
	border-left-width: 1px;
	border-right-style: solid;
	border-left-style: solid;
	border-right-color: #969696;
	border-left-color: #969696;
}
a:link {
	font-family: "宋体";
	font-size: 12px;
	color: #333333;
	text-decoration: none;
}
a:visited {
	font-family: "宋体";
	font-size: 12px;
	color: #333333;
	text-decoration: none;
}
a:hover {
	font-family: "宋体";
	font-size: 12px;
	color: #333333;
	text-decoration: underline;
}
a:active {
	font-family: "宋体";
	font-size: 12px;
	color: #333333;
	text-decoration: none;
}
-->
</style>
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_Return').html($.i18n.prop('GuideReturn'));
			}

			function load(){
				//alert(window.languageCallback);
				getLanguage(display);
			}
			window.onload = load;
		</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="60%" height="70%" border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="#111111" class="f12" style="border-collapse: collapse">
  <tr> 
    <td width="100%" height="210"> <table class="f12" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="187">
        <tr> 
          <td width="100%" class="tablebody1" height="16" align="center"> <p align="center"><img src="images/error.gif" width="122" height="40" border="0">
            <p align="center">
              <br>
              
<%
    request.setCharacterEncoding("utf-8");
	Object result = request.getParameter("error");
	/*String result1 = new String(((String)result).getBytes("ISO-8859-1"), "gb2312");
	out.print(result1+"<br>");
    result1 = new String(((String)result).getBytes("ISO-8859-1"), "utf-8");
	out.print(result1+"<br>");
    result1 = new String(((String)result).getBytes("utf-8"), "gb2312");
	out.print(result1+"<br>");
    result1 = (String)result;*/
	out.print("<div style='color: red; text-align: center' >"+result+"</div>");
    String url = request.getParameter("url");
%>
             
              <br>
              <a href="<%= (url==null) ? "javascript:history.back(1)" : url%>"><b><label id="label_Return"></label></b></a></td>
        </tr>
      </table></td>
  </tr>
</table>
</body>
</html>
