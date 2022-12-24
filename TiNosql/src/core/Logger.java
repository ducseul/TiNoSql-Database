package core;

import core.constant.CONSTANT;
import core.constant.LOG_LEVEL;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import tinosql.Env;
import tinosql.TiNosql;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ducnm62
 */
public class Logger<T> {

    private Class<T> logClass;

    public Logger() {
    }

    public Logger(Class<T> logClass) {
        this.logClass = logClass;
    }

    public void log(String log) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        String logPattern = Env._LOG_PATTERN.replace("$LOGLEVEL", (Env.logLevel + "")).replace("$TIME", strDate);
        if (logPattern.contains("$CLASS")) {
            if (logClass != null) {
                logPattern = logPattern.replace("$CLASS", " " + logPattern.toString());
            } else {
                logPattern = logPattern.replace("$CLASS", "");
            }
        }
        System.out.println(String.format(logPattern, log));
    }

    public void ERROR(String log) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        String logPattern = Env._LOG_PATTERN.replace("$LOGLEVEL", (LOG_LEVEL.ERROR + "")).replace("$TIME", strDate);
        if (logPattern.contains("$CLASS")) {
            if (logClass != null) {
                logPattern = logPattern.replace("$CLASS", " " + logClass.getName());
            } else {
                logPattern = logPattern.replace("$CLASS", "");
            }
        }

        String rawLog = String.format(logPattern, log);
        System.err.println(rawLog);
        if (Env.isLog2File) {
            appendLogFile(rawLog, LOG_LEVEL.ERROR);
        }
    }

    public void INFO(String log) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        String logPattern = Env._LOG_PATTERN.replace("$LOGLEVEL", (LOG_LEVEL.INFO + "")).replace("$TIME", strDate);
        if (logPattern.contains("$CLASS")) {
            if (logClass != null) {
                logPattern = logPattern.replace("$CLASS", " " + logClass.getName());
            } else {
                logPattern = logPattern.replace("$CLASS", "");
            }
        }

        String rawLog = String.format(logPattern, log);
        System.err.println(rawLog);
        if (Env.isLog2File) {
            appendLogFile(rawLog, LOG_LEVEL.INFO);
        }
    }

    public void WARNING(String log) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        String logPattern = Env._LOG_PATTERN.replace("$LOGLEVEL", (LOG_LEVEL.WARNING + "")).replace("$TIME", strDate);
        if (logPattern.contains("$CLASS")) {
            if (logClass != null) {
                logPattern = logPattern.replace("$CLASS", " " + logClass.getName());
            } else {
                logPattern = logPattern.replace("$CLASS", "");
            }
        }
        String rawLog = String.format(logPattern, log);
        System.err.println(rawLog);
        if (Env.isLog2File) {
            appendLogFile(rawLog, LOG_LEVEL.WARNING);
        }
    }

    public static synchronized void appendLogFile(String log, LOG_LEVEL logLevel) {
        FileWriter fr = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String normalLogFile = "log-" + Utils.DateUtils.toString(new Date(), "dd-MM-yyyy") + ".log";
        File file = new File(normalLogFile);
        append(file, log);
        if (logLevel == LOG_LEVEL.ERROR || logLevel == LOG_LEVEL.WARNING) {
            String errorLogFile = "error-" + Utils.DateUtils.toString(new Date(), "dd-MM-yyyy");
            file = new File(errorLogFile);
            append(file, log);
        }
        
        
    }

    private static void append(File file, String log) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            pr.println("" + (log.endsWith("\n") ? log.substring(0, log.length() - 1) : log));
            pr.close();
            br.close();
            fr.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
