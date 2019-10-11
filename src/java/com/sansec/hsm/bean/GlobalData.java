package com.sansec.hsm.bean;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;


public class GlobalData{
	//版本号
	public static final String VERSION = "v1.3.0.0";

	//算法表示
	public static final int SGD_SM1_ECB	= 0x00000101;
	public static final int SGD_SM1_CBC	= 0x00000102;
	public static final int SGD_SM1_CFB	= 0x00000104;
	public static final int SGD_SM1_OFB	= 0x00000108;
	public static final int SGD_SM1_MAC	= 0x00000110;

	public static final int SGD_AES_ECB	= 0x00000401;
	
	public static final int SGD_CSM_VERSION = 0x01000000; /*csm server version*/
	public static final int SGD_PROTOCL_VERSION = 0x01000000; /*PROTOCOL*/
	public static final int SGD_STANDARD_VERSION = 0x00000001; /*standard*/
	
	public static final String DEFAULT_CFG_FILE = "/etc/swsds.ini";
	public static final int MAX_FILE_NAME_LENGTH = 32;
	public static final int MAX_NAME_LENGTH = 128;
	public static final int DEVICE_INFO_SIZE = 256;  //设备信息存储区大小
	public static final int DEVICE_STATUS_DWORD_SIZE = 32;
	public static final int MAX_BUFFER_SIZE = 8192; //最大传输字节
	public static final int USER_PIN_LENGTH = 8;    //IC卡PIN口令
	public static final int MAX_MANAGER_COUNT = 3;
	public static final int MAX_KEY_LENGT = 32;
        public static final int BACKUP_KEY_COUNT = 3;
	
	//#define MAX_SESSION_KEY_COUNT    200
	//#define MAX_RSA_KEY_PAIR_COUNT   100
	public static final int MAX_IV_LENGTH = 16;
	public static final int DEF_KEY_LENGTH_SM1 = 32;
	public static final int DEF_KEY_LENGTH_SSF33 = 16;
	public static final int DEF_BLOCK_LENGTH_SM1 = 16;
	public static final int DEF_BLOCK_LENGTH_SSF33 = 16;
	
	public static final int KEY_TYPE_ECDSA = 7;
	public static final int KEY_TYPE_DSA = 6;
	public static final int KEY_TYPE_RSA_EX = 5;
	public static final int KEY_TYPE_RSA = 4;
	public static final int KEY_TYPE_RSA_DER = 0x14;//DER格式密钥
	public static final int KEY_TYPE_ECC = 3;
	public static final int KEY_TYPE_SM2 = 3;

	public static final int KEY_TYPE_SESSION_KEY = 2;
	public static final int KEY_TYPE_KEK = 1;
	public static final int KEY_TYPE_EXTERNAL_KEY = 0;
	
	public static final int ExRSAref_MAX_BITS = 4096;
	public static final int ExRSAref_MAX_LEN = ((ExRSAref_MAX_BITS + 7) / 8);         //512
	public static final int ExRSAref_MAX_PBITS = ((ExRSAref_MAX_BITS + 1) / 2);       //2048
	public static final int ExRSAref_MAX_PLEN = ((ExRSAref_MAX_PBITS + 7)/ 8);        //256
	
	public static final int LiteRSAref_MAX_BITS = 2048;
	public static final int LiteRSAref_MAX_LEN = ((LiteRSAref_MAX_BITS + 7) / 8);     //256
	public static final int LiteRSAref_MAX_PBITS = ((LiteRSAref_MAX_BITS + 1) / 2);   //1024
	public static final int LiteRSAref_MAX_PLEN = ((LiteRSAref_MAX_PBITS + 7)/ 8);    //128
	
	public static final int ECCref_MAX_BITS = 256;
	public static final int ECCref_MAX_LEN = ((ECCref_MAX_BITS+7) / 8);               //32
	public static final int ECCref_MAX_CIPHER_LEN = 136;
	
	public static final int KEY_TYPE_USER_FILE = 0x31;
	public static final int KEY_TYPE_USER_DATA = 0x30;
	public static final int KEY_TYPE_MGMT = 0x20;
	public static final int KEY_TYPE_SECKEY_RSA = 0x21; //保护密钥私钥部分
	public static final int KEY_TYPE_SECKEY_SYMM  = 0x22; //保护密钥对称密钥
	public static final int KEY_TYPE_SECKEY_MK = 0x23; //保护密钥主密钥KEK


	public static final String DEF_USER_NAME = "";
	public static final String DEF_USER_PWD = "swxa1234";
	public static final String DEF_SERVICE_NAME = "swserver";
	public static final String DEF_FTP_PATH = "/var/swhsm";
	public static final String BACKUP_FILE_NAME = "swhsmbak.dat";
	public static final String UPLOAD_BACKUP_FILE = "/var/swhsm/upload/swhsmbak.dat";
	public static final String DOWNLOAD_BACKUP_FILE = "/var/swhsm/download/swhsmbak.dat";
	public static final String TEST_BACKUP_FILE = "/root/cm/test_swhsmbak.dat";
	public static final int BACKUP_PROTOCOL_VERSION = 1;
	public static final byte[] BACKUP_PROTOCOL_LABEL = "SWXACBAK".getBytes();
	//public static final int BACKUP_ALGORITHM_ID = SGD_SM1_ECB;
	public static final int BACKUP_ALGORITHM_ID = SGD_SM1_ECB;
        
        public static final int MIN_LABEL_LEN = 3;
        public static final int MAX_LABEL_LEN = 32;

        public static Pointer session;
        public static PointerByReference hUSBKey;
                         
         public static int[] uiUserID ;
         public static int[] uiUserType ;
         

}
