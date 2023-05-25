package com.qifeng.will.srv.impl;

import com.qifeng.will.base.dto.BannerDto;
import com.qifeng.will.base.dto.BaseRspDto;
import com.qifeng.will.srv.service.IBaseTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoStrategyTask implements IBaseTask {


    @Override
    public String getTaskType() {
        return "1";
    }

    @Override
    public BaseRspDto<Object> execute(String req) {

        log.info("UserInfoStrategyTask execute");
        //业务处理
        BaseRspDto<Object> userinfo = new BaseRspDto<Object>();
        userinfo.setKey("1");
        userinfo.setData(BannerDto.builder().name("咦咦咦咦").build());
        return userinfo;
    }
}
