/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sansec.hsm.bean.config;

import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.util.ConfigUtil;

/**
 *
 * @author root
 */
public class DeviceInfo {
    public static final String PATH = "/etc/swhsm/device.ini";
    private String manufacture;         // 生产产商
    private String deviceType;          // 设备型号
    private String productType;         // 产品型号
    private String version;             // 版本
    private String serialNumber;        // 序列号
    public DeviceInfo() {
        
    }
    
    public String getManufacture() {
        return manufacture;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getProductType() {
        return productType;
    }

    public String getVersion() {
        return version;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void load() throws DeviceException {
        manufacture = ConfigUtil.getValue(PATH, "factory", "manufacturer");
        deviceType = ConfigUtil.getValue(PATH, "factory", "type");
        productType = ConfigUtil.getValue(PATH, "factory", "pn");
        serialNumber = ConfigUtil.getValue(PATH, "factory", "sn");
        version = ConfigUtil.getValue(PATH, "factory", "version");
    }

    

    @Override
    public String toString() {
        return "Manufacture     = "+manufacture+"\n"+
               "DeviceType      = "+deviceType+"\n"+
               "ProductType     = "+productType+"\n"+
               "SerialNumber    = "+serialNumber+"\n"+
               "Version         = "+version+"\n";
    }

    public static void main(String[] args) throws DeviceException {
        DeviceInfo info = new DeviceInfo();
        info.load();
        System.out.println(info);
    }
}
