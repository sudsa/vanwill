package com.qifeng.will.base.func;

/**
 * 处理抛出异常的IF
 * 定义一个抛出异常的形式的函数式接口
 * 这个接口只有参数没有返回值的消费性接口
 */
@FunctionalInterface
public interface ThrowExceptionFunction {
    /**
     * 抛出异常信息
     * @param message
     */
    void throwException(String message);

}
