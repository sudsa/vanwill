package com.qifeng.will.base.util;


import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author 杜志诚
 * @create 2021/6/15 0015 11:30
 */
public class EnumsUtils {

    private EnumsUtils() {
    }

    public static <T extends BaseEnum<K, V>, K, V> V getDesc(K code, Class<T> className) {
        return Objects.requireNonNull(
                getEnumObject(className, e -> e.getCode().equals(code)))
                .map(T::getDesc).orElse(null);
    }

    public static <T extends BaseEnum<K, V>, K, V> K getCode(V desc, Class<T> className) {
        return Objects.requireNonNull(
                getEnumObject(className, e -> Objects.equals(e.getDesc(), desc)))
                .map(T::getCode).orElse(null);
    }

    public static <T> Optional<T> getEnumObject(Class<T> className, Predicate<T> predicate) {
        if (!className.isEnum()) {
            return Optional.empty();
        } else {
            return Arrays.stream(className.getEnumConstants()).filter(predicate).findAny();
        }
    }
}
