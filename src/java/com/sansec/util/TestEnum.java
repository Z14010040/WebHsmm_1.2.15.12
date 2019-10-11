package com.sansec.util;

import com.sansec.hsm.bean.HSMError;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/3/22 16:33
 */
public class TestEnum {
    public static void main(String[] args) {
        int rv = 22020102;
        String result = HSMError.getErrorInfo(rv);
        System.out.println(result);
    }
}
