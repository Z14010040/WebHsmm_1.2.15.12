<%-- 
    Document   : 
    Created on : 2011-9-22, 10:51:27
    Author     : root
--%>
<%@page import="java.io.InputStream"%>
<%@page import="com.sansec.util.SignatureTools"%>
<%@page import="java.io.ObjectInputStream"%>
<%@page import="com.sansec.hsm.util.IPUtil"%>
<%@page import="com.sansec.hsm.util.OperationLogUtil"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="com.sansec.hsm.bean.Config"%>
<%@page import="com.sansec.hsm.bean.Backup"%>
<%@page import="com.sansec.hsm.bean.usermgr.HSMUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.sansec.hsm.bean.Privilege"%>
<%@page import="com.sansec.hsm.bean.HSMError"%>
<%@page import="com.sansec.hsm.lib.kmapi"%>
<%@page import="com.sansec.hsm.bean.usermgr.Operator"%>
<%@page import="com.sansec.hsm.bean.usermgr.Manager"%>
<%@page import="com.sansec.hsm.exception.NoPrivilegeException" %>
<%@page import="com.sansec.hsm.exception.DeviceException" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/common.jsp" %>

<%!
    String url = "";
    String result = "";

    /* 显示导出分量页面 */
    boolean show_tips(HttpServletRequest req) {

        url = "/key/backup/tips.jsp";

        return true;
    }

    /* 显示导出分量页面 */
    boolean show_export_part(HttpServletRequest req, int part) {
        
    
        try {
            Backup.checkPrivilege();
        } catch (NoPrivilegeException e) {
            req.setAttribute("result", e.getMessage());
            LogUtil.println("NoPrivilegeException: "+e);
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

            return true;
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?url=/key/backup/tips.jsp&error="+result;

            return false;
        }
        req.setAttribute("part", part);
        url = "/key/backup/exportPart.jsp";
        return true;
    }
    
    /* 导出分量 */
    boolean export_part(HttpServletRequest req) {
    
        HttpSession session = req.getSession();
        String username = (String)session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
	    OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        
        int part = Integer.parseInt(req.getParameter("part"));
        String pin = req.getParameter("pin");
        Backup bak = Backup.getInstnce();
        try {
            Manager mgr = new Manager(pin);
            mgr.login();
            bak.exportPart(part, pin);
            req.setAttribute("result", "Export component ["+part+"] successfully");
            if( part == Config.getMaxManangerCount() ) {
                bak.generateFile();
                url = "/key/backup/exportFile.jsp";
                return true;
            } else {
                return show_export_part(req, part+1);
            }
        } catch(DeviceException ex) {
            ex.printStackTrace();
            result = urlEncode(ex.getMessage());
            System.out.println("测试："+result);
            //确保备份的时候，插入的是该机器的usbKey
            if(result.contains("Manager+login+error")){
                result = "The key is not match with the machine!!";
            }
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?url=/key/backup/tips.jsp&error="+result;
            return false;
        } catch(IOException ex) {
            result = urlEncode(ex.getMessage());
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?url=/key/backup/tips.jsp&error="+result;
            return false;
        }
    }

    boolean show_finish(HttpServletRequest req) {
//        try {
//            Backup.getInstnce().deleteFile();
//        } catch (Exception ex) {
//        }

        url = "/key/backup/finish.jsp";
        return true;
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
    if("showTips".equals(action)) {
        // 开始备份密钥
        forTransport = show_tips(request);
    } else if("showExportPart".equals(action)) {
        // 显示导出密钥分量
        forTransport = show_export_part(request, 1);
    } else if("exportPart".equals(action)) {
        // 导出密钥分量
        forTransport = export_part(request);
    } else if("showFinish".equals(action)) {
        // 完成备份
        forTransport = show_finish(request);
    } else {
        forTransport = false;
        result = urlEncode("action parameter error");
        url = "/error.jsp?error="+result;
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