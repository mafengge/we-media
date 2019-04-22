package com.media.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtil {

    /**
     * 将json字符串转换成目标对象
     *
     * @param jsonData json字符串
     * @param clazz 目标对象的Class类
     * @return 目标对象
     */
    public static final <T> T fromJson(String jsonData, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, clazz);
    }

    /**
     * 将json字符串转换成JavaBean列表
     *
     * @param jsonData json字符串
     * @param clazz JavaBean的Class对象
     * @return JavaBean列表
     */
    public static final <T> List<T> fromJsonArray(String jsonData, Class<T> clazz) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement el = parser.parse(jsonData);
        JsonArray jsonArray = el.getAsJsonArray();
        Iterator<JsonElement> it = jsonArray.iterator();
        List<T> list = new ArrayList<T>();
        while (it.hasNext()) {
            JsonElement je = (JsonElement) it.next();
            list.add(gson.fromJson(je, clazz));
        }

        return list;
    }

    /**
     * 将对象转换为json字符串
     *
     * @param object 转换对象
     * @param pretty 是否按照json格式返回，true:是；false:否
     * @return json字符串
     */
    public static final String toJson(Object object, boolean pretty) {
        GsonBuilder gb = new GsonBuilder();
        if (pretty) {
            gb.setPrettyPrinting();
        }
        gb.disableHtmlEscaping();
        Gson gson = gb.create();

        return gson.toJson(object);
    }

}
