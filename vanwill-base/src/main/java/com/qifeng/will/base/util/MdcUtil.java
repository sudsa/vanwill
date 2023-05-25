package com.qifeng.will.base.util;

import org.slf4j.MDC;

//MDC工具类
public class MdcUtil {
    private static final String TRACE_ID = "TRACE_ID";

    public static String get() {
        return MDC.get(TRACE_ID);
    }

    public static void add(String value) {
        MDC.put(TRACE_ID, value);
    }

}
