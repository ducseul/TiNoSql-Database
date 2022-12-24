/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinosql;

import core.constant.LOG_LEVEL;
import java.util.Properties;

/**
 *
 * @author hacke
 */
public class Env {

    final public String dbVersion = "1.2.1";
    public static String _LOG_PATTERN;
    public static LOG_LEVEL logLevel;
    
    public static Properties prop;
    protected static int _PORT;
    protected static String userRoot;
    protected static String rootPassword;
    public static String dataDir;
    
    public static boolean isLog2File;
    public static String logDir;
}
