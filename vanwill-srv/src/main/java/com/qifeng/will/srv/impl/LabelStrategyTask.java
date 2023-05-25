package com.qifeng.will.srv.impl;

import com.qifeng.will.base.dto.BannerDto;
import com.qifeng.will.base.dto.BaseRspDto;
import com.qifeng.will.srv.service.IBaseTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LabelStrategyTask implements IBaseTask {


    @Override
    public String getTaskType() {
        return "3";
    }

    @Override
    public BaseRspDto<Object> execute(String req) {
        log.info("LabelStrategyTask execute");
        //业务处理
        BaseRspDto<Object> label = new BaseRspDto<Object>();
        label.setKey("3");
        label.setData(BannerDto.builder().name("散散散散").build());
        return label;
    }
}
