package com.uxun.pos.view.utils;

import com.uxun.pos.R;

import java.lang.reflect.Field;


public class ResourceUtils {


    public static Integer getResourceByID(String name) {
        return (Integer) getStaticValueByReflect(name, R.id.class);
    }

    public static Integer getResourceByLayout(String name) {
        return (Integer) getStaticValueByReflect(name, R.layout.class);
    }

    private static Object getStaticValueByReflect(String filedName, Class<?> clazz) {
        //1.参数校验
        if (filedName == null || clazz == null) {
            return null;
        }
        filedName = filedName.trim();
        if ("".equals(filedName)) {
            return null;
        }
        //2.查找字段
        Field field;
        try {
            field = clazz.getField(filedName);
        } catch (NoSuchFieldException e) {
            return null;
        }
        //3.返回值
        if (field != null) {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }
}
