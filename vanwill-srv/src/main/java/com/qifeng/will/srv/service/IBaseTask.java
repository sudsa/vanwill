package com.qifeng.will.srv.service;


import com.qifeng.will.base.dto.BaseRspDto;

//策略模式+工厂模式
public interface IBaseTask {

    //返回每个策略类的key
    String getTaskType();


    BaseRspDto<Object> execute(String req);

    


}
