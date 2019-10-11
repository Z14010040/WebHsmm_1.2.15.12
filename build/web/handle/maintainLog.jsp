<%@page import="debug.log.LogUtil.Log"%>
<%@include file="/common.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

 <%!
    final Log logger = LogUtil.getLog("maintainLog.jsp");
    String url = "";
    String result = "";
    
    boolean manageLog(HttpServletRequest req) {
    	url = "/log/log_manage.jsp";
        
        return true;
    }
    
    boolean serverLog(HttpServletRequest req) {
    	url = "/log/log_server.jsp";
        //..
        return true;
    }
    
    int neededPrivilegeLog = -1;
    boolean checkPrivilegeLog(HttpServletRequest req) {
        loggerCommon.debug("checkPrivilege->begin");
        boolean checkResult = false;
        try {
            checkResult = isHasPrivilege(neededPrivilegeLog, req);
        }   catch (NoPrivilegeException e) {            
             LogUtil.println("权限不足：",e);
            String err = urlEncode(e.getMessage());
            req.setAttribute("result", e.getMessage());
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false&errmsg="
                    + err;
        } catch (DeviceException e) {
            String err = urlEncode(e.getMessage());
            LogUtil.println("",e);
            
            String url = "/error.jsp?error="+err;            
        }
        return checkResult;
     }
%>


<%
    // 获取执行动作参数
    String action = "";
    try {
        action = request.getParameter("action");
    } catch(Exception ex) {
    }

    LogUtil.println("maintainLog.jsp -> action: "+action);

    // 执行
    boolean forTransport = false;   // 重定向标志
   
    if("manageLog".equals(action)) {
        neededPrivilegeLog = Privilege.PRIVILEGE_SHOW_LOG;
        if (checkPrivilegeLog(request)) {
            forTransport = manageLog(request);
        }
         else 
         {forTransport = true;
         }
    } else if("serverLog".equals(action)) {
        neededPrivilegeLog = Privilege.PRIVILEGE_SHOW_LOG;
        if (checkPrivilegeLog(request)) {
           forTransport = serverLog(request); 
        }
         else 
         {forTransport = true;
         }
    }
   
    
    LogUtil.println("maintainConfig.jsp -> forTransport: "+forTransport);
    LogUtil.println("maintainConfig.jsp -> url: "+url);
%>

<%
    // 跳转
    if( forTransport ) {
        //ServletUtil.genLog(request, "ok,"+result);
%>
<jsp:forward page="<%= url %>" />
<%
    } else {
        //ServletUtil.genLog(request, "error,"+result);
        response.sendRedirect(url);
    }
%>
