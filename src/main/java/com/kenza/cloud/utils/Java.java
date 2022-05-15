package com.kenza.cloud.utils;

import java.util.Optional;

public class Java {

    public static <T> Optional<T> convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return Optional.ofNullable(clazz.cast(o));
        } catch(ClassCastException e) {
            return Optional.empty();
        }
    }
}
