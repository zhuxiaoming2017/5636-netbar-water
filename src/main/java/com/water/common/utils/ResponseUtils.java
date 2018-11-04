package com.water.common.utils;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpServletResponse帮助类
 */
public final class ResponseUtils {
    public static final Logger log = LoggerFactory
            .getLogger(ResponseUtils.class);


    public static final String STATUS = "status";
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String WARN = "warn";
    public static final String DATA = "data";
    public static final String INFO = "info";
    public static final String MSG = "msg";


    /**
     * 发送文本。使用UTF-8编码。
     *
     * @param response HttpServletResponse
     * @param text     发送的字符串
     */
    public static void renderText(HttpServletResponse response, String text) {
        render(response, "text/plain", text);
    }

    /**
     * 发送HTML。使用UTF-8编码。
     *
     * @param response HttpServletResponse
     * @param text     发送的字符串
     */
    public static void renderHtml(HttpServletResponse response, String text) {
        render(response, "text/html", text);
    }

    /**
     * 发送json。使用UTF-8编码。
     *
     * @param response HttpServletResponse
     * @param text     发送的字符串
     */
    public static void renderJson(HttpServletResponse response, String text) {
//        render(response, "application/json", text);
        render(response, "text/plain", text);
    }

    /**
     * 发送xml。使用UTF-8编码。
     *
     * @param response HttpServletResponse
     * @param text     发送的字符串
     */
    public static void renderXml(HttpServletResponse response, String text) {
        render(response, "text/xml", text);
    }

    /**
     * 发送内容。使用UTF-8编码。
     *
     * @param response
     * @param type
     * @param text
     */
    public static void render(HttpServletResponse response, String type,
                              String text) {
        response.setContentType(type + ";charset=UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    // 输出JSON警告消息，返回null
    public static String ajaxJsonWarnMessage(HttpServletResponse response, String message) {

        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, WARN);
        jsonMap.put(MESSAGE, message);
        renderJson(response, JSONObject.toJSONString(jsonMap));
        return null;
    }

    // 输出JSON成功消息，返回null
    public static String ajaxJsonSuccessMessage(HttpServletResponse response, String message) {

        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put(MESSAGE, message);
        renderJson(response, JSONObject.toJSONString(jsonMap));
        return null;
    }

    // 输出JSON错误消息，返回null
    public static String ajaxJsonErrorMessage(HttpServletResponse response, String message) {

        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, ERROR);
        jsonMap.put(MESSAGE, message);
        renderJson(response, JSONObject.toJSONString(jsonMap));
        return null;
    }

    // 输出JSON错误消息，返回null
    public static String ajaxJsonErrorResult(HttpServletResponse response, String message, Object data) {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put(STATUS, ERROR);
        jsonMap.put(MESSAGE, message);
        jsonMap.put(DATA, data);
        String json = JSONObject.toJSONString(jsonMap);
        renderJson(response, json);
        return null;
    }

    // 输出JSON结果成功消息，返回null

    public static String ajaxJsonSuccessResult(HttpServletResponse response, String message, Object data) {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put(MESSAGE, message);
        jsonMap.put(DATA, data);


        String json = JSONObject.toJSONString(jsonMap);
        renderJson(response, json);
        return null;
    }


    public static String ajaxJsonWarnResult(HttpServletResponse response, String message, Object data) {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put(STATUS, WARN);
        jsonMap.put(MESSAGE, message);
        jsonMap.put(DATA, data);
 
        String json = JSONObject.toJSONString(jsonMap);
        renderJson(response, json);
        return null;
    }

    // 输出JSON成功消息，返回null
    public static String ajaxJsonSuccessInfoMessage(HttpServletResponse response, String message) {

        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put(MSG, message);
        renderJson(response, JSONObject.toJSONString(jsonMap));
        return null;
    }

    // 输出JSON错误消息，返回null
    public static String ajaxJsonErrorInfoMessage(HttpServletResponse response, String message) {

        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, ERROR);
        jsonMap.put(MSG, message);
        renderJson(response, JSONObject.toJSONString(jsonMap));
        return null;
    }

    // 输出JSON错误消息，返回null
    public static String ajaxJsonErrorInfoResult(HttpServletResponse response, String message, Object data) {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put(STATUS, ERROR);
        jsonMap.put(MSG, message);
        jsonMap.put(INFO, data);
        String json = JSONObject.toJSONString(jsonMap);
        renderJson(response, json);
        return null;
    }

    // 输出JSON结果成功消息，返回null

    public static String ajaxJsonSuccessInfoResult(HttpServletResponse response, String message, Object data) {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put(MSG, message);
        jsonMap.put(INFO, data);


        String json = JSONObject.toJSONString(jsonMap);
        renderJson(response, json);
        return null;
    }


    public static String ajaxJsonWarnInfoResult(HttpServletResponse response, String message, Object data) {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put(STATUS, WARN);
        jsonMap.put(MSG, message);
        jsonMap.put(INFO, data);

        String json = JSONObject.toJSONString(jsonMap);
        renderJson(response, json);
        return null;
    }
    
/*    public static void ajaxJsonSuccessResult(HttpServletResponse response, String message, TypeAdapterFactory typeAdapterFactory, Object data) {

//        Map<String, Object> jsonMap = new HashMap<String, Object>();
//        jsonMap.put(STATUS, SUCCESS);
//        jsonMap.put(MESSAGE, message);
//        jsonMap.put(DATA, data);
//
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapterFactory(typeAdapterFactory).setExclusionStrategies(new BaseEntityExclusionStrategy())
//                .create();
//
//
//        String json = gson.toJson(jsonMap);
//        renderJson(response, json);
        ajaxJsonSuccessResult(response, message, typeAdapterFactory, new BaseEntityExclusionStrategy(), data);
    }*/
/*
    public static void ajaxJsonSuccessResult(HttpServletResponse response, String message, TypeAdapterFactory typeAdapterFactory, ExclusionStrategy exclusionStrategy, Object data) {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put(MESSAGE, message);
        jsonMap.put(DATA, data);

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(typeAdapterFactory).setExclusionStrategies(exclusionStrategy)
                .create();


        String json = gson.toJson(jsonMap);
        renderJson(response, json);
    }
*/

}
