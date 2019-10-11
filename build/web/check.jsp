<%-- 
    Document   : checkLogin
    Created on : 2011-9-14, 11:51:08
    Author     : root
--%>

<%@page import="com.sansec.hsm.bean.Language"%>
<%@page import="debug.log.LogUtil"%>
<%@page import="com.sansec.hsm.bean.login.User"%>
<%@page pageEncoding="UTF-8"%>

<%
    Object loginUser = session.getAttribute("user");
    //LogUtil.println((String)user);
    LogUtil.println("CheckLogin->user: "+loginUser);
    if( !(loginUser instanceof String) ) {
        out.println("<html>");
        out.println("<head>");
        out.println("<script type='text/javascript'>");
        out.println("    setTimeout('location.href=\"/\"',1000);");
        //out.println("");
        out.println("</script>");
        out.println("</head>");
        out.println("<body>");
        out.print("<div style='color: red; text-align: center' >"+Language.get("LoginTimeout") +"</div>");
        //response.sendRedirect("/");
        return;
        //out.println("</body>");
    }
%>