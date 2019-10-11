<%-- 
    Document   : 
    Created on : 2011-9-22, 10:51:27
    Author     : root
--%>
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

    /* 添加管理员 */
    boolean add_manager(HttpServletRequest req, boolean isGuide) {
    
        HttpSession session = req.getSession();
        String username = (String)session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
	    OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        int rv = 0;
        String time = req.getParameter("time");
        System.out.println("maintainUser.jsp:come in....");
        System.out.println(time);
        String enable = req.getParameter("enable");
        System.out.println(enable);
        String pin = req.getParameter("pin");
        try {
            Manager mgr = new Manager(pin);
            //第一次访问用来判断key有没有重复添加过！
            if(!("2".equals(time)) ){
                System.out.println("maintainUser.jsp: first time！");
                rv = mgr.rigisterInformation();
//                System.out.println("maintainUser.jsp: 第49行:rv"+rv);
                time = "2";
                if(rv==1){
                   enable = "true";
                }else{
                   enable = "false";
                }
                req.setAttribute("time", time);
                req.setAttribute("enable", enable);
                req.setAttribute("pin", pin);
                System.out.println("maintainUser.jsp:第59行");
                url = "/user/managerAdd.jsp?isGuide="+isGuide;
                return true;
            }
            req.setAttribute("time", "1");
            req.setAttribute("enable", "false");
            // 添加前判断管理员数目是否达到最大
            if(mgr.isMaxCount()) {
                result = "The current number of administrators has reached the maximum, and you cannot continue to add new administrators.";
                req.setAttribute("result", result);
                if( isGuide ) {
                    url = "/user/managerAdd.jsp?isGuide=true";
                    return true;
                } else {
                    return show_management(req);
                }
            }

            // 管理员添加操作
            
            mgr.add();

            result = mgr.getResult();
            req.setAttribute("result", result);
            // 添加后判断管理员数目是否达到最大
            if( mgr.isMaxCount() ) {
                if( isGuide ) {
                    url = "/user/operatorAdd.jsp";
                    req.setAttribute("isGuide", "true");
                    System.out.println("添加管理员完成，接下来添加操作员");
                    return true;
                } else {
                    return show_management(req);
                }
            } else {
                if( isGuide ) {
                    url = "/user/managerAdd.jsp?isGuide=true";

                    return true;
                } else {
                    return show_management(req);
                }
            }
        } catch (NoPrivilegeException ex) {
            //result = urlEncode(ex.getMessage());
            req.setAttribute("result", ex.getMessage());
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

            return true;
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }

    /* 添加操作员 */
    boolean add_operator(HttpServletRequest req, boolean isGuide) {
        /* 这里是为了处理页面跳转 */
        String isguide = req.getParameter("isGuide");
        System.out.println("isguide="+isguide);
        if("true".equals(isguide)){
            isGuide = true;
            //便于operatorAdd.jsp中的input标签对value进行赋值
            req.setAttribute("isGuide", "true");
        }
        
        HttpSession session = req.getSession();
        String username = (String)session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
	    OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        int rv = 0;
        String time = req.getParameter("time");
        System.out.println("maintainUser.jsp:come in....");
        System.out.println(time);
        String enable = req.getParameter("enable");
        System.out.println(enable);
        String pin = req.getParameter("pin");
        try {
           Operator opr = new Operator(pin);
            //第一次访问用来判断key有没有重复添加过！
            if(!("2".equals(time)) ){
                System.out.println("maintainUser.jsp: first time！");
                rv = opr.rigisterInformation();
                System.out.println("maintainUser.jsp: 第135行:rv="+rv);
                time = "2";
                if(rv==1){
                   enable = "true";
                }else{
                   enable = "false";
                }
                req.setAttribute("time", time);
                req.setAttribute("enable", enable);
                req.setAttribute("pin", pin);
                System.out.println("maintainUser.jsp:第145行");
                url = "/user/operatorAdd.jsp?isGuide="+isGuide;
                return true;
            }
            req.setAttribute("time", "1");
            req.setAttribute("enable", "false");
            req.setAttribute("isGuide", "false");
            
            // 操作员添加操作
            opr.add();
            opr.login();
            result = opr.getResult();
            req.setAttribute("result", result);
            System.out.println(result);
            if( isGuide ) {
                url = "/key/backup/tips.jsp?isGuide=false";
                
                return true;
            } else {
                
                return show_management(req);
            }
        } catch (NoPrivilegeException ex) {
            //result = urlEncode(ex.getMessage());
            req.setAttribute("result", ex.getMessage());
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

            return true;
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }

    /* 删除用户 */
    boolean delete_user(HttpServletRequest req, HSMUser user) {
         HttpSession session = req.getSession();
        String username = (String)session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
	    OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        try {
            user.delete();

            req.setAttribute("result", user.getResult());

            return show_management(req);
        } catch (NoPrivilegeException ex) {
            //result = urlEncode(ex.getMessage());
            req.setAttribute("result", ex.getMessage());
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

            return true;
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }

    /* 删除管理员 */
    boolean delete_manager(HttpServletRequest req) {
         
        int id = Integer.parseInt(req.getParameter("id"));
        Manager mgr = new Manager(id);

        return delete_user(req, mgr);
    }

     /* 删除操作员 */
    boolean delete_operator(HttpServletRequest req) {
        Manager mgr = new Manager();
        try {
            if (mgr.getAllLoginManager().size() < (Config.getMaxManangerCount()/2 + 1)) {
                result = "Permissions are not met";
                url = "/error.jsp?error="+result;

                return false;
            }
        
            Operator opt = new Operator();

            return delete_user(req, opt);
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }

    

    /* 显示权限 */
    boolean show_privilege(HttpServletRequest req) {
        try {
            Privilege rights = new Privilege();
            if( !rights.check(Privilege.PRIVILEGE_SHOW_PRIVILEGE_TABLE) ) {
                throw new NoPrivilegeException("No permission to display permission table!");
            }

            req.setAttribute("showDeviceInfo",      rights.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_DEVICE_INFO));
            req.setAttribute("showRunStatus",       rights.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_RUN_STATUS));
            req.setAttribute("showMaintainInfo",    rights.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_MAINTAIN_INFO));
            req.setAttribute("modifyMaintainInfo",  rights.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_MAINTAIN_INFO));
            req.setAttribute("startService",        rights.getPrivilegeInfo(Privilege.PRIVILEGE_START_SERVICE));
            req.setAttribute("stopService",         rights.getPrivilegeInfo(Privilege.PRIVILEGE_STOP_SERVICE));
            req.setAttribute("modifyServiceConfig", rights.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_NETWORK_CONFIG));
            req.setAttribute("resetNetwork",        rights.getPrivilegeInfo(Privilege.PRIVILEGE_RESTART_NETWORK));
            req.setAttribute("modifyNetworkConfig", rights.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG));
            req.setAttribute("changeSystemPwd",     rights.getPrivilegeInfo(Privilege.PRIVILEGE_CHANGE_SYSTEM_PWD));
            
            req.setAttribute("showLoginStatus",     rights.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_LOGIN_STATE));
            req.setAttribute("shwoPrivilegeTable",  rights.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_PRIVILEGE_TABLE));
            req.setAttribute("initManager",         rights.getPrivilegeInfo(Privilege.PRIVILEGE_INIT_MANAGER));
            req.setAttribute("addManager",          rights.getPrivilegeInfo(Privilege.PRIVILEGE_ADD_MANAGER));
            req.setAttribute("delManager",          rights.getPrivilegeInfo(Privilege.PRIVILEGE_DELETE_MANAGER));
            req.setAttribute("addOperator",         rights.getPrivilegeInfo(Privilege.PRIVILEGE_ADD_OPERATOR));
            req.setAttribute("delOperator",         rights.getPrivilegeInfo(Privilege.PRIVILEGE_DELETE_OPERATOR));

            req.setAttribute("showKeyStatus",       rights.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_KEY_INFO));
            req.setAttribute("generateKey",         rights.getPrivilegeInfo(Privilege.PRIVILEGE_GEN_KEY_PAIR));
            req.setAttribute("delKey",              rights.getPrivilegeInfo(Privilege.PRIVILEGE_DELETE_KEY_PAIR));
            req.setAttribute("importKey",           rights.getPrivilegeInfo(Privilege.PRIVILEGE_IMPORT_KEY_PAIR));
            req.setAttribute("setAccessPin",        rights.getPrivilegeInfo(Privilege.PRIVILEGE_SET_SKAP));
            req.setAttribute("keyBackup",           rights.getPrivilegeInfo(Privilege.PRIVILEGE_BACKUP));
            req.setAttribute("keyRestore",          rights.getPrivilegeInfo(Privilege.PRIVILEGE_RESTORE));
            
            req.setAttribute("showlog",          rights.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_LOG));
            req.setAttribute("auditlog",          rights.getPrivilegeInfo(Privilege.PRIVILEGE_AUDIT_LOG));

            url = "/user/privilegeTable.jsp";

            return true;
        } catch (NoPrivilegeException ex) {
            //result = urlEncode(ex.getMessage());
            req.setAttribute("result", ex.getMessage());
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

            return true;
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }

    /* 显示用户登录 */
    boolean show_login(HttpServletRequest req) {
    
        HttpSession session = req.getSession();
        String username = (String)session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
	    OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        
        String privilegeInfo = "";
        int managerCount = 0;
        int operatorCount = 0;
        String onlineManagerInfo = "";
        String operatorStatus = "";
        try {
            Privilege rights = new Privilege();
            privilegeInfo = rights.getPrivilegeInfo();
            Operator opr = new Operator();
            List<Integer> listOpr = opr.getAllExistsOperator();
            operatorCount = listOpr.size();
            Manager mgr = new Manager();
            List<Integer> list = mgr.getAllExistsManager();
            managerCount = list.size();
            list = mgr.getAllLoginManager();
            for(int i=0; i<list.size(); i++) {
                onlineManagerInfo += Language.get("UserManagerNoPrefix")+list.get(i)+Language.get("UserManagerNo")+";";
            }
            if( onlineManagerInfo.contains(";") ) {
                onlineManagerInfo = onlineManagerInfo.substring(0, onlineManagerInfo.lastIndexOf(";"));
            } else {
                onlineManagerInfo = Language.get("UserStatusLogout");
            }
            Operator opt = new Operator();
            if( opt.isExists() ) {
                if( opt.isLogin() ) {
                    operatorStatus = Language.get("UserStatusLogin");
                } else {
                    operatorStatus = Language.get("UserStatusLogout");
                }
            } else {
                operatorStatus = Language.get("UserStatusNotAdded");
            }

            boolean bInited = rights.isInited();


            // 设置返回
            req.setAttribute("privilegeInfo", privilegeInfo);
            req.setAttribute("managerCount", managerCount);
            req.setAttribute("operatorCount", operatorCount);
            req.setAttribute("onlineManagerInfo", onlineManagerInfo);
            req.setAttribute("operatorStatus", operatorStatus);
            if( !bInited ) {
                req.setAttribute("result", Language.get("DeviceStatusInitialize"));
            } 
//            else{
//                req.setAttribute("result", "");
//            }

            url = "/user/userLogin.jsp";

            return true;
        } catch(Exception ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }

    /* 处理用户登录 */
    boolean user_login(HttpServletRequest req) {
        try {
            String pin = req.getParameter("pin");
            Manager mgr = new Manager(pin);
            mgr.login();

            int no = mgr.getId();
            if(no == 0) {
                req.setAttribute("result", Language.get("UserOperatorLogin"));
            } else {
//                req.setAttribute("result", Language.get("UserManagerLoginPrefix") + no+Language.get("UserManagerLogin"));
                req.setAttribute("result",Language.get("UserManagerLoginSuccess"));
            }
            return show_login(req);
        } catch(Exception ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;
            return false;
        }
    }

    /* 显示用户管理 */
    boolean show_management(HttpServletRequest req) {
        try {
            Manager mgr = new Manager();
            List<Integer> list = mgr.getAllExistsManager();
            int maxCountManager = mgr.getMaxCount();
            if( maxCountManager <list.size())
            {
                boolean updateManager = mgr.setMaxCount(list.size());
                if(updateManager)
                                  {
                    maxCountManager = mgr.getMaxCount();
                }
                else
                {
                    throw new Exception("The current number of administrators exceeds the device configuration, please perform initialization!");
                }                                         
            }
            
            int[] arr = new int[maxCountManager];
            for(int i=0; i<list.size(); i++) {
                arr[list.get(i)-1] = 1;
            }

            list = new ArrayList<Integer>();
            for(int i=0; i<arr.length; i++) {
                list.add(arr[i]);
            }

            LogUtil.println("maintainUser.jsp -> show_manage() -> list: "+list);

            // 设置返回
            req.setAttribute("allManager", list);
            url = "/user/userManagement.jsp";

            return true;
        } catch(Exception ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }


    /* 所有管理员登出 */
    boolean all_manager_logout(HttpServletRequest req) {
        Manager mgr = new Manager(0);
        return logout(req, mgr);
    }

    /* 所有操作员登出 */
    boolean all_operator_logout(HttpServletRequest req) {
        Operator opt = new Operator(1);

        return logout(req, opt);
    }

    /* 用户登出 */
    boolean logout(HttpServletRequest req, HSMUser user) {
        try {
            user.logout();

            req.setAttribute("result", user.getResult());

            return show_login(req);
        } catch(Exception ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }


    /* 修改用户PIN */
    boolean change_user_pin(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String username = (String)session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
	    OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
    
        HSMUser user = new HSMUser();
        String oldPin = req.getParameter("oldPin");
        String newPin = req.getParameter("newPin");
        if( newPin == null || !newPin.equals(req.getParameter("newPin2"))) {
            req.setAttribute("result", "Input error!");
        }
        try {
            user.changePin(oldPin, newPin);
//            修改配置文件中的pin
            Manager mgr = new Manager(newPin);
            mgr.login();
            int no = mgr.getId();
            if(no==0){
                mgr.modifyIniFile(0, "pin",newPin);
            } 
            req.setAttribute("result", user.getResult());
            url = "/user/changePin.jsp";

            return true;
        } catch (NoPrivilegeException ex) {
            //result = urlEncode(ex.getMessage());
            req.setAttribute("result", ex.getMessage());
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

            return true;
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

            return false;
        }
    }
      /* 修改loginPIN */
    boolean change_login_pin(HttpServletRequest req) {
    
        HttpSession session = req.getSession();
        String username = (String)session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
	    OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        
        HSMUser user = new HSMUser();
        String oldPin = req.getParameter("oldPin");
        String newPin = req.getParameter("newPin");
        if( newPin == null || !newPin.equals(req.getParameter("newPin2"))) {
            req.setAttribute("result", "Input error!");
        }
        try {
            user.changeloginPin(oldPin, newPin);

            req.setAttribute("result", user.getResult());
            url = "/user/changeloginPin.jsp";

            return true;
       
        } catch(DeviceException ex) {
            result = urlEncode(ex.getMessage());
            url = "/error.jsp?error="+result;

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
    if("addManager".equals(action)) {
        // 添加管理员
        forTransport = add_manager(request, isGuide);
    } else if("addOperator".equals(action)) {
        // 添加操作员
        forTransport = add_operator(request, isGuide);
    } else if("showPrivilegeTable".equals(action)) {
        // 显示权限
        forTransport = show_privilege(request);
    } else if("showUserLogin".equals(action)) {
        // 显示登录页面
        forTransport = show_login(request);
    } else if("showUserManagement".equals(action)) {
        // 显示管理页面
        forTransport = show_management(request);
    } else if("login".equals(action)) {
        // 管理员/操作员登录
        forTransport = user_login(request);
    } else if("logoutAllManager".equals(action)) {
        // 所有管理员登出
        forTransport = all_manager_logout(request);
    } else if("logoutAllOperator".equals(action)) {
        // 所有操作员登出
        forTransport = all_operator_logout(request);
    } else if("delManager".equals(action)) {
        // 删除管理员
        forTransport = delete_manager(request);
    } else if("delOperator".equals(action)) {
        // 删除操作员
        forTransport = delete_operator(request);
    } else if("changePin".equals(action)) {
        // 修改PIN
        forTransport = change_user_pin(request);
    } else if("changeloginPin".equals(action)) {
        // 修改PIN
        forTransport = change_login_pin(request);
    } else {
        forTransport = false;
        result = urlEncode("action Parameter error");
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