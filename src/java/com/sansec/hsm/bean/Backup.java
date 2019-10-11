/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sansec.hsm.bean;

import com.sansec.hsm.bean.usermgr.Operator;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.lib.kmapi;
import com.sansec.hsm.lib.swsds;
import com.sansec.hsm.lib.swsds.BackupInfo;
import com.sansec.hsm.lib.swsds.DataHead;
import com.sansec.hsm.lib.swsds.RSArefPrivateKey;
import com.sansec.hsm.util.ByteUtil;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import com.sansec.util.ByteTools;
import com.sansec.util.FileTools;
import com.sansec.util.SignatureTools;
import static com.sansec.util.SignatureTools.DIGEST_ALG;
import static com.sansec.util.SignatureTools.KEY_ALGORITHM;
import static com.sansec.util.SignatureTools.version;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import debug.log.LogUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Backup {
    public static final String operationname = "SECKEY BACKUP";
    private static Backup instance = new Backup();
    public static Backup getInstnce() {
        return instance;
    }
    
    private Backup() {
    }
    
    public void exportPart(int part, String pin) throws DeviceException {
     /*
        ModuleInstance module = ModuleInstance.getInstance();
*/
       PointerByReference session2 = new PointerByReference(Pointer.NULL);
        String result;
        int rv;
        Pointer session = null;
        if(part == 1) {
            rv = kmapi.INSTANCE.KM_BackupInit(session2);
                session = session2.getValue();
                GlobalData.session = session;
            if (rv != HSMError.SDR_OK) {
                result = "Data Backup Failed,Backup Init Error:" + HSMError.getErrorInfo(rv);
                OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed",result, ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException(result);
            }
        }
         PointerByReference hUSBKey = null;
         int[] uiUserID;
         int[] uiUserType;
         if(part>1){
             hUSBKey = new PointerByReference(GlobalData.hUSBKey.getPointer());
             uiUserID = GlobalData.uiUserID;
             uiUserType = GlobalData.uiUserType;
         }else{
           hUSBKey = new PointerByReference(Pointer.NULL);
           uiUserID = new int[3];
           uiUserType = new int[3];
         }
        //rv = kmapi.INSTANCE.KM_BackupExportKeyComponent(session, pin,(part%GlobalData.BACKUP_KEY_COUNT == 0)?GlobalData.BACKUP_KEY_COUNT:(part%GlobalData.BACKUP_KEY_COUNT),uiUserID,uiUserType,hUSBKey);
      int num = part - 1 ;
       session = GlobalData.session;
        rv = kmapi.INSTANCE.KM_BackupExportKeyComponent(session, pin,num,uiUserID,uiUserType,hUSBKey);
        GlobalData.uiUserID=uiUserID;
        GlobalData.uiUserType=uiUserType;
        GlobalData.hUSBKey=hUSBKey;
        //可以考虑将这些数据持久化
        
        if (rv != HSMError.SDR_OK) {
            swsds.INSTANCE.SWCSM_BackupFinal(session);
            result = "Export key part ["+part+"] error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            //OperationLogUtil.println(operationname,"Data Backup Failed,Get KeyComponent Error:" + HSMError.getErrorInfo(rv));
            throw new DeviceException(result);
        }
        
        OperationLogUtil.genLogMsg("INFO","Export key part ["+part+"]" ,"success",null,null,null);
    }

    public static void checkPrivilege() throws DeviceException, NoPrivilegeException {
         // 检查权限
        Privilege rights = new Privilege();
        if( !rights.check(Privilege.PRIVILEGE_BACKUP) ) {
           throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_BACKUP));
        }
        Operator opt = new Operator();
        if(!opt.isExists()){
            throw new DeviceException("No operator.");
        }
        if(!opt.isLogin()){
            throw new DeviceException("No operator login.");
        }
    }

    public void generateFile() throws DeviceException, IOException {
        int rv = 0;
        final int DEF_SEC_KEY_BITS = 128;
        int[] pnKekStatus = new int[kmapi.MAX_KEK_COUNT];
        int[] pnRsaStatus = new int[kmapi.MAX_RSA_KEY_COUNT];
        int[] pnEccStatus = new int[kmapi.MAX_ECC_KEY_COUNT];
        int stDataInfoSize = new DataHead().size();
        int stPriKeySize = new RSArefPrivateKey.ByReference().size();
        String result = "";
        byte[] pTmpBuf = new byte[4396];
        byte[] pDataBuf = new byte[4396];
        byte[] sUserFile = new byte[128];
        int tmplen;
        IntByReference nRsaCount = new IntByReference();
        IntByReference nEccCount = new IntByReference();
        IntByReference nKekCount = new IntByReference();
        IntByReference nPubKeyLen = new IntByReference();
        IntByReference nPriKeyLen = new IntByReference();
        IntByReference nDataLen = new IntByReference();
        IntByReference nKeyBits = new IntByReference();
        IntByReference nKeyIndex = new IntByReference();
        Pointer hSWCSMKey = null;
        PointerByReference phSWCSMKey = new PointerByReference(Pointer.NULL);

        byte[] buf = null;

        Pointer session = GlobalData.session;

        //1. Create backup file
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        

        //2. Write backup file info
        BackupInfo stBackupInfo = new BackupInfo();
        stBackupInfo.nTotalLength = stBackupInfo.size();
        System.out.println("2. ---------------------------------");

        int dataLen = 0;
        
        //dataLen = 4096;
        
        //3. Export management info
        LogUtil.println("SWCSM_BackupExportManagementInfo");
        
         rv = swsds.INSTANCE.SWCSM_BackupExportManagementInfo(session, pDataBuf, nDataLen);
        if (rv != HSMError.SDR_OK) {
            result += "Data Backup Failed,Backup Management Info Error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException(result);
        } else {
             dataLen = nDataLen.getValue();
        }

        System.out.println("---------2.5---------");
        DataHead stDataInfo = new DataHead();
        stDataInfo.nType = GlobalData.KEY_TYPE_MGMT;
        stDataInfo.nEncryptFlag = 1;
        stDataInfo.nLength = dataLen + stDataInfoSize;
        stDataInfo.nCheckSum = ByteUtil.checkSum(pDataBuf, dataLen);
        
        // write data head
        outBuf.write(stDataInfo.toByte());
        // write data content
        outBuf.write(pDataBuf, 0, dataLen);
        stBackupInfo.nTotalLength += stDataInfo.nLength;
        System.out.println("3. ---------------------------------");
        

        int keyIndex = 0;
        //4.Export secure key RSA
        LogUtil.println("KM_ExportSecKey");
        rv = kmapi.INSTANCE.KM_ExportSecKey(pDataBuf, nDataLen, nKeyIndex);
        if (rv != HSMError.SDR_OK) {
            result += "Get system protection key error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Export MK Error:" + HSMError.getErrorInfo(rv) ,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            //OperationLogUtil.println(operationname,"Data Backup Failed,Export MK Error:" + HSMError.getErrorInfo(rv));
            throw new DeviceException(result);
        } else {
            keyIndex = nKeyIndex.getValue();
            //System.out.println("4. nKeyIndex = "+keyIndex);
        }
        

        if( Config.isSecKeyByMK() ) {
            LogUtil.println("SWCSM_BackupExportKEK");
            rv = swsds.INSTANCE.SWCSM_BackupExportKEK(session, keyIndex, pDataBuf, nDataLen);
        } else {
            LogUtil.println("SWCSM_BackupExportRSAKey");
            rv = swsds.INSTANCE.SWCSM_BackupExportRSAKey(session, keyIndex*2, nKeyBits, pDataBuf, nDataLen);
        }
        if (rv != HSMError.SDR_OK) {
            result += "Backup system protection key error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Backup MK Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            //OperationLogUtil.println(operationname,"Data Backup Failed,Backup MK Error:" + HSMError.getErrorInfo(rv));
            throw new DeviceException(result);
        } else {
            dataLen = nDataLen.getValue();
            //System.out.println("4. nDatalen = "+dataLen);
        }
        
        LogUtil.println("SWCSM_BackupFinal");
        swsds.INSTANCE.SWCSM_BackupFinal(session); //密码卡备份过程结束

        stDataInfo = new DataHead();
        stDataInfo.nLength = dataLen + stDataInfoSize;
        if( Config.isSecKeyByMK() ) {
            stDataInfo.nType = GlobalData.KEY_TYPE_SECKEY_MK;
            stDataInfo.nBits = DEF_SEC_KEY_BITS;
        } else {
            stDataInfo.nType = GlobalData.KEY_TYPE_SECKEY_RSA;
            stDataInfo.nBits = nKeyBits.getValue();
        }
        stDataInfo.nEncryptFlag = 1;
        stDataInfo.nIndex = keyIndex;
        stDataInfo.nCheckSum = ByteUtil.checkSum(pDataBuf, dataLen);
        // write data head
        outBuf.write(stDataInfo.toByte());
        // write data content
        outBuf.write(pDataBuf, 0, dataLen);
        stBackupInfo.nTotalLength += stDataInfo.nLength;
        //System.out.println(" 4. stBackupInfo.nTotalLength = "+stBackupInfo.nTotalLength);
        System.out.println("4. ---------------------------------");
       
        //5.Export secure key symm
        LogUtil.println("KM_ExportSecKey");
        rv = kmapi.INSTANCE.KM_ExportSecKey(pDataBuf, nDataLen, nKeyIndex);
        if (rv != HSMError.SDR_OK) {
            result += "Data Backup Failed,Get MK Status Error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Get MK Status Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException(result);
        } else {
            dataLen = nDataLen.getValue();
            //System.out.println("5. nDatalen = "+dataLen);
        }

        stDataInfo = new DataHead();
        stDataInfo.nLength = nDataLen.getValue() + stDataInfoSize;
        stDataInfo.nType = GlobalData.KEY_TYPE_SECKEY_SYMM;
        stDataInfo.nEncryptFlag = 1;
        stDataInfo.nBits = 128;
        stDataInfo.nCheckSum = ByteUtil.checkSum(pDataBuf, dataLen);
         // write data head
        outBuf.write(stDataInfo.toByte());
        // write data content
        outBuf.write(pDataBuf, 0, dataLen);
        stBackupInfo.nTotalLength += stDataInfo.nLength;
        //System.out.println(" 5. stBackupInfo.nTotalLength = "+stBackupInfo.nTotalLength);
        System.out.println("5. ---------------------------------");
           

        // 6.Get seckey handle
        if( Config.isSecKeyByMK() ) {
            LogUtil.println("SDF_GetSymmKeyHandle");
            rv = swsds.INSTANCE.SDF_GetSymmKeyHandle(session, keyIndex, phSWCSMKey);
        } else {
            LogUtil.println("SDF_ImportKeyWithISK_RSA");
            rv = swsds.INSTANCE.SDF_ImportKeyWithISK_RSA(session, keyIndex, pDataBuf, nDataLen.getValue(), phSWCSMKey);
        }

        if (rv != HSMError.SDR_OK) {
            result = "Data Backup Failed,Get MK Status Error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Get MK Status Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException(result);
        } else {
            hSWCSMKey = phSWCSMKey.getValue();
            //System.out.println("6. hSWCSMKey = "+hSWCSMKey);
        }
        //System.out.println(" 6. stBackupInfo.nTotalLength = "+stBackupInfo.nTotalLength);
        System.out.println("6. ---------------------------------");
        
        
        //7.Backup RSA keys
        nRsaCount.setValue(kmapi.MAX_RSA_KEY_COUNT);
        LogUtil.println("KM_GetKeyStatus(RSA)");
        rv = kmapi.INSTANCE.KM_GetKeyStatus(GlobalData.KEY_TYPE_RSA, pnRsaStatus, nRsaCount);
        if (rv != HSMError.SDR_OK) {
            result += "Data Backup Failed,Get RSA Status Error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Get RSA Status Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            //OperationLogUtil.println(operationname,"Data Backup Failed,Get RSA Status Error:" + HSMError.getErrorInfo(rv));
            throw new DeviceException(result);
        }

        int pubKeyLen;
        int priKeyLen = stPriKeySize;
        byte[] bPriKeyBuf = new byte[priKeyLen];
        byte[] bPubKeyBuf = null;
        byte[] tmpBuf = new byte[1024];
        for (int i = 0; i < nRsaCount.getValue(); i++) {

            if (pnRsaStatus[i] > 0) {
                LogUtil.println("KM_GetKeyPair:" + (i + 1));
                rv = kmapi.INSTANCE.KM_GetKeyPair(i + 1, GlobalData.KEY_TYPE_RSA, bPriKeyBuf, nPriKeyLen, null, nPubKeyLen);
                if (rv != HSMError.SDR_OK) {
                    result += "Data Backup Failed,Get RSA Status Errors:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Get RSA Status Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                    //OperationLogUtil.println(operationname,"Data Backup Failed,Get RSA Status Error:" + HSMError.getErrorInfo(rv));
                    throw new DeviceException(result);
                } 

                priKeyLen = nPriKeyLen.getValue();
                stDataInfo = new DataHead();
                stDataInfo.nType = GlobalData.KEY_TYPE_RSA;
                stDataInfo.nEncryptFlag = 1;
                buf = new byte[4];
                System.arraycopy(bPriKeyBuf, 0, buf, 0, buf.length);
                stDataInfo.nBits = ByteUtil.bytes2intReverse(buf);
                stDataInfo.nIndex = i + 1;
                // the starting 4 bytes of private key is bits
                stDataInfo.nLength = priKeyLen - 4 + stDataInfoSize;
                //System.out.println("nPriKeyLen - 4 = "+(priKeyLen-4));

                buf = new byte[priKeyLen-4];
                System.arraycopy(bPriKeyBuf, 4, buf, 0, buf.length);
                rv = swsds.INSTANCE.SDF_Encrypt(session, hSWCSMKey, GlobalData.BACKUP_ALGORITHM_ID, null, buf, buf.length, pDataBuf, nDataLen);
                if (rv != HSMError.SDR_OK) {
                    result += "Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                    //OperationLogUtil.println(operationname,"Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv));
                    throw new DeviceException(result);
                } else {
                    dataLen = nDataLen.getValue();
                }
                stDataInfo.nCheckSum = ByteUtil.checkSum(pDataBuf, dataLen);
                // write data head
                outBuf.write(stDataInfo.toByte());
                // write data content
                outBuf.write(pDataBuf, 0, dataLen);
                stBackupInfo.nTotalLength += stDataInfo.nLength;
            }
        }
       //System.out.println(" 7. stBackupInfo.nTotalLength = "+stBackupInfo.nTotalLength);
        System.out.println("7. ---------------------------------");
        

        //8.Backup ECC keys
        nEccCount.setValue(kmapi.MAX_ECC_KEY_COUNT);
        LogUtil.println("KM_GetKeyStatus(ECC)");
        rv = kmapi.INSTANCE.KM_GetKeyStatus(GlobalData.KEY_TYPE_ECC, pnEccStatus, nEccCount);
        if (rv != HSMError.SDR_OK) {
            result += "Data Backup Failed,Get Ecc Status Error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Get Ecc Status Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            //OperationLogUtil.println(operationname,"Data Backup Failed,Get Ecc Status Error:" + HSMError.getErrorInfo(rv));
            throw new DeviceException(result);
        }

        pubKeyLen = 32+32+4;
        priKeyLen = 4 + 32;
        bPubKeyBuf = new byte[pubKeyLen];
        bPriKeyBuf = new byte[priKeyLen];
        tmpBuf = new byte[1024];
        for (int i = 0; i < nEccCount.getValue(); i++) {
            if (pnEccStatus[i] > 0) {
                LogUtil.println("KM_GetKeyPair:" + (i+1));
                rv = kmapi.INSTANCE.KM_GetKeyPair(i + 1, GlobalData.KEY_TYPE_ECC, bPriKeyBuf, nPriKeyLen, bPubKeyBuf, nPubKeyLen);
                if (rv != HSMError.SDR_OK) {
                    result += "Data Backup Failed,Get Ecc Status Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Get Ecc Status Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                   // OperationLogUtil.println(operationname,"Data Backup Failed,Get Ecc Status Error:" + HSMError.getErrorInfo(rv));
                    throw new DeviceException(result);
                } 
                
                stDataInfo = new DataHead();
                stDataInfo.nType = GlobalData.KEY_TYPE_ECC;
                stDataInfo.nEncryptFlag = 1;
                buf = new byte[4];
                System.arraycopy(bPriKeyBuf, 0, buf, 0, buf.length);
                stDataInfo.nBits = ByteUtil.bytes2intReverse(buf);
                stDataInfo.nIndex = i + 1;
                stDataInfo.nLength = priKeyLen + pubKeyLen - 8 + stDataInfoSize;

                //先加密ECC公钥
                LogUtil.println("arraycopy");
                buf = new byte[pubKeyLen-4];
                System.arraycopy(bPubKeyBuf, 4, buf, 0, pubKeyLen-4);
                LogUtil.println("SDF_Encrypt1");
                rv = swsds.INSTANCE.SDF_Encrypt(session, hSWCSMKey, GlobalData.BACKUP_ALGORITHM_ID, null, buf, pubKeyLen-4, tmpBuf, nDataLen);
                if (rv != HSMError.SDR_OK) {
                    result += "Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv);
                   // OperationLogUtil.println(operationname,"Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv));
                    OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }
                int pos = 0;
                dataLen = nDataLen.getValue();
                LogUtil.println("arraycopy");
                System.arraycopy(tmpBuf, 0, pDataBuf, pos, dataLen);
                pos += dataLen;
          
                //再加密ECC私钥
                LogUtil.println("SDF_Encrypt2");
                buf = new byte[priKeyLen - 4];
                System.arraycopy(bPriKeyBuf, 4, buf, 0, priKeyLen - 4);
                rv = swsds.INSTANCE.SDF_Encrypt(session, hSWCSMKey, GlobalData.BACKUP_ALGORITHM_ID, null, buf, priKeyLen - 4, tmpBuf, nDataLen);
                if (rv != HSMError.SDR_OK) {
                    result += "Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                   // OperationLogUtil.println(operationname,"Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv));
                    throw new DeviceException(result);
                }
                dataLen = nDataLen.getValue();
                System.arraycopy(tmpBuf, 0, pDataBuf, pos, dataLen);
                pos += dataLen;
                // private key length + public key length
                dataLen = pos;
                LogUtil.println("checkSum");
                stDataInfo.nCheckSum = ByteUtil.checkSum(pDataBuf, dataLen);
                // write data head
                outBuf.write(stDataInfo.toByte());
                // write data content
                outBuf.write(pDataBuf, 0, dataLen);
                stBackupInfo.nTotalLength += stDataInfo.nLength;
            }
        }
        //System.out.println(" 8. stBackupInfo.nTotalLength = "+stBackupInfo.nTotalLength);
        System.out.println("8. ---------------------------------");

        //9.Backup KEKs
        nKekCount.setValue(kmapi.MAX_KEK_COUNT);
        LogUtil.println("KM_GetKeyStatus(KEK)");
        rv = kmapi.INSTANCE.KM_GetKeyStatus(GlobalData.KEY_TYPE_KEK, pnKekStatus, nKekCount);
        if (rv != HSMError.SDR_OK) {
            result += "Data Backup Failed,Get KEK Status Error:" + HSMError.getErrorInfo(rv);
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Get KEK Status Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            //OperationLogUtil.println(operationname,"Data Backup Failed,Get KEK Status Error:" + HSMError.getErrorInfo(rv));
            throw new DeviceException(result);
        }

        for (int i = 0; i < nKekCount.getValue(); i++) {
            if (pnKekStatus[i] > 0) {
                LogUtil.println("KM_GetKEK" + (i+1));
                rv = kmapi.INSTANCE.KM_GetKEK(i + 1, pTmpBuf, nDataLen);
                if (rv != HSMError.SDR_OK) {
                    result += "Data Backup Failed,Get Symmkey Error:" + HSMError.getErrorInfo(rv);
                    //OperationLogUtil.println(operationname,"Data Backup Failed,Get Symmkey Error:" + HSMError.getErrorInfo(rv));
                    OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Get Symmkey Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }

                tmplen = ((nDataLen.getValue() % 16) > 0) ? nDataLen.getValue() + 8 : nDataLen.getValue();
                stDataInfo.nType = GlobalData.KEY_TYPE_KEK;
                stDataInfo.nEncryptFlag = 1;
                stDataInfo.nBits = pnKekStatus[i];
                stDataInfo.nIndex = i + 1;
                stDataInfo.nLength = tmplen + stDataInfoSize;

                rv = swsds.INSTANCE.SDF_Encrypt(session, hSWCSMKey,
                                    GlobalData.BACKUP_ALGORITHM_ID, null,
                                    pTmpBuf, tmplen,
                                    pDataBuf, nDataLen);
                if (rv != HSMError.SDR_OK) {
                    result += "Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv);
                    //OperationLogUtil.println(operationname,"Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv));
                    OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed,Encrypt Error:" + HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }

                dataLen = nDataLen.getValue();
                stDataInfo.nCheckSum = ByteUtil.checkSum(pDataBuf, dataLen);
                // write data head
                outBuf.write(stDataInfo.toByte());
                // write data content
                outBuf.write(pDataBuf, 0, dataLen);
                stBackupInfo.nTotalLength += stDataInfo.nLength;
            }//end if (pnKekStatus[i] > 0)
        }//end for
        //System.out.println(" 9. stBackupInfo.nTotalLength = "+stBackupInfo.nTotalLength);
        System.out.println("9. ---------------------------------");
        
        //9.Backup user files
        LogUtil.println("KM_GetUserDataInfo");
        kmapi.INSTANCE.KM_GetUserDataInfo(sUserFile, nDataLen, nKeyBits);
        //System.outBuf.println("sUserInfo = "+new BigInteger(1, sUserFile).toString(16));
        //System.outBuf.println(new String(sUserFile));
        String Userfile = new String(sUserFile)+ "";
        if(Userfile.indexOf(0)>=0)
        {
            Userfile = Userfile.substring(0, Userfile.indexOf(0));
        }
        LogUtil.println("KM_GetUserDataInfo: "+Userfile.length());
        LogUtil.println("KM_GetUserDataInfo: "+Userfile+ ".");
        LogUtil.println("KM_GetUserDataInfo len: "+ nDataLen.getValue());
        File file = new File(Userfile);
        if( file.exists() ) {
            stDataInfo.nType = GlobalData.KEY_TYPE_USER_DATA;
            stDataInfo.nEncryptFlag = nKeyBits.getValue();
            stDataInfo.nLength = nDataLen.getValue() + stDataInfoSize;
            // write data head
            outBuf.write(stDataInfo.toByte());

            FileInputStream fis = new FileInputStream(file);
            buf = new byte[1024];
            while( fis.available() > 0) {
                int n = fis.read(buf);
                outBuf.write(buf);
            }
            // close file input stream
            fis.close();
            stBackupInfo.nTotalLength += stDataInfo.nLength;
        }
        //System.out.println(" 10. stBackupInfo.nTotalLength = "+stBackupInfo.nTotalLength);
        System.out.println("9. ---------------------------------");

        //10.Rewrite backup file info
        stBackupInfo.nAlgorithmID = GlobalData.BACKUP_ALGORITHM_ID;
        stBackupInfo.nVersion = GlobalData.BACKUP_PROTOCOL_VERSION;
        stBackupInfo.nProtocolFlag = GlobalData.BACKUP_PROTOCOL_LABEL;
        //memcpy(stBackupInfo.nProtocolFlag, BACKUP_PROTOCOL_LABEL, sizeof(stBackupInfo.nProtocolFlag));
        stBackupInfo.tBackupTime = (int) (new Date().getTime()/1000);//时间待改动
        System.out.println("10. ---------------------------------");
        // close output stream
        outBuf.close();
        
        byte[] content = ByteTools.byteMerger(stBackupInfo.toByte(), outBuf.toByteArray());
        deleteFile();
        SignatureTools.signFile(content, GlobalData.DOWNLOAD_BACKUP_FILE);

        //-------备份文件大小检查和验签-------
        File f= new File(GlobalData.DOWNLOAD_BACKUP_FILE);
        Boolean sizeFlag = false;
        if (f.length()>1000*1000) {    //1000*1000  即：1000k
            sizeFlag = true;
        }
        Boolean flag = FileTools.fileBeginWithStr(GlobalData.DOWNLOAD_BACKUP_FILE, "Swxa-SecKMS-Card-Backup-Dat-File\n"+"Version:");
        Boolean vertifyResult = SignatureTools.vertifyFile(flag, GlobalData.DOWNLOAD_BACKUP_FILE);
        if(sizeFlag&&vertifyResult){
            OperationLogUtil.genLogMsg("INFO","Verify Signature" ,"success",null,null,null);
        }else{
            OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed:Create Backup File Error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
        }
        //---------结束-----------
    }

    public static void deleteFile() {
        File file = new File(GlobalData.DOWNLOAD_BACKUP_FILE);
        boolean flag = file.delete();
        LogUtil.println("flag: "+flag);
    }

//    public static void main(String[] args) throws DeviceException, IOException {
//        Backup backup = Backup.getInstnce();
//        for(int i=0; i<3; i++)
//        {
//            System.out.println("input enter: "+i);
//            System.in.read();
//            backup.exportPart(i+1, "12345678");
//        }
//
//        Backup.getInstnce().generateFile();
//
//        Backup.getInstnce().deleteFile();
//        System.out.println("finish...");
//    }
}
