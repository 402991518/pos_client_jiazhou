package com.uxun.pos.view.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 * 继承模型: A->B->C->D,A实例的层级为(expiryClass为D.class):A.class,B.class,C.class
 */

public class InheritModel {

    private Class<?> expiryClass;//截止类

    private List<Class<?>> inheritClass = new ArrayList<>();//object对象多级继承的类型

    public InheritModel(Object object, Class<?> expiryClass) {
        if (object != null && expiryClass != null) {
            Class<?> clazz = object.getClass();
            if (expiryClass.isAssignableFrom(clazz)) {
                this.expiryClass = expiryClass;
                this.addSuperClass(clazz);
            }
        }
    }

    private void addSuperClass(Class<?> clazz) {
        if (clazz == expiryClass) {
            return;
        }
        this.inheritClass.add(clazz);
        this.addSuperClass(clazz.getSuperclass());
    }

    public List<Class<?>> getInheritClass() {
        return inheritClass;
    }
}
