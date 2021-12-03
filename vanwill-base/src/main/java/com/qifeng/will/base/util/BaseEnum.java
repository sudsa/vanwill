package com.qifeng.will.base.util;

/**
 * @author 杜志诚
 * @create 2021/6/16 0016 11:10
 */
public interface BaseEnum<K, V> {
    /**
     * 获取code
     *
     * @return code
     */
    K getCode();

    /**
     * 获取 desc
     *
     * @return desc
     */
    V getDesc();
}
