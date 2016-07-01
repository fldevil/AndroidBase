package com.bjxrgz.startup.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Fan-pc on 2015/11/13.
 * 单例创建json
 */
public class GsonUtils {

    private static final Gson GSON = new Gson();
    private static final Gson GSON_BUILDER = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getName().contains("outputData");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

    public static Gson getInstance(){
        return GSON;
    }

    public static Gson getNoDataInstance(){
        return GSON_BUILDER;
    }
}
