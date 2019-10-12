/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sansec.hsm.bean;

import com.sansec.hsm.lib.kmapi;
import debug.log.LogUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 读配置文件
 *
 * @author root
 */
public class Config {
	// 管理员数目

	private static int maxManagerCount = 3;
	private static String deviceType = "SJJ1012";
	//
	private static boolean bSecKeyByMK = true;
	private static boolean bECCSupport = true;
	private static final String DEFAULT_CONFIG_FILE = "serverconf.properties";
	public static final String DEFAULT_WEBHSMM_CONFIG_FILE = "/mnt/linux/etc/webhsmm.properties";
	private static final String DEFAULT_SIMPLE_FACTORY_NAME = "三未信安";
	private static final String DEFAULT_FACTORY_NAME = "三未信安科技发展有限公司";
	private static final String DEFAULT_HOME_PAGE_LINK = "http://www.sansec.com.cn";
	private static String simpleFactoryName = "";
	private static String factoryName = "";
	private static String homePageLink = "http://www.jit.com.cn/"; //JIT
	private static String sCurrentLanguage = "";
	private static final String DEFAULT_LANGUAGE = "en";
	private static final String CONFIG_TAG_LANGUAGE = "language";
        
        public static final String CONFIG_TAG_WEBLOGIN = "admin"; 
	public static final String DEFAULT_WEBPASSWORD = "N2MyMjJmYjI5MjdkODI4YWYyMmY1OTIxMzRlODkzMjQ4MDYzN2MwZA==";

	public static String getLanguage() {
		Properties pps = new Properties();
		try {
			pps.load(new FileInputStream(DEFAULT_WEBHSMM_CONFIG_FILE));

			sCurrentLanguage = pps.getProperty(CONFIG_TAG_LANGUAGE);

			//LogUtil.println("sCurrentLanguage : " + sCurrentLanguage);
			if (sCurrentLanguage == null) {
				System.out.println("DEFAULT_LANGUAGE : " + DEFAULT_LANGUAGE);
				return DEFAULT_LANGUAGE;
			} else {
				System.out.println("sCurrentLanguage : " + sCurrentLanguage);
				return sCurrentLanguage;
			}
		} catch (Exception e) {
		}
		return DEFAULT_LANGUAGE;
	}

	public static boolean setLanguage(String newLanguage) {
		System.out.println("setLanguage : " + newLanguage);

		Properties pps = new Properties();
		try {
			pps.load(new FileInputStream(DEFAULT_WEBHSMM_CONFIG_FILE));

			pps.setProperty(CONFIG_TAG_LANGUAGE, newLanguage);
			OutputStream out = new FileOutputStream(DEFAULT_WEBHSMM_CONFIG_FILE);

			pps.store(out, null);

			return true;
		} catch (Exception e) {
			LogUtil.println(e.getLocalizedMessage());
		}
		return false;
	}

	public static boolean isSecKeyByMK() {
		LogUtil.println("bSecKeyByMK:" + bSecKeyByMK);
		return bSecKeyByMK;
	}

	public static boolean isECCSupport() {
		LogUtil.println("bECCSupport:" + bECCSupport);
		return bECCSupport;
	}

	public static String getSimpleFactoryName() {
		if (simpleFactoryName.isEmpty()) {
			return DEFAULT_SIMPLE_FACTORY_NAME;
		} else {
			return simpleFactoryName;
		}
	}

	public static String getFactoryName() {
		if (factoryName.isEmpty()) {
			return DEFAULT_FACTORY_NAME;
		} else {
			return factoryName;
		}
	}

	public static boolean isShowHomePageLink() {
		return true;
	}

	public static String getHomePageLink() {
		if (homePageLink.isEmpty()) {
			return DEFAULT_HOME_PAGE_LINK;
		} else {
			return homePageLink;
		}
	}

	public static int getMaxManangerCount() {
		LogUtil.println("maxManagerCount:" + maxManagerCount);
		return maxManagerCount;
	}

	public static boolean SetMaxManangerCount(int newManagerCount) {
		LogUtil.println("maxManagerCount:" + maxManagerCount);
//		if (newManagerCount < 4) {
//			return false;
//		}
		newManagerCount = 3;

		try {
			InputStream inStream = new FileInputStream("/mnt/linux/etc/webhsmm.properties");
			Properties p = new Properties();
			p.load(inStream);
			inStream.close();
			p.setProperty("max_manager_count", "" + newManagerCount);

			FileWriter writer = new FileWriter("/mnt/linux/etc/webhsmm.properties");
			Enumeration<Object> enums = p.keys();
			while (enums.hasMoreElements()) {
				String key = (String) enums.nextElement();
				writer.write(key + "=" + p.getProperty(key) + "\n");
			}
			writer.flush();
			writer.close();


		} catch (Exception ex) {
			LogUtil.println("Config.initialize() Exception:" + ex.toString());
			return false;
		}

		maxManagerCount = newManagerCount;
		return true;
	}

	public static String getDeviceType() {
		LogUtil.println("deviceType:" + deviceType);
		return "";
	}
	private static boolean bImpExpKeysByICSupport = false;

	public static boolean isImpExpKeysByICSupport() {
		LogUtil.println("bImpExpKeysByICSupport:" + bImpExpKeysByICSupport);
		return bImpExpKeysByICSupport;
	}

	public static void initialize() {

		int rv = kmapi.INSTANCE.KM_LoadKeys();
		// load key
		LogUtil.println("Conifig.java->initialize(): load keys ...");
		System.out.println("Conifig.java->initialize(): load keys ...");
		if (rv != HSMError.SDR_OK) {
			LogUtil.println("Conifig.java->initialize(): load keys error!");
		}

		// load config
		//InputStream inStream = new Config().getClass().getClassLoader().getResourceAsStream("config.properties");
//		try {
//			InputStream inStream = new FileInputStream("/mnt/linux/etc/webhsmm.properties");
//			Properties p = new Properties();
//			p.load(inStream);
////			maxManagerCount = Integer.parseInt((String) p.get("max_manager_count"));
//			maxManagerCount = 3;
//			deviceType = (String) p.get("device_type");
//
//			int ecc = Integer.parseInt((String) p.get("ecc_support"));
//			if (ecc > 0) {
//				bECCSupport = true;
//			} else {
//				bECCSupport = false;
//			}
//
//			int ic = Integer.parseInt((String) p.get("impexpkeysbyic"));
//			if (ic > 0) {
//				bImpExpKeysByICSupport = true;
//			} else {
//				bImpExpKeysByICSupport = false;
//			}
//
//		} catch (Exception ex) {
//			LogUtil.println("Config.initialize() Exception:" + ex.toString());
//		}
	}

	public static void main(String[] args) {
		//Config.initialize();

		System.out.println("config initialize ok");
	}
}
