<%@page import="com.sansec.hsm.bean.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="getLanguage" class="com.sansec.hsm.bean.Config" scope="page" />

<%
String currentLanguage = Config.getLanguage();

response.setContentType("text/xml; charset=UTF-8"); 
response.setHeader("Cache-Control", "no-cache");
out.println("<response>");
out.println("<command>" + "GETLANGUAGE" + "</command>");
out.println("<result>" + currentLanguage + "</result>");
out.println("</response>");

%>
