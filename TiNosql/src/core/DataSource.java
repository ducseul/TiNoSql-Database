/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import tinosql.Env;

/**
 *
 * @author Ducnm62
 */
public class DataSource {

    private static Logger logger;
    private static ConcurrentHashMap<String, Collection> dataSrc;

    public static synchronized ConcurrentHashMap<String, Collection> getInstance() {
        if (logger == null) {
            logger = new Logger(DataSource.class);
        }

        if (dataSrc == null) {
            System.out.println(">> DataSource is null. Create Instance");
            DataSource.dataSrc = new ConcurrentHashMap<>();
            //load the old file.
            loadSavedFile();
        }
        return DataSource.dataSrc;
    }

    private static void loadSavedFile() {
        logger.INFO("Attemp to save log file");
        String dataSrc = Utils.getProperties("dataDir", "data");
        File file = new File(dataSrc);
        if (!file.exists()) {
            file.mkdir();
        }
        Env.dataDir = file.getAbsolutePath();
        String dataSrcStr = dataSrc + "\\data.tino";

        File myObj = new File(dataSrcStr);
        FileInputStream is = null;
        ObjectInputStream ois = null;
        if (myObj.exists()) {
            try {
                is = new FileInputStream(dataSrcStr);
                ois = new ObjectInputStream(is);
                ConcurrentHashMap<String, Collection> dataSrcTmp = (ConcurrentHashMap) ois.readObject();
                DataSource.dataSrc.putAll(dataSrcTmp);
                logger.INFO("Load data from saved " + dataSrcStr + " done");
            } catch (FileNotFoundException ex) {
                logger.WARNING("Can't read database at " + myObj.getAbsolutePath());
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ois.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    is.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            logger.WARNING("Not found the saved database.");
        }
    }

    public static void saveData() {
        try {
            File myObj = new File(Env.dataDir + "\\data.tino");
            if (!myObj.exists()) {
                myObj.createNewFile();
                logger.WARNING("First time create db file.");
            }
            try {
                FileOutputStream fos = new FileOutputStream(myObj);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                // write object to file
                oos.writeObject(DataSource.getInstance());
                System.out.println("Done");
                // closing resources
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.INFO("Save done!");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
