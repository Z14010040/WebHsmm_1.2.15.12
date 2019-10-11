package debug.log;

import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class LogUtil {
    public static boolean debug = true; //开发调试

 //   private static final String name = "/mnt/linux/log/webhsm.log";

    private static final Logger logger = Logger.getLogger("web");
//    private static final HashMap<String,Level> LEVEL_MAP = 
//            new HashMap<String,Level>(){
//                {
//                    put("off",Level.OFF);
//                    put("all",Level.ALL);
//                    put("trace",Level.FINEST);
//                    put("debug",Level.CONFIG);
//                    put("info",Level.INFO);
//                    put("warn",Level.WARNING);
//                    put("error",Level.SEVERE);
//                }
//    };
    private static final HashMap<Integer,Level> INT_LEVEL_MAP = 
            new HashMap<Integer,Level>(){
                {                    
                    put(0,Level.TRACE);
                    put(1,Level.DEBUG);
                    put(2,Level.INFO);
                    put(3,Level.WARN);
                    put(4,Level.ERROR);
                }
    };

    /**
     * 日志初始化，根据配置文件的信息来初始化日志级别
     */
    public static void init() {
        //PropertyConfigurator.configure("serverconf.properties");
    }

    public static void println(String msg) {
        println(msg,null);
    }

    public static void println(String msg,Throwable e) {
        String msgStr = new Date() + msg;
        
        if (e == null) {
            logger.debug( msgStr);
        } else {
            logger.debug( msgStr,e);            
        } 
    }

    public static void log(Level level,String msg,Throwable e) {
        logger.log(level,msg,e);        
    }

    public static Log getLog(Class<?> classz) {
        return new Log(classz);
    }
    
    public static Log getLog(String tag) {
        return new Log(tag);
    }

    public static class Log {
        
        private Logger logger;

        public Log(String tag) {            
            this.logger = Logger.getLogger(tag);
        }

        public Log(Class<?> classz) {            
            this.logger = Logger.getLogger(classz);
        }
        
        public void showLog(int level,String msg) {
            LogUtil.log(INT_LEVEL_MAP.get(level),msg,null);
        }
        
        public void trace(String msg,Throwable e) {
            logger.trace(msg, e);
        }
        
        public void trace(String msg) {
            trace(msg,null);
        }

        public void debug(String msg,Throwable e) {
            logger.debug(msg, e);
        }

        public void debug(String msg) {
            debug(msg,null);
        }

        public void info(String msg,Throwable e) {
           logger.info(msg, e);
        }

        public void info(String msg) {
            info(msg,null);
        }

        public void warn(String msg,Throwable e) {
            logger.warn(msg, e);
        }

        public void warn(String msg) {
            warn(msg,null);
        }

        public void error(String msg,Throwable e) {
            logger.error(msg, e);
        }

        public void error(String msg) {
            error(msg,null);
        }

    }
}
