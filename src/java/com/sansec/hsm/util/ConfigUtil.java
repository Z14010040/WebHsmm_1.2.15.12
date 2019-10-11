package com.sansec.hsm.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件读写
 * @author root
 */
public class ConfigUtil {

    public static final String path = "swsds.ini";

    public static void testGet() {
        String str = null;
        str = getValue(path, "HSM1", "ip");
        System.out.println(str);
        str = getValue(path, "HSM", "ip");
        System.out.println(str);
        str = getValue(path, "timeout", "service");
        System.out.println(str);
        str = getValue(path, "connectionpool", "PoolSize1");
        System.out.println(str);
    }

    public static void testSet() {
        String str = null;
        setValue(path, "HSM1", "ip", "192.168.0.601");
        str = getValue(path, "HSM1", "ip");
        System.out.println(str);

        setValue(path, "HSM", "ip", "192.168.0.501");
        str = getValue(path, "HSM", "ip");
        System.out.println(str);

        setValue(path, "timeout", "service", "701");
        str = getValue(path, "timeout", "service");
        System.out.println(str);

        setValue(path, "connectionpool", "PoolSize1", "21");
        str = getValue(path, "connectionpool", "PoolSize1");
        System.out.println(str);
    }

    public static void testAdd() {
        String str = null;

        for (int i = 1; i < 6; i++) {
            addValue(path, "connectionpool", "PoolSize", i, "192.168.0." + (100 + i));
        }

        str = getValue(path, "connectionpool", "PoolSize1");
        System.out.println(str);

        for (int i = 2; i < 6; i++) {
            addValue(path, "servicemode", "mode", i, "" + i);
        }
        str = getValue(path, "servicemode", "mode2");
        System.out.println(str);
    }

    public static void testDel() {
        delValue(path, "servicemode", "mode", 1);
    }

    public static void main(String[] args) {
        //testGet();
        //testSet();
        //testAdd();
        testDel();
    }

    /**
     * 设置配置项键值
     * @param filePath 配置文件路径
     * @param item 配置项
     * @param key 键
     * @param value 键对应的值
     */
    public static void setValue(String filePath, String item, String key, String value) {
        if (getValue(filePath, item, key) == null) {
            // 不存在修改啥
            return;
        }
        key = key.toLowerCase();
        String buf = "";
        //item = "["+item+"]";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line;
            String tmpLine;

            boolean findItemFlag = false;
            while ((line = br.readLine()) != null) {
                tmpLine = line.trim().toLowerCase();
                //以#开始的一行为注释
                if (tmpLine.startsWith("#")) {
                    buf += line + "\n";
                    continue;
                }
                // find item
                //line = line.toLowerCase();
                if (tmpLine.equals("[" + item + "]")) {
                    findItemFlag = true;
                    buf += line + "\n";
                    continue;
                }

                // find key
                if (findItemFlag && tmpLine.startsWith(key)) {
                    buf += line.substring(0, line.indexOf("=") + 1);
                    buf += " " + value + "\n";
                    //break;
                } else {
                    buf += line + "\n";
                }
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
            bw.write(buf);
            bw.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }

    /**
     * 获取配置项键值
     * @param filePath 配置文件路径
     * @param item 配置项
     * @param key 键
     * @return
     */
    public static String getValue(String filePath, String item, String key) {
        String str = null;
        key = key.toLowerCase();
        //item = "["+item+"]";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line;
            String tmpLine;
            boolean findItemFlag = false;
            while ((line = br.readLine()) != null) {
                tmpLine = line.trim().toLowerCase();
                //以#开始的一行为注释
                if (tmpLine.startsWith("#")) {
                    continue;
                }
                //line = line.toLowerCase();
                if (tmpLine.equals("[" + item + "]")) {
                    findItemFlag = true;
                    continue;
                }
                if (findItemFlag && tmpLine.startsWith(key)) {
                    str = line.substring(line.indexOf("=") + 1);
                    str = str.trim();
                    break;
                } else if (findItemFlag && line.contains("[")) {
                    break;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return str;
    }

    /**
     * 添加配置项键值
     * @param filePath 配置文件路径
     * @param item 配置项
     * @param keyPrefix 键前缀
     * @param keyIndex 键序号
     * @param value 键对应的值
     */
    public static void addValue(String filePath, String item, String keyPrefix, int keyIndex, String value) {
        if (getValue(filePath, item, keyPrefix + keyIndex) != null) {
            setValue(filePath, item, keyPrefix + keyIndex, value);
            return;
        }

        String str = "";
        String temp = "[" + item + "]";
        item = temp;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line;

            boolean forInsert = false;
            while ((line = br.readLine()) != null) {
                //以#开始的一行为注释
                if (line.trim().startsWith("#")) {
                    str += line + "\n";
                    continue;
                }
                // find item
                line = line.toLowerCase();
                if (line.contains(item.toLowerCase())) {
                    forInsert = true;
                    str += line + "\n";
                    continue;
                }

                // ! find key
                if (forInsert && !line.contains(keyPrefix.toLowerCase())) {
                    str += keyPrefix + keyIndex + " = " + value + "\n";
                    forInsert = false;
                }

                str += line + "\n";
            }


            if (forInsert) {
                str += keyPrefix + keyIndex + " = " + value + "\n";
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
            bw.write(str);
            bw.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /**
     * 删除配置项键值
     * @param filePath 配置文件路径
     * @param item 配置项
     * @param keyPrefix 键前缀
     * @param keyIndex 键序号
     * @param value 键对应的值
     */
    public static void delValue(String filePath, String item, String keyPrefix, int keyIndex) {
        String key = keyPrefix + keyIndex;
        String str = "";
        item = "[" + item + "]";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line;

            List<String[]> list = new ArrayList<String[]>();

            boolean findItemFlag = false;
            boolean listSaveFlag = false;
            while ((line = br.readLine()) != null) {
                //以#开始的一行为注释
                if (line.trim().startsWith("#")) {
                    str += line + "\n";
                    continue;
                }
                // find item
                line = line.toLowerCase();
                if (line.contains(item.toLowerCase())) {
                    findItemFlag = true;
                    str += line + "\n";
                } else {
                    if (findItemFlag) {
                        if (!line.contains(keyPrefix)) {
                            //next item, so save
                            findItemFlag = false;
                            listSaveFlag = true;
                            int index = 1;
                            for (int i = 0; i < list.size(); i++) {
                                String tmpKey = list.get(i)[0];
                                if (tmpKey.equalsIgnoreCase(key)) {
                                    continue;
                                } else {
                                    str += keyPrefix + (index++) + " = " + list.get(i)[1] + "\n";
                                }
                            }

                            str += line + "\n";
                        } else {
                            //it's key, add to list
                            String[] tmp = new String[2];
                            tmp[0] = line.substring(0, line.indexOf("=")).trim();
                            tmp[1] = line.substring(line.indexOf("=") + 1).trim();
                            list.add(tmp);
                        }
                    } else {
                        str += line + "\n";
                    }
                }
            }

            if (!listSaveFlag) {
                int index = 1;
                for (int i = 0; i < list.size(); i++) {
                    String tmpKey = list.get(i)[0];
                    if (tmpKey.equalsIgnoreCase(key)) {
                        continue;
                    } else {
                        str += keyPrefix + (index++) + " = " + list.get(i)[1] + "\n";
                    }
                }
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
            bw.write(str);
            bw.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

   /**  
     *  复制单个文件  
     *  @param  oldPath  String  原文件路径  如：c:/fqf.txt  
     *  @param  newPath  String  复制后路径  如：f:/fqf.txt  
     *  @return  boolean  
     */  
   public static void  copyFile(String  oldPath,  String  newPath)  {  
       try  {  
//           int  bytesum  =  0;  
           int  byteread  =  0;  
           File  oldfile  =  new  File(oldPath);  
           if  (oldfile.exists())  {  //文件存在时  
               InputStream  inStream  =  new  FileInputStream(oldPath);  //读入原文件 
               FileOutputStream  fs  =  new  FileOutputStream(newPath);  
               byte[]  buffer  =  new  byte[1444];  
//               int  length;  
               while  (  (byteread  =  inStream.read(buffer))  !=  -1)  {  
//                   bytesum  +=  byteread;  //字节数  文件大小  
//                   System.out.println(bytesum);  
                   fs.write(buffer,  0,  byteread);  
               }  
               inStream.close();  
           }  
       }  
       catch  (Exception  e)  {  
           //System.out.println("复制文件出错");  
           //e.printStackTrace();  
       }  
   }

}
