package com.qifeng.will.es.dto;

/**
 * ClassName: EsBaseDto
 * Description:
 *
 * @author FUIOU
 * @date 2023/5/24 14:19
 */
public class  EsBaseDto<T> {

    private T data;

    private String id;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
