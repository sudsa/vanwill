package com.qifeng.will.base.func;

/**
 * 处理if分支操作
 * 接口的参数是两个Runnable接口-无参无返回值
 * 分别代表TRUE和FALSE的进行操作
 */
@FunctionalInterface
public interface BranchHandler {
    /**
     * if分支操作
     * @param trueHandle true操作
     * @param falseHandle false操作
     */
    void trueOfFalseHandle(Runnable trueHandle, Runnable falseHandle);
}
