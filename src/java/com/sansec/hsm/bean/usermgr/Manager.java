package com.sansec.hsm.bean.usermgr;

import com.sansec.hsm.bean.Config;
import com.sansec.hsm.bean.GlobalData;
import com.sansec.hsm.bean.HSMError;
import com.sansec.hsm.bean.Language;
import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.lib.kmapi;
import com.sansec.hsm.util.ErrorInfo;
import debug.log.LogUtil;
import java.util.ArrayList;
import java.util.List;
import com.sansec.hsm.util.OperationLogUtil;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.IOException;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileReader;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;

public class Manager extends HSMUser {
    
    public static final String operationname = "USER MANAGEMENT";
    public static final String filePath = "/mnt/linux/etc/swhsm/service.ini";
    public Manager() {

	}

	public Manager(int id) {
		super(id);
	}

	public Manager(String password) {
		super(password);
	}
	
	/**
	 * 管理员数目是否达到最大了
	 * @return
	 * @throws DeviceException 
	 */
	public boolean isMaxCount() throws DeviceException {
		// 获用户状态
		int[] userStatus = getUserStatus();
		
		if (userStatus[1] >= Config.getMaxManangerCount()) {
            //result = "目前管理员数目已经达到最大数，不能继续添加新的管理员";
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @throws NoPrivilegeException 
	 * @throws DeviceException
	 */
	public void add() throws NoPrivilegeException, DeviceException {
            // 获用户状态
//            System.out.println("Manager.java开始。。。。。");
            int[] userStatus = getUserStatus();
//            for(int i =0; i<userStatus.length;i++){
//                System.out.println(userStatus[i]);
//            }
                

            Privilege rights = new Privilege();
            LogUtil.println("Privilege: "+rights);
        
//        if (userStatus[1] != 0) {
//            if( !rights.check(Privilege.PRIVILEGE_MANAGER) ) {
//			    throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MANAGER));
//            }
//		} else {
//            if( !rights.check(Privilege.PRIVILEGE_INIT_MANAGER) ) {
//			    throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_INIT_MANAGER));
//            }
//		}
    
            if(userStatus[1] >= GlobalData.MAX_MANAGER_COUNT) {
                result = "The current number of administrators has reached the maximum, and you cannot continue to add new administrators.";
                OperationLogUtil.genLogMsg("ERROR","AddManager" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                return;
            }
        
            // 确定下一个要添加的用户的ID
            int mgrNo ;
            for (mgrNo = 1; mgrNo < GlobalData.MAX_MANAGER_COUNT; mgrNo++) {
                    if (userStatus[1 + mgrNo] == 0) {
                            break;
                    }
            }

            int rv = kmapi.INSTANCE.KM_AddManager(mgrNo, password);
//            System.out.println(mgrNo);
//            System.out.println(password);
//            System.out.println("Manager.java结束。。。。");
            if (rv != HSMError.SDR_OK) {
                result = "Add Manager Failed:" + HSMError.getErrorInfo(rv);
                OperationLogUtil.genLogMsg("ERROR","AddManager" ,"Failed",result, ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException(result);
            } else {
                result = "Add No. " + mgrNo + " Manager Succeed.";
                OperationLogUtil.genLogMsg("INFO","AddManager" ,"success",null,null,null);
            }

               
            if(userStatus[1] == 0) {
                // 只有第一次增加管理员时设置系统保护密钥
                // 设置主密钥及保护密钥，此时已经满足管理员权限
                rv = kmapi.INSTANCE.KM_SetSecKey();
                if (rv != HSMError.SDR_OK) {
                    result = "Generate SecKey Failed:" + HSMError.getErrorInfo(rv)  ;
                    OperationLogUtil.genLogMsg("ERROR","AddManager" ,"Failed","Generate SecKey Failed,Error="+HSMError.getErrorInfo(rv), ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }
                OperationLogUtil.genLogMsg("INFO","SetSecKey" ,"success",null,null,null);
            }
	} 
	
	/**
	 * 管理员登录
     * @throws com.sansec.hsm.exception.DeviceException
	 */
    @Override
	public void login() throws DeviceException {
        try {
            super.login();
        } catch (DeviceException e) {
            OperationLogUtil.genLogMsg("ERROR", "ManagerLogin", "Failed",  "User Login Failed:" + e.getMessage(), ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException(Language.get("UserManagerLoginError") + e.getMessage());
        }

        result = "NO." + super.getId() + " Manager Login Succeed.";
        if (super.getId() >= 1) {
            OperationLogUtil.genLogMsg("INFO", "ManagerLogin", "success",  null, null, null);

        }
    }
    /**
	 * 获取Key的注册信息
         * @throws com.sansec.hsm.exception.DeviceException
	 */
	public int rigisterInformation() throws DeviceException {
                
        IntByReference userType = new IntByReference();
        IntByReference keyStatus = new IntByReference();
        int rv = kmapi.INSTANCE.KM_ChekUSBKey(password,userType, keyStatus); //OperationLogUtil.println(operationname,"User Login Failed:"+e.getMessage());
		result = "This USBKey has already been registered!";
        if(rv==1){
            OperationLogUtil.genLogMsg("INFO","GetUSBKeyRegisterInformation" ,"success",null,null,null);
        }
        return keyStatus.getValue();
	}
	
	/**
	 * 管理员登出
         * @throws com.sansec.hsm.exception.DeviceException
	 */
    @Override
	public void logout() throws DeviceException {
		try {
            this.id = 2;
			super.logout();
		} catch (DeviceException e) {
            OperationLogUtil.genLogMsg("ERROR","ManagerLogout" ,"Failed","Manager Logout Failed:"+e.getMessage(),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new DeviceException("Administrator logout failed:"+e.getMessage());
		}
		
		//result = super.getId() + "号管理员登出成功";
        OperationLogUtil.genLogMsg("INFO","ManagerLogout" ,"success",null,null,null);
	}
	
        
        
	/**
	 * 删除管理员
	 * @throws DeviceException
	 * @throws NoPrivilegeException
	 */
    @Override
	public void delete() throws DeviceException, NoPrivilegeException {
		Privilege rights = new Privilege();
		if( !rights.check(Privilege.PRIVILEGE_DELETE_MANAGER) ) {
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_DELETE_MANAGER));
		}
		// 华大 / 姓姜
		// 获用户状态
		int[] userStatus = getUserStatus();
		
		if(userStatus[1] <= 1) {
			throw new DeviceException("The current number of administrators has reached the minimum and you cannot continue to delete administrators."+" File: "+ErrorInfo.returnErrorInfo().get(0)+".LineNum: "+ErrorInfo.returnErrorInfo().get(1));
		}

		int index = id;
		if(userStatus[index+1] > 0) {
            int rv = kmapi.INSTANCE.KM_DelManager(index);
            if(rv != 0) {
                result = "Delete NO." + index + " Manager Failed:" + HSMError.getErrorInfo(rv);
                OperationLogUtil.genLogMsg("ERROR","DeleteManager" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException(result);
            } else {
                result = "Delete NO." + index + " Manager Succeed.";
                OperationLogUtil.genLogMsg("INFO","DeleteManager" ,"success",null,null,null);
            }
        } else {
            result = "NO." + index + "Administrator does not exist.";
            throw new DeviceException(result);
        }
	}

    /* 获取已经登录的管理员 */
    public List<Integer> getAllLoginManager() throws DeviceException {
        // 获取用户状态
        int[] userStatus = getUserStatus();
        List<Integer> list = new ArrayList<Integer>();
        for(int i=0; i<3; i++) {
            if(userStatus[i+7] > 0) {
                list.add(i+1);
            }
        }
        return list;
    }

    /* 获取已经存在的管理员 */
    public List<Integer> getAllExistsManager() throws DeviceException {
        // 获取用户状态
        int[] userStatus = getUserStatus();
        
        List<Integer> list = new ArrayList<Integer>();
        for(int i=0; i<3; i++) {
            if(userStatus[i+2] > 0) {
                list.add(i+1);
            }
        }
        return list;
    }

    public int getMaxCount() {
        return Config.getMaxManangerCount();
    }
    
    public boolean setMaxCount(int maxCount) {
        return Config.SetMaxManangerCount(maxCount);
    }
    
    public String readIniFile(int sectionNum,String name){
        String value = "false";
        IniFile iniFile=new BasicIniFile();
        File file=new File(filePath);
        IniFileReader rad=new IniFileReader(iniFile, file);
        try {
            rad.read();
            IniSection iniSection=iniFile.getSection(sectionNum);
            IniItem iniItem=iniSection.getItem(name);
            if(iniItem==null){
                iniItem = new IniItem(name);
                iniItem.setValue(value);
                iniSection.addItem(iniItem);
                iniFile.addSection(iniSection);
                IniFileWriter wir=new IniFileWriter(iniFile, file);
                wir.write();
            }else {
                value = iniItem.getValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
    public void modifyIniFile(int sectionNum,String name, String value){
        IniFile iniFile=new BasicIniFile();
        File file=new File(filePath);
        IniFileReader rad=new IniFileReader(iniFile, file);
        IniFileWriter wir=new IniFileWriter(iniFile, file);
        try {
            rad.read();
            IniSection iniSection=iniFile.getSection(sectionNum);
            IniItem iniItem=iniSection.getItem(name);
            iniItem.setValue(value);
            iniSection.addItem(iniItem);
            iniFile.addSection(iniSection);
            wir.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws DeviceException {
        // 获取用户状态
        Manager mgr = new Manager();
        int[] userStatus = mgr.getUserStatus();
        for(int i=0; i<userStatus.length; i++) {
            System.out.println("userStatus["+i+"] = "+userStatus[i]);
        }

        List<Integer> list = null;
        list = mgr.getAllExistsManager();
        System.out.println("Exists Managers: "+list);

        list = mgr.getAllLoginManager();
        System.out.println("Login Managers: "+list);
    }
}
