package com.sansec.hsm.lib;


import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

public interface dmapi extends Library {
	final int MAX_NAME_LENGTH = 128;
	final int MAX_CSM_COUNT = 10;
    final int MAX_SERVICE_COUNT = 10;
	final int MAX_HOST_COUNT = 256;
	final int MAX_PIN_LENGTH = 16;
    

	
	dmapi INSTANCE = (dmapi)Native.loadLibrary("dmapi", dmapi.class);
	/**
	 * 备出厂信息定义，不允许用户自行修改
	 * 结构体来自<dmapi.h>
	 * typedef struct FactoryInfo_st{
	 *   char sManufacturer[128];    //厂家名称
	 *   char sType[16];             //产品批号
	 * 	 char sPn[16];               //产品号
	 * 	 char sSn[16];               //设备序列号
	 * 	 unsigned int nVersion;      //设备版本信息
	 * }FACTORY_INFO;
	 *
	 */
    public static class FACTORY_INFO extends Structure {
		public byte[] sManufacturer = new byte[128];					//厂家名称
		public byte[] sType = new byte[16];								//产品批号
		public byte[] sPn = new byte[16];								//产品号
		public byte[] sSn = new byte[16];								//设备序列号
		public int nVersion;											//设备版本信息
		public static class ByReference extends FACTORY_INFO 
			implements Structure.ByReference{}
		public static class ByValue extends FACTORY_INFO 
			implements Structure.ByValue{}
	}
    /**
     * 设备管理函数，int-->SGD_RV-->unsigned int
     * @param pstFactoryInfo
     * @return
     */
	public int DM_GetFactoyInfo(FACTORY_INFO.ByReference pstFactoryInfo);
	
	/**
pstFactoryInfo
	 * 设备维护信息定义，用户可通过UI进行配置
	 * 结构体来自<dmapi.h>
	 * typedef struct MaintainInfo_st{
	 *   char sSystem[MAX_NAME_LENGTH];          //应用系统的名称
	 *   char sCompany[MAX_NAME_LENGTH];         //用户的公司名称
	 *   char sDepartment[MAX_NAME_LENGTH];      //用户的部门信息
	 *   char sContactor[16];                   //设备维护联系人
	 *   char sTel[16];                         //设备维护联系人电话
	 *   char sMobile[16];                      //设备维护联系人手机
	 *   char sMail[32];                        //设备维护联系人邮件
	 * }MAINTAIN_INFO;
	 * 
	 */
	public static class MAINTAIN_INFO extends Structure {
		public byte[] sSystem = new byte[MAX_NAME_LENGTH];				//应用系统的名称
		public byte[] sCompany = new byte[MAX_NAME_LENGTH];				//用户的公司名称
		public byte[] sDepartment = new byte[MAX_NAME_LENGTH];			//用户的部门信息
		public byte[] sContactor = new byte[16];						//设备维护联系人
		public byte[] sTel = new byte[16];								//设备维护联系人电话
		public byte[] sMobile = new byte[16];							//设备维护联系人手机
		public byte[] sMail = new byte[32];								//设备维护联系人邮件
		public static class ByReference extends MAINTAIN_INFO 
			implements Structure.ByReference{}
		public static class ByValue extends MAINTAIN_INFO 
			implements Structure.ByValue{}
	}
	/**
	 * 设备管理函数，int-->SGD_RV-->unsigned int
	 * @param pstMaintainInfo
	 * @return
	 */
	public int DM_SetMaintainInfo(MAINTAIN_INFO.ByReference pstMaintainInfo);
	/**
	 * 设备管理函数，int-->SGD_RV-->unsigned int
	 * @param pstMaintainInfo
	 * @return
	 */
	public int DM_GetMaintainInfo(MAINTAIN_INFO.ByReference pstMaintainInfo);		

	/**
	 * 网络配置定义，支持多网卡，一网卡多IP
	 * 结构体来自<dmapi.h>

	 * typedef struct NetCfgInfo_st{
	 *   char sAlias[128];           //好记的名称
	 *   char sIPv4[16];             //IP地址
	 *   char sIPv4Mask[16];         //子网掩码
	 *   char sIPv4GW[16];           //网关地址
	 * }NETCONFIG_INFO;
	 * @author lqx
	 */
	public static class NETCONFIG_INFO extends Structure {
		public byte[] sAlias = new byte[128];				//好记的名称
		public byte[] sIPv4 = new byte[16];					//IP地址
		public byte[] sIPv4Mask = new byte[16];				//子网掩码
		public byte[] sIPv4GW = new byte[16];				//网关地址
		public static class ByReference extends NETCONFIG_INFO 
			implements Structure.ByReference{}
		public static class ByValue extends NETCONFIG_INFO 
			implements Structure.ByValue{}
	}
	/**
	 * 网络管理函数，int-->SGD_RV-->unsigned int
	 * @param pstNetConfigInfo
	 * @return
	 */
	public int DM_GetNetworkConfig(NETCONFIG_INFO.ByReference pstNetConfigInfo);
	/**
	 * 网络管理函数，int-->SGD_RV-->unsigned int
	 * @param pstNetConfigInfo
	 * @return
	 */
	public int DM_SetNetworkConfig(NETCONFIG_INFO.ByReference pstNetConfigInfo);		
	
	/**
	 * 服务管理类函数，int-->SGD_UINT32-->unsigned int
	 * @return
	 */
	public int DM_GetServicesCount(); 						
	
	/**服务信息定义
	 * 结构体来自<dmapi.h>
	 * typedef struct ServerInfo_st{
	 *   char sAlias[128];              //好记的名称
	 *   char sIP[32];                  //绑定的IP，0.0.0.0表示不绑定某一地址 
	 *   unsigned int nPort;            //服务端口
	 *   unsigned int nOnboot;          //是否自动启动
	 *   unsigned int nMaxConcurrent;   //最大并发数
	 *   unsigned int nSessionTimeout;  //服务的超时时间，防止服务进程长时间挂起
	 *   unsigned int nModuleCount;
	 *   char sModuleName[MAX_CSM_COUNT][16];
	 *   unsigned int nWhiteListCount;
	 *   unsigned int nWhiteListHost[MAX_HOST_COUNT];
	 *   char sConnectPwd[MAX_PIN_LENGTH];
	 *   char sUserPin[MAX_PIN_LENGTH];
	 * }SERVER_INFO;
	 * @author lqx
	 */
	public static class SERVER_INFO extends Structure {
		public byte[] sAlias = new byte[128];                       //好记的名称
		public byte[] sIP = new byte[32];                           //绑定的IP，0.0.0.0表示不绑定某一地址
		public int nPort;                                           //服务端口,显示给用户
		public int nOnboot;                                         //是否自动启动,显示给用户
		public int nMaxConcurrent;                                  //最大并发数,显示给用户
		public int nSessionTimeout;                                 //服务的超时时间，防止服务进程长时间挂起
		public int nModuleCount;
		public byte[] sModuleName = new byte[MAX_CSM_COUNT * 16];   //160个byte
		public int nWhiteListCount;
		public int[] nWhiteListHost = new int[MAX_HOST_COUNT];      //最大连接数
		public byte[] sConnectPwd = new byte[MAX_PIN_LENGTH];       //服务连接密码
		public byte[] sUserPin = new byte[MAX_PIN_LENGTH];          //服务起动口令
		public static class ByReference extends SERVER_INFO 
			implements Structure.ByReference{}
		public static class ByValue extends SERVER_INFO 
			implements Structure.ByValue{}
	}
	/**
	 * 服务管理类函数，int-->SGD_RV-->unsigned int
	 * @param pstServerInfo
	 * @return
	 */
	public int DM_AddService(SERVER_INFO.ByReference pstServerInfo);		
	
	/**
	 * 服务管理类函数，int-->SGD_RV-->unsigned int
	 * @param nIndex SGD_UINT32-->int
	 * nIndex start from 1
	 * @return
	 */
	public int DM_DeleteService(int nIndex); 		
	
	/**
	 * 服务管理类函数，int-->SGD_RV-->unsigned int
	 * @param sCfgFile String-->SGD_CHAR*-->char*
	 * @return
	 */
	public int DM_GetServiceInfo(String sCfgFile, SERVER_INFO.ByReference pstServerInfo);			
	
	/**
	 * 服务管理类函数，int-->SGD_RV-->unsigned int
	 * @param nIndex int-->SGD_UINT32-->unsigned int
	 * @param pstServerInfo
	 * @return
	 */
	public int DM_GetServiceInfoByIndex(int nIndex, SERVER_INFO.ByReference pstServerInfo);
	
	/**
	 * 服务管理类函数，int-->SGD_RV-->unsigned int
	 * @param sCfgFile String-->SGD_CHAR*-->char*
	 * @param pstServerInfo
	 * @return
	 */
	public int DM_ModifyService(String sCfgFile, SERVER_INFO.ByReference pstServerInfo);
	
	/**
	 * 服务管理类函数，int-->SGD_RV-->unsigned int
	 * @param nIndex int-->SGD_UINT32-->unsigned int
	 * @param pstServerInfo
	 * @return
	 */
	public int DM_ModifyServiceByIndex(int nIndex, SERVER_INFO.ByReference pstServerInfo);
}
