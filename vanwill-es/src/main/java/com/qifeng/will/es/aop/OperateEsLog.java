package com.qifeng.will.es.aop;

import java.lang.annotation.*;

/**
 * ClassName: OperateEsLog
 * Description:
 *
 * @author FUIOU
 * @date 2023/5/24 16:24
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateEsLog {

    String operType() default "01";
    String businessType() default "配置管理";
    String operDesc() default "配置管理";
}
