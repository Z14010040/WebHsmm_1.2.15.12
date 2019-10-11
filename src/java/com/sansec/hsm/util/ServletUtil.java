package com.sansec.hsm.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sansec.hsm.bean.GlobalData;
import com.sansec.hsm.bean.log.LogBean;
import com.sansec.hsm.bean.login.UserTrace;
import com.sansec.util.RegUtil;

import debug.log.LogUtil;
import debug.log.LogUtil.Log;

/**
 *
 * @author root
 */
public final class ServletUtil {
    /**
     *
     */
    public static final String CLIB_NOT_FOUND = "未找到底层库";
    /**
     *
     */
    public static final String SERVICE_NOT_FOUND = "未找到服务处理类";
    /**
     *
     */
    public static final String CLIB_NOT_INIT = "初始化底层库失败";
    /**
     *
     */
    public static final String ERR_MSG_LABEL = "error,标签名必须为"+GlobalData.MIN_LABEL_LEN+"-" 
            + GlobalData.MAX_LABEL_LEN + "位由"
            +"数字和字符组成";
    /**
     *
     */
    public static final String SUFFIX_CERT_REQUEST = ".csr";
    /**
     *
     */
    public static final String ATTR_OPERATION_TYPE = "operation";
    
    public static final int RESPONSE_TYPE_PLAIN_TEXT = 0;
    
    public static final int RESPONSE_TYPE_JS = 1;
    
    private static final Log logger = LogUtil.getLog(ServletUtil.class);
    
    private ServletUtil() {
        
    }
    /**
     * 产生操作日志，如果msg以ok,开头，则认为是操作正确，否则认为操作失败.
     *
     * @param request
     * @param msg
     */
    //..目前除日志读取外不使用servlet 因此忽略日志
   /* public static void genLog(HttpServletRequest request,String msg) {
        logger.debug("msg:"+msg);
        if (msg != null && !"".equals(msg)) {
            HttpSession session = request.getSession();
            UserTrace user = (UserTrace)session.getAttribute(GlobalData.SESSION_USER_LOGIN);
            String operation = (String)request.getAttribute(ATTR_OPERATION_TYPE);
            if (operation == null) {
                operation = "未知";
            }
            boolean isSuccess = false;
            if (msg.startsWith("ok,")) {
                isSuccess = true;
                msg = msg.substring(3);
            } else if (msg.startsWith("error")) {
                msg = msg.substring(6);
            }
            LogBean bean = new LogBean();
            try {
                bean.setMsg(RegUtil.htmlReplace(
                        java.net.URLDecoder.decode(msg, "utf-8")));
            } catch (UnsupportedEncodingException e) {
                logger.warn("", e);
            }
            bean.setOperation(operation);
            if (user != null) {
                bean.setOperator(user.getUsername());
            } else {
                bean.setOperator("未知");
            }
            
            bean.setSuccess(isSuccess);
            try {
                boolean rv = LogManage.create(bean);
                logger.debug("添加结果："+rv);
            } catch (SQLException ex) {
                logger.warn("", ex);
            }
        }
        
    }*/
    /**
     *     *
     * @param request 
     * @param response servlet response
     * @param msg 
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public static void genResponse(HttpServletRequest request,
            HttpServletResponse response, String msg, int responseType)
            throws ServletException, IOException {
        logger.debug("提示信息：" + msg);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
    //    genLog(request,msg);        
        
        if ("true".equalsIgnoreCase(request.getParameter("isGuid"))) {//当前请求是初始化向导发出的
            String redirectUrl = request.getParameter("redirect");
            String url;
            if (redirectUrl == null || "".equals(redirectUrl)) {
                url = "/error.jsp?error="+java.net.URLEncoder.encode("跳转地址为空", "UTF-8");
            } else {
                url = java.net.URLDecoder.decode(redirectUrl, "UTF-8");
            }
            response.sendRedirect(url);
        } else {
            if (responseType == RESPONSE_TYPE_JS) {
                String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />";
                msg = "<script language=\"javascript\">alert(\"" + msg + "\");</script>";
                msg = html + msg + "</head></html>";
            }
            try {
                out.print(msg);
            } finally {
                out.close();
            }
        }
        
    }
    
    public static void genResponseWithoutLog(HttpServletRequest request,
            HttpServletResponse response, String msg, int responseType)
            throws ServletException, IOException {
        logger.debug("提示信息：" + msg);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        if ("true".equalsIgnoreCase(request.getParameter("isGuid"))) {//当前请求是初始化向导发出的
            String redirectUrl = request.getParameter("redirect");
            String url;
            if (redirectUrl == null || "".equals(redirectUrl)) {
                url = "/error.jsp?error="+java.net.URLEncoder.encode("跳转地址为空", "UTF-8");
            } else {
                url = java.net.URLDecoder.decode(redirectUrl, "UTF-8");
            }
            response.sendRedirect(url);
        } else {
            if (responseType == RESPONSE_TYPE_JS) {
                String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />";
                msg = "<script language=\"javascript\">alert(\"" + msg + "\");</script>";
                msg = html + msg + "</head></html>";
            }
            try {
                out.print(msg);
            } finally {
                out.close();
            }
        }
        
    }
    
    public static void genResponse(HttpServletRequest request,
            HttpServletResponse response, String msg)
            throws ServletException, IOException {
        genResponse(request,response,  msg, RESPONSE_TYPE_PLAIN_TEXT);
    }
    
    public static void genResponseWithoutLog(HttpServletRequest request,
            HttpServletResponse response, String msg)
            throws ServletException, IOException {
    	genResponseWithoutLog(request,response,  msg, RESPONSE_TYPE_PLAIN_TEXT);
    }
    
    /**
     *
     * @param label
     * @return
     */
    public static boolean isVaildLabel(String label) {
        
        return RegUtil.isNomarlString(label,
                GlobalData.MIN_LABEL_LEN, GlobalData.MAX_LABEL_LEN);
    }
    
    /**
     *
     * @param request
     * @param filename
     * @param content
     * @return
     * @throws IOException
     */
    public static boolean saveInWebInf(HttpServletRequest request,String filename,
            String content) throws IOException {
        boolean result = false;
        String filePath = request.getSession().getServletContext().getRealPath("/")
                + "/WEB-INF/certreq/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        filePath += filename;
        PrintStream out = new PrintStream(filePath);
        out.write("-----BEGIN NEW CERTIFICATE REQUEST-----\r\n".getBytes());
        out.write(content.getBytes());
        out.write("\n-----END NEW CERTIFICATE REQUEST-----".getBytes());
        out.close();
        
        return result;
    }
    
    /**
     *
     * @param request
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] getInWebInf(HttpServletRequest request,String filename)
            throws IOException {
        byte[] content = null;
        String filePath = request.getSession().getServletContext().getRealPath("/")
                + "/WEB-INF/certreq/" + filename;
        content = Streams.file2bytes(new File(filePath));
        return content;
    }
    
    /**
     *
     * @param response
     * @param filename
     * @param content
     * @throws IOException
     */
    public static void createDownload(HttpServletResponse response,String filename,
            byte[] content) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        BufferedOutputStream bos = null;
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            bos= new BufferedOutputStream(out);
            bos.write(content, 0, content.length);
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }
    
    /**
     *
     * @param request
     * @param response
     * @param msg
     * @throws ServletException
     * @throws IOException
     */
    public static void createAlert(HttpServletRequest request,
            HttpServletResponse response,String msg)
            throws ServletException, IOException {
        if (msg.startsWith("error,")) {
            msg = msg.substring(6);
        }
        
        genResponse(request,response,msg,RESPONSE_TYPE_JS);
    }
}
