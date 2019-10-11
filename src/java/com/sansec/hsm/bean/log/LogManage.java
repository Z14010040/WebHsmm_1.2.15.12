package com.sansec.hsm.bean.log;


import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import debug.log.LogUtil;
import debug.log.LogUtil.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public final class LogManage {
    private static final Log logger = LogUtil.getLog(LogManage.class);    
    static final String name = "/mnt/linux/var/Managelog";
    public static final String operationname = "LOG MANAGEMENT";
    private LogManage() {
        
    }
    /**
     * 初始化日志表，如果不存在，则创建
     *
     */

    /**
     * 生成日志
     * @param bean
     * @return 
     * @throws SQLException  
     */

    /**
     * 将指定的日志置为已审计状态
     * @param id 
     * @throws SQLException 
     */
    public static void audit(int id)  {
        audit(Integer.toString(id));
    }
    /**
     * 删除一条日志
     * @param id
     * @return
     * @throws SQLException  
     */
    public static boolean delete(int id) {
        boolean result = false;
      
        return result;
    }
    /**
     * 批量将指定的日志置为已审计状态
     * @param ids 
     * @throws SQLException 
     */
    public static void audit(String ids) {
        
        int off;
        int Find;
        String id[]=ids.split(",");
        int index = 0;
    try {
        FileInputStream fis=new FileInputStream(new File(name));//新建一个FileInputStream对象
        
        try {
            InputStreamReader Isr = new InputStreamReader(fis,"UTF-8");
     //       InputStreamReader Isr = new InputStreamReader(fis );
            char[] b=new char[fis.available() ];//新建一个字节数组
   //         fis.read(b);//将文件中的内容读取到字节数组中
            Isr.read(b, 0, fis.available());
            Isr.close();
            fis.close();
            String Log=new String(b);
            
            for( index = 0; index< id.length;index++)
            {
                off = Log.length();
                
            while( (Find = Log.lastIndexOf("(", off))>=0)
            {
                String curId = Log.substring(Find + 1, Log.lastIndexOf(")", off));
      //           OperationLogUtil.println(operationname,"curId ." + curId);
                if(curId.equals(id[index]))
                {
                    Log = Log.substring(0,Find) + "{" + curId + "}" + Log.substring(Log.lastIndexOf(")", off) + 1);
     //               index ++;
     //               if (index >= id.length)
                    break;
                }
                
                off = Find -1;
            }
            Log = Log.replaceAll("\0", "");
            }

        FileOutputStream fos = new FileOutputStream(name);
       // BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos,"GBK"));
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
        //bw.write(new String(Log.getBytes("utf-8"),"GBK"));
        bw.write(Log);
        bw.close();
        fos.close();
        
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
   
    }
    /**
     * 批量删除日志
     * @param ids 
     * @return
     * @throws SQLException  
     */
    public static boolean delete(String ids)  {
        boolean result = false;
       
        return result;
    }
    /**
     * 获取日志数据，分页显示
     * 
     * @param pageSize 分页大小
     * @param offset 偏移量
     * @return
     * @throws SQLException  
     */
    public static ArrayList<LogBean> getList(int pageSize,int offset)
            {
        ArrayList<LogBean> list = null; 
         list = new ArrayList<LogBean>();
         ArrayList<LogBean> sublist = new ArrayList<LogBean>(); 
         int sublen = 0;
         int Curlog = 0;
         boolean audited = false;
         String s;
         boolean more;
         try {
 
           BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(name),"UTF-8"));
           while ( (s = br.readLine())!=null)
           {
               Curlog ++;
               if (s.length() <=1)
                   continue;
               audited = true;
      //         if(Curlog <= offset)
      //             continue;
      //         if (Curlog > offset + pageSize)
      //             break;
               String Serial = new String();
                if ( (s.indexOf("(")) >= 0)
                {
                   Serial =  s.substring(s.indexOf("(") + 1,s.indexOf(")"));
                   audited = false;
                }
                else if ( (s.indexOf("{")) >= 0)
                {
                   Serial =  s.substring(s.indexOf("{") + 1,s.indexOf("}"));
                   audited = true;
                }
                else
                {
                    continue;
                }
                String Time = s.substring(s.indexOf("<") + 1,s.indexOf(">"));
                String Type = s.substring(s.indexOf("<",s.indexOf(">")) + 1,s.indexOf(">",s.indexOf(">")+2));
                String Msg = "　" +s.substring(s.lastIndexOf(">")+1);
 
               LogBean bean = new LogBean();
                bean.setId(Integer.parseInt(Serial));                
                bean.setSuccess(true);
                bean.setSerial(Serial);
                bean.setCreateTimeS(Time);
                 bean.setOperator(Type);
                bean.setMsg(Msg);
                bean.setOperation("management");
                bean.setParameters("123");
                bean.setAudited(audited);
                
                list.add(bean);
           }         
           
           br.close();
        } catch (Exception ex) {
            Logger.getLogger(OperationLogUtil.class.getName()).log(Level.SEVERE, null, ex);
            OperationLogUtil.genLogMsg("ERROR",operationname,"Failed","Read Logs Failed." + ex.toString(), ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
        }
            
            
         Collections.reverse(list);
         if ((offset + pageSize)> list.size())
             sublen =  list.size();
         else
             sublen =  (offset + pageSize);
         for(int i = offset; i< sublen; i++)
         {sublist.add(list.get(i));}
            
        
        return sublist;
    }
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public static int getListCount()  {
        int count = 0;

         try {
           String s;
        BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(name),"UTF-8"));
           while ( (s = br.readLine())!=null)
           {
               count++;
           }
           br.close();
        } catch (Exception ex) {
            Logger.getLogger(OperationLogUtil.class.getName()).log(Level.SEVERE, null, ex);
            OperationLogUtil.genLogMsg("ERROR",operationname,"Failed","Read Logs Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
        }
        return count;
    }
}
