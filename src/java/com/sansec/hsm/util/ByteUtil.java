package com.sansec.hsm.util;

import java.math.BigInteger;

public class ByteUtil {

    /**
     * 字节数组转换成java中的字符串
     * @param bytes
     * @return
     */
    public static String ByteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length && bytes[i] != (byte) 0; i++) {
            sb.append((char) (bytes[i]));
        }

        return sb.toString();
    }

    /**
     * 字符串转换成字节流（字节数组）型
     * @param str
     * @return
     */
    public static byte[] StringToByte(String str, int size) {
        int len = (str == null) ? 0 : str.length();
        byte[] bytes = new byte[size];
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                bytes[i] = (byte) (str.charAt(i));
            }
        }
        
        return bytes;
    }

    public static boolean differByte(byte[] a, byte[] b, int count) {
        boolean flag = false;
        int length = a.length < b.length ? a.length : b.length;
        if (count <= length) {
            for (int i = 0; i < count; i++) {
                if (a[i] != b[i]) {
                    flag = true;
                    //System.out.println("内容不同");
                    break;
                }
            }
        }
        //System.out.println("内容相同");
        return flag;
    }

    /*public static byte[] briArrayToByte(byte[][] b, int line, int list) {
    byte[] temb = new byte[line * list];
    for (int i = 0; i < line; i++) {
    for (int j = 0; j < list; j++) {
    temb[i * list + j] = b[i][j];
    }
    }
    return temb;
    }*/
    public static byte[] int2bytes(int num) {
        byte[] b = new byte[4];
        //int mask = 0xff;
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }


    public static byte[] int2bytesReverse(int num) {
        byte[] b = new byte[4];
        //int mask = 0xff;
        for (int i = 0; i < 4; i++) {
            b[3 - i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }
    //字节数组转化成int
    public static int bytes2int(byte b[]) {
        int s = 0;
        for (int i = 0; i < b.length; i++) {
            s = s | ((b[i] & 0xff) << ((b.length - i - 1) * 8));
        }
        return s;
    }

    public static int bytes2intReverse(byte b[]) {
        int s = 0;
        byte[] tmp = new byte[4];
        for (int i = 0; i < 4; i++) {
            tmp[i] = b[3 - i];
        }
        return bytes2int(tmp);
    }

    public static int checkSum(byte[] arr) {
        byte[] tmp = new byte[4];
        int sum = 0;
        for(int i=0; i<arr.length; i+=4) {
            System.arraycopy(arr, i, tmp, 0, 4);
            int t = ByteUtil.bytes2intReverse(tmp);
            sum ^= t;
        }

        return sum;
    }

    public static int checkSum(byte[] arr, int len) {
        byte[] tmp = new byte[len];
        System.arraycopy(arr, 0, tmp, 0, len);

        return checkSum(tmp);
    }
    
    public static String getHex(int num) {
        final char digits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int length = 32;
        StringBuffer sb = new StringBuffer();       //定义一个可变长字符串
        char[] result = new char[length];
        String tmp = "0x0";
        do {
            result[--length] = digits[num & 15];
            num >>>= 4;
        } while (num != 0);
        for (int i = length; i < result.length; i++) {
            sb.append((char) (result[i]));
        }
        tmp += new String(sb);
        return tmp;
    }

    public static void main(String[] args) {
        int n  = 1234;
        System.out.println("buf: "+ Integer.toHexString(n));
        byte[] buf = ByteUtil.int2bytes(n);

        System.out.println("buf: "+new BigInteger(1, buf).toString(16));

        buf = ByteUtil.int2bytesReverse(n);

        System.out.println("buf: "+new BigInteger(1, buf).toString(16));

        byte[] arr = "12345678".getBytes();
        System.out.println(ByteUtil.checkSum(arr));

    }
}