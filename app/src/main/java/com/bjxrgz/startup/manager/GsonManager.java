package com.bjxrgz.startup.manager;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Fan-pc on 2015/11/13.
 * describe Gson工具类
 */
public class GsonManager {

    private static Gson GSON;
    private static Gson GSON_BUILDER;

    public static Gson getInstance() {
        if (GSON == null) {
            synchronized (GsonManager.class) {
                if (GSON == null) {
                    GSON = new Gson();
                }
            }
        }
        return GSON;
    }

    public static Gson getNoDataInstance() {
        if (GSON_BUILDER == null) {
            synchronized (GsonManager.class) {
                if (GSON_BUILDER == null) {
                    GSON_BUILDER = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getName().contains("outputData");
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    }).create();
                }
            }
        }
        return GSON_BUILDER;
    }

    public static <T> List<T> getList(JSONObject object, String key, Type type) {
//        Type type = new TypeToken<List<xls>>() {
//        }.getType();
        JSONArray array = object.optJSONArray(key);
        return getList(array, type);
    }

    public static <T> List<T> getList(JSONArray array, Type type) {
        return GSON.fromJson(array.toString(), type);
    }
}
