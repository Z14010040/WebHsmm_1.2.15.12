package com.sansec.hsm.servlet.log;
import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.bean.log.LogManage;
import com.sansec.hsm.servlet.AbstractHSMServlet;
import com.sansec.hsm.util.ServletUtil;
import com.sansec.util.RegUtil;
import debug.log.LogUtil;
import debug.log.LogUtil.Log;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author root
 */
public class AuditLog extends AbstractHSMServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogUtil.getLog(AuditLog.class);

    @Override
    public void init() throws ServletException {
        this.privilege = Privilege.PRIVILEGE_AUDIT_LOG;
        System.out.println("init.......");
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String ids = request.getParameter("ids");
        if (RegUtil.isMathing("\\d+",ids)) {
            int idInt;
            try {
                idInt = Integer.parseInt(ids);                
            } catch (Exception e) {
                ServletUtil.genResponseWithoutLog(request, response, "error,数值转化失败");
                return;
            }
            try {
                LogManage.audit(idInt);
                ServletUtil.genResponseWithoutLog(request, response, "ok,更新成功");
            } catch (Exception ex) {
                logger.error("", ex);
                ServletUtil.genResponseWithoutLog(request, response, "error,:"+ex.getMessage());
            }
        } else if (RegUtil.isMathing("(\\d+,)+", ids)) {
            ids = ids.substring(0, ids.length()-1);
            try {
                LogManage.audit(ids);
                ServletUtil.genResponseWithoutLog(request, response, "ok,更新成功");
            } catch (Exception ex) {
                logger.error("", ex);
                ServletUtil.genResponseWithoutLog(request, response, "error,:"+ex.getMessage());
            }
        } else {
            ServletUtil.genResponseWithoutLog(request, response, "error,非法参数:"+ids);
        }
    }

}
