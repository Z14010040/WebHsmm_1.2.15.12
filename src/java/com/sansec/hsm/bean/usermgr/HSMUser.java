package com.sansec.hsm.bean.usermgr;

import com.sansec.hsm.bean.Config;
import com.sansec.hsm.bean.GlobalData;
import com.sansec.hsm.bean.HSMError;
import com.sansec.hsm.bean.Language;
import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.bean.login.User;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.lib.kmapi;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import com.sansec.util.ExecShell;
import com.sun.jna.ptr.IntByReference;
import debug.log.LogUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

public class HSMUser {
    public static final String operationname = "USER MANAGEMENT";
    public static final String PINSAVEFILE = "/mnt/linux/etc/swhsm/user.ini";
    public static boolean CheckManagerFunc = false;
	protected int id;
	protected String password;
	protected String result; // 操作结果
	public String getResult() {
		return result;
	}

    public HSMUser() {
	}

	public HSMUser(String password) {
		this.password = password;
	}
	
	public HSMUser(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * 用户登录
	 * @throws DeviceException
	 */
	public void login() throws DeviceException {
		IntByReference userId = new IntByReference();
		IntByReference userType = new IntByReference();
//        System.out.println("HSMUser.java-----login------password:"+password);
		int rv = kmapi.INSTANCE.KM_UserLogin(password,userType,userId);
		if (rv != HSMError.SDR_OK) {

		    result = HSMError.getErrorInfo(rv);
			throw new DeviceException(result);
		}
		
		id = userType.getValue();
        if(id==0){

        //登陆后1.记录下登陆状态，并加入自动登陆. 2.启动SecKMS的tomcat 3.获取自动登陆的结果判断，决定是否启动SecKMS的tomcat

        /*
          开启自动登陆
        */
            try{
                 Process process = ExecShell.getExecShellProcess("sh  /opt/shell/starttomcat.sh");
                 process.waitFor();
                 }catch(InterruptedException e){
            }
        }
        OperationLogUtil.genLogMsg("INFO","HSMUserLogin" ,"success",null,null,null);
	}
	
	/**
	 * 用户登出
	 * @throws DeviceException
	 */
	public void logout() throws DeviceException {
		int rv = kmapi.INSTANCE.KM_UserLogout(id);
		if (rv != HSMError.SDR_OK) {
			result =HSMError.getErrorInfo(rv);;
            OperationLogUtil.genLogMsg("ERROR","HSMUserLogout" ,"Failed","HSMUser Logout Failed:"+HSMError.getErrorInfo(rv) ,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new DeviceException(result);
		}
        if(id == 1) {
            result = Language.get("UserOperatorLogoutSuccess");
        } else {
            result = Language.get("UserManagerLogoutSuccess");
        }
        OperationLogUtil.genLogMsg("INFO","HSMUserLogout" ,"success",null,null,null);
	}

        public void saveloginPin(String newPin){
            
            Properties pps = new Properties();
            byte[] pin = newPin.getBytes();
            
            try {
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(pin);
                byte[] pass = md.digest();
                String newPass = Base64.getEncoder().encodeToString(pass);
                
                pps.load(new FileInputStream(Config.DEFAULT_WEBHSMM_CONFIG_FILE));

                pps.setProperty(Config.CONFIG_TAG_WEBLOGIN, newPass);
                OutputStream out = new FileOutputStream(Config.DEFAULT_WEBHSMM_CONFIG_FILE);

                pps.store(out, null);
		} catch (Exception e) {
			LogUtil.println(e.getLocalizedMessage());
		}
            
        }
        
        public boolean checkManager()
        {
             if(!CheckManagerFunc)
            {
                return true;
            }
        try {
            boolean value=false;
            int i = 0,rv = 0;
            String pin;
            IntByReference userId = new IntByReference();
            IntByReference userType = new IntByReference();
                userType.setValue(1);
            Properties p = new Properties();       
            File pins = new File(PINSAVEFILE);
            FileInputStream fis;
            fis = new FileInputStream(pins);
            p.load(fis);
            while (true)
            {
                pin =  p.getProperty("user"+i);
                i++;
                if(pin == null)
                {
                    value = false;
                    break;
                }
                rv = kmapi.INSTANCE.KM_UserLogin(pin, userType,userId);
		if (rv != HSMError.SDR_OK) {
			continue;
		}
                else 
                {
                    value = true;
                    break;
                }
                
            }
            fis.close();
            p.clear();
            return value;
        } catch (Exception ex) {
           
            return false;
        }
        }
        
        public void savePin(String newPin)
        {
        try {
            int i = 0;
            String pin= new String();
            Properties p = new Properties();       
            File pins = new File(PINSAVEFILE);
            FileInputStream fis;
            fis = new FileInputStream(pins);
            p.load(fis);
            for (;;)
            {
                pin =  p.getProperty("user"+i);
 
                if(pin == null)
                {
                    break;
                }
                i++;
                if (pin.equalsIgnoreCase(newPin))
                {
       //             OperationLogUtil.println(operationname,"重复.");
                    fis.close();
                    p.clear();
                    return;
                }
            }
            fis.close();
            p.setProperty("user"+i, newPin);
            FileOutputStream fos = new FileOutputStream(PINSAVEFILE);
            p.store(fos, null);
            fos.close();
            
        } catch (Exception ex) {
   //         StackTraceElement stackTraceElement= ex.getStackTrace()[0];// 得到异常棧的首个元素 
  //          OperationLogUtil.println(operationname,"File="+stackTraceElement.getFileName()+".Line="+stackTraceElement.getLineNumber()
  //                  +".Method="+stackTraceElement.getMethodName());
   //          OperationLogUtil.println(operationname,"保存密码失败.");
        }
        }
        
    public void delete() throws DeviceException, NoPrivilegeException {

        
    }
	
	/**
	 * 修改PIN码
	 * @param oldPin 旧PIN码
	 * @param newPin 新PIN码
	 * @throws DeviceException
 	 * @throws NoPrivilegeException 
	 */
	public void changePin(String oldPin, String newPin) throws DeviceException, NoPrivilegeException {
		Privilege rigths = new Privilege();
		if ( !rigths.check(Privilege.PRIVILEGE_CHANGE_PIN) ) {
            OperationLogUtil.genLogMsg("ERROR","ChangePin" ,"Failed",Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_CHANGE_PIN),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_CHANGE_PIN));
		}
		
		int rv = kmapi.INSTANCE.KM_ChangePin(oldPin, newPin);
		if (rv != HSMError.SDR_OK) {
			result = "CHange User Pin Failed: " + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","ChangePin" ,"Failed","Change User Pin Failed: " + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException(result);
		} else {
            System.out.println(CheckManagerFunc);
            if(CheckManagerFunc){
                savePin(newPin);
            }
            result = "Change USB Key Pin Succeed.";
            OperationLogUtil.genLogMsg("INFO","ChangePin" ,"success",null,null,null);
		}
	}

        public void changeloginPin(String oldPin, String newPin) throws DeviceException {
              
            User user = new User("admin", oldPin);
            boolean bVerify = user.login();
            if(!bVerify){
                OperationLogUtil.genLogMsg("ERROR","ChangeLoginPin" ,"Failed","Change Login Pin Failed." ,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                result = "the old password is fault.";
                throw new DeviceException(result);
            }
              saveloginPin(newPin);
                                  
	      result = "Change Login Password Succeed.";
            OperationLogUtil.genLogMsg("INFO","ChangeLoginPin" ,"success",null,null,null);
	}

        
    /* 获取用户状态 */
    protected int[] getUserStatus() throws DeviceException {
            
        int[] userStatus = new int[GlobalData.DEVICE_STATUS_DWORD_SIZE];
		int rv = kmapi.INSTANCE.KM_GetUserStatus(userStatus);
		if (rv != HSMError.SDR_OK) {
			result = "Get administrator status failed: " + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Get User Status" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new DeviceException(result);
		}
//                if (userStatus[0]!= 1 &&userStatus[0]!=2)
//                {
//                    result = "当前设备状态异常["+ userStatus[0]+"]，请重新初始化设备.";
//			throw new DeviceException(result);
//                }

        return userStatus;
    }
}
