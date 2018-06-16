package com.my.blog.website.utils;

import com.google.gson.Gson;

/**
 * json转换工具
 * Created by Administrator on 2017/3/13 013.
 */
public class GsonUtils {

    // 获取gson对象
    private static final Gson gson = new Gson();

    // 传入的对象转换为json字符串
    public static String toJsonString(Object object){
        // 3目运算符 如果为object=null，返会null,不为空返回json字符串
      return object==null?null:gson.toJson(object);
    }
}
