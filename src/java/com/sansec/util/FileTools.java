package com.sansec.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Arrays;

public class FileTools {

	/**
	 * 将文件读出来，并将转化成字节数组
	 */

	public static byte[] toByteArray(String filename) throws IOException{

		File f = new File(filename);
		if(!f.exists()){
			throw new FileNotFoundException(filename);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());
		BufferedInputStream in = null;
		try{
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while(-1 != (len = in.read(buffer,0,buf_size))){
				bos.write(buffer,0,len);
			}
			return bos.toByteArray();
		}catch (IOException e) {
			e.printStackTrace();
			throw e;
		}finally{
			try{
				in.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}
	/**
	 * 判断文件是不是以指定的字符串开头
	 */
	public static Boolean fileBeginWithStr(String filePath,String str){
		Boolean flag = false;
		try {
			byte[] fileBytes = 	FileTools.toByteArray(filePath);
			byte[] strByte = new byte[str.getBytes().length];
			System.arraycopy(fileBytes,0, strByte, 0,strByte.length);
			if(Arrays.equals(str.getBytes(),strByte)){
				flag = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 获取文件的修改时间或创建时间
	 * @param fullFileName
	 */
	public static String getModifiedTime(String fullFileName){
		File file = new File(fullFileName);
        if(file.exists()){
            ZonedDateTime zt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault());
//            String time = zt.toLocalDateTime().toString().replace("T"," ");
//            System.out.println("Backup file create time or last modified time: "+time);

//            return time.split("\\.")[0];
			System.out.println("Backup file create time or last modified time: "+zt.toString());
			return zt.toString();
        }else {
            return null;
        }

	}
}