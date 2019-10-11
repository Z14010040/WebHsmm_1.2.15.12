/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sansec.hsm.bean;

/**
 *
 * @author geyuxing
 */
public class Enumerate {
    
    public static final int SWR_KEY_BASE = 0x1500000;
    
    public static int SWR_KEY_OpenUSBKey_ERR          = (SWR_KEY_BASE + 0x0000001);   //打开用户Key失败
    public static int SWR_KEY_CheckPin_ERR            = (SWR_KEY_BASE + 0x0000002);   //验证用户Key的PIN口令失败
    public static int SWR_KEY_ReadAllFile_ERR         = (SWR_KEY_BASE + 0x0000003);   //读取用户Key信息失败
    public static int SWR_KEY_CloseUSBKey_ERR         = (SWR_KEY_BASE + 0x0000004);   //关闭用户Key失败
    public static int SWR_KEY_DoProtocol_ECC_ERR      = (SWR_KEY_BASE + 0x0000006);   //用户Key运算错误
    public static int SWR_KEY_GenerateECCKeyPair_ERR  = (SWR_KEY_BASE + 0x0000007);   //用户Key产生设备密钥对失败
    public static int SWR_KEY_ExportECCPublicKey_ERR  = (SWR_KEY_BASE + 0x0000008);   //导出用户Key的设备公钥失败
    public static int SWR_KEY_WriteAllFile_ERR        = (SWR_KEY_BASE + 0x0000009);   //写入用户Key信息失败
    public static int SWR_KEY_UKEY_ProtocolStart_ERR  = (SWR_KEY_BASE + 0x000000A);   //未添加用户或用户启动错误
    public static int SWR_KEY_UKEY_Type_ERR           = (SWR_KEY_BASE + 0x000000B);   //用户类型错误，请插入正确的KEY
    public static int SWR_KEY_NoUser_Type_ERR         = (SWR_KEY_BASE + 0x000000C);   //未添加用户
    public static int SWR_KEY_PIN_LENGTH_ERR          = (SWR_KEY_BASE + 0x000000D);   //PIN口令长度应为8个字符
    public static int SWR_KEY_UKEY_OTHER_ERR          = (SWR_KEY_BASE + 0x000000E);   //未知错误
    //备份恢复专用KEY返回的错误码 add jyj 2017.12.11
    public static int SWR_KEY_NotManager_ERR              = (SWR_KEY_BASE + 0x000000F);   //用户Key不是管理员
    public static int SWR_KEY_NotAdded_ERR                = (SWR_KEY_BASE + 0x0000010);   //用户Key未添加
    public static int SWR_KEY_HaveBackupComponent_ERR     = (SWR_KEY_BASE + 0x0000011);   //用户Key已获得备份分量
    public static int SWR_KEY_GainComponent_ERR           = (SWR_KEY_BASE + 0x0000012);   //获取输出密钥分量错误
    public static int SWR_KEY_UKEYWriteDataFailure_ERR    = (SWR_KEY_BASE + 0x0000013);   //用户Key写入数据失败
    public static int SWR_KEY_UKEYNotData_ERR             = (SWR_KEY_BASE + 0x0000014);   //用户Key中没有用户数据
    public static int SWR_KEY_PublicKeyCipherCard_ERR     = (SWR_KEY_BASE + 0x0000015);   //导出密码卡设备密钥对的公钥错误
    public static int SWR_UserKeyToEncryptBackupKeyCipher_ERR    = (SWR_KEY_BASE + 0x0000016);   //用户Key转加密备份密钥密文错误
    public static int SWR_KEY_ImportBackupKey_ERR         = (SWR_KEY_BASE + 0x0000017);      //导入备份密钥错误
    public static int SWCSM_UKEY_ProtocolStart_ERR        = (SWR_KEY_BASE + 0x0000018);   //登录错误
    public static int COSM_DoProtocol_ERR                 = (SWR_KEY_BASE + 0x0000019);   //登录key运算错误
    public static int SWCSM_UKEY_ProtocolEnd_ERR          = (SWR_KEY_BASE + 0x000001A);   //登录结束错误
    public static int SWR_KEY_Repeatedly_Add_MANA         = (SWR_KEY_BASE + 0x000001B);    //管理Key重复添加
    public static int SWR_KEY_Repeatedly_Add_OPER         = (SWR_KEY_BASE + 0x000001C);    //操作Key重复添加
    public static int SWR_KEY_MANA_SING                   = (SWR_KEY_BASE + 0x000001D);    //管理Key签名失败
    public static int SWR_KEY_OPER_SING                   = (SWR_KEY_BASE + 0x000001E);    //操作Key签名失败
    public static int SWR_KEY_ADD_OPER                    = (SWR_KEY_BASE + 0x000001F);    //增加操作员失败
    public static int SWR_KEY_ADD_MANG                    = (SWR_KEY_BASE + 0x0000020);    //增加管理员失败
    public static int SWR_KEY_CHANG_PIN                   = (SWR_KEY_BASE + 0x0000021);    //修改用户key的PIN口令失败
    
    public static String getErrorInfo(int errorCode){
        
        String errorInfo = "";
        
        switch (errorCode) {
			case SWR_KEY_BASE + 0x0000001:
                            errorInfo = Language.get("SWR_KEY_OpenUSBKey_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000002:
                            errorInfo = Language.get("SWR_KEY_CheckPin_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000003:
                            errorInfo = Language.get("SWR_KEY_ReadAllFile_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000004:
                            errorInfo = Language.get("SWR_KEY_CloseUSBKey_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000006:
                            errorInfo = Language.get("SWR_KEY_DoProtocol_ECC_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000007:
                            errorInfo = Language.get("SWR_KEY_GenerateECCKeyPair_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000008:
                            errorInfo = Language.get("SWR_KEY_ExportECCPublicKey_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000009:
                            errorInfo = Language.get("SWR_KEY_WriteAllFile_ERR");
                            break;
                        case SWR_KEY_BASE + 0x000000A:
                            errorInfo = Language.get("SWR_KEY_UKEY_ProtocolStart_ERR");
                            break;
                        case SWR_KEY_BASE + 0x000000B:
                            errorInfo = Language.get("SWR_KEY_UKEY_Type_ERR");
                            break;
                        case SWR_KEY_BASE + 0x000000C:
                            errorInfo = Language.get("SWR_KEY_NoUser_Type_ERR");
                            break;
                        case SWR_KEY_BASE + 0x000000D:
                            errorInfo = Language.get("SWR_KEY_PIN_LENGTH_ERR");
                            break;
                        case SWR_KEY_BASE + 0x000000E:
                            errorInfo = Language.get("SWR_KEY_UKEY_OTHER_ERR");
                            break;
                        case SWR_KEY_BASE + 0x000000F:
                            errorInfo = Language.get("SWR_KEY_NotManager_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000010:
                            errorInfo = Language.get("SWR_KEY_NotAdded_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000011:
                            errorInfo = Language.get("SWR_KEY_HaveBackupComponent_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000012:
                            errorInfo = Language.get("SWR_KEY_GainComponent_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000013:
                            errorInfo = Language.get("SWR_KEY_UKEYWriteDataFailure_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000014:
                            errorInfo = Language.get("SWR_KEY_UKEYNotData_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000015:
                            errorInfo = Language.get("SWR_KEY_PublicKeyCipherCard_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000016:
                            errorInfo = Language.get("SWR_UserKeyToEncryptBackupKeyCipher_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000017:
                            errorInfo = Language.get("SWR_KEY_ImportBackupKey_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000018:
                            errorInfo = Language.get("SWCSM_UKEY_ProtocolStart_ERR");
                            break;
                        case SWR_KEY_BASE + 0x0000019:
                            errorInfo = Language.get("COSM_DoProtocol_ERR");
                            break;
                        case SWR_KEY_BASE + 0x000001A:
                            errorInfo = Language.get("SWCSM_UKEY_ProtocolEnd_ERR");
                            break;
                        case SWR_KEY_BASE + 0x000001B:
                            errorInfo = Language.get("SWR_KEY_Repeatedly_Add_MANA");
                            break;
                        case SWR_KEY_BASE + 0x000001C:
                            errorInfo = Language.get("SWR_KEY_Repeatedly_Add_OPER");
                            break;
                        case SWR_KEY_BASE + 0x000001D:
                            errorInfo = Language.get("SWR_KEY_MANA_SING");
                            break;
                        case SWR_KEY_BASE + 0x000001E:
                            errorInfo = Language.get("SWR_KEY_OPER_SING");
                            break;
                        case SWR_KEY_BASE + 0x000001F:
                            errorInfo = Language.get("SWR_KEY_ADD_OPER");
                            break;
                        case SWR_KEY_BASE + 0x0000020:
                            errorInfo = Language.get("SWR_KEY_ADD_MANG");
                            break;
                        case SWR_KEY_BASE + 0x0000021:
                            errorInfo = Language.get("SWR_KEY_CHANG_PIN");
                            break; 
                        default:
                            errorInfo = Language.get("SWR_KEY_UKEY_OTHER_ERR");
                            break;
        }
        
        return errorInfo;
    }    
}
