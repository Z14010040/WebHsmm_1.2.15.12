<%@ page import="com.sansec.hsm.bean.keymgr.AsymmetricKeyStatus"%>
<%@ page import="com.sansec.hsm.bean.Language"%>
<%@ page import="java.util.*"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%

	String algrithm = request.getParameter("algrithm");
	String isGuide = request.getParameter("isGuide");
	List<AsymmetricKeyStatus> status = (ArrayList<AsymmetricKeyStatus>) request.getAttribute(algrithm + "Status");
	//language
	String strKeyUsageSign = Language.get("KeyUsageSign");
	String strKeyUsageEncrypt = Language.get("KeyUsageEncrypt");
	String strKeyDelete = Language.get("KeyStatusTableDelete");
	String strChangePrivateAccess = Language.get("KeyStatusTableChangePrivateAccess");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<script language="JavaScript" type="text/javascript">
    function deleteKey(algrithm, index, isGuide) {
        //alert(algrithm+" : "+index+" : "+isGuide);
        if ((Number(index) % 2) == 0) {
            if (confirm($.i18n.prop('ConfirmDelete') + " "  + ((Number(index) / 2) + 1) + " "  +$.i18n.prop('JSConfirmNumber') + " "  + algrithm + " "  + $.i18n.prop('JSSignKeyPair') +"?"))
                location.href = "/handle/maintainKey.jsp?action=delete_"+algrithm+"&index="+index+"&isGuide="+isGuide;                     
        } else {
            //index默认成了字符串，用Number做一下转换
            if (confirm($.i18n.prop('ConfirmDelete') + " "  + ((Number(index) + 1) / 2) + " "  +$.i18n.prop('JSConfirmNumber') + " "  + algrithm + " "  + $.i18n.prop('JSEncryptKeyPair') +"?"))
                location.href = "/handle/maintainKey.jsp?action=delete_"+algrithm+"&index="+index+"&isGuide="+isGuide;         
        }
    }
</script>
<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
<script type="text/javascript" src="js/language.js"></script>
<script type="text/javascript">
	function showKeyStatus(){
		$('#label_KeyStatusTableHeader').html($.i18n.prop('KeyStatusTableHeader'));
		$('#label_KeyStatusTableEmpty').html($.i18n.prop('KeyStatusTableEmpty'));
		$('#label_KeyStatusTableIndex').html($.i18n.prop('KeyStatusTableIndex'));
		$('#label_KeyStatusTableUsage').html($.i18n.prop('KeyUsage'));
		$('#label_KeyStatusTableBits').html($.i18n.prop('KeyStatusTableBits'));
		$('#label_KeyStatusTableDelete').html($.i18n.prop('KeyStatusTableDeleteKey'));
		$('#label_KeyStatusTablePrivateAccess').html($.i18n.prop('KeyStatusTablePrivateAccess'));
	}
</script>

<div class="inheader"><%= algrithm%>&nbsp;<label id="label_KeyStatusTableHeader"></label></div>
<div class="inbody">
    <%
		if (status.size() == 0) {
    %>
    <table>
        <tr><td><label id="label_KeyStatusTableEmpty"></label>  </td></tr>
    </table>
    <%} else {
    %>
    <table style="text-align: center">
        <tr>
            <td width="15%"><label id="label_KeyStatusTableIndex"></label></td>
            <td width="20%"><label id="label_KeyStatusTableUsage"></label></td>
            <td width="20%"><label id="label_KeyStatusTableBits"></label></td>
            <td width="20%"><label id="label_KeyStatusTableDelete"></label></td>
            <td width="25%"><label id="label_KeyStatusTablePrivateAccess"></label></td>
        </tr>
        <%
			for (int i = 0; i < status.size(); i++) {
        %>
        <tr>
            <td rowspan="2"><%=status.get(i).getIndex()%></td>
            <td><%=strKeyUsageSign%></td>
            <%
				int key = status.get(i).getSignKeyLen();
				if (key > 0) {
            %>
            <td><%=key%></td>
            <td>
                <input type="button" value =<%=strKeyDelete%> onclick="deleteKey('<%= algrithm%>', '<%=((status.get(i).getIndex() - 1) * 2)%>', '<%= isGuide%>')">
            </td>

            <%
			} else {
            %>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <%    }
            %>
            <td rowspan="2">
                <a href="/handle/maintainKey.jsp?action=<%= algrithm%>SetPrivateKeyAccessPassword&isGuide=<%= isGuide%>&keyIndex=<%=status.get(i).getIndex()%>">
					<%=strChangePrivateAccess%></a>
            </td>
        </tr>
        <tr>
            <td><%=strKeyUsageEncrypt%></td>
            <%
				key = status.get(i).getEncKeyLen();
				if (key > 0) {
            %>
            <td><%=key%></td>
            <td>
                <input type="button" value =<%=strKeyDelete%> onclick="deleteKey('<%= algrithm%>', '<%=((status.get(i).getIndex() - 1) * 2 + 1)%>', '<%= isGuide%>')">
            </td>

            <%
			} else {
            %>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <%    }
            %> 
        </tr>

        <%
				}
			}
        %>
    </table>
</div>

