/*

 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sansec.hsm.bean.config;

import com.sansec.hsm.bean.HSMError;
import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.util.ConfigUtil;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import debug.log.LogUtil;

/**
 * @author root
 */
public class ServerInfo {
    public static final String operationname = "SERVICE CONFIGURATION";
    public static final String PATH = "/etc/swhsm/service.ini";
    private int port;
    private int onBoot;
    private int sessionTimeout;
    private int maxThread;
    private String connectPwd;
    private String serviceStartPwd;   // operator password

    public int getPort() {
        return port;
    }

    public int getOnBoot() {
        return onBoot;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public String getConnectPwd() {
        return connectPwd;
    }

    public String getServiceStartPwd() {
        return serviceStartPwd;
    }

    public ServerInfo() {
    }

    public ServerInfo(int port, int onBoot, int sessionTimeout, int maxThread, String connectPwd, String serviceStartPwd) {
        this.port = port;
        this.onBoot = onBoot;
        this.sessionTimeout = sessionTimeout;
        this.maxThread = maxThread;
        this.connectPwd = connectPwd;
        this.serviceStartPwd = serviceStartPwd;
    }


    public void load() throws NoPrivilegeException, DeviceException {
        Privilege rights = new Privilege();
        if (!rights.check(Privilege.PRIVILEGE_SHOW_SERVICE_CONFIG)) {
            OperationLogUtil.genLogMsg("ERROR", "Check Privilege", "Failed", Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_SERVICE_CONFIG), ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_SERVICE_CONFIG));
        }

        try {
            port = Integer.parseInt(ConfigUtil.getValue(PATH, "server", "port"));
            onBoot = Integer.parseInt(ConfigUtil.getValue(PATH, "server", "onboot"));
            maxThread = Integer.parseInt(ConfigUtil.getValue(PATH, "server", "maxconcurrent"));
            sessionTimeout = Integer.parseInt(ConfigUtil.getValue(PATH, "server", "sessiontimeout"));
            connectPwd = ConfigUtil.getValue(PATH, "server", "password");
            serviceStartPwd = ConfigUtil.getValue(PATH, "server", "pin");
        } catch (Exception e) {

        }
    }


    public void store() throws NoPrivilegeException, DeviceException {
        Privilege rights = new Privilege();
        if (!rights.check(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG)) {
            OperationLogUtil.genLogMsg("ERROR", "Check Privilege", "Failed", Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG), ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));

            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG));
        }

        ConfigUtil.setValue(PATH, "server", "port", "" + port);
        ConfigUtil.setValue(PATH, "server", "onboot", "" + onBoot);
        ConfigUtil.setValue(PATH, "server", "maxconcurrent", "" + maxThread);
        ConfigUtil.setValue(PATH, "server", "sessiontimeout", "" + sessionTimeout);
        ConfigUtil.setValue(PATH, "server", "password", connectPwd);
        ConfigUtil.setValue(PATH, "server", "pin", serviceStartPwd);

        try {
            Process process = Runtime.getRuntime().exec("sync");
            process.waitFor();
//            OperationLogUtil.println(operationname,"Configure Service:Port:"+port+", OnBoot:"+onBoot+", maxThread:"+maxThread +", sessionTimeout:"
//                   +sessionTimeout +", connectPWD:********, startPWD:******** Succeed.");
            OperationLogUtil.genLogMsg("INFO", operationname, "success", null, null, null);
        } catch (Exception ex) {
            LogUtil.println(ex.toString());
            OperationLogUtil.genLogMsg("ERROR", operationname, "Failed", "Configure Service Failed.", ErrorInfo.returnErrorInfo().get(0), ErrorInfo.returnErrorInfo().get(1));

        }
    }

    @Override
    public String toString() {
        return "Port            = " + port + "\n" + "OnBoot          = " + onBoot + "\n" + "MaxThread       = " + maxThread + "\n" + "SessionTimeout  = " + sessionTimeout + "\n" + "ConncetPwd      = " + connectPwd + "\n" + "ServcieStartPwd = " + serviceStartPwd + "\n";
    }
}
