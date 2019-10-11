package com.sansec.hsm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/2/13 15:26
 */
public class ErrorInfo {
    public static List<String> returnErrorInfo (){
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String errorFileName = e.getFileName();
        String errorLineNum = e.getLineNumber()+"";
        List list = new ArrayList<String>();
        list.add(errorFileName);
        list.add(errorLineNum);
        return list;
    }
}
