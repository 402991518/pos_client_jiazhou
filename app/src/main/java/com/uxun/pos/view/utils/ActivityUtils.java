package com.uxun.pos.view.utils;

import android.app.Activity;
import android.view.View;

import com.uxun.pos.utils.CamelUtils;
import com.uxun.pos.view.annotation.Binding;
import com.uxun.pos.view.domain.InheritModel;

import java.lang.reflect.Field;
import java.util.List;

public class ActivityUtils {
    //绑定Activity的layout
    public static void bindLayout(Activity ac) {
        //1.非空认证
        if (ac == null) {
            return;
        }
        String name = null;
        Binding binding = ac.getClass().getAnnotation(Binding.class);
        if (binding != null) {
            name = binding.value();
        }
        if (name == null || "".equals(name.trim())) {
            name = ac.getClass().getSimpleName();
            name = CamelUtils.camel2Underline(name);
        }
        Integer layout = ResourceUtils.getResourceByLayout(name);
        if (layout != null) {
            ac.setContentView(layout);
        }
    }

    //绑定Activity的widget
    public static void bindWidget(Activity ac, Class<?> expiryClass) {
        InheritModel inherit = new InheritModel(ac, expiryClass);
        List<Class<?>> inheritClass = inherit.getInheritClass();
        for (Class<?> clazz : inheritClass) {
            processPublicField(clazz, ac);
            processPrivateField(clazz, ac);
        }
    }

    //处理公有字段
    private static void processPublicField(Class<?> clazz, Activity ac) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (View.class.isAssignableFrom(field.getType())) {
                processField(field, ac);
            }
        }
    }

    //处理私有字段
    private static void processPrivateField(Class<?> clazz, Activity ac) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (View.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                processField(field, ac);
            }
        }
    }

    private static void processField(Field field, Activity ac) {
        String name = null;
        Binding binding = field.getAnnotation(Binding.class);
        if (binding != null) {
            name = binding.value();
        }
        if (name == null || "".equals(name.trim())) {
            name = field.getName();
        }
        Integer resourceID = ResourceUtils.getResourceByID(name);
        if (resourceID != null) {
            try {
                field.set(ac, ac.findViewById(resourceID));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
