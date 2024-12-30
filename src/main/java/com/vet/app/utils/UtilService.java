package com.vet.app.utils;

import java.lang.reflect.Field;

import org.springframework.stereotype.Component;

@Component
public class UtilService {

    public void copyNonNullProperties(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
            try {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error copying properties", e);
            }
        }
    }

}
