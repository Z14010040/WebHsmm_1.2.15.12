/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sansec.hsm.servlet;

import com.sansec.hsm.bean.Config;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sansec.hsm.bean.usermgr.Operator;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.util.ExecShell;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Administrator
 */
public class InitServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet InitServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet InitServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }

        @Override  
    public void init() throws ServletException{  
        // TODO Auto-generated method stub  
        super.init();
        Config.initialize();
//        String line;
//        //读取配置文件，判断是否已经初始化
//        try{
//            System.out.println("1");
//            File file=new File("/usr/card.txt");
//            if (!file.exists()) {
//            return;
//            }
//            System.out.println("2");
//            FileReader fr=new FileReader("/usr/card.txt");
//            BufferedReader br=new BufferedReader(fr);
//            line=br.readLine();
//            while(line != null){
//                line=br.readLine();
//                System.out.println(line);
//                     //如果已经初始化，获取密码
//                if(line.contains("pwd")){
//                    System.out.println("3");
//                    String pwd = line.trim().split(":")[1];
//                    Operator oper = new Operator(pwd);
//                    try{
//                        System.out.println("4");
//                        oper.login();
//                        System.out.println("5");
//                   /*
//                    Process process = ExecShell.getExecShellProcess("sh  /opt/shell/starttomcat.sh");
//                             process.waitFor();
//                             */
//                    }catch(DeviceException e){
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//            fr.close();
//            br.close();
//        }catch(FileNotFoundException e){
//            return;
//        }catch (IOException ex) {
//               // Logger.getLogger(InitServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}