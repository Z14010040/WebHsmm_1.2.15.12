//package com.sansec.util;
//
//import com.sansec.hsm.bean.*;
//import sun.misc.BASE64Encoder;
//
//import java.io.FileInputStream;
//import java.io.ObjectInputStream;
//import java.security.Key;
//
//public class FileTest {
////    @Test
////    public static void fileSign() {
////        ObjectInputStream ois = null;
////        try {
////            ois = new ObjectInputStream(new FileInputStream("BackupPrivateKey"));
////            Key key = (Key) ois.readObject();
////            String privatekey =(new BASE64Encoder()).encodeBuffer(key.getEncoded());
////            System.out.println(privatekey);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////    }
//
//    public static void main(String[] args) {
//        ObjectInputStream ois = null;
//        try {
//            String s0 = FileTest.class.getResource("BackupPrivateKey").getPath();
//            System.out.println(s0);
//            ois = new ObjectInputStream(new FileInputStream(s0));
//            Key key = (Key) ois.readObject();
//            String privatekey =(new BASE64Encoder()).encodeBuffer(key.getEncoded());
//            System.out.println(privatekey);
//            String s1 = FileTest.class.getResource("").getPath();
//            System.out.println(s1);
//            String s2 = FileTest.class.getResource("/").getPath();
//            System.out.println(s2);
//            String s3 = FileTest.class.getResource("/com").getPath();
//            System.out.println(s3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
