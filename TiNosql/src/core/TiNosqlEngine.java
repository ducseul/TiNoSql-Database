/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.google.gson.Gson;
import core.Collection;
import core.DataSource;
import core.Message;
import core.Utils;
import core.constant.CONSTANT;
import core.constant.RETURN_CODE;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import core.constant.LOG_LEVEL;
import tinosql.Env;

/**
 *
 * @author Ducnm62
 */
public class TiNosqlEngine extends Env {

    private static long _startTime;
    private static Message returnMessage;

    protected static long beginExecution() {
        TiNosqlEngine._startTime = System.currentTimeMillis();
        return TiNosqlEngine._startTime;
    }

    protected static long getBench(Long startTime) {
        if (startTime == null) {
            return System.currentTimeMillis() - TiNosqlEngine._startTime;
        } else {
            return System.currentTimeMillis() - startTime;
        }
    }

    protected static Message processsQuery(String request) {
        String[] keywords = request.split(" ");
        returnMessage = new Message();
        try {
            if (keywords.length <= 0) {
                returnMessage.setMessage(CONSTANT.MESSAGE.NO_DATA);
                return returnMessage;
            }
            switch (keywords[0].toLowerCase()) {
                case "create": {
                    //create collection_name
                    createCollection(keywords);
                    break;
                }
                case "list": {
                    //list => Return list collection
                    //list collection_name => Return key list
                    executeListQuery(keywords);
                    break;
                }

                case "from": {
                    Collection collection = DataSource.getInstance().get(keywords[1]);
                    if (collection == null) {
                        returnMessage.setMessage(String.format(CONSTANT.MESSAGE.NO_COLLECTION, keywords[1]));
                        returnMessage.setCode(RETURN_CODE.CODE_400);
                    } else {
                        if (keywords[2].equals("get") || keywords[2].equals("select")) { //from [collectionName] get [key]
                            String data = collection.getCollectionData().get(keywords[3]);
                            returnMessage.setCode(RETURN_CODE.CODE_200);
                            returnMessage.setMessage(CONSTANT.MESSAGE.SUCCESS);
                            returnMessage.setData(data);
                        }
                        if (keywords[2].equals("set")) { //from [collectionName] set [key] [value]
                            //Override if exist
                            String value = Utils.StringUtils.combineLastKeyword(keywords, 3, " ");
                            collection.getCollectionData().put(keywords[3], value);
                            returnMessage.setCode(RETURN_CODE.CODE_200);
                            returnMessage.setMessage(String.format(CONSTANT.MESSAGE.INSERT_DONE, keywords[3], keywords[4], keywords[1]));
                        }
                        if (keywords[2].equals("add")) { //from [collectionName] add [key] [value]
                            //return false if exist
                            if (collection.getCollectionData().get(keywords[3]) == null) {
                                collection.getCollectionData().put(keywords[3], keywords[4]);
                                returnMessage.setCode(RETURN_CODE.CODE_200);
                                returnMessage.setMessage(String.format(CONSTANT.MESSAGE.INSERT_DONE, keywords[3], keywords[4], keywords[1]));
                            } else {
                                returnMessage.setCode(RETURN_CODE.CODE_401);
                                returnMessage.setMessage(String.format(CONSTANT.MESSAGE.ADD_INVALID, keywords[3], keywords[1]));
                            }
                        }
                        if (keywords[2].equals("delete")) {//from [collection] delete [key]
                            String remove = collection.getCollectionData().remove(keywords[3]);
                            System.out.println(">>" + remove);
                        }
                        if (keywords[2].equals("find")) { // from [collection] find (%key%:%value%)
                            String combineLastKeyword = Utils.combineLastKeyword(keywords, 3);
                            System.out.println("combineLastKeyword::" + combineLastKeyword);
                        }
                    }
                    break;
                }
                case "ping": {
                    returnMessage.setMessage(CONSTANT.MESSAGE.PING_RESPONSE);
                    returnMessage.setExecutionTime(Long.parseLong(keywords[1] + ""));
                    returnMessage.setCode(RETURN_CODE.CODE_200);
                    break;
                }

                case "flush": {
                    //flush: all
                    //flush [collectionName]
                    break;
                }
                case "commit": {//save to file, need auto commit after while
                    DataSource.saveData();
                    returnMessage.setMessage(CONSTANT.MESSAGE.COMMON_OK);
                    returnMessage.setCode(RETURN_CODE.CODE_200);
                    break;
                }

                case "shutdown": {
                    System.out.println(">> User shutting down");
                    System.exit(0);
                    break;
                }
                default: {
                    returnMessage.setMessage(CONSTANT.MESSAGE.QUERY_FAIL);
                }
            }
            return returnMessage;
        } catch (Exception e) {
            returnMessage.setMessage(CONSTANT.MESSAGE.QUERY_FAIL);
            return returnMessage;
        }
    }

    private static void createCollection(String[] keywords) {
        try {
            //> create collection.. => Invalid
            if (keywords[1].equals("collection")) {
                returnMessage.setMessage(CONSTANT.MESSAGE.INVALIDARG_KEYWORD);
            } else {
                //> create collection_name
                Collection collection = new Collection();
                DataSource.getInstance().put(keywords[1], collection);
                returnMessage.setCode(RETURN_CODE.CODE_200);
                returnMessage.setMessage(String.format(CONSTANT.MESSAGE.CREATE_SUCCESS, keywords[1]));
            }
        } catch (IndexOutOfBoundsException e) {
            returnMessage.setCode(RETURN_CODE.CODE_400);
            returnMessage.setMessage(CONSTANT.MESSAGE.INVALIDARG_NO);
        }
    }

    private static void executeListQuery(String[] keywords) {
        if (keywords.length == 1 || keywords[1].equals("collection")) { //list collection
            ArrayList<String> list = new ArrayList<>();
            Enumeration<String> e = DataSource.getInstance().keys();
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                list.add(key);
            }
            returnMessage.setData(new Gson().toJson(list));
            returnMessage.setMessage(CONSTANT.MESSAGE.SUCCESS);
            returnMessage.setCode(RETURN_CODE.CODE_200);
        } else { //list [collectionName]
            Collection collection = DataSource.getInstance().get(keywords[1]);
            if (collection == null) {
                returnMessage.setMessage(String.format(CONSTANT.MESSAGE.NO_COLLECTION, keywords[1]));
                returnMessage.setCode(RETURN_CODE.CODE_400);
            } else {
                Enumeration<String> e = collection.getCollectionData().keys();
                ArrayList<String> list = new ArrayList<>();
                while (e.hasMoreElements()) {
                    String key = e.nextElement();
                    list.add(key);
                }
                returnMessage.setCode(RETURN_CODE.CODE_200);
                returnMessage.setMessage(String.format(CONSTANT.MESSAGE.COMMON_OK));
                returnMessage.setData(new Gson().toJson(list));
            }
        }
    }
}
