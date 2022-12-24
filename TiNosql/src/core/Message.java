/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.google.gson.Gson;
import core.constant.CONSTANT;
import core.constant.RETURN_CODE;

/**
 *
 * @author Ducnm62
 */
public class Message {

    private String executionTime;
    private String message;
    private RETURN_CODE code;
    private String data;

    public Message() {
        message = CONSTANT.MESSAGE.NO_DATA;
        code = RETURN_CODE.CODE_400;
        executionTime = "0ms";
        data = "";
    }

    public String getExecutionTime() {
        return executionTime.endsWith("ms") ? executionTime : executionTime + "ms";
    }

    public long getExecutionTimeRaw() {
        return Long.parseLong(getExecutionTime().replace("ms", "").trim());
    }

    public Message setExecutionTime(long executionTime) {
        this.executionTime = executionTime + "ms";
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RETURN_CODE getCode() {
        return code;
    }

    public void setCode(RETURN_CODE code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
