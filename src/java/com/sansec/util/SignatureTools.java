package com.sansec.util;

import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignatureTools {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String DIGEST_ALG = "SHA1WITHRSA";
    public static final String version = "V2.11.0";

    public static void main(String[] args) throws Exception {
        byte[] by1 = {(byte)0x99,(byte)0xaa,(byte)0x3e,(byte)0x68,(byte)0xed,(byte)0x81,(byte)0x73,(byte)0xa0,(byte)0xee,(byte)0xd0,(byte)0x66,(byte)0x84};
        byte[] by2 = {(byte)0x99,(byte)0xaa,(byte)0x3e,(byte)0x68,(byte)0xed,(byte)0x81,(byte)0x73,(byte)0xa0,(byte)0xee,(byte)0xd0,(byte)0x66,(byte)0x84};
        byte[] by3 = ByteTools.byteMerger(by1,by2);
        signFile(by3,"src/java/com/sansec/util/swhsmbak2.dat");
        Boolean verityResult= vertifyFile(true,"src/java/com/sansec/util/swhsmbak2.dat");
    }
    public static void signFile(byte[] bytes, String filePath) {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            if(out == null) {
                OperationLogUtil.genLogMsg("ERROR","Backup" ,"Failed","Data Backup Failed:Create Backup File Error.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
                throw new DeviceException("Data Backup Failed:Create Backup File Error!");
            }
            //字节缓冲，可以用来拼接字节数组
            ByteArrayOutputStream bOutput = new ByteArrayOutputStream();

            
            //计算签名，并将结果写到文件的开头
            //计算私钥对象
            String BackupPrivateKeyPath= "/mnt/linux/tomcat8/webapps/"+"BackupPrivateKey";
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BackupPrivateKeyPath));           
            Key key = (Key) ois.readObject();
            byte[] keyBytes =  key.getEncoded();
            ois.close();
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
  
            Signature signature;
            signature = Signature.getInstance(DIGEST_ALG);
            signature.initSign(priKey);
            
            String str = "Swxa-SecKMS-Card-Backup-Dat-File\n"+"Version:"+version+"\n";
            byte[] versionBytes = new byte[60];  //60个字节
            System.arraycopy(str.getBytes(), 0, versionBytes, 0, str.getBytes().length);
           
            bOutput.write(versionBytes);
            bOutput.write(ByteTools.intToByteArray(bytes.length)); //4个字节
            bOutput.write(bytes);
           
            signature.update(bOutput.toByteArray());
            byte[] signatureBytes = signature.sign();
            
            bOutput.write(ByteTools.intToByteArray(signatureBytes.length));// 4 个字节
            bOutput.write(signatureBytes);
                        
            out.write(bOutput.toByteArray());
            bOutput.close();
            out.close();
            OperationLogUtil.genLogMsg("INFO","Backup" ,"success",null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Boolean vertifyFile(Boolean flag,String filePath){
        Boolean vertifyResult = false;
        try{
            byte[] fileBytes = 	FileTools.toByteArray(filePath);//将整个备份文件读出来，组成一个字节数组
            if(flag){
                // ------将公钥从文件中读出来并获取公钥对象----------
                String BackupPublicKeyPath= "/mnt/linux/tomcat8/webapps/"+"BackupPublicKey";
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BackupPublicKeyPath));
                Key key = (Key) ois.readObject();
                ois.close();
                byte[] keyBytes =  key.getEncoded();
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PublicKey pubKey = keyFactory.generatePublic(keySpec);
                //------------结束----------

                //版本号的长度
                byte[] strByte = new byte[60];
                //这4个字节用来表示密文的长度
                byte[] intLengthBytes = new byte[4];
                System.arraycopy(fileBytes,60, intLengthBytes, 0,4);
                //密文的长度
                int length = ByteTools.byteArrayToInt(intLengthBytes);
                //用来被签名的数据
                byte[] plainBytes = new byte[64+length];
                System.arraycopy(fileBytes,0, plainBytes, 0,plainBytes.length);
                //签名数据
                byte[] signatureBytes = new byte[fileBytes.length-plainBytes.length-4];
                System.arraycopy(fileBytes,plainBytes.length+4, signatureBytes, 0,signatureBytes.length);

                //--------验证签名，返回字节流-------
                Signature signature;
                signature = Signature.getInstance(DIGEST_ALG);
                signature.initVerify(pubKey);
                signature.update(plainBytes);
                
                if(signature.verify(signatureBytes)){
                    vertifyResult=true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vertifyResult;
    }
}
