package com.sansec.hsm.servlet;

import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.util.ServletUtil;
import debug.log.LogUtil;
import debug.log.LogUtil.Log;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author root
 */
public abstract class AbstractHSMServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogUtil.getLog(AbstractHSMServlet.class);
    protected static final String IC_CARD_LOGIN_URL
            = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";            
    private static final String ERROR_PAGE = "/error.jsp";
    protected int privilege = -1;
    protected ThreadLocal<String> errmsg = new ThreadLocal<String>();
    protected static final int ERROR_TYPE_PLAIN = 0;
    protected static final int ERROR_TYPE_JS = 1;
    protected static final int ERROR_TYPE_FORWARD = 2;
    protected int showErrorType = ERROR_TYPE_PLAIN;
    protected String redirectUrl;    
    
    protected boolean checkPrivilege(HttpServletRequest req) {
        boolean check = false;
        try {
            logger.debug("当前权限：" + privilege);
            check = new Privilege().check(privilege,req);
            if (!check) {
                errmsg.set("权限不满足");
            }            
        } catch (DeviceException ex) {
            logger.error("", ex);
            errmsg.set(ex.getMessage());
        } catch (NoPrivilegeException ex) {
            logger.warn("", ex);
            errmsg.set(ex.getMessage());
        }
        return check;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        boolean check = checkPrivilege(req);
                
        if (!check) {
            logger.debug(errmsg.get());
            if (showErrorType == ERROR_TYPE_FORWARD) {
                if (redirectUrl != null) {
                    req.setAttribute("result", errmsg.get());
                    req.getRequestDispatcher(redirectUrl).forward(req,resp);
                } else {
                    String err = URLEncoder.encode(errmsg.get(),"UTF-8");
                    resp.sendRedirect(ERROR_PAGE+"?error="+err);
                }
            } else {
                if (req.getServletPath().startsWith("/admin/down")) {
                    ServletUtil.createAlert(req, resp, errmsg.get());
                } else {
                	if (privilege != Privilege.PRIVILEGE_DEL_LOG && privilege != Privilege.PRIVILEGE_AUDIT_LOG) {
                    	req.setAttribute(ServletUtil.ATTR_OPERATION_TYPE,
                    			Privilege.PRIVILEGE_NAME_MAP.get(privilege));
                        ServletUtil.genResponse(req, resp, "error,"+errmsg.get());
                    }
                	else{
                		ServletUtil.genResponseWithoutLog(req, resp, "error,"+errmsg.get());
                	}
                } 
            }
            
        } else {
            logger.debug("super service");
            super.service(req, resp);
        }
    }   
    
}
