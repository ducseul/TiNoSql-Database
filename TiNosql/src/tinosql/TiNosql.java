/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinosql;

import core.TiNosqlEngine;
import com.google.gson.Gson;
import core.DataSource;
import core.Message;
import core.Utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import core.Logger;
import core.constant.LOG_LEVEL;

/**
 *
 * @author Ducnm62
 */
public class TiNosql extends TiNosqlEngine {

    /**
     * @param args the command line arguments
     */
    private static Gson gson;
    private static Logger logger;

    public static void main(String[] args) {
        String request;
        String response;
        ServerSocket welcomeSocket;
        try {
            logger = new Logger(TiNosql.class);
            loadConfig();
            DataSource.getInstance();
            DataSource.saveData();
            welcomeSocket = new ServerSocket(_PORT);
            logger.INFO(String.format(">> Create socket at %s. Database is deployed.", (_PORT + "")));
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                long beginExecution = beginExecution();
                BufferedReader clientBufferReader
                        = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient
                        = new DataOutputStream(connectionSocket.getOutputStream());
                request = clientBufferReader.readLine();
                logger.INFO("<< Client said:" + request);
                Message processsQuery = processsQuery(request);

                if (request.startsWith("ping")) {
                    long startTime = processsQuery.getExecutionTimeRaw();
                    long haftRoundTime = getBench(startTime);
                    processsQuery.setMessage(String.format(processsQuery.getMessage(), haftRoundTime, "%s"));
                }
                long endTime = getBench(beginExecution);
                processsQuery.setExecutionTime(endTime);
                outToClient.writeBytes(processsQuery.toJson() + '\n');
                logger.INFO(">> Response done");
            }
        } catch (IOException ex) {
            logger.ERROR(String.format("Fail to bind port %s. TinoEngine fail to start.", (_PORT + "")));
            System.out.println();
        }

    }

    private static void loadConfig() {
        File file = new File("config.properties");
        try (InputStream input = new FileInputStream(file)) {
            prop = new Properties();
            prop.load(input);
            _LOG_PATTERN = Utils.getProperties("log.pattern", "[$LOGLEVEL] $TIME %s");
            {
                String logFileOutputRaw = Utils.getProperties("log.fileOutput", "false").toLowerCase().trim();
                if (logFileOutputRaw.equals("1")) {
                    logFileOutputRaw = "true";
                }
                if (logFileOutputRaw.equals("0")) {
                    logFileOutputRaw = "false";
                }
                isLog2File = Boolean.parseBoolean(logFileOutputRaw);

                if (isLog2File) {
                    logDir = Utils.getProperties("log.logDir", "log").toLowerCase().trim();
                }
            }
            dataDir = Utils.getProperties("db.dataDir", new File("data").getAbsolutePath());
            try {
                _PORT = Integer.parseInt(Utils.getProperties("db.port", "6543"));
            } catch (NumberFormatException ex) {
                logger.WARNING("Can't binding port from config. Use default 6543");
                _PORT = 6543;
            }
            try {
                logLevel = LOG_LEVEL.valueOf(Utils.getProperties("log.level", logLevel.ALL + ""));
            } catch (NumberFormatException ex) {
                logger.WARNING("No config log.level given. Use default ALL");
                _PORT = 6543;
            }
        } catch (IOException ex) {
            logger.WARNING("Can't read config file in " + file.getAbsolutePath());
        }

    }

}
