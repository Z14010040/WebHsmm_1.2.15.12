/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sansec.hsm.bean;

import com.sansec.hsm.bean.usermgr.Manager;
import com.sansec.hsm.bean.usermgr.Operator;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.lib.kmapi;
import com.sansec.hsm.lib.swsds;
import com.sansec.hsm.lib.swsds.BackupInfo;
import com.sansec.hsm.lib.swsds.DataHead;
import com.sansec.hsm.util.ByteUtil;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import com.sansec.util.FileTools;
import com.sansec.util.SignatureTools;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import debug.log.LogUtil;
import debug.log.PrintUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Restore {
    private Restore() {

    }
	 public static final String operationname = "SECKEY BACKUP";
    private static Restore instance = new Restore();
//    static {
//        Config.initialize();
//    }
    public static Restore getInstance() {
        return instance;
    }
    private String pin = null;
    public void importPart(int part, String pin) throws DeviceException {
        System.out.println(part);
        LogUtil.println("Restore.java -> importPart(): in...");
        this.pin = pin;
        try {
            System.out.println("begin init");
            PointerByReference session2 = new PointerByReference(Pointer.NULL);
            System.out.println("init1...");
            String result;
            System.out.println("init2...");
            Pointer session = null;
            System.out.println("init3...");
            int rv;
            System.out.println("init4...");
            if(part == 1) {
                /*
                 Privilege rights = new Privilege();
                   System.out.println("init5...");
                rights.init();
                */
                rv = kmapi.INSTANCE.KM_EraseAllKeys();
                if (rv != HSMError.SDR_OK) {
                //   module.close();
                    result = "Data Restore Failed,Restore Init Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
         
                    throw new DeviceException(result);
                }
            
                System.out.println("init6...");
                rv = kmapi.INSTANCE.KM_RestoreInit(session2);

                if (rv != HSMError.SDR_OK) {
                //   module.close();
                    result = "Data Restore Failed,Restore Init Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }
                session = session2.getValue();
                System.out.println(session);
                GlobalData.session = session;
             
            }
        
            PointerByReference _hUSBKey = null;
            int[] uiUserID;
            int[] uiUserType;
            if(part>1){
                _hUSBKey = new PointerByReference(GlobalData.hUSBKey.getPointer());
                uiUserID = GlobalData.uiUserID;
                uiUserType = GlobalData.uiUserType;
            }else{
              _hUSBKey = new PointerByReference(Pointer.NULL);
              uiUserID = new int[2];
              uiUserType = new int[2];
            }
            int num = part -1;
            session = GlobalData.session;
            System.out.println(session);
            rv = kmapi.INSTANCE.KM_RestoreExportKeyComponent(session, pin,num,uiUserID,uiUserType,_hUSBKey);
            // rv = kmapi.INSTANCE.KM_RestoreExportKeyComponent(session, pin,num,uiUserID,uiUserType,new PointerByReference(Pointer.NULL));
        
            if (rv != HSMError.SDR_OK) {
                swsds.INSTANCE.SWCSM_BackupFinal(session);
                result = "Data Restore Failed,Import key part ["+part+"] error:" + HSMError.getErrorInfo(rv);
                OperationLogUtil.genLogMsg("ERROR","Import key part ["+part+"]" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException(result);
            }
            //GlobalData.session = session;
            GlobalData.uiUserID=uiUserID;
            GlobalData.uiUserType=uiUserType;
            GlobalData.hUSBKey=_hUSBKey;
        } catch(DeviceException ex) {
            throw ex;
        } 
        OperationLogUtil.genLogMsg("INFO","Import key part ["+part+"]" ,"success",null,null,null);
        LogUtil.println("Restore.java -> importPart(): out...");
    }
    
    public void finish () {
        try {
            Process process = Runtime.getRuntime().exec("sync");
            process.waitFor();
        } catch (Exception ex) {
            LogUtil.println(ex.toString());
            //Logger.getLogger(Restore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void checkPrivilege() throws DeviceException, NoPrivilegeException {
         // 检查权限
        Privilege rights = new Privilege();
        if (rights.isInited())
        {
        if( !rights.check(Privilege.PRIVILEGE_RESTORE) ) {
            OperationLogUtil.genLogMsg("ERROR","Check Privilege" ,"Failed",Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_RESTORE), ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_RESTORE));
        }
        }
    }

    public void checkBackupFile() throws DeviceException, IOException {
        LogUtil.println("Restore.java -> checkBackupFile(): in...");
        FileInputStream in = null;
        try {
            Boolean flag = FileTools.fileBeginWithStr(GlobalData.UPLOAD_BACKUP_FILE, "Swxa-SecKMS-Card-Backup-Dat-File\n"+"Version:");
            Boolean vertifyResult = SignatureTools.vertifyFile(flag, GlobalData.UPLOAD_BACKUP_FILE);
            //check restore file
            in = new FileInputStream(GlobalData.UPLOAD_BACKUP_FILE);
            if(in == null) {
                OperationLogUtil.genLogMsg("ERROR","Check Backup File" ,"Failed","open backup file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("open backup file error.");
            }
            if(flag&&vertifyResult){
                OperationLogUtil.genLogMsg("INFO","Vertify Signature" ,"success",null,null,null);
                in.read(new byte[64]);
            }
            if(flag&&!vertifyResult){
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","the signature is vertified failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("the signature is vertified failed.");
            }
           
            byte[] buf = new byte[new BackupInfo().size()];
            int n = in.read(buf);
            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","read backup file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("read backup file error.");
            }

            BackupInfo backupInfo = new BackupInfo(buf);
            if(backupInfo.nVersion != GlobalData.BACKUP_PROTOCOL_VERSION ||
                    !Arrays.equals(GlobalData.BACKUP_PROTOCOL_LABEL, backupInfo.nProtocolFlag) ||
                    GlobalData.BACKUP_ALGORITHM_ID != backupInfo.nAlgorithmID) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","the format of backup file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("the format of backup file error.");
            }
        } catch(DeviceException ex) {
            throw ex;
        } finally {
            in.close();
        }
        LogUtil.println("Restore.java -> checkBackupFile(): out...");
    }

    public void run() throws DeviceException, IOException {
        LogUtil.println("Restore.java -> run(): in...");
        byte[] buf = null;
        String result;
        int dataHeadSize = new DataHead().size();   //32
        Pointer session = GlobalData.session;
        FileInputStream in = new FileInputStream(GlobalData.UPLOAD_BACKUP_FILE);
        if(in == null) {
            OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","open file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new DeviceException("open file error.");
        }
        int n;
        Boolean flg = FileTools.fileBeginWithStr(GlobalData.UPLOAD_BACKUP_FILE, "Swxa-SecKMS-Card-Backup-Dat-File\n"+"Version:");
        Boolean vertifyResult = SignatureTools.vertifyFile(flg, GlobalData.UPLOAD_BACKUP_FILE);
        if(vertifyResult){
            n = in.read(new byte[64]);
        }

        try {
            buf = new byte[new BackupInfo().size()];
            n = in.read(buf);

            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","read file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("read file error-1.");
            }
            // restore manage info
            // read head
            buf = new byte[dataHeadSize];
            n = in.read(buf);

            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("file error-1.");
            }
            DataHead head = new DataHead(buf);
            if(head.nType != GlobalData.KEY_TYPE_MGMT) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("file error-2.");
            }
            // read content
            buf = new byte[head.nLength-dataHeadSize];
            n = in.read(buf);
            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","read file failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("read file failed-2.");
            }

            int checkSum = ByteUtil.checkSum(buf);
            if(checkSum != head.nCheckSum) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","data failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("data failed.");
            }
            int rv = swsds.INSTANCE.SWCSM_RestoreImportManagementInfo(session, buf, buf.length);
            if (rv != HSMError.SDR_OK) {
                result = "Data Restore Failed,Restore Management Info Error:" + HSMError.getErrorInfo(rv);
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException(result);
            }

//            int[] uiUserID = GlobalData.uiUserID;
////            int[] uiUserType = GlobalData.uiUserType;
////            Pointer hUSBKey = GlobalData.hUSBKey.getValue();
////            rv =  kmapi.INSTANCE.KM_RestoreProtocol(session,uiUserID,uiUserType, hUSBKey);
////             if (rv != HSMError.SDR_OK) {
////                result = "RestoreProtocol Error:" + HSMError.getErrorInfo(rv);
////                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",1,result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
////                 throw new DeviceException(result);
////            }

            // 恢复系统保护密钥
            int protectedType;
            // read head
            buf = new byte[dataHeadSize];
            n = in.read(buf);
            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Data Restore Failed,Restore MK Error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("Data Restore Failed,Restore MK Error-1.");
            }
            head = new DataHead(buf);
            protectedType = head.nType;
//            System.out.println("注释：保护密钥类型："+protectedType);
            if((protectedType != GlobalData.KEY_TYPE_SECKEY_RSA) && (protectedType != GlobalData.KEY_TYPE_SECKEY_MK)) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Data Restore Failed,Restore MK Error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("Data Restore Failed,Restore MK Error-2.");
            }

            // read content
            buf = new byte[head.nLength-dataHeadSize];
            n = in.read(buf);
            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Data Restore Failed,Restore MK Error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("Data Restore Failed,Restore MK Error-3.");
            }

            checkSum = ByteUtil.checkSum(buf);
            if(checkSum != head.nCheckSum) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Data Restore Failed,Restore MK Error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("Data Restore Failed,Restore MK Error-4.");
            }
            int keyIndex = head.nIndex;
            if(protectedType == GlobalData.KEY_TYPE_SECKEY_RSA) {
                rv = swsds.INSTANCE.SWCSM_RestoreImportRSAKey(session, keyIndex*2, head.nBits, buf, buf.length);
            } else {
                rv = swsds.INSTANCE.SWCSM_RestoreImportKEK(session, keyIndex, buf, buf.length);
            }
            if (rv != HSMError.SDR_OK) {
                result = "Data Restore Failed,Restore MK Error:" + HSMError.getErrorInfo(rv);
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException(result);
            }

            // restore symm key
            // read head
            buf = new byte[dataHeadSize];
            n = in.read(buf);
            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("file error-3.");
            }
            head = new DataHead(buf);
            if(head.nType != GlobalData.KEY_TYPE_SECKEY_SYMM) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","file error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("file error-4.");
            }
            // read content
            buf = new byte[head.nLength-dataHeadSize];
            n = in.read(buf);
            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","read file failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("read file failed-3.");
            }

            checkSum = ByteUtil.checkSum(buf);
            if(checkSum != head.nCheckSum) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Data error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("Data error-1.");
            }
            rv = kmapi.INSTANCE.KM_ImportSecKey(buf, buf.length);
            if (rv != HSMError.SDR_OK) {
                result = "Data Restore Failed,Restore MK Error:" + HSMError.getErrorInfo(rv);
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException(result);
            }
            Pointer hSWCSMKey = null;
            PointerByReference phSWCSMKey = new PointerByReference();
            // 6.Get seckey handle
            if( protectedType == GlobalData.KEY_TYPE_SECKEY_RSA ) {
                rv = swsds.INSTANCE.SDF_ImportKeyWithISK_RSA(session, keyIndex, buf, buf.length, phSWCSMKey);
            } else {
                rv = swsds.INSTANCE.SDF_GetSymmKeyHandle(session, keyIndex, phSWCSMKey);
            }

            if (rv != HSMError.SDR_OK) {
                result = "Data Restore Failed,Restore MK Error:" + HSMError.getErrorInfo(rv);
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException(result);
            } else {
                hSWCSMKey = phSWCSMKey.getValue();
                //System.out.println("6. hSWCSMKey = "+hSWCSMKey);
            }

            IntByReference nDataLen = new IntByReference();
            byte[] bDataBuf = new byte[4096];
            byte[] bTmpBuf = new byte[4096];
            byte[] bPriKey = null;
            byte[] bPubKey = null;
            int nPriKeyLen = 0;
            int nPubKeyLen = 0;
            int dataLen;
        // restore key/user data
//        while(in.available() > 0) {
            // read head
            buf = new byte[dataHeadSize];
            n = in.read(buf);
            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Read file failed.",ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("Read file failed-1.");
            }
            head = new DataHead(buf);
            System.out.println(head.nType);
            if(head.nType != GlobalData.KEY_TYPE_RSA && head.nType != GlobalData.KEY_TYPE_ECC &&
                    head.nType != GlobalData.KEY_TYPE_KEK && head.nType != GlobalData.KEY_TYPE_USER_DATA) {

                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Read file failed.",ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("Read file failed-2.");
            }
            
            // read content
            buf = new byte[head.nLength-dataHeadSize];
            n = in.read(buf);
            if(n != buf.length) {
                OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Read file failed.",ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));

                throw new DeviceException("Read file failed-3.");
            }

            if(head.nType == GlobalData.KEY_TYPE_RSA || head.nType == GlobalData.KEY_TYPE_ECC ||
                    head.nType == GlobalData.KEY_TYPE_KEK)
            {
                checkSum = ByteUtil.checkSum(buf);
                if(checkSum != head.nCheckSum) {
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","Data error.",ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));

                    throw new DeviceException("Data error-2.");
                }
            }

            //type
            if(head.nType == GlobalData.KEY_TYPE_RSA) {
                bPriKey = new byte[4096];
                bPubKey = new byte[2048];
                //PrintUtil.printhex(buf);
                //System.out.println("..........................................");
                rv = swsds.INSTANCE.SDF_Decrypt(session, hSWCSMKey,
                        GlobalData.BACKUP_ALGORITHM_ID, null,
                        buf, buf.length, bDataBuf, nDataLen);
                if (rv != HSMError.SDR_OK) {
                    result = "Data Restore Failed,Decrypt RSA Key Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                } else {
                    dataLen = nDataLen.getValue();
                }
                byte[] bitsArr = ByteUtil.int2bytesReverse(head.nBits);
                System.arraycopy(bitsArr, 0, bPriKey, 0, bitsArr.length);
                System.arraycopy(bDataBuf, 0, bPriKey, bitsArr.length, dataLen);
                nPriKeyLen = dataLen+bitsArr.length;
                nPubKeyLen = dataLen/11*4+bitsArr.length;
                System.arraycopy(bPriKey, 0, bPubKey, 0, nPubKeyLen);
                rv = kmapi.INSTANCE.KM_ImportKeyPair(head.nIndex, GlobalData.KEY_TYPE_RSA,
                                        bPriKey, nPriKeyLen, bPubKey, nPubKeyLen);
                //PrintUtil.printhex(bPriKey, nPriKeyLen);
                if (rv != HSMError.SDR_OK) {
                    result = "Data Restore Failed,Restore RSA Key Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }
            } else if(head.nType == GlobalData.KEY_TYPE_ECC) {
                bPriKey = new byte[32+4];
                bPubKey = new byte[64+4];
                if(buf.length != bPriKey.length + bPubKey.length - 8) {
                    result = "ECC Backup Content error.";
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }
                // public key
                byte[] tmpIn = new byte[bPubKey.length - 4];
                byte[] tmpOut = new byte[tmpIn.length];                
                System.arraycopy(buf, 0, tmpIn, 0, tmpIn.length);
                rv = swsds.INSTANCE.SDF_Decrypt(session, hSWCSMKey,
                        GlobalData.BACKUP_ALGORITHM_ID, null,
                        tmpIn, tmpIn.length, tmpOut, nDataLen);
                byte[] bitsArr = ByteUtil.int2bytesReverse(head.nBits);
                System.arraycopy(bitsArr, 0, bPubKey, 0, bitsArr.length);
                System.arraycopy(tmpOut, 0, bPubKey, bitsArr.length, tmpOut.length);

                // private key
                tmpIn = new byte[bPriKey.length-4];
                tmpOut = new byte[tmpIn.length];
                System.arraycopy(buf, bPubKey.length - 4, tmpIn, 0, tmpIn.length);
                rv = swsds.INSTANCE.SDF_Decrypt(session, hSWCSMKey,
                        GlobalData.BACKUP_ALGORITHM_ID, null,
                        tmpIn, tmpIn.length, tmpOut, nDataLen);
                bitsArr = ByteUtil.int2bytesReverse(head.nBits);
                System.arraycopy(bitsArr, 0, bPriKey, 0, bitsArr.length);
                System.arraycopy(tmpOut, 0, bPriKey, bitsArr.length, tmpOut.length);

                rv = kmapi.INSTANCE.KM_ImportKeyPair(head.nIndex, GlobalData.KEY_TYPE_ECC,
                                    bPriKey, bPriKey.length, bPubKey, bPubKey.length);
                if (rv != HSMError.SDR_OK) {
                    result = "Data Restore Failed,Restore ECC Key Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }
            } else if(head.nType == GlobalData.KEY_TYPE_KEK) {
                rv = swsds.INSTANCE.SDF_Decrypt(session, hSWCSMKey,
                        GlobalData.BACKUP_ALGORITHM_ID, null,
                        buf, buf.length, bDataBuf, nDataLen);
                if (rv != HSMError.SDR_OK) {
                    result = "Data Restore Failed,Decrypt KEK Key Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                } else {
//                    dataLen = nDataLen.getValue();
                    dataLen = (head.nBits > 32)?(head.nBits / 8):head.nBits; //有效密钥长度
                }
                rv = kmapi.INSTANCE.KM_ImportKEK(head.nIndex, bDataBuf, dataLen);
                if (rv != HSMError.SDR_OK) {
                    result = "Data Restore Failed,Restore KEK Key Error:" + HSMError.getErrorInfo(rv);
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",result,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
                    throw new DeviceException(result);
                }
            } else if(head.nType == GlobalData.KEY_TYPE_USER_DATA) {
                byte[] bUserFile = new byte[128];
                IntByReference objDataLen = new IntByReference();
                IntByReference objFlag = new IntByReference();
                kmapi.INSTANCE.KM_GetUserDataInfo(bUserFile, objDataLen, objFlag);
                dataLen = objDataLen.getValue();
                if(dataLen != buf.length) {
                    OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed","File size error.",ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));

                    throw new DeviceException("File size error.");
                }
                String Userfile = new String(bUserFile)+ "";
                if(Userfile.indexOf(0)>=0)
                {
                    Userfile = Userfile.substring(0, Userfile.indexOf(0));
                }
                FileOutputStream fos = new FileOutputStream(Userfile);
                fos.write(buf, buf.length-dataLen, dataLen);
                fos.close();
            }
//        }

        } catch(DeviceException ex) {
            throw ex;
        } catch(IOException ex) {
            OperationLogUtil.genLogMsg("ERROR","Restore" ,"Failed",ex.getMessage(),ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
            throw ex;
        } finally {
            in.close();
        }
        OperationLogUtil.genLogMsg("INFO","Restore" ,"success",null,null,null);
        LogUtil.println("Restore.java -> run(): out...");
        Operator oo = new Operator(1);
        oo.logout();
        Manager manager = new Manager(0);
        manager.logout();
    }

//    public void deleteFile() {
//        LogUtil.println("Restore.java -> deleteFile(): in...");
//        File file = new File(GlobalData.BACKUP_FILE);
//        boolean flag = file.delete();
//        LogUtil.println("flag: "+flag);
//        LogUtil.println("Restore.java -> deleteFile(): out...");
//    }
}
