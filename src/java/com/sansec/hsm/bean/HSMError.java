package com.sansec.hsm.bean;

import java.util.HashMap;
import java.util.Map;

public class HSMError {

    public static final int SDR_OK = 0x0;                          /*成功*/

    public static final int SDR_BASE = 0x01000000;
    public static final int SDR_UNKNOWERR = (SDR_BASE + 0x00000001);      /*未知错误*/

    public static final int SDR_NOTSUPPORT = (SDR_BASE + 0x00000002);      /*不支持*/

    public static final int SDR_COMMFAIL = (SDR_BASE + 0x00000003);    /*通信错误*/

    public static final int SDR_HARDFAIL = (SDR_BASE + 0x00000004);    /*硬件错误*/

    public static final int SDR_OPENDEVICE = (SDR_BASE + 0x00000005);    /*打开设备错误*/

    public static final int SDR_OPENSESSION = (SDR_BASE + 0x00000006);    /*打开会话句柄错误*/

    public static final int SDR_PARDENY = (SDR_BASE + 0x00000007);    /*权限不满足*/

    public static final int SDR_KEYNOTEXIST = (SDR_BASE + 0x00000008);    /*密钥不存在*/

    public static final int SDR_ALGNOTSUPPORT = (SDR_BASE + 0x00000009);    /*不支持的算法*/

    public static final int SDR_ALGMODNOTSUPPORT = (SDR_BASE + 0x0000000A);    /*不支持的算法模式*/

    public static final int SDR_PKOPERR = (SDR_BASE + 0x0000000B);    /*公钥运算错误*/

    public static final int SDR_SKOPERR = (SDR_BASE + 0x0000000C);    /*私钥运算错误*/

    public static final int SDR_SIGNERR = (SDR_BASE + 0x0000000D);    /*签名错误*/

    public static final int SDR_VERIFYERR = (SDR_BASE + 0x0000000E);    /*验证错误*/

    public static final int SDR_SYMOPERR = (SDR_BASE + 0x0000000F);    /*对称运算错误*/

    public static final int SDR_STEPERR = (SDR_BASE + 0x00000010);    /*步骤错误*/

    public static final int SDR_FILESIZEERR = (SDR_BASE + 0x00000011);    /*文件大小错误*/

    public static final int SDR_FILENOEXIST = (SDR_BASE + 0x00000012);    /*文件不存在*/

    public static final int SDR_FILEOFSERR = (SDR_BASE + 0x00000013);    /*文件操作偏移量错误*/

    public static final int SDR_KEYTYPEERR = (SDR_BASE + 0x00000014);    /*密钥类型错误*/

    public static final int SDR_KEYERR = (SDR_BASE + 0x00000015);    /*密钥错误*/

    /*扩展错误码*/
    public static final int SWR_BASE = (SDR_BASE + 0x00010000);      /*自定义错误码基础值*/

    public static final int SWR_INVALID_USER = (SWR_BASE + 0x00000001);      /*无效的用户名*/

    public static final int SWR_INVALID_AUTHENCODE = (SWR_BASE + 0x00000002);      /*无效的授权码*/

    public static final int SWR_PROTOCOL_VER_ERR = (SWR_BASE + 0x00000003);      /*不支持的协议版本*/

    public static final int SWR_INVALID_COMMAND = (SWR_BASE + 0x00000004);      /*错误的命令字*/

    public static final int SWR_INVALID_PACKAGE = (SWR_BASE + 0x00000005);      /*错误的数据包格式*/

    public static final int SWR_INVALID_PARAMETERS = (SWR_BASE + 0x00000005);      /*参数错误*/

    public static final int SWR_FILE_ALREADY_EXIST = (SWR_BASE + 0x00000006);    /*已存在同名文件*/

    public static final int SWR_SOCKET_ERR_MASK = 0xFFFFFF00;    /*用于检查是否是SOCKET错误*/

    public static final int SWR_SOCKET_ERR_BASE = (SWR_BASE + 0x00000100);    /*用于检查是否是SOCKET错误*/

    public static final int SWR_SOCKET_TIMEOUT = (SWR_BASE + 0x00000100);    /*超时错误*/

    public static final int SWR_CONNECT_ERR = (SWR_BASE + 0x00000101);    /*连接服务器错误*/

    public static final int SWR_SET_SOCKOPT_ERR = (SWR_BASE + 0x00000102);    /*设置Socket参数错误*/

    public static final int SWR_SOCKET_SEND_ERR = (SWR_BASE + 0x00000104);    /*发送LOGINRequest错误*/

    public static final int SWR_SOCKET_RECV_ERR = (SWR_BASE + 0x00000105);    /*发送LOGINRequest错误*/

    public static final int SWR_SOCKET_RECV_0 = (SWR_BASE + 0x00000106);    /*发送LOGINRequest错误*/

    public static final int SWR_SEM_TIMEOUT = (SWR_BASE + 0x00000200);    /*超时错误*/

    public static final int SWR_NO_AVAILABLE_HSM = (SWR_BASE + 0x00000201);    /*没有可用的加密机*/

    public static final int SWR_NO_AVAILABLE_CSM = (SWR_BASE + 0x00000202);    /*加密机内没有可用的加密模块*/

    public static final int SWR_CONFIG_ERR = (SWR_BASE + 0x00000301);    /*配置文件错误*/

    public static final int SWR_CARD_BASE = (SDR_BASE + 0x00020000);    /*密码卡错误码*/

    public static final int SDR_BUFFER_TOO_SMALL = (SWR_CARD_BASE + 0x00000101); /*接收参数的缓存区太小*/

    public static final int SDR_DATA_PAD = (SWR_CARD_BASE + 0x00000102); /*数据没有按正确格式填充，或解密得到的脱密数据不符合填充格式*/

    public static final int SDR_DATA_SIZE = (SWR_CARD_BASE + 0x00000103); /*明文或密文长度不符合相应的算法要求*/

    public static final int SDR_CRYPTO_NOT_INIT = (SWR_CARD_BASE + 0x00000104); /*步骤错误*/

    public static final int SWR_MANAGEMENT_DENY = (SWR_CARD_BASE + 0x00001001);    //管理权限不满足
    public static final int SWR_OPERATION_DENY = (SWR_CARD_BASE + 0x00001002);  //操作权限不满足
    public static final int SWR_DEVICE_STATUS_ERR = (SWR_CARD_BASE + 0x00001003);  //当前设备状态不满足现有操作
    public static final int SWR_LOGIN_ERR = (SWR_CARD_BASE + 0x00001011);  //登录失败
    public static final int SWR_USERID_ERR = (SWR_CARD_BASE + 0x00001012);  //用户ID数目/号码错误
    public static final int SWR_PARAMENT_ERR = (SWR_CARD_BASE + 0x00001013); //参数错误
    public static final int SWR_KEYTYPEERR = (SWR_CARD_BASE + 0x00000020);    //密钥类型错误
    public static final int SWR_PIN_ERR_e = (SWR_CARD_BASE + 0x000163ce);
    public static final int SWR_PIN_ERR_d = (SWR_CARD_BASE + 0x000163cd);
    public static final int SWR_PIN_ERR_c = (SWR_CARD_BASE + 0x000163cc);
    public static final int SWR_PIN_ERR_b = (SWR_CARD_BASE + 0x000163cb);
    public static final int SWR_PIN_ERR_a = (SWR_CARD_BASE + 0x000163ca);
    public static final int SWR_PIN_ERR_9 = (SWR_CARD_BASE + 0x000163c9);
    public static final int SWR_PIN_ERR_8 = (SWR_CARD_BASE + 0x000163c8);
    public static final int SWR_PIN_ERR_7 = (SWR_CARD_BASE + 0x000163c7);
    public static final int SWR_PIN_ERR_6 = (SWR_CARD_BASE + 0x000163c6);
    public static final int SWR_PIN_ERR_5 = (SWR_CARD_BASE + 0x000163c5);
    public static final int SWR_PIN_ERR_4 = (SWR_CARD_BASE + 0x000163c4);
    public static final int SWR_PIN_ERR_3 = (SWR_CARD_BASE + 0x000163c3);
    public static final int SWR_PIN_ERR_2 = (SWR_CARD_BASE + 0x000163c2);
    public static final int SWR_PIN_ERR_1 = (SWR_CARD_BASE + 0x000163c1);
    public static final int SWR_PIN_ERR_0 = (SWR_CARD_BASE + 0x000163c0);
    public static final int SWR_IC_LOCKED = (SWR_CARD_BASE + 0x00016983);
    public static final int SWR_IC_TYPE_ERR = (SWR_CARD_BASE + 0x0001ff01);
    public static final int SWR_IC_ERR_3 = (SWR_CARD_BASE + 0x0001ff03);
    public static final int SWR_IC_ERR_4 = (SWR_CARD_BASE + 0x0001ff04);
    public static Map<Integer, String> errors = new HashMap<Integer, String>();

    static {
        errors.put(SDR_OK, "成功");
        errors.put(SDR_UNKNOWERR, "未知错误");
        errors.put(SDR_NOTSUPPORT, "不支持");
        errors.put(SDR_COMMFAIL, "通信错误");
        errors.put(SDR_HARDFAIL, "硬件错误");
        errors.put(SDR_OPENDEVICE, "打开设备错误");
        errors.put(SDR_OPENSESSION, "打开会话句柄错误");
        errors.put(SDR_PARDENY, "权限不满足");
        errors.put(SDR_KEYNOTEXIST, "密钥不存在");
        errors.put(SDR_ALGNOTSUPPORT, "算法不支持");
        errors.put(SDR_ALGMODNOTSUPPORT, "算法模式不支持");
        errors.put(SDR_PKOPERR, "公钥运算错误");
        errors.put(SDR_SKOPERR, "私钥运算错误");
        errors.put(SDR_SIGNERR, "签名错误");
        errors.put(SDR_VERIFYERR, "验证错误");
        errors.put(SDR_SYMOPERR, "对称运算错误");
        errors.put(SDR_STEPERR, "步骤错误");
        errors.put(SDR_FILESIZEERR, "文件大小错误");
        errors.put(SDR_FILENOEXIST, "文件不存在");
        errors.put(SDR_FILEOFSERR, "文件操作偏移量错误");
        errors.put(SDR_KEYTYPEERR, "密钥类型错误");
        errors.put(SDR_KEYERR, "密钥错误");

        errors.put(SWR_INVALID_USER, "无效的用户名");
        errors.put(SWR_INVALID_AUTHENCODE, "无效的授权码");
        errors.put(SWR_PROTOCOL_VER_ERR, "不支持的协议版本");
        errors.put(SWR_INVALID_COMMAND, "错误的命令字");
        errors.put(SWR_INVALID_PACKAGE, "错误的数据包格式");
        errors.put(SWR_INVALID_PARAMETERS, "参数错误");
        errors.put(SWR_FILE_ALREADY_EXIST, "已存在同名文件");

        errors.put(SWR_SOCKET_ERR_MASK, "用于检查是否是SOCKET错误");
        errors.put(SWR_SOCKET_ERR_BASE, "用于检查是否是SOCKET错误");
        errors.put(SWR_SOCKET_TIMEOUT, "超时错误");
        errors.put(SWR_CONNECT_ERR, "连接服务器错误");
        errors.put(SWR_SET_SOCKOPT_ERR, "设置Socket参数错误");
        errors.put(SWR_SOCKET_SEND_ERR, "发送LOGINRequest错误");
        errors.put(SWR_SOCKET_RECV_ERR, "发送LOGINRequest错误");
        errors.put(SWR_SOCKET_RECV_0, "发送LOGINRequest错误");

        errors.put(SWR_SEM_TIMEOUT, "超时错误");
        errors.put(SWR_NO_AVAILABLE_HSM, "没有可用的加密机");
        errors.put(SWR_NO_AVAILABLE_CSM, "加密机内没有可用的加密模块");

        errors.put(SWR_CONFIG_ERR, "配置文件错误");

        errors.put(SDR_BUFFER_TOO_SMALL, "接收参数的缓存区太小");
        errors.put(SDR_DATA_PAD, "数据没有按正确格式填充，或解密得到的脱密数据不符合填充格式");
        errors.put(SDR_DATA_SIZE, "明文或密文长度不符合相应的算法要求");
        errors.put(SDR_CRYPTO_NOT_INIT, "步骤错误");

        errors.put(SWR_MANAGEMENT_DENY, "管理权限不满足");
        errors.put(SWR_OPERATION_DENY, "操作权限不满足");
        errors.put(SWR_DEVICE_STATUS_ERR, "当前设备状态不满足现有操作");

        errors.put(SWR_LOGIN_ERR, "非本机管理员或操作员IC卡");
        errors.put(SWR_USERID_ERR, "用户ID数目/号码错误");
        errors.put(SWR_PARAMENT_ERR, "参数错误");
        errors.put(SWR_KEYTYPEERR, "密钥类型错误");
        errors.put(SWR_IC_LOCKED, "IC卡已锁，请联系厂商解锁或更换");
        errors.put(SWR_IC_TYPE_ERR, "没有正确插入IC卡或IC卡类型错误");
        errors.put(SWR_IC_ERR_3, "IC卡或读卡器故障");
        errors.put(SWR_IC_ERR_4, "IC卡或读卡器故障");
        errors.put(SWR_PIN_ERR_0, "IC卡PIN口令错误，还可重试 0 次");
        errors.put(SWR_PIN_ERR_1, "IC卡PIN口令错误，还可重试 1 次");
        errors.put(SWR_PIN_ERR_2, "IC卡PIN口令错误，还可重试 2 次");
        errors.put(SWR_PIN_ERR_3, "IC卡PIN口令错误，还可重试 3 次");
        errors.put(SWR_PIN_ERR_4, "IC卡PIN口令错误，还可重试 4 次");
        errors.put(SWR_PIN_ERR_5, "IC卡PIN口令错误，还可重试 5 次");
        errors.put(SWR_PIN_ERR_6, "IC卡PIN口令错误，还可重试 6 次");
        errors.put(SWR_PIN_ERR_7, "IC卡PIN口令错误，还可重试 7 次");
        errors.put(SWR_PIN_ERR_8, "IC卡PIN口令错误，还可重试 8 次");
        errors.put(SWR_PIN_ERR_9, "IC卡PIN口令错误，还可重试 9 次");
        errors.put(SWR_PIN_ERR_a, "IC卡PIN口令错误，还可重试 10 次");
        errors.put(SWR_PIN_ERR_b, "IC卡PIN口令错误，还可重试 11 次");
        errors.put(SWR_PIN_ERR_c, "IC卡PIN口令错误，还可重试 12 次");
        errors.put(SWR_PIN_ERR_d, "IC卡PIN口令错误，还可重试 13 次");
        errors.put(SWR_PIN_ERR_e, "IC卡PIN口令错误，还可重试 14 次");
    }

    public static Map<Integer, String> errors_en = new HashMap<Integer, String>();

    static {
        errors_en.put(SDR_OK, "success");
        errors_en.put(SDR_UNKNOWERR, "unkown error");
        errors_en.put(SDR_NOTSUPPORT, "not support");
        errors_en.put(SDR_COMMFAIL, "communication error");
        errors_en.put(SDR_HARDFAIL, "hsm error");
        errors_en.put(SDR_OPENDEVICE, "open device error");
        errors_en.put(SDR_OPENSESSION, "open session error");
        errors_en.put(SDR_PARDENY, "Permissions are not met");
        errors_en.put(SDR_KEYNOTEXIST, "Key not exist");
        errors_en.put(SDR_ALGNOTSUPPORT, "alg not support");
        errors_en.put(SDR_ALGMODNOTSUPPORT, "alg mode not support");
        errors_en.put(SDR_PKOPERR, "public key error");
        errors_en.put(SDR_SKOPERR, "private key error");
        errors_en.put(SDR_SIGNERR, "sign error");
        errors_en.put(SDR_VERIFYERR, "verify error");
        errors_en.put(SDR_SYMOPERR, "symm error");
        errors_en.put(SDR_STEPERR, "step error");
        errors_en.put(SDR_FILESIZEERR, "file size error");
        errors_en.put(SDR_FILENOEXIST, "file not exist");
        errors_en.put(SDR_FILEOFSERR, "file offset error");
        errors_en.put(SDR_KEYTYPEERR, "key type error");
        errors_en.put(SDR_KEYERR, "key error");

        errors_en.put(SWR_INVALID_USER, "invalid user");
        errors_en.put(SWR_INVALID_AUTHENCODE, "invalid auth code");
        errors_en.put(SWR_PROTOCOL_VER_ERR, "protocol error");
        errors_en.put(SWR_INVALID_COMMAND, "invalid command");
        errors_en.put(SWR_INVALID_PACKAGE, "invalid package");
        errors_en.put(SWR_INVALID_PARAMETERS, "invalid parameters");
        errors_en.put(SWR_FILE_ALREADY_EXIST, "file already exist");

        errors_en.put(SWR_SOCKET_ERR_MASK, "sokcet mask error");
        errors_en.put(SWR_SOCKET_ERR_BASE, "sokcet error base");
        errors_en.put(SWR_SOCKET_TIMEOUT, "timeout");
        errors_en.put(SWR_CONNECT_ERR, "connect error");
        errors_en.put(SWR_SET_SOCKOPT_ERR, "set socket error");
        errors_en.put(SWR_SOCKET_SEND_ERR, "send LOGINRequest error");
        errors_en.put(SWR_SOCKET_RECV_ERR, "recv LOGINRequest error");
        errors_en.put(SWR_SOCKET_RECV_0, "socket recv 0");

        errors_en.put(SWR_SEM_TIMEOUT, "timeout");
        errors_en.put(SWR_NO_AVAILABLE_HSM, "no available hsm");
        errors_en.put(SWR_NO_AVAILABLE_CSM, "no available csm");

        errors_en.put(SWR_CONFIG_ERR, "config error");

        errors_en.put(SDR_BUFFER_TOO_SMALL, "buffer too small");
        errors_en.put(SDR_DATA_PAD, "pad error");
        errors_en.put(SDR_DATA_SIZE, "length error");
        errors_en.put(SDR_CRYPTO_NOT_INIT, "step error");

        errors_en.put(SWR_MANAGEMENT_DENY, "Permissions are not met");
        errors_en.put(SWR_OPERATION_DENY, "Permissions are not met");
        errors_en.put(SWR_DEVICE_STATUS_ERR, "Permissions are not met");

        errors_en.put(SWR_LOGIN_ERR, "login error");
        errors_en.put(SWR_USERID_ERR, "user id error");
        errors_en.put(SWR_PARAMENT_ERR, "parameter error");
        errors_en.put(SWR_KEYTYPEERR, "key type error");
        errors_en.put(SWR_IC_LOCKED, "IC card Locked，Please contact manufacturer");
        errors_en.put(SWR_IC_TYPE_ERR, "IC type error");
        errors_en.put(SWR_IC_ERR_3, "IC card or Reader error");
        errors_en.put(SWR_IC_ERR_4, "IC card or Reader error");
        errors_en.put(SWR_PIN_ERR_0, "IC card PIN error，Also try again 0 times");
        errors_en.put(SWR_PIN_ERR_1, "IC card PIN error，Also try again 1 times");
        errors_en.put(SWR_PIN_ERR_2, "IC card PIN error，Also try again 2 times");
        errors_en.put(SWR_PIN_ERR_3, "IC card PIN error，Also try again 3 times");
        errors_en.put(SWR_PIN_ERR_4, "IC card PIN error，Also try again 4 times");
        errors_en.put(SWR_PIN_ERR_5, "IC card PIN error，Also try again 5 times");
        errors_en.put(SWR_PIN_ERR_6, "IC card PIN error，Also try again 6 times");
        errors_en.put(SWR_PIN_ERR_7, "IC card PIN error，Also try again 7 times");
        errors_en.put(SWR_PIN_ERR_8, "IC card PIN error，Also try again 8 times");
        errors_en.put(SWR_PIN_ERR_9, "IC card PIN error，Also try again 9 times");
        errors_en.put(SWR_PIN_ERR_a, "IC card PIN error，Also try again 10 times");
        errors_en.put(SWR_PIN_ERR_b, "IC card PIN error，Also try again 11 times");
        errors_en.put(SWR_PIN_ERR_c, "IC card PIN error，Also try again 12 times");
        errors_en.put(SWR_PIN_ERR_d, "IC card PIN error，Also try again 13 times");
        errors_en.put(SWR_PIN_ERR_e, "IC card PIN error，Also try again 14 times");
    }

    public static String getErrorInfo(int rv) {
        String info = "";
        System.out.println(rv);
        String lan = Config.getLanguage();
        if (lan.equalsIgnoreCase(Language.LANGUAGE_EN)) {
            if (errors_en.containsKey(rv)) {
                info = errors_en.get(rv);
            } else {
                try {
                    info = Enumerate.getErrorInfo(rv) + "! errcode:" + "[ 0x" + Integer.toHexString(rv) + " ]";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (errors.containsKey(rv)) {
                info = errors.get(rv);
            } else {
                info = "未知错误码";
                info += "[ 0x" + Integer.toHexString(rv) + " ]";
            }
        }

        return info;
    }
}
