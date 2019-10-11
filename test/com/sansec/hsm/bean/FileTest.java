package com.sansec.hsm.bean;

import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.Key;

public class FileTest {
//    @Test
//    public static void fileSign() {
//        ObjectInputStream ois = null;
//        try {
//            ois = new ObjectInputStream(new FileInputStream("BackupPrivateKey"));
//            Key key = (Key) ois.readObject();
//            String privatekey =(new BASE64Encoder()).encodeBuffer(key.getEncoded());
//            System.out.println(privatekey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public static void main(String[] args) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("src/java/com/sansec/util/BackupPrivateKey"));
            Key key = (Key) ois.readObject();
            String privatekey =(new BASE64Encoder()).encodeBuffer(key.getEncoded());
            System.out.println(privatekey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
