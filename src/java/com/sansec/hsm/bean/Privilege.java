package com.sansec.hsm.bean;

import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.lib.kmapi;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import com.sansec.hsm.util.ServletUtil;
import debug.log.LogUtil;
import debug.log.LogUtil.Log;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限管理类<br>
 *
 * @author Administrator
 *
 */
public final class Privilege {

	private static final Log logger = LogUtil.getLog(Privilege.class);
	public static final int PRIVILEGE_NONE = 0;  //
	public static final int PRIVILEGE_OPERATOR = 1;  //操作员权限
	public static final int PRIVILEGE_MANAGER = 2;  //管理员权限
	public static final int PRIVILEGE_SUPER_MANAGER = 4;  //超级管理员权限
	public static final int PRIVILEGE_ALL_MANAGEER = 8;//所有管理员都登录
	public static final int PRIVILEGE_AUDITOR = 16;//审计员权限
	//public final static String sPrivilegeNone = "无权限";
	//public final static String sPrivilegeOperator = "操作员权限";
	//public final static String sPrivilegeManager = "管理员权限";
	//public final static String sPrivilegeAuditor = "审计员权限";
	//public final static String sPrivilegeSuperManager = "超级管理员权限";
	//public final static String sPrivilegeALLManager = "全部管理员权限";
	public final static String sPrivilegeNone = Language.get("PrivilegeNone");
	public final static String sPrivilegeOperator = Language.get("PrivilegeOperator");
	public final static String sPrivilegeManager = Language.get("PrivilegeManager");
	public final static String sPrivilegeAuditor = Language.get("PrivilegeAuditor");
	public final static String sPrivilegeSuperManager = Language.get("PrivilegeSuperManager");
	public final static String sPrivilegeALLManager = Language.get("PrivilegeALLManager");
	private static final int PRIVILEGE_BASE = 0;
	// 系统管理权限
	public static final int PRIVILEGE_SHOW_DEVICE_INFO = PRIVILEGE_BASE + 1;
	public static final int PRIVILEGE_DEVICE_INIT = PRIVILEGE_BASE + 2;
	public static final int PRIVILEGE_SHOW_RUN_STATUS = PRIVILEGE_BASE + 3;
	public static final int PRIVILEGE_SHOW_MAINTAIN_INFO = PRIVILEGE_BASE + 4;
	public static final int PRIVILEGE_MODIFY_MAINTAIN_INFO = PRIVILEGE_BASE + 5;
	public static final int PRIVILEGE_START_SERVICE = PRIVILEGE_BASE + 6;
	public static final int PRIVILEGE_RESTART_SERVICE = PRIVILEGE_BASE + 7;
	public static final int PRIVILEGE_STOP_SERVICE = PRIVILEGE_BASE + 8;
	public static final int PRIVILEGE_SHOW_SERVICE_CONFIG = PRIVILEGE_BASE + 9;
	public static final int PRIVILEGE_MODIFY_SERVICE_CONFIG = PRIVILEGE_BASE + 10;
	public static final int PRIVILEGE_RESTART_NETWORK = PRIVILEGE_BASE + 11;
	public static final int PRIVILEGE_SHOW_NETWORK_CONFIG = PRIVILEGE_BASE + 12;
	public static final int PRIVILEGE_MODIFY_NETWORK_CONFIG = PRIVILEGE_BASE + 13;
	//人员管理权限
	public static final int PRIVILEGE_SHOW_LOGIN_STATE = PRIVILEGE_BASE + 14;
	public static final int PRIVILEGE_SHOW_PRIVILEGE_TABLE = PRIVILEGE_BASE + 15;
	public static final int PRIVILEGE_INIT_MANAGER = PRIVILEGE_BASE + 16;
	public static final int PRIVILEGE_ADD_MANAGER = PRIVILEGE_BASE + 17;
	public static final int PRIVILEGE_DELETE_MANAGER = PRIVILEGE_BASE + 18;
	public static final int PRIVILEGE_ADD_OPERATOR = PRIVILEGE_BASE + 19;
	public static final int PRIVILEGE_DELETE_OPERATOR = PRIVILEGE_BASE + 20;
	public static final int PRIVILEGE_CHANGE_PIN = PRIVILEGE_BASE + 21;
	//密钥管理权限
	public static final int PRIVILEGE_GEN_MAIN_KEY = PRIVILEGE_BASE + 22;
	public static final int PRIVILEGE_SHOW_KEY_INFO = PRIVILEGE_BASE + 23;
	public static final int PRIVILEGE_GEN_KEY_PAIR = PRIVILEGE_BASE + 24;
	public static final int PRIVILEGE_SET_SKAP = PRIVILEGE_BASE + 25;
	public static final int PRIVILEGE_DELETE_KEY_PAIR = PRIVILEGE_BASE + 26;
	public static final int PRIVILEGE_IMPORT_KEY_PAIR = PRIVILEGE_BASE + 27;
	public static final int PRIVILEGE_BACKUP = PRIVILEGE_BASE + 28;
	public static final int PRIVILEGE_RESTORE = PRIVILEGE_BASE + 29;
	//文件管理权限
	public static final int PRIVILEGE_UPLOAD_CERT = PRIVILEGE_BASE + 30;
	public static final int PRIVILEGE_UPLOAD_CA = PRIVILEGE_BASE + 31;
	public static final int PRIVILEGE_UPLOAD_CRL = PRIVILEGE_BASE + 32;
	public static final int PRIVILEGE_UPLOAD_CON = PRIVILEGE_BASE + 33;
	public static final int PRIVILEGE_UPLOAD_UPD = PRIVILEGE_BASE + 34;
	public static final int PRIVILEGE_GEN_CERT_REQ = PRIVILEGE_BASE + 36;
	//证书管理
	public static final int PRIVILEGE_ADD_CA = PRIVILEGE_BASE + 37;
	public static final int PRIVILEGE_DELETE_CA = PRIVILEGE_BASE + 38;
	public static final int PRIVILEGE_ADD_SIGN_CERT = PRIVILEGE_BASE + 39;
	public static final int PRIVILEGE_DELETE_SIGN_CERT = PRIVILEGE_BASE + 40;
	//日志管理
	public static final int PRIVILEGE_DEL_LOG = PRIVILEGE_BASE + 41;
	public static final int PRIVILEGE_AUDIT_LOG = PRIVILEGE_BASE + 42;
	//备份恢复
	public static final int PRIVILEGE_RESTORE_SHOW_UPLOAD = PRIVILEGE_BASE + 43;
	public static final int PRIVILEGE_UPLOAD_BACKUP = PRIVILEGE_BASE + 44;
	public static final int PRIVILEGE_SHOW_IMPORT_PART = PRIVILEGE_BASE + 45;
	public static final int PRIVILEGE_IMPORT_PART = PRIVILEGE_BASE + 46;
	public static final int PRIVILEGE_RSTORE_FINISH = PRIVILEGE_BASE + 47;
	public static final int PRIVILEGE_USER_LOGIN = PRIVILEGE_BASE + 48;
	public static final int PRIVILEGE_CHANGE_USER_PASSWD = PRIVILEGE_BASE + 49;
	public static final int PRIVILEGE_USER_LOGOUT = PRIVILEGE_BASE + 50;
	public static final int PRIVILEGE_SHOW_MANAGER_LIST = PRIVILEGE_BASE + 51;
	public static final int PRIVILEGE_IC_CARD_LOGIN = PRIVILEGE_BASE + 52;
	public static final int PRIVILEGE_IC_CARD_LOGOUT = PRIVILEGE_BASE + 53;
	public static final int PRIVILEGE_DEL_CRL = PRIVILEGE_BASE + 54;
	public static final int PRIVILEGE_MODIFY_CA = PRIVILEGE_BASE + 55;//修改ca中crl颁发地址；
	public static final int PRIVILEGE_ADD_AUDITOR = PRIVILEGE_BASE + 56;//增加审计员
	public static final int PRIVILEGE_DELETE_AUDITOR = PRIVILEGE_BASE + 57;//删除审计员
	public static final int PRIVILEGE_SHOW_LOG = PRIVILEGE_BASE + 58;//显示日志
	public static final int PRIVILEGE_START_CRLUP = PRIVILEGE_BASE + 59;//启动CRL定时更新DAEMON
	public static final int PRIVILEGE_STOP_CRLUP = PRIVILEGE_BASE + 60;//关闭CRL定时更新DAEMON
	public static final int PRIVILEGE_START_NTPUP = PRIVILEGE_BASE + 61;//启动NTP定时更新DAEMON
	public static final int PRIVILEGE_STOP_NTPUP = PRIVILEGE_BASE + 62;//关闭NTP定时更新DAEMON
	public static final int PRIVILEGE_CHANGE_SYSTEM_PWD = PRIVILEGE_BASE + 70;
	private int level;
	private String privilegeInfo;
	private String UserList;
	public static final HashMap<Integer, String> PRIVILEGE_NAME_MAP = new HashMap<Integer, String>() {
		{
			// 系统管理权限
			put(PRIVILEGE_SHOW_DEVICE_INFO, "显示设备信息");
			put(PRIVILEGE_DEVICE_INIT, "设备初始化");
			put(PRIVILEGE_SHOW_RUN_STATUS, "运行状态查看");
			put(PRIVILEGE_SHOW_MAINTAIN_INFO, "设备维护信息查看");
			put(PRIVILEGE_MODIFY_MAINTAIN_INFO, "设备维护信息修改");
			put(PRIVILEGE_START_SERVICE, "开启服务");
			put(PRIVILEGE_RESTART_SERVICE, "重启服务");
			put(PRIVILEGE_STOP_SERVICE, "关闭服务");
			put(PRIVILEGE_SHOW_SERVICE_CONFIG, "显示服务配置信息");
			put(PRIVILEGE_MODIFY_SERVICE_CONFIG, "修改服务配置信息");
			put(PRIVILEGE_RESTART_NETWORK, "重启网络");
			put(PRIVILEGE_SHOW_NETWORK_CONFIG, "网络配置信息查看");
			put(PRIVILEGE_MODIFY_NETWORK_CONFIG, "修改网络配置信息");
			//人员管理权限
			put(PRIVILEGE_SHOW_LOGIN_STATE, "用户登录状态查看");
			put(PRIVILEGE_SHOW_PRIVILEGE_TABLE, "用户权限表查看");
			put(PRIVILEGE_INIT_MANAGER, "");
			put(PRIVILEGE_ADD_MANAGER, "添加管理员");
			put(PRIVILEGE_DELETE_MANAGER, "删除管理员");
			put(PRIVILEGE_ADD_OPERATOR, "添加操作员");
			put(PRIVILEGE_ADD_AUDITOR, "添加审计员");
			put(PRIVILEGE_DELETE_OPERATOR, "删除操作员");
			put(PRIVILEGE_DELETE_AUDITOR, "删除审计员");
			put(PRIVILEGE_CHANGE_PIN, "修改登录密码");
			put(PRIVILEGE_SHOW_MANAGER_LIST, "管理员列表显示");
			put(PRIVILEGE_IC_CARD_LOGIN, "IC卡登录");
			put(PRIVILEGE_IC_CARD_LOGOUT, "IC卡退出");
			//密钥管理权限
			put(PRIVILEGE_GEN_MAIN_KEY, "");
			put(PRIVILEGE_SHOW_KEY_INFO, "密钥信息查看");
			put(PRIVILEGE_GEN_KEY_PAIR, "生成密钥对");
			put(PRIVILEGE_SET_SKAP, "");
			put(PRIVILEGE_DELETE_KEY_PAIR, "删除密钥对");
			put(PRIVILEGE_IMPORT_KEY_PAIR, "导入密钥对");
			put(PRIVILEGE_BACKUP, "密钥备份");
			put(PRIVILEGE_RESTORE, "密钥备份恢复");
			//文件管理权限
			put(PRIVILEGE_UPLOAD_CERT, "上传用户证书");
			put(PRIVILEGE_UPLOAD_CA, "上传CA证书");
			put(PRIVILEGE_UPLOAD_CRL, "上传CRL");
			put(PRIVILEGE_UPLOAD_CON, "");
			put(PRIVILEGE_UPLOAD_UPD, "");

			put(PRIVILEGE_GEN_CERT_REQ, "产生证书请求");
			//证书管理
			put(PRIVILEGE_ADD_CA, "添加CA");
			put(PRIVILEGE_MODIFY_CA, "修改CA");
			put(PRIVILEGE_DELETE_CA, "删除CA");
			put(PRIVILEGE_ADD_SIGN_CERT, "添加签名证书");
			put(PRIVILEGE_DELETE_SIGN_CERT, "删除签名证书");
			//日志管理
			put(PRIVILEGE_DEL_LOG, "删除日志");
			put(PRIVILEGE_AUDIT_LOG, "审计日志");
			put(PRIVILEGE_SHOW_LOG, "查看日志");
			//备份恢复
			put(PRIVILEGE_RESTORE_SHOW_UPLOAD, "显示上传文件");
			put(PRIVILEGE_UPLOAD_BACKUP, "上传备份文件");
			put(PRIVILEGE_SHOW_IMPORT_PART, "显示导入分量");
			put(PRIVILEGE_IMPORT_PART, "导入分量");
			put(PRIVILEGE_RSTORE_FINISH, "显示导入完成");
			//登录用户
			put(PRIVILEGE_USER_LOGIN, "用户登录");
			put(PRIVILEGE_CHANGE_USER_PASSWD, "修改登录密码");
			put(PRIVILEGE_USER_LOGOUT, "用户退出");

			put(PRIVILEGE_DEL_CRL, "删除crl");

			put(PRIVILEGE_START_CRLUP, "开启crl定时更新服务");
			put(PRIVILEGE_STOP_CRLUP, "关闭crl定时更新服务");
			put(PRIVILEGE_START_NTPUP, "开启ntp定时更新服务");
			put(PRIVILEGE_STOP_NTPUP, "关闭ntp定时更新服务");

			put(PRIVILEGE_CHANGE_SYSTEM_PWD, "修改系统登录密码");
		}
	};
	public static final HashMap<Integer, Integer> PRIVILEGE_VALUE_MAP = new HashMap<Integer, Integer>() {
		{
			// 系统管理权限
			put(PRIVILEGE_SHOW_DEVICE_INFO, PRIVILEGE_NONE);
			put(PRIVILEGE_DEVICE_INIT, PRIVILEGE_NONE);
			put(PRIVILEGE_SHOW_RUN_STATUS, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_SHOW_MAINTAIN_INFO, PRIVILEGE_NONE);
			put(PRIVILEGE_MODIFY_MAINTAIN_INFO, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_START_SERVICE, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_RESTART_SERVICE, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_STOP_SERVICE, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_SHOW_SERVICE_CONFIG, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_MODIFY_SERVICE_CONFIG, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_RESTART_NETWORK, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_SHOW_NETWORK_CONFIG, PRIVILEGE_NONE);
			put(PRIVILEGE_MODIFY_NETWORK_CONFIG, PRIVILEGE_OPERATOR);
			//人员管理权限
			put(PRIVILEGE_SHOW_LOGIN_STATE, PRIVILEGE_NONE);
			put(PRIVILEGE_SHOW_PRIVILEGE_TABLE, PRIVILEGE_NONE);
			put(PRIVILEGE_INIT_MANAGER, PRIVILEGE_NONE);
			put(PRIVILEGE_ADD_MANAGER, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_DELETE_MANAGER, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_ADD_OPERATOR, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_ADD_AUDITOR, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_DELETE_OPERATOR, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_DELETE_AUDITOR, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_CHANGE_PIN, PRIVILEGE_NONE);
			put(PRIVILEGE_SHOW_MANAGER_LIST, PRIVILEGE_NONE);
			put(PRIVILEGE_IC_CARD_LOGIN, PRIVILEGE_NONE);
			put(PRIVILEGE_IC_CARD_LOGOUT, PRIVILEGE_NONE);//IC卡退出，操作员权限 (Modified to none by DongFW 20141127)
			//密钥管理权限
			put(PRIVILEGE_GEN_MAIN_KEY, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_SHOW_KEY_INFO, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_GEN_KEY_PAIR, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_SET_SKAP, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_DELETE_KEY_PAIR, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_IMPORT_KEY_PAIR, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_BACKUP, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_RESTORE, PRIVILEGE_SUPER_MANAGER);
			//文件管理权限
			put(PRIVILEGE_UPLOAD_CERT, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_UPLOAD_CA, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_UPLOAD_CRL, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_UPLOAD_CON, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_UPLOAD_UPD, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_GEN_CERT_REQ, PRIVILEGE_OPERATOR);
			//证书管理
			put(PRIVILEGE_ADD_CA, PRIVILEGE_MANAGER);
			put(PRIVILEGE_MODIFY_CA, PRIVILEGE_MANAGER);
			put(PRIVILEGE_DELETE_CA, PRIVILEGE_MANAGER);
			put(PRIVILEGE_ADD_SIGN_CERT, PRIVILEGE_MANAGER);
			put(PRIVILEGE_DELETE_SIGN_CERT, PRIVILEGE_MANAGER);
			//日志管理

			put(PRIVILEGE_DEL_LOG, PRIVILEGE_MANAGER);
			put(PRIVILEGE_AUDIT_LOG, PRIVILEGE_MANAGER);
			put(PRIVILEGE_SHOW_LOG, PRIVILEGE_OPERATOR);

			//备份恢复,改为超级管理员权限
			put(PRIVILEGE_RESTORE_SHOW_UPLOAD, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_UPLOAD_BACKUP, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_SHOW_IMPORT_PART, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_IMPORT_PART, PRIVILEGE_SUPER_MANAGER);
			put(PRIVILEGE_RSTORE_FINISH, PRIVILEGE_SUPER_MANAGER);
			//登录用户
			put(PRIVILEGE_USER_LOGIN, PRIVILEGE_NONE);
			put(PRIVILEGE_CHANGE_USER_PASSWD, PRIVILEGE_NONE);
			put(PRIVILEGE_USER_LOGOUT, PRIVILEGE_NONE);

			put(PRIVILEGE_DEL_CRL, PRIVILEGE_OPERATOR);

			put(PRIVILEGE_START_CRLUP, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_STOP_CRLUP, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_START_NTPUP, PRIVILEGE_OPERATOR);
			put(PRIVILEGE_STOP_NTPUP, PRIVILEGE_OPERATOR);

			put(PRIVILEGE_CHANGE_SYSTEM_PWD, PRIVILEGE_OPERATOR);

		}
	};

	public Privilege() throws DeviceException {
		level = PRIVILEGE_NONE;
		privilegeInfo = sPrivilegeNone;
		load();
	}

	//public void init() {
	//level = PRIVILEGE_NONE;
	//}
    /*public static Privilege getInstance() {
	 //return instance;
	 return null;
	 }*/
	public int getLevel() {
		return level;
	}

	public String getUserList() {
		return UserList;
	}

	public String getPrivilegeInfo() {
		return privilegeInfo;
	}

	public static String getPrivilegeInfo(int nPrivilege) {
		Integer neededLevel = PRIVILEGE_VALUE_MAP.get(nPrivilege);
		if (neededLevel == PRIVILEGE_OPERATOR) {
			return sPrivilegeOperator;
		} else if (neededLevel == PRIVILEGE_MANAGER) {
			return sPrivilegeManager;
		} else if (neededLevel == PRIVILEGE_SUPER_MANAGER) {
			return sPrivilegeSuperManager;
		} else if (neededLevel == PRIVILEGE_AUDITOR) {
			return sPrivilegeAuditor;
		} else {
			return sPrivilegeNone;
		}
	}

	/**
	 * 加载权限
	 *
	 * @throws DeviceException
	 */
	private void load() throws DeviceException {
            int[] status = new int[GlobalData.DEVICE_STATUS_DWORD_SIZE];
            int rv = kmapi.INSTANCE.KM_GetUserStatus(status);
            if (rv != HSMError.SDR_OK) {
                    String info = HSMError.getErrorInfo(rv);
                    throw new DeviceException(info);
            }
            /*for(int i=0; i<status.length; i++) {
             System.out.println("status["+i+"]"+status[i]);
             }*/
            UserList = "";
            if (status[12] > 0 && status[14] > 0) {
                    this.level |= PRIVILEGE_OPERATOR;
                    privilegeInfo = sPrivilegeOperator;
            }

            int mgrExist = 0;
            int mgrLogin = 0;
            for (int i = 0; i < Config.getMaxManangerCount(); i++) {
                    mgrExist += status[2 + i];
                    mgrLogin += status[7 + i];
                    //                     UserList = UserList + ((status[7+i]>0)?(i+1)+"号管理员，":"");
                    UserList = "";
            }
            if (mgrLogin > 0) {
                    this.level |= PRIVILEGE_MANAGER | PRIVILEGE_OPERATOR;
                    privilegeInfo = sPrivilegeManager;
            }

            if ((mgrExist > 0) && (mgrLogin * 2 > mgrExist)) {
                    this.level |= PRIVILEGE_SUPER_MANAGER | PRIVILEGE_OPERATOR; //超级管理员具有操作员权限
                    privilegeInfo = sPrivilegeSuperManager;
            }
            if (mgrLogin == Config.getMaxManangerCount()) {
                    this.level |= PRIVILEGE_ALL_MANAGEER;
                    //privilegeInfo = sPrivilegeALLManager;
                    privilegeInfo = sPrivilegeSuperManager;
            }
	}

	/**
	 * 比较权限
	 *
	 * @param level
	 * @return
	 * @throws DeviceException
	 */
	public boolean check(int level) throws DeviceException, NoPrivilegeException {
		load();
		boolean result = false;

		Integer neededLevel = PRIVILEGE_VALUE_MAP.get(level);

		logger.debug("needed level:" + neededLevel + ",level now:" + this.level);
		if (neededLevel != null) {
			if (neededLevel == PRIVILEGE_NONE) {
				return true;
			}
			result = (this.level & neededLevel) > 0;
			if (!result) {
				throw new NoPrivilegeException(getPrivilegeInfo(level));
			}
		} else {
			throw new NoPrivilegeException("当前权限：" + level + "未定义");
		}
		return result;
	}

	//严格检查，如果传入的是操作员权限，必须是操作员权限
	public boolean strictCheck(int level) throws DeviceException, NoPrivilegeException {
		load();
		boolean result = false;

		Integer neededLevel = PRIVILEGE_VALUE_MAP.get(level);

		logger.debug("needed level:" + neededLevel + ",level now:" + this.level);
		if (neededLevel != null) {
			if (neededLevel == PRIVILEGE_NONE) {
				return true;
			}
			if (neededLevel == PRIVILEGE_OPERATOR) {
				//检测操作员权限
				int[] status = new int[GlobalData.DEVICE_STATUS_DWORD_SIZE];
				int rv = kmapi.INSTANCE.KM_GetUserStatus(status);
				if (rv != HSMError.SDR_OK) {
					String info = HSMError.getErrorInfo(rv);
					throw new DeviceException(info+".File: "+ErrorInfo.returnErrorInfo().get(0)+".LineNum: "+ErrorInfo.returnErrorInfo().get(1));
				}
				if (status[12] > 0 && status[14] > 0) {
					result = true;
				}else{
					result = false;
				}

			}else{
				result = (this.level & neededLevel) > 0;
			}
			
			if (!result) {
				throw new NoPrivilegeException(getPrivilegeInfo(level));
			}
		} else {
			throw new NoPrivilegeException("当前权限：" + level + "未定义");
		}
		return result;
	}

	public boolean check(int level, HttpServletRequest request)
			throws DeviceException, NoPrivilegeException {
		boolean result = false;
		result = check(level);
		if (result && level != PRIVILEGE_DEL_LOG && level != PRIVILEGE_AUDIT_LOG
				&& level != PRIVILEGE_SHOW_LOG) {
			request.setAttribute(ServletUtil.ATTR_OPERATION_TYPE,
					PRIVILEGE_NAME_MAP.get(level));
		}
		return result;
	}

	@Override
	public String toString() {
		return privilegeInfo + ": " + level;
	}

	/**
	 * 初始密码机
	 *
	 * @return
	 * @throws DeviceException
	 */
	public boolean init() throws DeviceException {
		int rv = kmapi.INSTANCE.KM_EraseAllKeys();
		LogUtil.println("init -> Privilege.java: rv = 0x" + Integer.toHexString(rv));
		if (rv != HSMError.SDR_OK) {
			OperationLogUtil.genLogMsg("ERROR","Initialize" ,"Failed","Error="+rv , ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			String result = "Key initialization error:" + HSMError.getErrorInfo(rv);
			throw new DeviceException(result+".File: "+ErrorInfo.returnErrorInfo().get(0)+".LineNum: "+ErrorInfo.returnErrorInfo().get(1));
		}
                
		OperationLogUtil.genLogMsg("INFO","Initialize" ,"success",null,null,null);
		return true;
	}

	public boolean isInited() throws DeviceException {
		int[] status = new int[GlobalData.DEVICE_STATUS_DWORD_SIZE];
		int rv = kmapi.INSTANCE.KM_GetUserStatus(status);
		if (rv != HSMError.SDR_OK) {
			String info = HSMError.getErrorInfo(rv);
			throw new DeviceException(info+".File: "+ErrorInfo.returnErrorInfo().get(0)+".LineNum: "+ErrorInfo.returnErrorInfo().get(1));
		}

		return (status[0] != 1);
	}

	public static void main(String[] args) throws DeviceException {
		Privilege rights = new Privilege();
		//boolean bCheck = rights.check(PRIVILEGE_OPERATOR);
		//System.out.println(bCheck);
		System.out.println(rights);
	}
}
