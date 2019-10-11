<%@page import="com.sansec.hsm.bean.Config"%>
<%@page import="debug.log.LogUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.sansec.hsm.bean.keymgr.SymmetricKeyStatus"%>
<%@ page import="com.sansec.hsm.bean.Language"%>
<%@page import="java.util.*"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	boolean isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
	//language
	String strGenerateTips = Language.get("KeyGenerateKeyTips");
	String strDeleteKey = Language.get("ButtonDelete");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>查看密钥状态</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script language="JavaScript" type="text/javascript">
            function submit1() {
                var index = document.getElementById("index").value;
                if(index == ""){
                    alert($.i18n.prop('InputCheckNull'));
                    return false;
                }else{
                    return true;
                }
                
                document.getElementById("submitbtn").style.display = "none";
                document.getElementById("hidemsg").style.display = "block";
                document.getElementById("title").style.display = "none";
            }

            function deleteKey(index) {
                if (confirm($.i18n.prop('ConfirmDelete') + " " + index + " "  +$.i18n.prop('JSConfirmNumber') + $.i18n.prop('JSKEK') +"?"))
                    location.href = "/handle/maintainKey.jsp?action=delete_kek&index="+index+"&isGuide=<%=isGuide%>";
            }

        </script>

		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_KEKManagement').html($.i18n.prop('KEKManagement'));
				$('#label_KEKManagementGenerate').html($.i18n.prop('KEKManagementGenerate'));
				$('#label_KEKManagementGenerateTips').html($.i18n.prop('KEKManagementGenerateTips'));
				$('#label_KEKManagementBits').html($.i18n.prop('KEKManagementBits'));
				$('#label_KEKManagementStatus').html($.i18n.prop('KEKManagementStatus'));
				$('#label_KEKManagementStatus').html($.i18n.prop('KEKManagementStatus'));
				$('#label_KEKManagementNoKey').html($.i18n.prop('KeyStatusTableEmpty'));
				$('#label_KeyStatusTableIndex').html($.i18n.prop('KeyStatusTableIndex'));
				$('#label_KeyStatusTableBits').html($.i18n.prop('KeyStatusTableBits'));
				$('#label_KeyStatusTableDelete').html($.i18n.prop('KeyStatusTableDeleteKey'));
				$('#label_Next').html($.i18n.prop('GuideNext'));
				$('#label_Previous').html($.i18n.prop('GuidePrevious'));
				document.getElementById("button_generateKey").value=$.i18n.prop('KeyGenerateKey');
			}

			function load(){
				getLanguage(display);
				getLanguage(showGuide);
				getLanguage(showKeyStatus);
			}
			window.onload = load;
		</script>
    </head>

    <body>
        <%
			List<SymmetricKeyStatus> list = (List<SymmetricKeyStatus>) request.getAttribute("status");
        %>
        <div class="totalInfo">
            <div class="guideInfo">
                <%
					if (isGuide) {
                %>
                <jsp:include page="/guide/guide.jsp">
                    <jsp:param name="step" value="4" />
                </jsp:include>
                <%        } else {
                %>
                <% if (Config.isECCSupport()) {%>
                <jsp:include page="/guide/key.jsp">
                    <jsp:param name="step" value="3" />
                </jsp:include>
                <% } else {%> 
                <jsp:include page="/guide/key.jsp">
                    <jsp:param name="step" value="2" />
                </jsp:include>
                <% }%> 
                <%            }
                %>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_KEKManagement"></label></div>
                <div class="tobody">
                    <div class="inheader"><label id="label_KEKManagementGenerate"></label></div>
                    <div class="inbody">
                        <form method="post" action="/handle/maintainKey.jsp?action=generate_kek&isGuide=<%=isGuide%>">
                            <table>
                                <tr>
                                    <td width="50%">
                                        <label id="label_KEKManagementGenerateTips"></label>
                                    </td>
                                    <td  width="50%" style="display:none;">
                                        <input type="text" name="index" id="index" value="1"/>
                                    </td>
                                </tr>
                                <tr style="display:none;">
                                    <td ><label id="label_KEKManagementBits"></label>(bits)</td>
                                    <td>
                                        <select name="length">
                                            <option value='128'>128</option>
                                            <option value='192'>192</option>
                                            <option value='256'>256</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="text-align: center">
                                        <input type="submit" name="submit" id="button_generateKey" style="display: inline" onclick="return submit1()" value="产生密钥"/>
                                        <div id="hidemsg" style="display:none">
                                            <font color="#0000FF"><%=strGenerateTips%></font>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="blank"></div>

                    <div id="title">${ result }</div>
                    <%
						if (isGuide) {
                    %>
                    
                    <div class="tobottom">
                        <ul>
                            <!-- modify for visahsm 17/11/09 -->  
                          <li><a href="/handle/maintainKeyBackup.jsp?action=showTips&isGuide=true" target="panel" style="width:60px"><label id="label_Next"></label></a></li>
                          <!-- 
                          <%
								if (Config.isECCSupport()) {
                            %>                            
                            <li><a href="/handle/maintainKey.jsp?action=ECCManagement&isGuide=true" target="panel" style="width:60px"><label id="label_Previous"></label></a></li>
                            <%                            } else {
                            %>                            
                            <li><a href="/handle/maintainKey.jsp?action=RSAManagement&isGuide=true" target="panel" style="width:60px"><label id="label_Previous"></label></a></li>
                            <%                                }
                            %>   
                          -->   
                        </ul>
                    </div>
                    <%                    }
                    %>
                    <div class="inheader"><label id="label_KEKManagementStatus"></label></div>
                    <div class="inbody">
                        <%
							if (list == null || list.size() == 0) {
                        %>
                        <table>
                            <tr><td> <label id="label_KEKManagementNoKey"></label> </td></tr>
                        </table>
                        <%                        } else {
                        %>
                        <table style="text-align: center">
                            <tr>
                                <td width="30%"><label id="label_KeyStatusTableIndex"></label></td>
                                <td width="40%"><label id="label_KeyStatusTableBits"></label></td>
                                <td width="30%"><label id="label_KeyStatusTableDelete"></label></td>
                            </tr>

                            <%
								for (int i = 0; i < list.size(); i++) {
									SymmetricKeyStatus key = list.get(i);
                            %>
                            <tr>
                                <td><%= key.getIndex()%></td>
                                <td><%= key.getLength()%></td>
                                <td><input type="button" value=<%=strDeleteKey%> onclick="deleteKey(<%= key.getIndex()%>)"></td>
                            </tr>
                            <%
									}
								}
                            %>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
