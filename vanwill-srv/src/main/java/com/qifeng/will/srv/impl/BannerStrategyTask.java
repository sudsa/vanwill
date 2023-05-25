package com.qifeng.will.srv.impl;

import com.qifeng.will.base.dto.BannerDto;
import com.qifeng.will.base.dto.BaseRspDto;
import com.qifeng.will.srv.service.IBaseTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BannerStrategyTask implements IBaseTask {


    @Override
    public String getTaskType() {
        return "2";
    }

    @Override
    public BaseRspDto<Object> execute(String req) {
        log.info("BannerStrategyTask execute");
        //业务处理

        BaseRspDto<Object> banner = new BaseRspDto<Object>();
        banner.setKey("2");
        banner.setData(BannerDto.builder().name("儿儿儿儿").build());
        return banner;
    }
}
