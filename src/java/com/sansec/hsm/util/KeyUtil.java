package com.sansec.hsm.util;

import java.util.ArrayList;
import java.util.List;

public class KeyUtil {
	//奇数密钥对为签名密钥对
	public static int transferSignKeyIndex(int index) {
		return ((index << 1) - 1);
	}
	//偶数密钥对为加密密钥对
	public static int transferEncKeyIndex(int index) {
		return (index << 1);
	}

    public static List<Integer> analyzeIndexString(String indexStr, int maxIndex) {
        /*String key = "1,2,3 , 5 - 40";*/
        if (indexStr == null) {
            return null;
        }
        String[] arr = indexStr.split(",");
        List<Integer> list = new ArrayList<Integer>();
        String info = "";
        int rv = 1;
        try {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].contains("-")) {
                    String[] subArr = arr[i].trim().split("-");
                    Integer n1 = Integer.parseInt(subArr[0].trim());
                    Integer n2 = Integer.parseInt(subArr[1].trim());
                    if (n1 > n2 || n1 < 1 || n2 > maxIndex) {
                        rv = 0;
                        return null;
                    }
                    for (int k = n1; k <= n2; k++) {
                        list.add(k);
                    }
                } else {
                    int n = Integer.parseInt(arr[i].trim());
                    if (n < 1 || n > maxIndex) {
                        rv = 0;
                        return null;
                    }
                    list.add(n);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }
}
