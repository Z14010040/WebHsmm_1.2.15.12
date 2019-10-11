/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sansec.hsm.bean.config;

import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.util.ConfigUtil;
import com.sansec.hsm.util.OperationLogUtil;
import debug.log.LogUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class WhiteHostInfo {
	 public static final String operationname = "WHITELIST MANAGEMENT";
    public static final String PATH = "/etc/swhsm/service.ini";
    private String ip;
    private String name;
    public String getIp() {
        return ip;
    }
    public String getName() {
        return name;
    }
    public WhiteHostInfo() {
        
    }
    public WhiteHostInfo(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public List<WhiteHostInfo> load() throws NoPrivilegeException, DeviceException {
        Privilege rights = new Privilege();
        if( !rights.check(Privilege.PRIVILEGE_SHOW_SERVICE_CONFIG) ) {
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG));
        }

        List<WhiteHostInfo> list = new ArrayList<WhiteHostInfo>();
        for(int i=1; true; i++) {
            String str = ConfigUtil.getValue(PATH, "whitelist", "host"+i);
            LogUtil.println("load() -> str: "+str);
            if(str == null||str.compareTo("")==0) {
                break;
            } else {
                WhiteHostInfo host = new WhiteHostInfo("host"+i, str);
                list.add(host);
            }
        }
        
        return list;
    }

    public void add() throws NoPrivilegeException, DeviceException {
        Privilege rights = new Privilege();
        if( !rights.check(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG) ) {
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG));
        }

        List<WhiteHostInfo> list = load();
        LogUtil.println("add() -> list: "+list);
        ConfigUtil.addValue(PATH, "whitelist", "host", (list.size()+1), ip);
        OperationLogUtil.genLogMsg("INFO","Add "+ip+" To Whitelist" ,"success",null,null,null);

        try {
            Process process = Runtime.getRuntime().exec("sync");
            process.waitFor();
        } catch (Exception ex) {
            LogUtil.println(ex.toString());
        }
    }

    public void delete() throws NoPrivilegeException, DeviceException {
        Privilege rights = new Privilege();
        if( !rights.check(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG) ) {
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_SERVICE_CONFIG));
        }

        String keyPrefix = "";
        String value;
        int keyIndex = -1;
        for(int i=0; i<name.length(); i++) {
            try {
                keyIndex = Integer.parseInt(name.substring(i));
                keyPrefix = name.substring(0, i);
                break;
            } catch(Exception ex) {
                continue;
            }
        }
        
       String delvalue = ConfigUtil.getValue(PATH, "whitelist",keyPrefix+keyIndex);
        while (true)
        { 
            ConfigUtil.setValue(PATH, "whitelist", keyPrefix+keyIndex,"");
            ConfigUtil.delValue(PATH, "whitelist", keyPrefix, keyIndex);
            value = ConfigUtil.getValue(PATH, "whitelist", keyPrefix+(keyIndex+1));
            if(value == null||value.compareTo("")==0)
            {
                break;
            }
            ConfigUtil.addValue(PATH, "whitelist", keyPrefix, keyIndex,value);
            keyIndex++;
        }
        OperationLogUtil.genLogMsg("INFO","Delete "+delvalue+" From Whitelist" ,"success",null,null,null);
        try {
            Process process = Runtime.getRuntime().exec("sync");
            process.waitFor();
        } catch (Exception ex) {
            LogUtil.println(ex.toString());
        }
    }

}
