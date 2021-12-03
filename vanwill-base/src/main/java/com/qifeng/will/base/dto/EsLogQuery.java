package com.qifeng.will.base.dto;

import lombok.Data;

@Data
public class EsLogQuery {

    private String indexName;//operatelog",
    private String businessId;//1",
    private String businessType;//qwe",
    private String startTime;
    private String endTime;
    private int pageNum;
    private int pageSize;

}
