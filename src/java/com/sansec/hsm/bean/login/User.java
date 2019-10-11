/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sansec.hsm.bean.login;

import com.sansec.hsm.bean.Config;
import com.sansec.hsm.bean.GlobalData;
import com.sansec.hsm.bean.Language;
import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.lib.MyCDll;
import com.sansec.hsm.util.ErrorInfo;
import debug.log.LogUtil;
import com.sansec.hsm.util.OperationLogUtil;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.catalina.tribes.util.Arrays;

/**
 *
 * @author root
 */
public class User {
	 public static final String operationname = "USER MANAGEMENT";
    private String username;
    private String password;
    protected String result; // 操作结果
    boolean flag;

    public User() {
        this.username = GlobalData.DEF_USER_NAME;
        this.password = GlobalData.DEF_USER_PWD;
    }
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getResult() {
        return this.result;
    }
    
    
    public boolean readNameAndPassword(String username,String password) throws FileNotFoundException,IOException{
        
        Properties pps = new Properties();
        byte[] pin = password.getBytes();

        try {
            MessageDigest md=MessageDigest.getInstance("SHA");
            md.update(pin);
            byte[] pass = md.digest();
            String webPass = Base64.getEncoder().encodeToString(pass);

            pps.load(new FileInputStream(Config.DEFAULT_WEBHSMM_CONFIG_FILE));

            String oldPass = pps.getProperty(username);
            if (oldPass.equals(webPass)) {
                return true;
            }
        } catch (Exception e) {
            LogUtil.println(e.getLocalizedMessage());
            return false;
        }
        return false;
    }
    
    
    
    public boolean checkPasswd() {
        int rv = 0;
        if(username == null || password == null) {
            return false;
        }
  
        boolean loginsuccess = false;
             try {
                 loginsuccess = readNameAndPassword(username,password);
                 if(!loginsuccess) {
//                    OperationLogUtil.println(operationname,"User "+this.username + " Login Failed, Error="+ rv);
                    return false;
                 }
            } catch (IOException ex) {
                  Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                  //OperationLogUtil.println(operationname,"User "+this.username + " Login Failed, Error="+ rv);
                  return false;
            }
        
        
        return true;
    }

    public boolean modifyPassword(String oldPasswd, String newPasswd) throws DeviceException, NoPrivilegeException{
        this.password = oldPasswd;
        
      Privilege rights = new Privilege();
		if( !rights.check(Privilege.PRIVILEGE_CHANGE_SYSTEM_PWD) ) {
            OperationLogUtil.genLogMsg("ERROR","Check Privilege" ,"Failed",Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_CHANGE_SYSTEM_PWD), ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_CHANGE_SYSTEM_PWD));
		}
        if (!checkPasswd()) {
            this.result = Language.get("LoginCheckPassworError");
            OperationLogUtil.genLogMsg("ERROR","CheckPassword" ,"Failed","Failed",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            return false;
        }
        
        try {
             Properties props=System.getProperties();
                    
            if(props.getProperty("os.arch").indexOf("arm")<0)  
            {
                 Process process = Runtime.getRuntime().exec("password " + this.username + " " + newPasswd);
                 process.waitFor();
            }
            else
            {
                Process process = Runtime.getRuntime().exec("passwd " + this.username );
                InputStream fis=process.getInputStream();  
                BufferedReader br=new BufferedReader(new InputStreamReader(fis));
                OutputStream fos=process.getOutputStream();
                PrintStream ps=new PrintStream(fos);
                String s;
                ps.print(newPasswd +"\n");
                ps.flush();
            
                 while((s=br.readLine())!=null){
     //   OperationLogUtil.println(operationname,"User "+this.username +" "+s+"."); 
                    if(s.indexOf("New password")>=0)
                        break;
                    else if (s.indexOf("Changing")>=0)
                        continue;
                    else
                         {this.result = Language.get("LoginChangePasswordError");
                             return false;}
                 }

                ps.print(newPasswd + "\n");
                ps.flush();
                while((s=br.readLine())!=null){
  //      OperationLogUtil.println(operationname,"User "+this.username +"  "+s+".");  
                 if(s.indexOf("changed")>0)
                    break;
                 else if (s.indexOf("Bad password")>=0)
                    continue;
                 else if (s.indexOf("Retype")>=0)
                    continue;
                  else
                     {this.result = Language.get("LoginChangePasswordError");
                         return false;}
                  }
     /*        ps.print(newPasswd + "\n"); 
            ps.flush(); 
            while((s=br.readLine())!=null){  
        OperationLogUtil.println(operationname,"User "+this.username +" again "+s+".");  
          }  */
            br.close();  
         fis.close();  
         ps.close();
         fos.close();
         process.waitFor();
            }
              
        } catch (Exception ex) {
            LogUtil.println("password " + this.username + newPasswd + ":" + ex);
            OperationLogUtil.genLogMsg("ERROR","CheckPassword" ,"Failed","Failed",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            this.result = Language.get("LoginChangePasswordError");
            return false;
        }
        OperationLogUtil.genLogMsg("INFO","CheckPassword" ,"success",null,null,null);
        this.result = Language.get("LoginChangePasswordSuccess");
        return true;
    }
    
    public boolean login() {
         if (checkPasswd()) {
             OperationLogUtil.genLogMsg("INFO","Login" ,"success",null,null,null);
            return true;
        } else {
            this.result = Language.get("LoginVerifyError");
            OperationLogUtil.genLogMsg("ERROR","Login" ,"Failed","Failed" , ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            return false;
        }
    }
    
        public boolean logout() {
            OperationLogUtil.genLogMsg("INFO","Logout" ,"success",null,null,null);
            return true;
       
    }
}
