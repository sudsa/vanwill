package com.qifeng.will.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class OperateDto {

    //业务id
    private String businessId;

    //业务类型
    private String businessType;

    //操作类型
    private String operateType;

    //操作人id
    private String operId;

    //操作时间
    private String operTime;
    //操作列表
    List<OperateDetail> operDetails;

}
