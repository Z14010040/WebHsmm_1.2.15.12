/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sansec.hsm.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zhaoxueqiang
 */
public class Language {
	private static final String LANGUAGE_FILE_EN = "/strings_en.properties";
	private static final String LANGUAGE_FILE_ZH = "strings_zh.properties";
	private static final String LANGUAGE_FILE_PATH = "/mnt/linux/tomcat8/webapps/";

	public static final String LANGUAGE_ZH = "zh";
	public static final String LANGUAGE_EN = "en";

	public static final String DEFAULT_LANGUAGE = "en";

	public static String get(String key){
		String val = null;
		String sLanguageFile = null;
		Properties pps = new Properties();

		String lan = Config.getLanguage();
		if(lan.equalsIgnoreCase(LANGUAGE_EN)){
			sLanguageFile = LANGUAGE_FILE_EN;
		}else{
			sLanguageFile = LANGUAGE_FILE_ZH;
		}
		
		String configPath = LANGUAGE_FILE_PATH + sLanguageFile;
		/*
		URL url = Language.class.getClassLoader().getResource(LANGUAGE_FILE_PATH + sLanguageFile);
		if(url != null){
			configPath = url.getPath().replaceAll("%20", " ").toLowerCase();
			System.out.println("language file path : " + configPath );
		}else{
			System.out.println("Can't get language file!!! : " + LANGUAGE_FILE_PATH + sLanguageFile);
			System.out.println(Language.class.getClassLoader().getResource(""));
			System.out.println(Language.class.getClassLoader().getResource("/"));

			System.out.println(Language.class.getResource(""));
			System.out.println(Language.class.getResource("/"));
			System.out.println(Language.class.getResource(LANGUAGE_FILE_PATH + sLanguageFile));
			return null;
		}*/

		if(! new File(configPath).exists()){
			System.out.println("language file : " + configPath  + " not exist!");
			return null;
		}

		try {
			pps.load(new FileInputStream(configPath));
		} catch (Exception ex) {
			System.out.println("load language file exception : " + ex.getMessage() );
		}
		

		val = pps.getProperty(key);
		System.out.println("Key : " + key + " get : " + val );

		return val;
	}
}
