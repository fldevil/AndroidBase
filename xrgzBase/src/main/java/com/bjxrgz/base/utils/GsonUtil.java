package com.bjxrgz.base.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fan-pc on 2015/11/13.
 * describe Gson工具类
 */
public class GsonUtil {

    public static Gson get() {
        return new Gson();
    }

    public static <T> T getObject(String json, Class<T> clazz) {
        T t = null;
        try {
            t = get().fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            LogUtil.e(e.toString());
        }
        return t;
    }

    public static <T> List<T> getList(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            list = get().fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            LogUtil.e(e.toString());
        }
        return list;
    }
}
