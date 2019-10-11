package com.sansec.hsm.lib;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface kmapi extends Library {
    final int MAX_NAME_LENGTH = 128;
    final int MAX_RSA_KEY_COUNT = 100;
    final int MAX_RSA_KEY_PAIR_COUNT = (MAX_RSA_KEY_COUNT / 2);

    final int MAX_KEK_COUNT = 200;
    final int MAX_SESSION_KEY_COUNT = 2048;
    final int MAX_DSA_KEY_COUNT = 0;
    final int MAX_ECDSA_KEY_COUNT = 0;
    final int MAX_ECC_KEY_COUNT = 100; //add by gao, 2010-3-18
    final int MAX_ECC_KEY_PAIR_COUNT = (MAX_ECC_KEY_COUNT / 2);

    final int MAX_ISK_ACCESS_PWD_LEN = 32; //内部私钥访问控制码
    final int MAX_KEY_LENGTH = 32;
    final int MAX_KEK_LENGTH = MAX_KEY_LENGTH;
    final int MAX_SESSION_KEY_LENGTH = MAX_KEY_LENGTH;

    kmapi INSTANCE = (kmapi) Native.loadLibrary("kmapi", kmapi.class);

    public int KM_Finalize();

    /**
     * int-->SGD_RV-->unsigned int
     * 调用该系列函数前，必须先调用此函数
     * 从存储区加载到共享内存
     *
     * @return
     */
    public int KM_LoadKeys();

    public int KM_SetSecKey();

    public int KM_EraseAllKeys();

    public int KM_InitKeysotre();

    /**
     * int-->SGD_RV-->unsigned int
     *
     * @param pucSecKeyCipher      String-->SGD_UCHAR*-->unsigned char*
     * @param uiSecKeyCipherLength IntByReference-->SGD_UINT32*-->unsigned int*
     * @return
     */
    public int KM_ImportSecKey(byte[] pucSecKeyCipher, int uiSecKeyCipherLength);

    public int KM_ExportSecKey(byte[] pucSecKeyCipher, IntByReference puiSecKeyCipherLength, IntByReference puiSecKeyIndex);

    public int KM_GetLoginStatus();

    public int KM_GetUserStatus(int[] punCurStatus);

    public int KM_UserLogin(String sPin, IntByReference pUserType, IntByReference punUserID);

    public int KM_UserLogout(int nSel);

    public int KM_ChangePin(String sOldPin, String sNewPin);

    public int KM_AddManager(int unMgrNo, String sPin);

    public int KM_DelManager(int unMgrNo);

    public int KM_InitOperatorPasswd();

    public int KM_AddOperator(String sPin, int unMgrNo);

    public int KM_DelOperator(int nSel);

    public int KM_ChekUSBKey(String sPin, IntByReference pUserType, IntByReference punUserID);

    /**
     * RSA密钥管理函数
     * int-->SGD_RV-->unsigned int
     *
     * @param unKeyType        int-->SGD_UINT32-->unsigned int
     * @param punKeyStatusList int-->SGD_UINT32*-->unsigned int*
     * @param punListSize      IntByReference-->SGD_UINT32*-->unsigned int*
     * @return
     */
    public int KM_GetKeyStatus(int unKeyType, int[] punKeyStatusList, IntByReference punListSize);

    public int KM_CheckECCSupport();


    public int KM_GenerateKeyPair(int unIndex, int unKeyType, int unKeyBits);

    public int KM_GenerateKeyPairEx(int unIndex, int unKeyType, int unKeyBits, int unPublicExponent);

    public int KM_ImportKeyPair(int unIndex, int unKeyType, byte[] pbPriKey, int unPriKeyLen, byte[] pbPubKey, int unPubKeyLen);

    public int KM_GetKeyPair(int unIndex, int unKeyType, byte[] pbPriKey, IntByReference punPriKeyLen, byte[] pbPubKey, IntByReference punPubKeyLen);

    //public int KM_GetKeyPair(int unIndex, int unKeyType, RSArefPrivateKey.ByReference pbPriKey, IntByReference punPriKeyLen, byte[] pbPubKey, IntByReference punPubKeyLen);
    //public int KM_GetKeyPair(int unIndex, int unKeyType, ECCrefPrivateKey.ByReference pbPriKey, IntByReference punPriKeyLen, byte[] pbPubKey, IntByReference punPubKeyLen);
    public int KM_ExportPubKey(int unIndex, int unKeyType, String pbPubKey, IntByReference punPubKeyLen);

    public int KM_DeleteKeyPair(int unIndex, int unKeyType);


    public int KM_SetAuthenCode(int unIndex, String pbAuthenCode, int unAuthenCodeLen);

    public int KM_CheckAuthenCode(int unIndex, String pbAuthenCode, int unAuthenCodeLen);

    public int KM_GenerateKEK(int unIndex, int unKeyBits);

    public int KM_ImportKEK(int unIndex, byte[] pbKey, int unKeyLength);

    public int KM_GetKEK(int unIndex, byte[] pbKey, IntByReference punKeyLength);

    public int KM_DeleteKEK(int unKeyID);

    public int KM_PutSessionKey(String pbKey, int unKeyLength, IntByReference punKeyID);

    public int KM_GetSessionKey(int unKeyID, String pbKey, IntByReference punKeyLength);

    public int KM_DestroySessionKey(int unKeyID);

    public int KM_CreateFileSystem();

    public int KM_CreateFile(String sFileName, int unFileSize);

    public int KM_ReadFile(String sFileName, int unOffset, String pucBuffer, int unReadLength);

    public int KM_WriteFile(String sFileName, int unOffset, String pucBuffer, int unWriteLength);

    public int KM_DeleteFile(String sFileName);

    public int KM_GetFileStatus(IntByReference punSizeUsed, IntByReference punSizeFree, IntByReference punFileCount);

    /**
     * typedef struct UserFileInfo_st{
     * char sName[MAX_NAME_LENGTH];
     * char sUser[MAX_NAME_LENGTH];
     * unsigned int nSize;
     * unsigned int nPermissions;
     * unsigned int tCreated;
     * unsigned int tModified;
     * }USERFILEINFO;
     */
    public static class USERFILEINFO extends Structure {
        public byte[] sName = new byte[MAX_NAME_LENGTH];
        public byte[] sUser = new byte[MAX_NAME_LENGTH];
        public int nSize;
        public int nPermissions;
        public int tCreated;
        public int tModified;

        public static class ByReference extends USERFILEINFO implements Structure.ByReference {
        }

        public static class ByValue extends USERFILEINFO implements Structure.ByValue {
        }
    }

    public int KM_GetFileInfo(String sFileName, USERFILEINFO pstFileInfo);

    public int KM_GetUserDataInfo(byte[] pcFilePath, IntByReference punTotleSize, IntByReference punEncryptFlag);

    public int KM_BackupInit(PointerByReference hsessionHandle);

    public int KM_BackupExportKeyComponent(Pointer hsessionHandle, String pin, int num, int[] uiUserID, int[] uiUserType, PointerByReference hUSBKey);

    public int KM_RestoreInit(PointerByReference hsessionHandle);

    public int KM_RestoreExportKeyComponent(Pointer hsessionHandle, String pin, int num, int[] uiUserID, int[] uiUserType, PointerByReference hUSBKey);

    public int KM_RestoreProtocol(Pointer hSWCSMSession, int[] uiUserID, int[] uiUserType, Pointer hUSBKey);

}