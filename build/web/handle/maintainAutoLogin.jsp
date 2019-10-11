<%-- 
    Document   : 
    Created on : 2011-9-22, 10:51:27
    Author     : root
--%>
<%@page import="com.sansec.hsm.bean.Backup"%>
<%@page import="com.sansec.hsm.bean.Config"%>
<%@page import="com.sansec.hsm.util.IPUtil"%>
<%@page import="com.sansec.hsm.util.OperationLogUtil"%>
<%@page import="com.sansec.hsm.bean.Language"%>
<%@include file="/common.jsp" %>
<%@page import="com.sansec.hsm.bean.usermgr.HSMUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.sansec.hsm.bean.Privilege"%>
<%@page import="com.sansec.hsm.bean.HSMError"%>
<%@page import="com.sansec.hsm.lib.kmapi"%>
<%@page import="com.sansec.hsm.bean.usermgr.Operator"%>
<%@page import="com.sansec.hsm.bean.usermgr.Manager"%>
<%@page import="com.sansec.hsm.bean.usermgr.HSMUser"%>
<%@page import="com.sansec.hsm.exception.NoPrivilegeException" %>
<%@page import="com.sansec.hsm.exception.DeviceException" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!
    String url = "";
    String result = "";

    /* 处理用户登录 */
    void autoLogin(HttpServletRequest req,Boolean isGuide) {
        try {
//            Manager mgr = new Manager(pin);
//            mgr.login();

//            int no = mgr.getId();
//            if(no == 0) {
//                req.setAttribute("result", Language.get("UserOperatorLogin"));
//            } else {
////                req.setAttribute("result", Language.get("UserManagerLoginPrefix") + no+Language.get("UserManagerLogin"));
//                req.setAttribute("result",Language.get("UserManagerLoginSuccess"));
//            }

            url = "/user/autoLogin.jsp";
        } catch(Exception ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;
        }
    }
    /* 显示导出分量页面 */
    Boolean check(HttpServletRequest req, Boolean part) {
        
    
        try {
            Operator opt = new Operator();
            if(!opt.isExists()){
                System.out.println("!opt.isExists()");
                result =urlEncode("No operator.Please add new operator!");
                req.setAttribute("result", result);
                LogUtil.println("failed result: "+result);
                url = "/error.jsp?url=/user/autoLogin.jsp&error="+result;
                return false;
            }
            if(!opt.isLogin()){
                System.out.println("!opt.isLogin()");
                req.setAttribute("result", "No operator login.");
                LogUtil.println("NoPrivilegeException: "+"No operator login.");
                url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";
                return false;
            }
            String pin = req.getParameter("pin");
            Manager mgr = new Manager(pin);
            mgr.login();
            
            int no = mgr.getId();
            if(no==0){
                mgr.modifyIniFile(0, "enable", req.getParameter("enable"));
                LogUtil.println("Set auto login sucess!");
                url = "/user/autoLoginFinish.jsp?enable="+req.getParameter("enable");
            } else{
                url = "/user/autoLogin.jsp?result="+"Key type error ! Please ensure the key type is operator !";
                return false;
            }
            return true;
        } catch(DeviceException ex) {
            url = "/user/autoLogin.jsp?result="+"The pin code is not correct or the operator key is not inserted into the machine !";
            return false;
        } 
    }
%>


<%
    /* 获取执行动作参数 */
    String action = "";
    boolean isGuide = false;
    try {
        action = request.getParameter("action");
        isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
    } catch(Exception ex) {
        result = "Parameter error: "+ex;
        result = urlEncode(result);
        response.sendRedirect("/error.jsp?error="+result);
    }

    LogUtil.println("maintainUser.jsp -> action: "+action);
    LogUtil.println("maintainUser.jsp -> isGuide: "+isGuide);

    /* 执行 */
    boolean forTransport = false;   // 重定向标志
    if("autoLogin".equals(action)) {
        // 自动登陆
        autoLogin(request, isGuide);
    }else if("check".equals(action)){
        check(request, isGuide);
    }

    LogUtil.println("maintainUser.jsp -> forTransport: "+forTransport);
    LogUtil.println("maintainUser.jsp -> url: "+url);
%>

<%
    // 跳转
    if( forTransport ) {
%>
<jsp:forward page="<%= url %>" />
<%
    } else {
        response.sendRedirect(url);
    }
%>