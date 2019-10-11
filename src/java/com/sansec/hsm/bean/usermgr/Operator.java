package com.sansec.hsm.bean.usermgr;

import com.sansec.hsm.bean.GlobalData;
import com.sansec.hsm.bean.HSMError;
import com.sansec.hsm.bean.Language;
import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.lib.kmapi;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import com.sansec.util.ExecShell;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Operator extends HSMUser {
	 public static final String operationname = "USER MANAGEMENT";
	public Operator(int id) {
		super(id);
	}

	public Operator(String password) {
		super(password);
	}

    public Operator() {
        
    }
	

	/**
	 * 
	 * @throws NoPrivilegeException 
	 * @throws DeviceException
	 */
	public void add() throws NoPrivilegeException, DeviceException {
	/*
            Privilege rights = new Privilege();
		if( !rights.check(Privilege.PRIVILEGE_ADD_OPERATOR) ) {
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_ADD_OPERATOR));
		}
		
		// 获取密钥状态
		int[] curStatus = new int[GlobalData.DEVICE_STATUS_DWORD_SIZE];
		int rv = kmapi.INSTANCE.KM_GetUserStatus(curStatus);
		if (rv != HSMError.SDR_OK) {
			result = "获取管理员状态失败:" + HSMError.getErrorInfo(rv);
			throw new DeviceException(result);
		} 
		
		if(curStatus[13] == 0) {
			rv = kmapi.INSTANCE.KM_InitOperatorPasswd();
			if( rv != HSMError.SDR_OK ) {
				result = "初始化操作员口令失败:" + HSMError.getErrorInfo(rv);
				throw new DeviceException(result);
			}
		}
		
	*/	
	
		int rv = kmapi.INSTANCE.KM_AddOperator(password,0);
		if (rv != HSMError.SDR_OK) {
			result += "Add Operator Failed:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","AddOperator" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
			throw new DeviceException(result);
		} else {
			//result = "增加操作员成功.";
			result = "Add Operator Succeed.";
            OperationLogUtil.genLogMsg("INFO","AddOperator" ,"success",null,null,null);

            //写入文件
            try{
            Process process1 =ExecShell.getExecShellProcess("\\cp /opt/app/card.txt /usr/");
                process1.waitFor();

                Process process2 = ExecShell.getExecShellProcess("sed -i '/pwd/s/12345678/"+password+"/' /usr/card.txt");
                process2.waitFor();
            }catch(InterruptedException e){

            }

            //登出
            Operator oo = new Operator(1);
            oo.logout();
		}
		
		
		// 更新权限
		//Privilege rigths = Privilege.getInstance();
		//rigths.loadPrivilege();
	} 
	/**
	 * 获取Key的注册信息
         * @throws com.sansec.hsm.exception.DeviceException
	 */
	public int rigisterInformation() throws DeviceException {
                
                IntByReference userType = new IntByReference();
                IntByReference keyStatus = new IntByReference();
              //  userType.setValue(0);
                int rv = kmapi.INSTANCE.KM_ChekUSBKey(password,userType, keyStatus); //OperationLogUtil.println(operationname,"User Login Failed:"+e.getMessage());
		result = "This USBKey has already been registered!";
//                System.out.println(userType.getValue());
//                System.out.println(keyStatus.getValue());
        if(rv==1){
            OperationLogUtil.genLogMsg("INFO","USBKeyRegister" ,"success",null,null,null);
        }
        return keyStatus.getValue();
	}
	/**
	 * 管理员登录
	 */
    @Override
	public void login() throws DeviceException {
		try {
			super.login();
		} catch (DeviceException e) {
            OperationLogUtil.genLogMsg("ERROR","OperatorLogin" ,"Failed","Operator Login Failed:"+e.getMessage(),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new DeviceException(Language.get("UserOperatorLoginError") + " : " +e.getMessage());
		}
		result = Language.get("UserOperatorLogin");
        OperationLogUtil.genLogMsg("INFO","OperatorLogin" ,"success",null,null,null);
	}
	
	/**
	 * 管理员登出
     * @throws com.sansec.hsm.exception.DeviceException
	 */
    @Override
	public void logout() throws DeviceException {
		try {
			super.logout();
		} catch (DeviceException e) {
            OperationLogUtil.genLogMsg("ERROR","OperatorLogout" ,"Failed","Operator Logout Failed:"+e.getMessage(),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new DeviceException("Operator logout failed:"+e.getMessage());
		}

                
		result = Language.get("UserOperatorLogoutSuccess");
        OperationLogUtil.genLogMsg("INFO","OperatorLogout" ,"success",null,null,null);
	}
	
	/**
	 * 删除操作员
	 * @throws DeviceException
	 * @throws NoPrivilegeException
	 */
    @Override
	public void delete() throws DeviceException, NoPrivilegeException {
	/*	Privilege rights = new Privilege();

		if( !rights.check(Privilege.PRIVILEGE_DELETE_OPERATOR) ) {
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_DELETE_OPERATOR));
		}
		
        int rv = kmapi.INSTANCE.KM_InitOperatorPasswd();
        if(rv != HSMError.SDR_OK) {
            result = "Delete Operator Failed:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.println(operationname,result);
        } else {
        	result = Language.get("UserOperatorResetSuccess");
                OperationLogUtil.println(operationname,"Delete Operator Succeed.");
        }
            */
              //  for(int i =0;i<2;i++){
         
          int rv = kmapi.INSTANCE.KM_DelOperator(1);
        if(rv != HSMError.SDR_OK) {
            result = "Delete Operator Failed:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","DeleteOperator" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
        } else {
        	result = Language.get("UserOperatorResetSuccess");
            OperationLogUtil.genLogMsg("INFO","DeleteOperator" ,"success",null,null,null);
        }
        //}
	}

    /* 操作员是否存在 */
    public boolean isExists() throws DeviceException {
        // 获取用户状态
        int[] userStatus = getUserStatus();

        return (userStatus[12] > 0);
    }

    /* 获取已经存在的操作员 */
    public List<Integer> getAllExistsOperator() throws DeviceException {
        // 获取用户状态
        int[] userStatus = getUserStatus();
        
        List<Integer> list = new ArrayList<Integer>();
        for(int i=0; i<2; i++) {
            if(userStatus[i+12] > 0) {
                list.add(i+1);
            }
        }
        return list;
    }
    
    /* 操作员是否登录 */
    public boolean isLogin() throws DeviceException {
        // 获取用户状态
        int[] userStatus = getUserStatus();

        return (userStatus[12] > 0 && userStatus[14] > 0);
    }


    public static void main(String[] args) throws DeviceException {
        Operator opt = new Operator();
        System.out.println("exists: "+opt.isExists());
        System.out.println("login: "+opt.isLogin());
    }
}
