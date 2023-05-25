package com.qifeng.will.base.func;

import java.util.function.Consumer;

/**
 * 空值和非空值的处理
 */
@FunctionalInterface
public interface PresentOrElseHandler<T extends Object> {

    /**
     * 值不为空执行消费操作
     * 值为空执行其他操作
     * @param action
     * @param emptyAction
     */
    void presentOrElseHandler(Consumer<? super T> action, Runnable emptyAction);
}
