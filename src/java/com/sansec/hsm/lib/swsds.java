package com.sansec.hsm.lib;

import com.sansec.hsm.bean.GlobalData;
import com.sansec.hsm.util.ByteUtil;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;
import java.util.List;

public interface swsds extends Library {

    swsds INSTANCE = (swsds) Native.loadLibrary("swsds", swsds.class);

    /*设备管理类函数*/
    int SDF_OpenDevice(PointerByReference phDeviceHandle);

    int SDF_CloseDevice(Pointer hDeviceHandle);

    int SDF_OpenSession(Pointer hDeviceHandle, PointerByReference phSessionHandle);

    int SDF_CloseSession(Pointer hSessionHandle);

    /*设备信息*/
    public static class DEVICEINFO extends Structure {

        public byte[] IssuerName = new byte[40];
        public byte[] DeviceName = new byte[16];
        public byte[] DeviceSerial = new byte[16];
        public int DeviceVersion;
        public int StandardVersion;
        public int[] AsymAlgAbility = new int[2];
        public int SymAlgAbility;
        public int HashAlgAbility;
        public int BufferSize;

        public static class ByReference extends DEVICEINFO
                implements Structure.ByReference {
        }

        public static class ByValue extends DEVICEINFO
                implements Structure.ByValue {
        }
    }

    int SDF_GetDeviceInfo(Pointer hSessionHandle, DEVICEINFO.ByReference pstDeviceInfo);

    int SDF_GenerateRandom(Pointer hSessionHandle, int uiLength, byte[] pucRandom);

    int SDF_GetPrivateKeyAccessRight(Pointer hSessionHandle, int uiKeyIndex, byte[] pucPassword, int uiPwdLength);

    int SDF_ReleasePrivateKeyAccessRight(Pointer hSessionHandle, int uiKeyIndex);

    /*密钥管理类函数*/
    public static class RSArefPublicKey extends Structure {

        public int bits;
        public byte[] m = new byte[GlobalData.ExRSAref_MAX_LEN];
        public byte[] e = new byte[GlobalData.ExRSAref_MAX_LEN];
        
        public static class ByReference extends RSArefPublicKey
                implements Structure.ByReference {
        }

        public static class ByValue extends RSArefPublicKey
                implements Structure.ByValue {
        }
        /*public byte[] toByte() {
        int rsalen = GlobalData.ExRSAref_MAX_LEN;
        int size = 4 + rsalen * 2;
        byte[] tmp = new byte[size];
        byte[] bit = ByteUtil.int2bytes(bits);
        System.arraycopy(bit, 0, tmp, 0, 4);
        System.arraycopy(m, 0, tmp, 4, rsalen);
        System.arraycopy(e, 0, tmp, 4 + rsalen, rsalen * 2);
        return tmp;
        }*/
    }

    public static class RSArefPrivateKey extends Structure {

        public int bits;                                               //4
        public byte[] m = new byte[GlobalData.ExRSAref_MAX_LEN];            //512
        public byte[] e = new byte[GlobalData.ExRSAref_MAX_LEN];            //512
        public byte[] d = new byte[GlobalData.ExRSAref_MAX_LEN];            //512
        public byte[] prime = new byte[2 * GlobalData.ExRSAref_MAX_PLEN];   //512
        public byte[] pexp = new byte[2 * GlobalData.ExRSAref_MAX_PLEN];    //512
        public byte[] coef = new byte[GlobalData.ExRSAref_MAX_PLEN];        //128

        @Override
        protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "bits", "m", "e", "d", "prime", "pexp", "coef" });
		}
        public static class ByReference extends RSArefPrivateKey
                implements Structure.ByReference {
        }

        public static class ByValue extends RSArefPrivateKey
                implements Structure.ByValue {
        }

        public byte[] toByte() {
            int rsalen = GlobalData.ExRSAref_MAX_LEN;
            int rsaplen = GlobalData.ExRSAref_MAX_PLEN;
            int size = 4 + rsalen * 3 + rsaplen * 5;
            byte[] tmp = new byte[size];
            byte[] bit = ByteUtil.int2bytes(bits);
            //bit = ByteUtil.int2bytes(ByteUtil.bytes2intReverse(bit));
            System.arraycopy(bit, 0, tmp, 0, 4);
            System.arraycopy(m, 0, tmp, 4, rsalen);
            System.arraycopy(e, 0, tmp, 4 + rsalen, rsalen);
            System.arraycopy(d, 0, tmp, 4 + rsalen * 2, rsalen);
            System.arraycopy(prime, 0, tmp, 4 + rsalen * 3, rsaplen * 2);
            System.arraycopy(pexp, 0, tmp, 4 + rsalen * 3 + rsaplen * 2, rsaplen * 2);
            System.arraycopy(coef, 0, tmp, 4 + rsalen * 3 + rsaplen * 4, rsaplen);
            
            return tmp;
        }
    }

    public static class RSArefPublicKeyLite extends Structure {

        public int bits;
        public byte[] m = new byte[GlobalData.LiteRSAref_MAX_LEN];
        public byte[] e = new byte[GlobalData.LiteRSAref_MAX_LEN];

        public static class ByReference extends RSArefPublicKeyLite
                implements Structure.ByReference {
        }

        public static class ByValue extends RSArefPublicKeyLite
                implements Structure.ByValue {
        }
        /*public byte[] toByte() {
        int lrsalen = GlobalData.LiteRSAref_MAX_LEN;
        int size = 4 + lrsalen * 2;
        byte[] tmp = new byte[size];
        byte[] bit = ByteUtil.int2bytes(bits);
        System.arraycopy(bit, 0, tmp, 0, 4);
        System.arraycopy(m, 0, tmp, 4, lrsalen);
        System.arraycopy(e, 0, tmp, 4 + lrsalen, lrsalen * 2);
        return tmp;
        }*/
    }

    public static class RSArefPrivateKeyLite extends Structure {

        public int bits;
        public byte[] m = new byte[GlobalData.LiteRSAref_MAX_LEN];              //256
        public byte[] e = new byte[GlobalData.LiteRSAref_MAX_LEN];              //256
        public byte[] d = new byte[GlobalData.LiteRSAref_MAX_LEN];              //256
        public byte[] prime = new byte[2 * GlobalData.LiteRSAref_MAX_PLEN];     //1024
        public byte[] pexp = new byte[2 * GlobalData.LiteRSAref_MAX_PLEN];      //1024
        public byte[] coef = new byte[GlobalData.LiteRSAref_MAX_PLEN];          //256

        public static class ByReference extends RSArefPrivateKeyLite
                implements Structure.ByReference {
        }

        public static class ByValue extends RSArefPrivateKeyLite
                implements Structure.ByValue {
        }
        /*public byte[] toByte() {
        int lrsalen = GlobalData.LiteRSAref_MAX_LEN;
        int lrsaplen = GlobalData.LiteRSAref_MAX_PLEN;
        int size = 4 + lrsalen * 3 + lrsaplen * 5;
        byte[] tmp = new byte[size];
        byte[] bit = ByteUtil.int2bytes(bits);
        System.arraycopy(bit, 0, tmp, 0, 4);
        System.arraycopy(m, 0, tmp, 4, lrsalen);
        System.arraycopy(e, 0, tmp, 4 + lrsalen, lrsalen);
        System.arraycopy(d, 0, tmp, 4 + lrsalen * 2, lrsalen);
        System.arraycopy(prime, 0, tmp, 4 + lrsalen * 3, lrsaplen * 2);
        System.arraycopy(pexp, 0, tmp, 4 + lrsalen * 3 + lrsaplen * 2, lrsaplen * 2);
        System.arraycopy(coef, 0, tmp, 4 + lrsalen * 3 + lrsaplen * 4, lrsaplen);
        return tmp;
        }*/
    }

    public static class ECCrefPublicKey extends Structure {

        public int bits;
        public byte[] x = new byte[GlobalData.ECCref_MAX_LEN];
        public byte[] y = new byte[GlobalData.ECCref_MAX_LEN];

        public static class ByReference extends ECCrefPublicKey
                implements Structure.ByReference {
        }

        public static class ByValue extends ECCrefPublicKey
                implements Structure.ByValue {
        }

        public byte[] toByte() {
            int ecclen = GlobalData.ECCref_MAX_LEN;
            int size = 4 + ecclen * 2;
            byte[] tmp = new byte[size];
            byte[] bit = ByteUtil.int2bytes(bits);
            //bit = ByteUtil.int2bytes(ByteUtil.bytes2intReverse(bit));
            System.arraycopy(bit, 0, tmp, 0, 4);
            System.arraycopy(x, 0, tmp, 4, ecclen);
            System.arraycopy(y, 0, tmp, 4 + ecclen, ecclen);
            return tmp;
        }
    }

    public static class ECCrefPrivateKey extends Structure {

        public int bits;
        public byte[] D = new byte[GlobalData.ECCref_MAX_LEN];

        public static class ByReference extends ECCrefPrivateKey
                implements Structure.ByReference {
        }

        public static class ByValue extends ECCrefPrivateKey
                implements Structure.ByValue {
        }

        public byte[] toByte() {
            int ecclen = GlobalData.ECCref_MAX_LEN;
            int size = 4 + ecclen;
            byte[] tmp = new byte[size];
            byte[] bit = ByteUtil.int2bytes(bits);
            //bit = ByteUtil.int2bytes(ByteUtil.bytes2intReverse(bit));
            System.arraycopy(bit, 0, tmp, 0, 4);
            System.arraycopy(D, 0, tmp, 4, ecclen);
            return tmp;
        }
    }

        public static class ECCCipher extends Structure {

        public int clength;
        public byte[] x = new byte[GlobalData.ECCref_MAX_LEN];
        public byte[] y = new byte[GlobalData.ECCref_MAX_LEN];
        public byte[] C = new byte[GlobalData.ECCref_MAX_CIPHER_LEN];
        public byte[] M = new byte[GlobalData.ECCref_MAX_LEN];

        public static class ByReference extends ECCrefPublicKey
                implements Structure.ByReference {
        }

        public static class ByValue extends ECCrefPublicKey
                implements Structure.ByValue {
        }
//
//        public byte[] toByte() {
//            int ecclen = GlobalData.ECCref_MAX_LEN;
//            int size = 4 + ecclen * 2;
//            byte[] tmp = new byte[size];
//            byte[] bit = ByteUtil.int2bytes(bits);
//            //bit = ByteUtil.int2bytes(ByteUtil.bytes2intReverse(bit));
//            System.arraycopy(bit, 0, tmp, 0, 4);
//            System.arraycopy(x, 0, tmp, 4, ecclen);
//            System.arraycopy(y, 0, tmp, 4 + ecclen, ecclen);
//            return tmp;
//        }
    }
    
       
        public static class ECCSignature extends Structure {

        public int clength;
        public byte[] r = new byte[GlobalData.ECCref_MAX_LEN];
        public byte[] s = new byte[GlobalData.ECCref_MAX_LEN];

        public static class ByReference extends ECCrefPublicKey
                implements Structure.ByReference {
        }

        public static class ByValue extends ECCrefPublicKey
                implements Structure.ByValue {
        }
//
//        public byte[] toByte() {
//            int ecclen = GlobalData.ECCref_MAX_LEN;
//            int size = 4 + ecclen * 2;
//            byte[] tmp = new byte[size];
//            byte[] bit = ByteUtil.int2bytes(bits);
//            //bit = ByteUtil.int2bytes(ByteUtil.bytes2intReverse(bit));
//            System.arraycopy(bit, 0, tmp, 0, 4);
//            System.arraycopy(x, 0, tmp, 4, ecclen);
//            System.arraycopy(y, 0, tmp, 4 + ecclen, ecclen);
//            return tmp;
//        }
    }
        
    int SDF_ExportSignPublicKey_RSA(Pointer hSessionHandle, int uiKeyIndex, RSArefPublicKey.ByReference pucPublicKey);

    int SDF_ExportEncPublicKey_RSA(Pointer hSessionHandle, int uiKeyIndex, RSArefPublicKey.ByReference pucPublicKey);

    int SDF_GenerateKeyPair_RSA(Pointer hSessionHandle, int uiKeyBits, RSArefPublicKey.ByReference pucPublicKey, RSArefPrivateKey.ByReference pucPrivateKey);

    int SDF_GenerateKeyWithIPK_RSA(Pointer hSessionHandle, int uiIPKIndex, int uiKeyBits, String pucKey, IntByReference puiKeyLength, PointerByReference phKeyHandle);

    int SDF_GenerateKeyWithEPK_RSA(Pointer hSessionHandle, int uiKeyBits, RSArefPublicKey.ByReference pucPublicKey, String pucKey, IntByReference puiKeyLength, PointerByReference phKeyHandle);

    int SDF_ImportKeyWithISK_RSA(Pointer hSessionHandle, int uiISKIndex, byte[] pucKey, int uiKeyLength, PointerByReference phKeyHandle);
    // add by chenming
    // 2011-11-16
    


    int SDF_ExchangeDigitEnvelopeBaseOnRSA(Pointer hSessionHandle, int uiKeyIndex, RSArefPublicKey.ByReference pucPublicKey, String pucDEInput, int uiDELength, String pucDEOutput, IntByReference puiDELength);

    int SDF_GenerateKeyWithKEK(Pointer hSessionHandle, int uiKeyBits, int uiAlgID, int uiKEKIndex, String pucKey, IntByReference puiKeyLength, PointerByReference phKeyHandle);

    int SDF_ImportKeyWithKEK(Pointer hSessionHandle, int uiAlgID, int uiKEKIndex, String pucKey, int uiKeyLength, PointerByReference phKeyHandle);

    int SDF_ImportKey(Pointer hSessionHandle, String pucKey, int uiKeyLength, PointerByReference phKeyHandle);

    int SDF_DestroyKey(Pointer hSessionHandle, Pointer hKeyHandle);


    /*非对称密码运算函数*/
    int SDF_ExternalPublicKeyOperation_RSA(Pointer hSessionHandle, RSArefPublicKey.ByReference pucPublicKey, String pucDataInput, int uiInputLength, String pucDataOutput, IntByReference puiOutputLength);

    int SDF_ExternalPrivateKeyOperation_RSA(Pointer hSessionHandle, RSArefPrivateKey.ByReference pucPrivateKey, String pucDataInput, int uiInputLength, String pucDataOutput, IntByReference puiOutputLength);

    int SDF_InternalPublicKeyOperation_RSA(Pointer hSessionHandle, int uiKeyIndex, int uiKeyUsage, String pucDataInput, int uiInputLength, String pucDataOutput, IntByReference puiOutputLength);

    int SDF_InternalPrivateKeyOperation_RSA(Pointer hSessionHandle, int uiKeyIndex, int uiKeyUsage, String pucDataInput, int uiInputLength, String pucDataOutput, IntByReference puiOutputLength);

    /*对称密码运算函数*/
    int SDF_Encrypt(Pointer hSessionHandle,
            Pointer hKeyHandle,
            int uiAlgID,
            ByteByReference pucIV,
            byte[] pucData,
            int uiDataLength,
            byte[] pucEncData,
            IntByReference puiEncDataLength);

    int SDF_Decrypt(Pointer hSessionHandle, Pointer hKeyHandle, int uiAlgID, byte[] pucIV, byte[] pucEncData, int uiEncDataLength, byte[] pucData, IntByReference puiDataLength);

    int SDF_CalculateMAC(Pointer hSessionHandle, Pointer hKeyHandle, int uiAlgID, String pucIV, String pucData, int uiDataLength, String pucMAC, IntByReference puiMACLength);

    /*杂凑运算函数*/
    int SDF_HashInit(Pointer hSessionHandle, int uiAlgID, ECCrefPublicKey.ByReference pucPublicKey, String pucID, int uiIDLength);

    int SDF_HashUpdate(Pointer hSessionHandle, String pucData, int uiDataLength);

    int SDF_HashFinal(Pointer hSessionHandle, String pucHash, IntByReference puiHashLength);

    /*用户文件操作函数*/
    int SDF_CreateFile(Pointer hSessionHandle, String pucFileName, int uiNameLen, int uiFileSize);

    int SDF_ReadFile(Pointer hSessionHandle, String pucFileName, int uiNameLen, int uiOffset, IntByReference puiReadLength, String pucBuffer);

    int SDF_WriteFile(Pointer hSessionHandle, String pucFileName, int uiNameLen, int uiOffset, int uiWriteLength, String pucBuffer);

    int SDF_DeleteFile(Pointer hSessionHandle, String pucFileName, int uiNameLen);

    int SDF_GetSymmKeyHandle(Pointer hSessionHandle, int uiKeyIndex, PointerByReference phKeyHandle);

    
    int SDF_ExportSignPublicKey_ECC(Pointer hSessionHandle, int uiKeyIndex,ECCrefPublicKey.ByReference pucPublicKey);
    
    int SDF_ExportEncPublicKey_ECC(Pointer hSessionHandle, int uiKeyIndex,ECCrefPublicKey.ByReference pucPublicKey);
    int SDF_GenerateKeyPair_ECC(Pointer hSessionHandle, int uiAlgID,int uiKeyBits,ECCrefPublicKey.ByReference pucPublicKey,ECCrefPrivateKey.ByReference pucPrivateKey);
    int SDF_ExternalSign_ECC(Pointer hSessionHandle,int uiAlgID,ECCrefPrivateKey.ByReference pucPrivateKey,String pucData,int uiDataLength,ECCSignature.ByReference pucSignature);
    int SDF_ExternalVerify_ECC(Pointer hSessionHandle,int uiAlgID,ECCrefPublicKey.ByReference pucPublicKey,String pucDataInput,int uiInputLength,ECCSignature.ByReference pucSignature);
    int SDF_InternalSign_ECC(Pointer hSessionHandle,int uiISKIndex,String pucData,int uiDataLength,ECCSignature.ByReference pucSignature);
    int SDF_InternalVerify_ECC(Pointer hSessionHandle,int uiISKIndex,String pucData,int uiDataLength,ECCSignature.ByReference pucSignature);
    int SDF_ExternalEncrypt_ECC(Pointer hSessionHandle,int uiAlgID,ECCrefPublicKey.ByReference pucPublicKey,String pucData,int uiDataLength,ECCCipher.ByReference pucEncData);
    int SDF_ExternalDecrypt_ECC(Pointer hSessionHandle,int uiAlgID,ECCrefPrivateKey.ByReference pucPrivateKey,ECCCipher.ByReference pucEncData,String pucData,IntByReference puiDataLength);
    
    /*备份恢复类函数 2010.6.1 update*/
    
    
    
    int SWCSM_BackupInit_NoIC(Pointer hSessionHandle, int uiAlgorithmID, String passwd, int uiPwdLength);

    int SWCSM_BackupInit(Pointer hSessionHandle, int uiAlgorithmID);

    int KM_BackupExportKeyComponent(Pointer hsessionHandle, String pin, int num, int[] uiUserID,int[] uiUserType,Pointer hUSBKey);

    int SWCSM_BackupExportManagementInfo(Pointer hSessionHandle, byte[] pucData, IntByReference puiDataLength);

    int SWCSM_BackupExportECCKey(Pointer hSessionHandle, int uiIndex, IntByReference puiKeyBits, byte[] pucKeyData, IntByReference puiKeyDataLength);

    int SWCSM_BackupExportRSAKey(Pointer hSessionHandle, int uiIndex, IntByReference puiKeyBits, byte[] pucKeyData, IntByReference puiKeyDataLength);

    int SWCSM_BackupExportKEK(Pointer hSessionHandle, int uiIndex, byte[] pucKeyData, IntByReference puiKeyDataLength);

    int SWCSM_BackupFinal(Pointer hSessionHandle);

    int SWCSM_RestoreInit_NoIC(Pointer hSessionHandle, int uiAlgorithmID, String passwd, int uiPwdLength);

    int SWCSM_RestoreInit(Pointer hSessionHandle, int uiAlgorithmID);

    int SWCSM_RestoreImportKeyComponent(Pointer hSessionHandle, String pucPin);

    int SWCSM_RestoreImportManagementInfo(Pointer hSessionHandle, byte[] pucData, int uiDataLength);

    int SWCSM_RestoreImportKEK(Pointer hSessionHandle, int uiIndex, byte[] pucKeyData, int uiKeyDataLength);

    int SWCSM_RestoreImportRSAKey(Pointer hSessionHandle, int uiIndex, int uiKeyBits, byte[] pucKeyData, int uiKeyDataLength);

    int SWCSM_RestoreImportECCKey(Pointer hSessionHandle, int uiIndex, int uiKeyBits, byte[] pucKeyData, int uiKeyDataLength);

    int SWCSM_RestoreFinal(Pointer hSessionHandle);

    public static class BackupInfo {
        public int nTotalLength;       //total length
        public byte[] nProtocolFlag = new byte[8]; //backup file label
        public int nVersion;           //protocol version, cur is 1
        public int tBackupTime;        //backup time
        public int nAlgorithmID;       //alogrithm id
        public int[] nResv = new int[2];

        public BackupInfo(byte[] buf) {
            byte[] bTmpBuf = new byte[4];
            int pos = 0;
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nTotalLength = ByteUtil.bytes2intReverse(bTmpBuf);
            bTmpBuf = new byte[8];
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nProtocolFlag = bTmpBuf;
            bTmpBuf = new byte[4];
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nVersion = ByteUtil.bytes2intReverse(bTmpBuf);
            bTmpBuf = new byte[4];
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.tBackupTime = ByteUtil.bytes2intReverse(bTmpBuf);
            bTmpBuf = new byte[4];
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nAlgorithmID = ByteUtil.bytes2intReverse(bTmpBuf);
            bTmpBuf = new byte[4];
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nResv[0] = ByteUtil.bytes2intReverse(bTmpBuf);
            bTmpBuf = new byte[4];
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nResv[1] = ByteUtil.bytes2intReverse(bTmpBuf);
        }

        public BackupInfo() {
        }

        public int size() {
            return 6*4+8;
        }

        public byte[] toByte() {
            byte[] tmp = new byte[size()];
            byte[] nTo = ByteUtil.int2bytesReverse(nTotalLength);
            byte[] nVe = ByteUtil.int2bytesReverse(nVersion);
            byte[] tBa = ByteUtil.int2bytesReverse(tBackupTime);
            byte[] nAl = ByteUtil.int2bytesReverse(nAlgorithmID);
            byte[] nRe1 = ByteUtil.int2bytesReverse(nResv[0]);
            byte[] nRe2 = ByteUtil.int2bytesReverse(nResv[1]);

            System.arraycopy(nTo, 0, tmp, 0, 4);
            System.arraycopy(nProtocolFlag, 0, tmp, 4, 8);
            System.arraycopy(nVe, 0, tmp, 12, 4);
            System.arraycopy(tBa, 0, tmp, 16, 4);
            System.arraycopy(nAl, 0, tmp, 20, 4);
            System.arraycopy(nRe1, 0, tmp, 24, 4);
            System.arraycopy(nRe2, 0, tmp, 28, 4);

            return tmp;
        }
    }

    public class DataHead {
        public int nLength;                 //data length, include DATA_HEAD
        public int nType;                   //data type
        public int nEncryptFlag;            //1-cipher, 0-plain
        public int nIndex;                  //
        public int nBits;                   //
        public int nCheckSum;               //check code on cipher
        public int[] nResv = new int[2];

        public DataHead(byte[] buf) {
            byte[] bTmpBuf = new byte[4];
            int pos = 0;
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nLength = ByteUtil.bytes2intReverse(bTmpBuf);
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nType = ByteUtil.bytes2intReverse(bTmpBuf);
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nEncryptFlag = ByteUtil.bytes2intReverse(bTmpBuf);
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nIndex = ByteUtil.bytes2intReverse(bTmpBuf);
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nBits = ByteUtil.bytes2intReverse(bTmpBuf);
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nCheckSum = ByteUtil.bytes2intReverse(bTmpBuf);
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nResv[0] = ByteUtil.bytes2intReverse(bTmpBuf);
            System.arraycopy(buf, pos, bTmpBuf, 0, bTmpBuf.length);
            pos += bTmpBuf.length;
            this.nResv[1] = ByteUtil.bytes2intReverse(bTmpBuf);
        }
        public DataHead() {
            
        }

        public int size() {
            return 8*4;
        }
        
        public byte[] toByte() {
            int size = 4 * 8;
            byte[] tmp = new byte[size];
            byte[] nLe = ByteUtil.int2bytesReverse(nLength);
            byte[] nTy = ByteUtil.int2bytesReverse(nType);
            byte[] nEn = ByteUtil.int2bytesReverse(nEncryptFlag);
            byte[] nIn = ByteUtil.int2bytesReverse(nIndex);
            byte[] nBi = ByteUtil.int2bytesReverse(nBits);
            byte[] nCh = ByteUtil.int2bytesReverse(nCheckSum);
            byte[] nRe1 = ByteUtil.int2bytesReverse(nResv[0]);
            byte[] nRe2 = ByteUtil.int2bytesReverse(nResv[1]);

            /*
            System.out.println("nLength:"+new BigInteger(1, nLe).toString(16)+", "+nLength);
            System.out.println("nType:"+new BigInteger(1, nTy).toString(16)+", "+nType);
            System.out.println("tEncryptFlag:"+new BigInteger(1, nEn).toString(16));
            System.out.println("nIndex:"+new BigInteger(1, nIn).toString(16));
            System.out.println("nBits:"+new BigInteger(1, nBi).toString(16));
            System.out.println("nCheckSum:"+new BigInteger(1, nCh).toString(16));
            */

            System.arraycopy(nLe, 0, tmp, 0, 4);
            System.arraycopy(nTy, 0, tmp, 4, 4);
            System.arraycopy(nEn, 0, tmp, 8, 4);
            System.arraycopy(nIn, 0, tmp, 12, 4);
            System.arraycopy(nBi, 0, tmp, 16, 4);
            System.arraycopy(nCh, 0, tmp, 20, 4);
            System.arraycopy(nRe1, 0, tmp, 24, 4);
            System.arraycopy(nRe2, 0, tmp, 28, 4);
            //System.out.println("tmp:"+new BigInteger(1, tmp).toString(16));
            
            return tmp;
        }
    }
    /*设备管理类函数*/

    int SWCSM_InitDevice(Pointer hSessionHandle, int uiFlag);

    int SWCSM_GetCurrentStatus(Pointer hSessionHandle, IntByReference puiCurStatus);

    int SWCSM_AddOneManager(Pointer hSessionHandle, int uiManagerNumber, String pucPin);

    int SWCSM_DelOneManager(Pointer hSessionHandle, int uiManagerNumber);

    int SWCSM_InitOperatorPassword(Pointer hSessionHandle);

    int SWCSM_AddOneOperator(Pointer hSessionHandle, String pucPin);

    int SWCSM_Login(Pointer hSessionHandle, String pucPin, IntByReference puiUserID);

    int SWCSM_Logout(Pointer hSessionHandle, int puiUserID);
    
    int SWCSM_UKEY_ProtocolStart(Pointer hSWCSMSession, int uiUserNumber, IntByReference uiToken, ECCCipher.ByReference K2Cipher_R_Cipher);
}
