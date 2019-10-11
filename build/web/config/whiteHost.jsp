<%@page import="com.sansec.hsm.bean.Language"%>
<%@include file="/common.jsp" %>
<%@page import="com.sansec.hsm.bean.config.WhiteHostInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	//language
	String strWhiteHostDelete=Language.get("ButtonDelete");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>对客户机授权</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script type="text/javascript">
            function submit1(){
                var scount=0;
                var flag = true;
                var ip = document.getElementById("newIP");
                var iplength = ip.value.length;
                var Letters = "1234567890.";
                for (i=0; i < iplength; i++){
                    var CheckChar = ip.value.charAt(i);
                    if (Letters.indexOf(CheckChar) == -1) {
                        alert ($.i18n.prop('IPCheckError'));
                        ip.value="";
                        ip.focus();
                        return false;
                    }
                }

                for (var i = 0;i<iplength;i++){
                    (ip.value.substr(i,1)==".")?scount++:scount;
                }

                if(scount!=3) {
                    alert ($.i18n.prop('IPCheckError'));
                    ip.value="";
                    ip.focus();
                    return false;
                }

                first = ip.value.indexOf(".");
                last = ip.value.lastIndexOf(".");
                str1 = ip.value.substring(0,first);
                subip = ip.value.substring(0,last);
                sublength = subip.length;
                second = subip.lastIndexOf(".");
                str2 = subip.substring(first+1,second);
                str3 = subip.substring(second+1,sublength);
                str4 = ip.value.substring(last+1,iplength);

                if (str1=="" || str2=="" ||str3== "" ||str4 == ""){
                    alert($.i18n.prop('IPCheckError'));
                    ip.value="";
                    ip.focus();
                    return false;
                }
                if (str1< 0 || str1 >255) {
                    alert ($.i18n.prop('IPCheckNumberRange'));
                    ip.value="";
                    ip.focus();
                    return false;
                } else if (str2< 0 || str2 >255) {
                    alert ($.i18n.prop('IPCheckNumberRange'));
                    ip.value="";
                    ip.focus();
                    return false;
                } else if (str3< 0 || str3 >255) {
                    alert ($.i18n.prop('IPCheckNumberRange'));
                    ip.value="";
                    ip.focus();
                    return false;
                } else if (str4< 0 || str4 >255) {
                    alert ($.i18n.prop('IPCheckNumberRange'));
                    ip.value="";
                    ip.focus();
                    return false;
                }
                
                document.forms[0].submit();
            }
            
            function submit2(){
                var flag = true;
                var index=document.getElementById("ipIndex").value;
                var len=document.getElementById("mount").value;
                var type = "^[0-9]*[1-9][0-9]*$";
                var re = new RegExp(type);
                if(index.match(re)==null) {
                    alert( "请输入大于零的整数!");
                    flag = false;
                }
                if(index > len){
                    alert("索引超过了显示的数量");
                    flag = false;
                }
                if(flag) {
                    document.forms[1].submit();
                }
            }
        </script>
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_WhiteHostManagement').html($.i18n.prop('WhiteHostManagement'));
				$('#label_WhiteHostTips1').html($.i18n.prop('WhiteHostTips1'));
				$('#label_WhiteHostTip2').html($.i18n.prop('WhiteHostTip2'));
				$('#label_WhiteHostAdd').html($.i18n.prop('WhiteHostAdd'));
				$('#label_WhiteHostAddInfo').html($.i18n.prop('WhiteHostAddInfo'));
				$('#label_WhiteHostStatus').html($.i18n.prop('WhiteHostStatus'));
				$('#label_WhiteHostListEmpty').html($.i18n.prop('WhiteHostListEmpty'));
				document.getElementById("button_add").value=$.i18n.prop('ButtonAdd');
				$('#label_Next').html($.i18n.prop('GuideNext'));
				$('#label_Previous').html($.i18n.prop('GuidePrevious'));
			}

			function load(){
				//alert(window.languageCallback);
				getLanguage(display);
				getLanguage(showGuide);
			}
			window.onload = load;
		</script>
    </head>

    <body>
        <%
            boolean isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
        %>
        <div class="totalInfo">
            <div class="guideInfo">
        <%
            if( isGuide ) {
        %>
                <jsp:include page="/guide/guide.jsp">
                    <jsp:param name="step" value="6" />
                </jsp:include>
        <%
            } else {
        %>
                <jsp:include page="/guide/service.jsp">
                    <jsp:param name="step" value="3" />
                </jsp:include>
        <%
            }
        %>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_WhiteHostManagement"></label></div>
                <div class="tobody">
                    <div>
                        <label id="label_WhiteHostTips1"></label><br>
                        <font color="blue"><label id="label_WhiteHostTip2"></label></font>
                    </div>
                    <div class="blank"></div>
                    <div class="inheader"><label id="label_WhiteHostAdd"></label></div>
                    <div class="inbody">
                        <div style="border: #CCCCCC 1px solid; margin-bottom: 10px; margin-top: 10px">
                            <form method="post" action="/handle/maintainConfig.jsp?action=addWhiteHostInfo&isGuide=<%= isGuide %>">
                                <label id="label_WhiteHostAddInfo"></label>：<input type="text" name="newIP" id="newIP"value=""/>
                                <input type="button" id="button_add" name="ip" value="添加" onclick="submit1()"/>
                            </form>  
                        </div>
                    </div>
                    <div id="title">${ result }</div>

                    <div class="blank"></div>
                              
                    <div class="inheader"><label id="label_WhiteHostStatus"></label></div>
                    <div class="inbody">
                        <%
                            List<WhiteHostInfo> list = (List<WhiteHostInfo>)request.getAttribute("whitehosts");
                            //LogUtil.println("jsp -> list: "+list);
                            if(list.size() == 0) {
                        %>
                        <div style="border: #CCCCCC 1px solid; margin-bottom: 10px; margin-top: 10px">
                            <label id="label_WhiteHostListEmpty"></label>!
                        </div>
                        <%
                            } else {
                        %>
                        <table>
                            <%
                                for (int i = 0; i < list.size(); i++) {
                                    WhiteHostInfo host = list.get(i);
                            %>
                            <tr>
                                <td width="50%" align="ceneter"><%= host.getIp() %></td>
                                <td width="50%" align="ceneter"><a method="post" href="/handle/maintainConfig.jsp?action=delWhiteHostInfo&isGuide=<%= isGuide %>&name=<%= host.getName() %>"><%=strWhiteHostDelete%></a></td>
                            </tr>
                            <%
                                }
                            %>
                        </table>
                        <%
                            }
                        %>
                        <!--<input type="hidden" name="mount" id="mount" value=""/>-->
                    </div>                    
                </div>
                <%
                    if( isGuide ) {
                %>
                <div class="tobottom">
                    <ul>
                        <li><a href="/handle/maintainKeyBackup.jsp?action=showTips&isGuide=true" target="panel" style="width:60px"><label id="label_Next"></label></a></li>
                        <li><a href="/handle/maintainConfig.jsp?action=showServiceInfo&isGuide=true" target="panel" style="width:60px"><label id="label_Previous"></label></a></li>
                    </ul>
                </div>   
                <%
                    }
                %>
            </div>
        </div>
    </body>
</html>
