package com.qifeng.will.base.util;

import com.qifeng.will.base.func.BranchHandler;
import com.qifeng.will.base.func.PresentOrElseHandler;
import com.qifeng.will.base.func.ThrowExceptionFunction;
import org.apache.commons.lang3.StringUtils;

public class ComUtils {

    /**
     * 如果参数是true抛出异常
     * @param b
     * @return ThrowExceptionFunction
     */
    public static ThrowExceptionFunction isTrue(boolean b){
        return (errmessage)->{
            if(b){
                throw  new RuntimeException(errmessage);
            }
        };
    }

    /**
     * 参数为true false时，分别进行不同的操作
     * @param b 判断条件
     * @return BranchHandler
     */
    public static BranchHandler isTrueOrFalse(boolean b){
        return ((trueHandle, falseHandle) -> {
            if(b){
                trueHandle.run();
            }else{
                falseHandle.run();
            }
        });
    }

    public static PresentOrElseHandler<?> isNotBlankOrNull(String str){
        return (consumer, runnable) -> {
            if(StringUtils.isNotBlank(str)){
                consumer.accept(str);
            }else{
                runnable.run();
            }
        };
    }
}
