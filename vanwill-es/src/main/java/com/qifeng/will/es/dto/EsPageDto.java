package com.qifeng.will.es.dto;

/**
 * ClassName: EsPageDto
 * Description:
 *
 * @author FUIOU
 * @date 2023/5/24 14:19
 */
public class EsPageDto<T> {
    private Integer pageNum;


    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
