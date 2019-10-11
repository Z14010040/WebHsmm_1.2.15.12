/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sansec.hsm.bean.config;

import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.util.ConfigUtil;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import debug.log.LogUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class MaintainInfo {
    public static final String PATH = "/etc/swhsm/device.ini";
    public static final String operationname = "SHOW SUPPORT INFO";
    private String systemName;          // 系统名称
    private String company;             // 公司名称
    private String department;          // 所属部门
    private String contact;             // 联系人
    private String telephone;           // 固定电话
    private String mobile;              // 手机号码
    private String mail;                // 邮箱地址
    public MaintainInfo() {
        
    }

    public MaintainInfo(String systemName, String company, String department, String contact, String telephone, String mobile, String mail) {
        this.systemName = systemName;
        this.company = company;
        this.department = department;
        this.contact = contact;
        this.telephone = telephone;
        this.mobile = mobile;
        this.mail = mail;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getCompany() {
        return company;
    }

    public String getDepartment() {
        return department;
    }

    public String getContact() {
        return contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMail() {
        return mail;
    }

    public void load() {
		systemName = ConfigUtil.getValue(PATH, "maintain", "system");
		company = ConfigUtil.getValue(PATH, "maintain", "company");
		department = ConfigUtil.getValue(PATH, "maintain", "department");
		contact = ConfigUtil.getValue(PATH, "maintain", "contactor");
        telephone = ConfigUtil.getValue(PATH, "maintain", "tel");
		mobile = ConfigUtil.getValue(PATH, "maintain", "mobile");
		mail = ConfigUtil.getValue(PATH, "maintain", "email");
    }

    public void store() throws DeviceException, NoPrivilegeException{
        /* 获取权限 */
        Privilege rights = new Privilege();
        LogUtil.println("Privilege: "+rights);
        /* 检查权限 */
        if( !rights.check(Privilege.PRIVILEGE_MODIFY_MAINTAIN_INFO) ) {
            OperationLogUtil.genLogMsg("ERROR","Check Privilege" ,"Failed",Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_MAINTAIN_INFO), ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_MAINTAIN_INFO));
        }
        
        ConfigUtil.setValue(PATH, "maintain", "system", systemName);
	ConfigUtil.setValue(PATH, "maintain", "company", company);
	ConfigUtil.setValue(PATH, "maintain", "department", department);
	ConfigUtil.setValue(PATH, "maintain", "contactor", contact);
        ConfigUtil.setValue(PATH, "maintain", "tel", telephone);
	ConfigUtil.setValue(PATH, "maintain", "mobile", mobile);
	ConfigUtil.setValue(PATH, "maintain", "email", mail);
        
         try {
            Process process = Runtime.getRuntime().exec("sync");
            process.waitFor();
             OperationLogUtil.genLogMsg("INFO",operationname ,"success",null,null,null);
        } catch (Exception ex) {
            LogUtil.println(ex.toString());
             OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Modify Support Info Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
        }
    }

    @Override
    public String toString() {
        return "SystemName      = "+systemName+"\n"+
               "Company         = "+company+"\n"+
               "Department      = "+department+"\n"+
               "Contact         = "+contact+"\n"+
               "Telephone       = "+telephone+"\n"+
               "Mobile          = "+mobile+"\n"+
               "Mail            = "+mail+"\n";
    }

    public static void main(String[] args){
        MaintainInfo info = new MaintainInfo();
        info.load();
        System.out.println(info);
        info.company = "Sansec";
        info.department = "生产部";
        info.contact = "lfj";
        info.mail = "lfj_jack@163.com7";
        try {
            info.store();
        } catch (DeviceException ex) {
            Logger.getLogger(MaintainInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoPrivilegeException ex) {
            Logger.getLogger(MaintainInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

        info.load();
        System.out.println(info);
    }
    /**
     [factory]
manufacturer=SANSEC
type=SJJ0930
pn=SH2U11-SC5S
sn=2010101413301052
version=1.02.0012
[maintain]
system=XX  11234
company=Sansec
contactor=
department=Sansec
tel=123123123
mobile=133333333336
email=lfj_jack@163.com7

     */
}
