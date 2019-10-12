<%-- 
    Document   : 
    Created on : 2011-9-22, 10:39:00
    Author     : root
--%>
<%@page import="com.sansec.hsm.bean.login.UserList" %>
<%@page import="com.sansec.hsm.util.IPUtil" %>
<%@page import="com.sansec.hsm.util.OperationLogUtil" %>
<%@page import="com.sansec.hsm.exception.DeviceException" %>
<%@page import="com.sansec.hsm.exception.NoPrivilegeException" %>
<%@page import="com.sansec.hsm.bean.usermgr.Manager" %>
<%@page import="com.sansec.hsm.bean.login.User" %>
<%@page import="com.sansec.hsm.bean.login.UserTrace" %>
<%@ page import="com.sansec.hsm.lib.kmapi" %>
<%@ page import="debug.log.LogUtil" %>
<%@ page import="com.sansec.hsm.bean.HSMError" %>
<%--
<jsp:useBean id="usertrace" class="com.sansec.hsm.bean.login.UserTrace" scope="session" />
--%>
<%! UserList userlist = UserList.getInstance();
    UserTrace usertrace = new UserTrace();
%>

<%!
    String url = "";
    String result = "";

    boolean login(HttpServletRequest req) {
        String username = req.getParameter("user");
        String password = req.getParameter("pwd");
        String userIp = IPUtil.parseIpAddr(req);
        OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        User user = new User(username, password);
        boolean bVerify = user.login();
        if (bVerify) {
            HttpSession session = req.getSession();
            session.setAttribute("user", username);

            //????id???????
            usertrace.setUsername(userIp);

            //?????????session?
            session.setAttribute("usertrace", usertrace);

            //??????????
            userlist.addUser(usertrace.getUsername());

            //??session??
            session.setMaxInactiveInterval(1800);

            url = "/mainFrame.jsp";

////            加载共享内存
//            int rv = kmapi.INSTANCE.KM_LoadKeys();
//            // load key
//            LogUtil.println("Conifig.java->initialize(): load keys ...");
//            if (rv != HSMError.SDR_OK) {
//                LogUtil.println("Conifig.java->initialize(): load keys error!");
//            }

            return true;
        } else {
            req.setAttribute("error", user.getResult());
            url = "/";
            return true;
        }

    }

    boolean logout(HttpServletRequest req) {

        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("user");
        String password = req.getParameter("pwd");
        String userIp = IPUtil.parseIpAddr(req);
        OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        User user = new User(username, password);
        boolean bVerify = user.logout();

        session.removeAttribute("user");
        session.removeAttribute("usertrace");

        url = "/";
        return false;
    }

    boolean modify_passwd(HttpServletRequest req) {
        String oldPasswd = req.getParameter("oldPwd");
        String newPasswd = req.getParameter("newPwd");

        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("user");
        String userIp = IPUtil.parseIpAddr(req);
        OperationLogUtil.setUserIP(userIp);
        OperationLogUtil.setUser(username);
        try {
            User user = new User();
            user.modifyPassword(oldPasswd, newPasswd);

            req.setAttribute("result", user.getResult());
            url = "/config/modifyPasswd.jsp";

            return true;

        } catch (NoPrivilegeException ex) {
            //result = urlEncode(ex.getMessage());
            req.setAttribute("result", ex.getMessage());
            url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

            return true;
        } catch (DeviceException ex) {
            req.setAttribute("result", ex.getMessage());
            url = "/error.jsp?error=" + result;

            return false;
        }
    }

%>


<%
    String action = request.getParameter("action");
    boolean forTransport = false;
    if ("login".equals(action)) {
        forTransport = login(request);
    } else if ("logout".equals(action)) {
        forTransport = logout(request);
    } else if ("modifyPasswd".equals(action)) {
        forTransport = modify_passwd(request);
    } else {
        url = "/login.jsp";
        forTransport = false;
    }
%>

<%
    if (forTransport) {
%>
<jsp:forward page="<%= url %>"/>
<%
    } else {
        response.sendRedirect(url);
    }
%>
