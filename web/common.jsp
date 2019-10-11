<%@page import="debug.log.LogUtil.Log"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="com.sansec.hsm.util.ServletUtil"%>
<%@page import="com.sansec.hsm.exception.DeviceException"%>
<%@page import="com.sansec.hsm.exception.NoPrivilegeException"%>
<%@page import="com.sansec.hsm.bean.Privilege"%>
<%@include file="/check.jsp" %>
<%@page import="debug.log.LogUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page pageEncoding="UTF-8"%>

<%!
    String name="北京三未信安科技发展有限公司";
%>

<%!
    final Log loggerCommon = LogUtil.getLog("commom.jsp");
    String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch(Exception e) {
            LogUtil.println("common.jsp -> urlEncode() exception: "+e);
            return str;
        }
    }

    String filter(String str) {
        //LogUtil.println("common.jsp -> filter() str: "+str);
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch(Exception e) {
            //LogUtil.println("common.jsp -> filter() exception: "+e);
            return str;
        }
    }


    Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    boolean ipCheck(String str) {
        return pattern.matcher(str).matches();
    }
    
     String getCookie(String key,HttpServletRequest request) {
        String value = null;
        Cookie myCookie[]=request.getCookies();//创建一个Cookie对象数组
        for(int i=0,n=myCookie.length;i<n;i++) {
            Cookie cookie= myCookie[i];
            if (cookie.getName().equals(key)) {
                value = cookie.getValue(); 
            }
        }
        return value;
    }
    static boolean isHasPrivilege(int level,HttpServletRequest request)
        throws DeviceException, NoPrivilegeException {
        boolean result = false;
        Privilege rights = new Privilege();
        if( rights.check(level,request) ) {
           result = true;
        }
        return result;
    }
    static void setActionInfo(int level,HttpServletRequest request) {
        request.setAttribute(ServletUtil.ATTR_OPERATION_TYPE,
                Privilege.PRIVILEGE_NAME_MAP.get(level));
    }
    

    int neededPrivilege = -1;
    boolean checkPrivilege(HttpServletRequest req) {
        loggerCommon.debug("checkPrivilege->begin");
        boolean checkResult = false;
        try {
            checkResult = isHasPrivilege(neededPrivilege, req);
        }   catch (NoPrivilegeException e) {            
             LogUtil.println("权限不足：",e);
            String err = urlEncode(e.getMessage());
            String url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false&errmsg="
                    + err;
        } catch (DeviceException e) {
            String err = urlEncode(e.getMessage());
            LogUtil.println("",e);
            
            String url = "/error.jsp?error="+err;            
        }
        return checkResult;
     }
%>

