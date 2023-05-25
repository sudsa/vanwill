package com.qifeng.will.base.dto;

import lombok.Data;

@Data
public class BaseRspDto<T extends Object> {

    private String msg;

    private T data;

    private String key;//区分标识


}
