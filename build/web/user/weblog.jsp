<%@page import="com.sansec.hsm.util.OperationLogUtil"%>
<%@ include file="/common.jsp" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>北京三未信安密码设备</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
    </head>
    <body >
        <%
                String weblog = OperationLogUtil.readlog();
                String logs[]=weblog.split("\r\n");
        %> 
        <div class="totalInfo">
            <div class="guideInfo">
                <ul>
                <li style="width:80px">查看日志:</li> 
                <li><a href="/user/weblog.jsp" target="panel" style="width:60px;color:#007DC9"><b>查看日志</b></a></li>   
                <li style="width:500px">日志记录了密码机用户登录、密钥操作、网络配置等行为</li>
                </ul>
            </div>
        <div class="blank"></div>
        <div class="Info">
                <div class="toheader">管理日志</div>
                <div class="tobody">
                    
        <table  width="100%" style="table-layout:fixed;text-align: center" >
        <tr>
            <td width="20%" bgcolor="#eeeeee">操作时间</td>
            <td width="10%" bgcolor="#eeeeee">方式</td>
            <td width="70%" bgcolor="#eeeeee">管理内容</td>
        </tr>
        
        
       
                     <%
                     for(int i= 0;i<logs.length;i++){
                        if(logs[i].length() <=1 ) break;
                        String Time = logs[i].substring(logs[i].indexOf("{") + 1,logs[i].indexOf("}"));
                        String Type = logs[i].substring(logs[i].indexOf("{",logs[i].indexOf("}")) + 1,logs[i].indexOf("}",logs[i].indexOf("}")+2));
                        String Msg = "　" +logs[i].substring(logs[i].lastIndexOf("}")+1);
                        
                     %>
                     <tr>
                         <td width="20%" style=“word-break:break-all;”><%=Time%></td>
                         <td width="10%"><%=Type%></td>
                         <td width="70%" style="word-break:keep-all;text-align: left"><%=Msg%></td>
                     </tr>
                     
                      
                    <%
                                        }
                     %> 
         </table>
                    </div>
            </div>
          </div>
    </body>
</html>
