/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sansec.hsm.bean.keymgr;

import com.sansec.hsm.bean.GlobalData;
import com.sansec.hsm.bean.HSMError;
import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.lib.kmapi;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.KeyUtil;
import com.sansec.hsm.util.OperationLogUtil;
import com.sun.jna.ptr.IntByReference;
import debug.log.LogUtil;
import java.util.ArrayList;
import java.util.List;


/**
 * 非对称密钥
 * @author root
 */
public class AsymmetricKeyStatus {
	 public static final String operationname = "USER KEY MANAGEMENT";
    private int index;
    private int encKeyLen = -1;
    private int signKeyLen = -1;
    private String algrithm;

    public AsymmetricKeyStatus(int index, int signKeyLen, int encKeyLen, String algrithm) {
        this.index = index;
        this.encKeyLen = encKeyLen;
        this.signKeyLen = signKeyLen;
        this.algrithm = algrithm;
    }

    public AsymmetricKeyStatus(int index) {
        this.index = index;
    }

    public AsymmetricKeyStatus(String algrithm) {
        this.algrithm = algrithm;
    }

    public int getIndex() {
        return index;
    }

    public int getEncKeyLen() {
        return encKeyLen;
    }

    public int getSignKeyLen() {
        return signKeyLen;
    }

    /* 产生密钥对 */
    public void generate() throws DeviceException, NoPrivilegeException {
        /* 获取权限 */
        Privilege rights = new Privilege();
        LogUtil.println("Privilege: "+rights);

        /* 检查权限 */
        if( !rights.check(Privilege.PRIVILEGE_GEN_KEY_PAIR) ) {
            OperationLogUtil.genLogMsg("ERROR","Check Privilege" ,"Failed",Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_GEN_KEY_PAIR),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_GEN_KEY_PAIR));
        }

        /* 产生密钥 */
        int nSignKeyIndex = KeyUtil.transferSignKeyIndex(index);
        int nEncKeyIndex = KeyUtil.transferEncKeyIndex(index);
        if( signKeyLen > 0 && encKeyLen > 0 ) {
            // 产生密钥对
            generateKeyPair(nSignKeyIndex, signKeyLen, nEncKeyIndex, encKeyLen);
            return ;
        }

        if( signKeyLen > 0 ) {
            // 产生签名密钥对
            generateSignKeyPair(nSignKeyIndex, signKeyLen);
        }

        if( encKeyLen > 0 ) {
            // 产生加密密钥对
            generateEncKeyPair(nEncKeyIndex, encKeyLen);
        }
    }

    /* 删除密钥对 */
    public void delete() throws DeviceException, NoPrivilegeException {
        /* 获取权限 */
        Privilege rights = new Privilege();
        LogUtil.println("Privilege: "+rights);

        /* 检查权限 */
        if( !rights.check(Privilege.PRIVILEGE_DELETE_KEY_PAIR) ) {
            OperationLogUtil.genLogMsg("ERROR","Check Privilege" ,"Failed",Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_GEN_KEY_PAIR),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_DELETE_KEY_PAIR));
        }

        /* 删除密钥对 */
        int nSignKeyIndex = KeyUtil.transferSignKeyIndex(index);
        int nEncKeyIndex = KeyUtil.transferEncKeyIndex(index);
        LogUtil.println("delete() -> nSignKeyIndex: "+nSignKeyIndex);
        LogUtil.println("delete() -> nEncKeyIndex: "+nEncKeyIndex);
        if( signKeyLen > 0 && encKeyLen > 0 ) {
            // 删除密钥对
            deleteKeyPair(nSignKeyIndex, nEncKeyIndex);
            return ;
        }

        if( signKeyLen > 0 ) {
            // 删除签名密钥对
            deleteSignKeyPair(nSignKeyIndex);
        }

        if( encKeyLen > 0 ) {
            // 删除加密密钥对
            deleteEncKeyPair(nEncKeyIndex);
        }
    }

    private int generate(int keyIndex, int keyLength) {
        int algId = GlobalData.KEY_TYPE_RSA;
        if( !"RSA".equalsIgnoreCase(algrithm) ) {
            algId = GlobalData.KEY_TYPE_ECC;
        }
        int rv = kmapi.INSTANCE.KM_GenerateKeyPairEx(keyIndex, algId, keyLength, 65537);
        
        return rv;
    }

    private int delete(int keyIndex) {
        int algId = GlobalData.KEY_TYPE_RSA;
        if( !"RSA".equalsIgnoreCase(algrithm) ) {
            algId = GlobalData.KEY_TYPE_ECC;
        }
        LogUtil.println("delete() -> algrithm: "+this.algrithm);
        LogUtil.println("delete() -> index: "+this.index);
        int rv = kmapi.INSTANCE.KM_DeleteKeyPair(keyIndex, algId);

        return rv;
    }

    /* 获取密钥状态 */
    public List<AsymmetricKeyStatus> getKeyPairStatus() throws DeviceException, NoPrivilegeException {
        List<AsymmetricKeyStatus> list = new ArrayList<AsymmetricKeyStatus>();
        /* 获取权限 */
        Privilege rights = new Privilege();
        LogUtil.println("Privilege: "+rights);

        /* 检查权限 */
        if( !rights.check(Privilege.PRIVILEGE_SHOW_KEY_INFO) ) {
            OperationLogUtil.genLogMsg("ERROR","Check Privilege" ,"Failed",Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_KEY_INFO),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_KEY_INFO));
        }

        int[] keyStatus = getKeyStatus();
        for (int i = 0; i < keyStatus.length; i += 2) {
            if (keyStatus[i] > 0 || keyStatus[i + 1] > 0) {             
                int nIndex = i/2+1;
                int nSignKeyLength = -1;
                int nEncKeyLength = -1;
                if (keyStatus[i] > 0) {
                    nSignKeyLength = keyStatus[i];
                }

                if (keyStatus[i + 1] > 0) {
                    nEncKeyLength = keyStatus[i + 1];
                }

                AsymmetricKeyStatus key = new AsymmetricKeyStatus(nIndex, nSignKeyLength, nEncKeyLength, algrithm);
                list.add(key);
            }
        }
           
        return list;
    }

     /* 产生签名密钥对 */
    private void generateSignKeyPair(int keyIndex, int keyLength) throws DeviceException {
        /* 检查密钥是否存在 */
         Privilege rights = new Privilege();
        int[] keyStatus = getKeyStatus();
        if(keyStatus[keyIndex-1] > 0) {
            OperationLogUtil.genLogMsg("ERROR","GenerateSignKeyPair" ,"Failed",index + "号"+algrithm.toUpperCase()+"签名密钥密钥已存在，请先删除后再更新!",ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException(index + "号"+algrithm.toUpperCase()+"签名密钥密钥已存在，请先删除后再更新!");
        }

        int rv = generate(keyIndex, keyLength);
        if( rv != HSMError.SDR_OK ) {
            OperationLogUtil.genLogMsg("ERROR","GenerateSignKeyPair" ,"Failed","Generate NO."+ index +" "+ keyLength+"Bits Signature Keypair Failed:"+HSMError.getErrorInfo(rv) ,ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException("生成"+algrithm.toUpperCase()+"签名密钥失败: "+HSMError.getErrorInfo(rv));
        }
        
        //OperationLogUtil.printlnNew("INFO", "GenerateSignKeyPair" ,"Generate NO."+ index +" "+ keyLength+"Bits Signature Keypair Succeed." );
        OperationLogUtil.genLogMsg("INFO","GenerateSignKeyPair" ,"success",null,null,null);
    }

    /* 产生加密密钥对 */
    private void generateEncKeyPair(int keyIndex, int keyLength) throws DeviceException {
        /* 检查密钥是否存在 */
        Privilege rights = new Privilege();
        int[] keyStatus = getKeyStatus();
        if(keyStatus[keyIndex-1] > 0) {
            OperationLogUtil.genLogMsg("ERROR","GenerateEncryptionKeyPair" ,"Failed",index + "号"+algrithm.toUpperCase()+"加密密钥已存在，请先删除后再更新!",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new DeviceException(index + "号"+algrithm.toUpperCase()+"加密密钥已存在，请先删除后再更新!");
        }
        
        int rv = generate(keyIndex, keyLength);
        if( rv != HSMError.SDR_OK ) {
            OperationLogUtil.genLogMsg("ERROR","GenerateEncryptionKeyPair" ,"Failed","Generate NO."+ index +" "+ keyLength+"Bits Encryption Keypair Failed:"+HSMError.getErrorInfo(rv) ,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
             throw new DeviceException("生成"+algrithm.toUpperCase()+"加密密钥失败: "+HSMError.getErrorInfo(rv));
        }
        //OperationLogUtil.printlnNew("INFO", "GenerateEncryptionKeyPair" ,"Generate NO."+ index +" "+ keyLength+"Bits Encryption Keypair Succeed.");
        OperationLogUtil.genLogMsg("INFO","GenerateEncryptionKeyPair" ,"success",null,null,null);
    }

    /* 产生密钥对 */
    private void generateKeyPair(int nSignKeyIndex, int nSignKeyLength, int nEncKeyIndex, int nEncKeyLength) throws DeviceException {
        /* 检查密钥是否存在 */
        int[] keyStatus = getKeyStatus();
        if(keyStatus[nSignKeyIndex-1] > 0 && keyStatus[nEncKeyIndex-1] > 0 ) {
            OperationLogUtil.genLogMsg("ERROR","generateKeyPair" ,"Failed",index + "号"+algrithm.toUpperCase()+"签名密钥对和加密密钥已存在，请先删除后再更新!",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new DeviceException(index + "号"+algrithm.toUpperCase()+"签名密钥对和加密密钥已存在，请先删除后再更新!");
        }
        if( keyStatus[nSignKeyIndex-1] > 0 ) {
            OperationLogUtil.genLogMsg("ERROR","generateKeyPair" ,"Failed",index + "号"+algrithm.toUpperCase()+"签名密钥对已存在，请先删除后再更新!",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new DeviceException(index + "号"+algrithm.toUpperCase()+"签名密钥对已存在，请先删除后再更新!");
        }
        if( keyStatus[nEncKeyIndex-1] > 0 ) {
            OperationLogUtil.genLogMsg("ERROR","generateKeyPair" ,"Failed",index + "号"+algrithm.toUpperCase()+"加密密钥对已存在，请先删除后再更新!",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new DeviceException(index + "号"+algrithm.toUpperCase()+"加密密钥对已存在，请先删除后再更新!");
        }
        
        generateSignKeyPair(nSignKeyIndex, nSignKeyLength);
        generateEncKeyPair(nEncKeyIndex, nEncKeyLength);

        //result = "生成" + index + "号签名密钥对和加密密钥对成功.";
    }


    /* 删除签名密钥对 */
    private void deleteSignKeyPair(int keyIndex) throws DeviceException {
         Privilege rights = new Privilege();
        int rv = delete(keyIndex);
        if( rv != HSMError.SDR_OK ) {
            OperationLogUtil.genLogMsg("ERROR","DeleteSignKeyPair" ,"Failed","Delete NO."+ index +  " "+algrithm.toUpperCase()+" Signature Keypair Failed:"+HSMError.getErrorInfo(rv) ,ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
             throw new DeviceException("删除"+algrithm.toUpperCase()+"签名密钥失败:"+HSMError.getErrorInfo(rv));
        }
        //OperationLogUtil.printlnNew("INFO", "DeleteSignKeyPair" ,"Delete NO."+ index +  " "+algrithm.toUpperCase()+" Signature Keypair Succeed." );
        OperationLogUtil.genLogMsg("INFO","DeleteSignKeyPair" ,"success",null,null,null);
    }

    /* 删除加密密钥对 */
    private void deleteEncKeyPair(int keyIndex) throws DeviceException {
        Privilege rights = new Privilege();
        int rv = delete(keyIndex);
        if( rv != HSMError.SDR_OK ) {
            OperationLogUtil.genLogMsg("ERROR","DeleteSignKeyPair" ,"Failed","Delete NO."+ index +  " "+algrithm.toUpperCase()+" Encryption Keypair Failed:"+HSMError.getErrorInfo(rv),ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
        }
        OperationLogUtil.genLogMsg("INFO","DeleteSignKeyPair" ,"success",null,null,null);
    }

    /* 删除密钥对 */
    private void deleteKeyPair(int nSignKeyIndex, int nEncKeyIndex) throws DeviceException {
        deleteSignKeyPair(nSignKeyIndex);
        deleteEncKeyPair(nEncKeyIndex);

        //result = "删除" + index + "号签名密钥对和加密密钥对成功.";
    }

    /* 获取密钥状态 */
    private int[] getKeyStatus() throws DeviceException {
        int[] keyStatus = new int[kmapi.MAX_RSA_KEY_COUNT];
        IntByReference nListCount = new IntByReference();
        nListCount.setValue(kmapi.MAX_RSA_KEY_COUNT);
        int algId = GlobalData.KEY_TYPE_RSA;
        if( !"RSA".equalsIgnoreCase(algrithm) ) {
            algId = GlobalData.KEY_TYPE_ECC;
        }
        int rv =0x0;
        //int rv = kmapi.INSTANCE.KM_GetKeyStatus(algId, keyStatus, nListCount);
        if(rv != HSMError.SDR_OK) {

            throw new DeviceException("获取"+algrithm.toUpperCase()+"密钥对状态错误: "+HSMError.getErrorInfo(rv));
        }

        return keyStatus;
    }

    /* 获取密钥状态 */
    public void modifyPAP(String password) throws NoPrivilegeException, DeviceException {
        /* 获取权限 */
        Privilege rights = new Privilege();
        LogUtil.println("Privilege: "+rights);

        /* 检查权限 */
        if( !rights.check(Privilege.PRIVILEGE_SET_SKAP) ) {
			throw new NoPrivilegeException("没有私钥访问控制码的权限!");
        }
            
        int rv = kmapi.INSTANCE.KM_SetAuthenCode(index, password, password.length());
        if (rv != HSMError.SDR_OK) {
            String result = "设置" + index + "号密钥私钥访问控制码错误:" + HSMError.getErrorInfo(rv);
            throw new DeviceException(result);
        }
    }

    @Override
    public String toString() {
        return "     index: "+this.index+"\n"+
               "signKeyLen: "+this.signKeyLen+"\n"+
               " encKeyLen: "+this.encKeyLen+"\n"+
               "  algrithm: "+this.algrithm;
    }
}
