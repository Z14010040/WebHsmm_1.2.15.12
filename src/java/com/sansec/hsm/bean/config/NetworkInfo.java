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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;

/**
 *
 * @author root
 */
public class NetworkInfo {
	 public static final String operationname = "NETWORK MANAGEMENT";
    public static final int MAX_ETH_COUNT = 4;
    public static final String BOND0 = "/etc/sysconfig/network-scripts/ifcfg-bond0";
    public static final String ETH = "/etc/sysconfig/network-scripts/ifcfg-eth";
    public static final String NETCONFIG = "/mnt/linux/etc/network.param";
    public static final String NETEXEC = "/mnt/linux/etc/network.sh";
    public static final String ROUTE = "/etc/sysconfig/network-scripts/routes";
    
    public static final String BOND0NOBOND = "/etc/sysconfig/network-scripts/ifcfg-bond0.bond";
    public static final String ETHNOBONDSUFFIX = ".nobond";
    public static final String ETHBONDSUFFIX = ".bond";
    public static final String WEBCONFXML = "/mnt/linux/tomcat6/conf/server.xml";
    public static final String WEBCONFCOPY = "/mnt/linux/tomcat6/conf/ex.server.xml";
    public static final int WEBETH = 1;
    // index=0 -> eth0, index=1 -> eth1
    private int index;
    private int nEth = 0;//非绑定网口数量
    private String name;
    private String ip;
    private String mask;
    private String gateway;
    private boolean bond = false;
    private boolean webbond = false;
    
    public NetworkInfo() {
   
    }

    public NetworkInfo(int index, String name, String ip, String mask, String gateway) {
        this.index = index;
        this.name = name;
        this.ip = ip;
        this.mask = mask;
        this.gateway = gateway;
    }


    public String getIp() {
        return ip;
    }

    public String getMask() {
        return mask;
    }

    public String getGateway() {
        return gateway;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

   
    
    
  public String getBondingIP(String  path){
        
        String sXML = new String();
        String sLine = new String();
        String sIP = new String();
        int index,indextmp;
       
        try
        {
            if (path.isEmpty()) {
                path=WEBCONFXML;
            }
            File file = new File(path);
            if(!file.exists()) {
   //             OperationLogUtil.println(operationname,"配置文件不存在.");
                return "0.0.0.0";
            }
            BufferedReader rXML = new BufferedReader(new FileReader(file));
            while((sLine = rXML.readLine())!=null){
                sXML += sLine+"\n";
                if((index = sLine.indexOf("address"))>-1)
                {
                    index=sLine.indexOf("\"",index);
                    indextmp=sLine.indexOf("\"",index+1);
                    sIP=sLine.substring(index+1,indextmp);                 
                }
            }
            rXML.close();
        }     
        catch (Exception ex) 
        {
 //           OperationLogUtil.println(operationname,"读取网页管理配置失败.");
        }  
        
        return sIP;
 }
  
  public boolean setBondingIP(String newIP,String  path){
        
        
        String sXML = new String();
        String sLine = new String();
        String sLeft = new String();
        String sRight = new String();
        int index,indextmp;
        
        try
        {
            File file = new File(path);
            BufferedReader rXML = new BufferedReader(new FileReader(file));
            while((sLine = rXML.readLine())!=null){
                
                if((index = sLine.indexOf("URIEncoding"))>-1)
                {
                     if((index = sLine.indexOf("address"))>-1)
                    {
                        indextmp=sLine.indexOf("\"",sLine.indexOf("\"",index)+1);
                        sLeft=sLine.substring(0, index);
                        sRight=sLine.substring(indextmp+1);
                    }
                     else 
                     {
                         indextmp=sLine.lastIndexOf('/');
                         sLeft=sLine.substring(0, indextmp);
                         if(!sLeft.endsWith(" "))
                         {
                             sLeft=sLeft+" ";
                         }
                         sRight=sLine.substring(indextmp);
                     }
                     if(newIP.length()>0)
                     {
                         newIP="address=\""+newIP+"\"";
                     }
                     sLine=sLeft+newIP+sRight;
                }
                
                sXML += sLine+"\n";
            }
            rXML.close();
            BufferedWriter wXML = new BufferedWriter(new FileWriter(file));
            wXML.write(sXML);
            wXML.close();
        }     
        catch (Exception ex) 
        {
            OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Configure Network Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

        }  
        return true;
 }
    
  
    public void setWebBond(boolean webbond) {
        this.webbond = webbond;
    }
    
    public boolean getWebBond() {
        return webbond;
    }

    
    public boolean getBond() {
        return bond;
    }

    public void setBond(boolean bond) {
        this.bond = bond;
    }

    public void store() throws NoPrivilegeException, DeviceException {
        LogUtil.println("store(): "+this.toString());
        Privilege rights = new Privilege();
        if( !rights.check(Privilege.PRIVILEGE_MODIFY_NETWORK_CONFIG) ) {
            OperationLogUtil.genLogMsg("ERROR","Check Privilege" ,"Failed",Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_NETWORK_CONFIG), ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_MODIFY_NETWORK_CONFIG));
        }
         Properties props=System.getProperties();

             
        String path;
        if(props.getProperty("os.arch").indexOf("arm")<0)  
        {
        if(bond) {
            try {
                File file1 = new File(BOND0NOBOND);    
                File file2 = new File(BOND0);
                boolean mv = file1.renameTo(file2);
                
                for (int i=0; i<MAX_ETH_COUNT; i++) {
                    String oldPath = ETH + i + ETHBONDSUFFIX;
                    String newPath = ETH + i;
                    ConfigUtil.copyFile(oldPath, newPath);
                }
             } catch (Exception ex) {
                LogUtil.println("cp eth config error: " + ex);
                throw new DeviceException("修改网络配置文件错误-1!");
            }            
            
            path = BOND0;
        } else {            
            if(index == 0) {
                try {
                    File file1 = new File(BOND0NOBOND);   
                    File file2 = new File(BOND0);
                    file2.renameTo(file1);

                    for (int i=0; i<MAX_ETH_COUNT; i++) {
                        String oldPath = ETH + i + ETHNOBONDSUFFIX;
                        String newPath = ETH + i;
                        ConfigUtil.copyFile(oldPath, newPath);
                    }
                } catch (Exception ex) {
                    LogUtil.println("cp eth config error: " + ex);
                    throw new DeviceException("修改网络配置文件错误-2!");
                }         
            }
            
            path = ETH + index;
        }
        }
        else
        {
            path = NETCONFIG;
        }
        Properties p = new Properties();
        File eth = new File(path);
        if( !eth.exists()) {
             throw new DeviceException("文件不存在[ "+eth.getAbsolutePath()+"]");
        }

        try {
            FileInputStream fis = new FileInputStream(eth);
            p.load(fis);
            fis.close();
            p.setProperty("IPADDR", ip);
            p.setProperty("NETMASK", mask);
            p.setProperty("GATEWAY", gateway);
            //add by zxq 2014-5-15 for stroe file
            //Properties.store add '\' before '='
            //FileOutputStream fos = new FileOutputStream(eth);
            //p.store(fos, null);
            //fos.close();
            //
            FileWriter writer = new FileWriter(path);
            Enumeration<Object> enums = p.keys();
            while(enums.hasMoreElements()){
            	String key = (String)enums.nextElement();
            	writer.write(key+"="+p.getProperty(key)+"\n");
            }
            writer.flush();
            writer.close();
            //end add by zxq 2014-5-15
            
            //add by GAO, 2012/5/10
            if (props.getProperty("os.arch").indexOf("arm")<0 &&(!gateway.isEmpty()) && (gateway.compareTo("0.0.0.0") != 0)) {
                this.storeRoute();
            }
            
            Process process = Runtime.getRuntime().exec("sync");
            process.waitFor();
            
            if (getBond()||this.index == WEBETH)
            {
                String webip = getBondingIP(WEBCONFXML);                                                                                                                                                                             
           
            if( !getWebBond() )
            {
                
               if(webip.compareTo("")!=0 )
               {   
            //       OperationLogUtil.println(operationname,"不绑定网页服务ip.");
           //        OperationLogUtil.println(operationname,"需重启管理服务，解除.");
          //         OperationLogUtil.println(operationname,"需重启管理服务,web."+webip);
          //         setBondingIP("",WEBCONFXML);
               //     process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/mnt/linux/tomcat6/restart &"});
               //     process.waitFor();                   
               //    OperationLogUtil.println(operationname,"重启管理服务.");
               }
            }
            else
            {
                
                String bondingIP ;
                bondingIP = this.getIp();
                if(webip.compareTo(bondingIP)!=0)
                {
                    OperationLogUtil.genLogMsg("INFO",operationname ,"success",null,null,null);
          //         OperationLogUtil.println(operationname,"需重启管理服务，ip."+bondingIP);
           //         OperationLogUtil.println(operationname,"需重启管理服务,web."+webip);
                   setBondingIP(bondingIP,WEBCONFXML);
            //        process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/mnt/linux/tomcat6/restart &"});
            //        process.waitFor();
                    
            //        OperationLogUtil.println(operationname,"重启管理服务.");
               }
            }
            }
            if (bond)
            OperationLogUtil.genLogMsg("INFO",operationname ,"success",null,null,null);
            else
            OperationLogUtil.genLogMsg("INFO",operationname ,"success",null,null,null);
        } catch(Exception e) {
            OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Configure Network Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

            throw new DeviceException(""+e);
        }
    }


    private void storeRoute() throws Exception { 
        FileOutputStream fos = new FileOutputStream(ROUTE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write("default "+gateway+" - -\n");
        bw.close();
        fos.close();
    }

    public void restart() throws DeviceException, NoPrivilegeException {
        // 检查权限
        Privilege rights = new Privilege();
        if( !rights.check(Privilege.PRIVILEGE_RESTART_NETWORK) ) {
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_RESTART_NETWORK));
        }

        try {
            Properties props=System.getProperties();
            Process process;
             if(props.getProperty("os.arch").indexOf("arm")<0)  
             {
            process = Runtime.getRuntime().exec("sudo /etc/init.d/network restart");
            process.waitFor();
             }
             else
             {
                 process = Runtime.getRuntime().exec(NETEXEC);
                 process.waitFor();
             }
            
            OperationLogUtil.genLogMsg("INFO",operationname ,"success",null,null,null);
            String webip = getBondingIP(WEBCONFXML);
            String currentIp = getBondingIP(WEBCONFCOPY);

            if(nEth != 1 && webip.compareTo(currentIp) != 0 )
            {
           //     OperationLogUtil.println(operationname,"不绑定网页服务.");
                             
          //         OperationLogUtil.println(operationname,"需重启管理服务，解除.");
          //         OperationLogUtil.println(operationname,"需重启管理服务,web."+webip);
                    process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/mnt/linux/tomcat6/restart &"});
                    process.waitFor();  
                    ConfigUtil.copyFile(WEBCONFXML, WEBCONFCOPY);
            }
    /*        else
            {
                OperationLogUtil.println(operationname,"Limit Web Management IP Succeed.");
                String bondingIP ;
                if( interlist.get(0).getBond())
                    bondingIP=interlist.get(0).getIp();
                else
                    bondingIP=interlist.get(1).getIp();
                if(webip.compareTo(bondingIP)!=0)
                {
       //            OperationLogUtil.println(operationname,"需重启管理服务，ip."+bondingIP);
         //           OperationLogUtil.println(operationname,"需重启管理服务,web."+webip);
                   setBondingIP(bondingIP,WEBCONFXML);
                    process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/mnt/linux/tomcat6/restart &"});
                    process.waitFor();
                    
                    OperationLogUtil.println(operationname,"Restart Network Succeed.");
               }
            }*/
        } catch (Exception ex) {
            LogUtil.println("sudo /etc/init.d/network restart error: " + ex);
            OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Restart Network Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
            throw new DeviceException("重启网络错误!");
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
    }
    
    public List<NetworkInfo> getNetworks()  throws NoPrivilegeException, DeviceException {
        Privilege rights = new Privilege();
        if( !rights.check(Privilege.PRIVILEGE_SHOW_NETWORK_CONFIG) ) {
            throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_NETWORK_CONFIG));
        }
        
        Properties props=System.getProperties();
                    
           
        List<NetworkInfo> list = new ArrayList<NetworkInfo>(MAX_ETH_COUNT);
        Properties p = new Properties();
        
        File bond0 = new File(BOND0);
        if(bond0.exists()) {
            try {
                FileInputStream fis = new FileInputStream(bond0);
                p.load(fis);
                String tName = p.getProperty("DEVICE");
                String tIp = p.getProperty("IPADDR");
                String tMask = p.getProperty("NETMASK");
                String tGateway = p.getProperty("GATEWAY");
                NetworkInfo net = new NetworkInfo(0, tName, tIp, tMask, tGateway);
                net.setBond(true);
                LogUtil.println("load() -> eth0: "+net.toString());
                list.add(net);
                fis.close();
            } catch (Exception ex) {
                    // can't do exception
            }
            p.clear();
            return list;
        }
        
         if(props.getProperty("os.arch").indexOf("arm")<0)  
         {
        for(int i=0; i<MAX_ETH_COUNT; i++) {
            File eth = new File(ETH + i);
            if(eth.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(eth);
                    p.load(fis);
                    String tName = p.getProperty("DEVICE");
                    String tIp = p.getProperty("IPADDR");
                    String tMask = p.getProperty("NETMASK");
                    String tGateway = p.getProperty("GATEWAY");
                    NetworkInfo net = new NetworkInfo(i, tName, tIp, tMask, tGateway);
                    net.setBond(false);
                    LogUtil.println("load() -> eth0: "+net.toString());
                    list.add(net);
                    fis.close();
                    nEth ++;
                } catch (Exception ex) {
                    // can't do exception
                }
            } else {
                break;
            }
            p.clear();            
        }}
         else
         {
             File eth = new File(NETCONFIG);
            if(eth.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(eth);
                    p.load(fis);
                    String tName = p.getProperty("DEVICE");
                    String tIp = p.getProperty("IPADDR");
                    String tMask = p.getProperty("NETMASK");
                    String tGateway = p.getProperty("GATEWAY");
                    NetworkInfo net = new NetworkInfo(0, tName, tIp, tMask, tGateway);
                    net.setBond(false);
                    LogUtil.println("load() -> eth0: "+net.toString());
                    list.add(net);
                    fis.close();
                    nEth ++;
                } catch (Exception ex) {
                    // can't do exception
                }
                p.clear();   
            }
         }

        return list;
    }


    @Override
    public String toString() {
        return "index   = "+index + "\n"+
               "name    = "+name +"\n"+
               "DEVICE  = "+name + "\n"+
               "IPADDR  = "+ip + "\n"+
               "NETWORK = "+mask + "\n"+
               "GATEWAY = "+gateway;
    }


    public static void main(String[] args) throws NoPrivilegeException, DeviceException {
        List<NetworkInfo> list = new NetworkInfo().getNetworks();
        System.out.println(list);
    }
}
