<%@page import="com.sansec.hsm.bean.Config" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<jsp:useBean id="setLanguage" class="com.sansec.hsm.bean.Config" scope="page"/>

<%
    String newLanguage = request.getParameter("LANGUAGE");
    System.out.println("newLanguage : " + newLanguage);
    Boolean rv = Config.setLanguage(newLanguage);
    String result = rv ? "ok" : "fail";

    response.setContentType("text/xml; charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");
    out.println("<response>");
    out.println("<command>" + "SETLANGUAGE" + "</command>");
    out.println("<result>" + result + "</result>");
    out.println("</response>");

%>
