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
import com.sansec.hsm.util.OperationLogUtil;
import com.sun.jna.ptr.IntByReference;
import debug.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 对称密钥
 *
 * @author root
 */
public class SymmetricKeyStatus {
    public static final String operationname = "SYMMKEY MANAGEMENT";
    private int index;
    private int length;

    public SymmetricKeyStatus(int index, int length) {
        this.index = index;
        this.length = length;
    }

    public SymmetricKeyStatus() {

    }

    public int getIndex() {
        return index;
    }

    public int getLength() {
        return length;
    }

    public List<SymmetricKeyStatus> getKekStatus() throws DeviceException, NoPrivilegeException {
        List<SymmetricKeyStatus> list = new ArrayList<SymmetricKeyStatus>();
        /* 获取权限 */
        Privilege rights = new Privilege();
        LogUtil.println("Privilege: " + rights);

        /* 检查权限 */
        if (!rights.check(Privilege.PRIVILEGE_SHOW_KEY_INFO)) {
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_KEY_INFO));
        }

        int[] keyStatus = getKeyStatus();
        for (int i = 0; i < kmapi.MAX_KEK_COUNT; i++) {
            if (keyStatus[i] > 0) {
                SymmetricKeyStatus key = new SymmetricKeyStatus(i + 1, keyStatus[i] * 8);
                list.add(key);
            }
        }

        return list;
    }

    /* 产生密钥 */
    public void generate() throws DeviceException, NoPrivilegeException {
        /* 获取权限 */
        Privilege rights = new Privilege();
        LogUtil.println("Privilege: " + rights);

        /* 检查权限 */
        if (!rights.check(Privilege.PRIVILEGE_GEN_KEY_PAIR)) {
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_GEN_KEY_PAIR));
        }

        /* 产生密钥 */
        /* 检查密钥是否存在 */
        int[] keyStatus = getKeyStatus();
        if (keyStatus[index - 1] > 0) {
            throw new DeviceException(index + "号对称密钥已存在，请先删除后再更新!");
        }

        int rv = kmapi.INSTANCE.KM_GenerateKEK(index, length);
        if (rv != HSMError.SDR_OK) {
            OperationLogUtil.genLogMsg("ERROR", "GenerateKey", "Failed", "Generate NO." + index + " Symmkey Failed:" + HSMError.getErrorInfo(rv), ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException("生成对称密钥失败: " + HSMError.getErrorInfo(rv));
        }
        OperationLogUtil.genLogMsg("INFO", "GenerateKey", "success", null, null, null);
    }

    /* 删除密钥对 */
    public void delete() throws DeviceException, NoPrivilegeException {
        /* 获取权限 */
        Privilege rights = new Privilege();
        LogUtil.println("Privilege: " + rights);

        /* 检查权限 */
        if (!rights.check(Privilege.PRIVILEGE_DELETE_KEY_PAIR)) {
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_DELETE_KEY_PAIR));
        }

        /* 删除密钥 */
        int rv = kmapi.INSTANCE.KM_DeleteKEK(index);
        if (rv != HSMError.SDR_OK) {
            OperationLogUtil.genLogMsg("ERROR", "DeleteKey", "Failed", "Delete NO." + index + " Symmkey Failed:" + HSMError.getErrorInfo(rv), ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException("删除对称密钥失败:" + HSMError.getErrorInfo(rv));
        }
        OperationLogUtil.genLogMsg("INFO", "DeleteKey", "success", null, null, null);
    }

    /* 获取密钥状态 */
    private int[] getKeyStatus() throws DeviceException {
        int[] keyStatus = new int[kmapi.MAX_KEK_COUNT];
        IntByReference nListCount = new IntByReference();
        nListCount.setValue(kmapi.MAX_KEK_COUNT);
        int algId = GlobalData.KEY_TYPE_KEK;
        //   int rv =0x0;
        int rv = kmapi.INSTANCE.KM_GetKeyStatus(algId, keyStatus, nListCount);
        if (rv != HSMError.SDR_OK) {
            throw new DeviceException("获取对称密钥对状态错误: " + HSMError.getErrorInfo(rv));
        }

        return keyStatus;
    }


    @Override
    public String toString() {
        return " KeyIndex: " + this.index + "\n" + "KeyLength: " + this.length + "\n";
    }


    public static void main(String[] args) throws DeviceException, NoPrivilegeException {
        SymmetricKeyStatus key = new SymmetricKeyStatus(2, 128);
        //key.generate();
        key.delete();
        List list = new SymmetricKeyStatus().getKekStatus();
        System.out.println(list);

    }
}
