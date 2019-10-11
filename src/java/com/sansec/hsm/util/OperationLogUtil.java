package com.sansec.hsm.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sansec.hsm.bean.login.UserList;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class OperationLogUtil {

	public static final String operationname = "LOG MANAGEMENT";
	public static final String STATUS_FAIL = "Failed";
	static boolean log = true;
	static final String name = "/home/log/webConsole/webConsole.log";
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String sUserIP = "";
	public static String user = "";

    public static void setUserIP(String sIP){
        sUserIP = sIP;
    }
    
     public static void setUser(String userName){
        user = userName;
    }
     
     public static String getUser(){
        if(user.isEmpty()){
            return "<WEB>";
        }else{
            return user;
        }
    }

    public static String getUserIP(){
        if(sUserIP.isEmpty()){
            return "<WEB>";
        }else{
            return sUserIP;
        }
    }

	public static void println(String operation,String msg) {
		if (!log) {
			return;
		}
		try {
			UserList userlist = UserList.getInstance();
			String[] users = userlist.getAllUsers();
			int serial = getserial();     //get the log serial
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name, true), "UTF-8"));
			bw.write("("+(serial+1)+ ")[INFO] - ["+sdf.format(new Date())+"]" + "<" + getUserIP() + ">" + msg);
			bw.newLine();
			bw.close();
		} catch (Exception ex) {
			Logger.getLogger(OperationLogUtil.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
        
	public static void printlnNew(String leave,String operation, String result) {
		if (!log) {
			return;
		}
		try {
			UserList userlist = UserList.getInstance();
			String[] users = userlist.getAllUsers();
			int serial = getserial();     //get the log serial
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name, true), "UTF-8"));
			bw.write("[HSM] ["+leave+"] - ["+sdf.format(new Date())+"]" + "[ip:" + getUserIP() + "][user:" + user+"][operation:"+operation+"][operresult:"+result+"]");
			bw.newLine();
			bw.close();
		} catch (Exception ex) {
			Logger.getLogger(OperationLogUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	public static void printlnLog(String string) {
		if (!log) {
			return;
		}
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name, true), "UTF-8"));
			bw.write("[HSM] ["+sdf.format(new Date())+"] "+string);
			bw.newLine();
			bw.close();
		} catch (Exception ex) {
			Logger.getLogger(OperationLogUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * 指定格式生成日志
	 * @param operation
	 * @param status
	 * @param msg
	 *
	 * @return
	 */
	public static void genLogMsg(String level, String operation, String status,String msg,String fileName,String lineNum) {
		String ip = getUserIP();
		String userName = user;
		String levelModified = String.format("%-7s",level);
		StringBuilder sbf = new StringBuilder();
		sbf.append("[").append(levelModified).append("]");

		sbf.append(" [operation:").append(operation).append(", ");
		sbf.append("status:").append(status);

		if (STATUS_FAIL.equals(status)) {
//			sbf.append(", errcode:").append(code).append(", ");
			sbf.append(", errmsg:").append(msg);
		}
		sbf.append("] ");

		sbf.append("[session ").append(ip).append("] ");
		if(StringUtils.isNotBlank(userName)){
			sbf.append("[user ").append(userName);
		}
		sbf.append("] ");

		if(StringUtils.isNotBlank(fileName)){
			sbf.append("[position ");
			sbf.append(fileName).append(":").append(lineNum).append("]");
		}
		printlnLog(sbf.toString());
	}

	public static int getserial() {
		int a = 1;
		if (!log) {
			return 0;
		}
		String sLog = new String();
		String s;
		String now = new String();
		String serial = new String();
		boolean more;
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(name), "UTF-8"));
			while ((s = br.readLine()) != null) {
				if ((!s.isEmpty()) && (s.length() > 0) && (s.getBytes()[s.getBytes().length - 1] != 0)) {
					now = s;
				}
			}
			br.close();

			if (now.isEmpty() || ((now.indexOf(")") < 1) && (now.indexOf("}") < 1))) {
				return 0;
			}
			if (now.indexOf(")") >= 1) {
				serial = now.substring(now.indexOf("(") + 1, now.indexOf(")"));
			} else {
				serial = now.substring(now.indexOf("{") + 1, now.indexOf("}"));

			}
			return Integer.parseInt(serial);
		} catch (Exception ex) {
			Logger.getLogger(OperationLogUtil.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
	}

	public static void interprintln(String operation, String msg) {
		if (!log) {
			return;
		}
		try {

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name, true), "UTF-8"));
			bw.write("<" + sdf.format(new Date()) + ">" + "<WEB>" + msg);
			bw.newLine();
			bw.close();
		} catch (Exception ex) {
			Logger.getLogger(OperationLogUtil.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public static void audit(int id) {
	}

	public static String readlog() {
		if (!log) {
			return "";
		}
		String sLog = new String();
		String s;
		boolean more;
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(name), "UTF-8"));
			while ((s = br.readLine()) != null) {
				s = s.replace('<', '{');
				s = s.replace('>', '}');
				sLog = sLog + s + "\r\n";
			}
			br.close();
		} catch (Exception ex) {
			Logger.getLogger(OperationLogUtil.class.getName()).log(Level.SEVERE, null, ex);
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Read Logs Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
		}

		return sLog;

	}
}
