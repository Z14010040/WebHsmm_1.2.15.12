<%-- 
    Document   : 
    Created on : 2011-9-22, 10:51:27
    Author     : root
--%>
<%@page import="com.sansec.hsm.util.IPUtil"%>
<%@page import="com.sansec.hsm.util.OperationLogUtil"%>
<%@page import="com.sansec.hsm.bean.Restore"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="com.sansec.hsm.bean.Config"%>
<%@page import="com.sansec.hsm.bean.Backup"%>
<%@include file="/common.jsp" %>
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
<%@page contentType="text/html" import="com.jspsmart.upload.*" pageEncoding="UTF-8"%>

<%!
    String url = "";
    String result = "";

    boolean show_upload_file(HttpServletRequest req) {
       /*
               try {
     
          Restore.getInstance().checkPrivilege();
          
        } catch (NoPrivilegeException e) {
            req.setAttribute("result", e.getMessage());
            LogUtil.println("NoPrivilegeException: "+e);
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

            return true;
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?url=/key/restore/tips.jsp&error="+result;

            return false;
        } 
    */
        url = "/key/restore/uploadFile.jsp";
        return true;
    }

    boolean upload_file(HttpServletRequest req, PageContext ctx) {
        SmartUpload su = new SmartUpload();
        try {
            //
	        su.initialize(ctx);
	        su.upload();
	        su.getFiles().getFile(0).saveAs("/var/swhsm/upload/swhsmbak.dat");

            //Restore
            Restore.getInstance().checkBackupFile();
        }catch(Exception ex) {
            result = urlEncode(ex.getMessage());
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?url=/key/restore/tips.jsp&error="+result;
            return false;
        }

        return show_import_part(req, 1);
    }
    
    boolean check_backup_file(HttpServletRequest req) {
        Restore restore = Restore.getInstance();
         try {
            Restore.getInstance().checkBackupFile();
        } catch (IOException ex) {
            result = urlEncode(ex.getMessage());
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?error="+result;
            return false;
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?error="+result;
            return false;
        } 
        
        return show_import_part(req, 1);
    }

    /* 显示导出分量页面 */
    boolean show_import_part(HttpServletRequest req, int part) {
        url = "/key/restore/importPart.jsp";

        req.setAttribute("part", part);

        return true;
    }
    
    /* 导出分量 */
    boolean import_part(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String username = (String)session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
	    OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        System.out.println("begin jsp..");
        int part = Integer.parseInt(req.getParameter("part"));
        String pin = req.getParameter("pin");
        Restore restore = Restore.getInstance();

        try {    
            restore.importPart(part, pin);
            //req.setAttribute("result", "分量["+part+"]成功");
            if( (Config.getMaxManangerCount() == 3 && part == 2) || (Config.getMaxManangerCount()==5 && part == 3) ) {
                restore.run();
//                Manager mgr = new Manager(pin);
//                mgr.login();
                return show_finish(req);
            } else {
                return show_import_part(req, part+1);
            }
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
//            System.out.println(result);
            //确保恢复的时候，插入的是备份文件的usbKey
            if(result.contains("Manager+login+error")){
                result = "The key is not match with the backup file!!";
            }
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?url=/key/restore/tips.jsp&error="+result;
            return false;
        } catch(IOException ex) {
            System.out.println("maintainKeyRestore.jsp");
            result = urlEncode(ex.getMessage());
            LogUtil.println("failed result: "+ex);
            url = "/error.jsp?error="+result;

            return false;
        }
    }

    boolean show_finish(HttpServletRequest req) {
        Restore restore = Restore.getInstance();
        restore.finish();
        
        url = "/key/restore/finish.jsp";
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
    if("showUploadFile".equals(action)) {
        forTransport = show_upload_file(request);
    } else if("uploadFile".equals(action)) {
        forTransport = upload_file(request, pageContext);
    } else if("showImportPart".equals(action)) {
        // 显示导出密钥分量
        forTransport = show_import_part(request, 1);
    } else if("importPart".equals(action)) {
        // 导出密钥分量
        forTransport = import_part(request);
    } else if("showFinish".equals(action)) {
        // 完成备份
        forTransport = show_finish(request);
    } else if("checkBackupFile".equals(action)) {
        // 完成备份
        forTransport = check_backup_file(request);
    }else {
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