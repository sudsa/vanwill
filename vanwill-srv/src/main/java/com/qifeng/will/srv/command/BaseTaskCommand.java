package com.qifeng.will.srv.command;

import com.qifeng.will.base.dto.BannerDto;
import com.qifeng.will.base.dto.BaseRspDto;
import com.qifeng.will.srv.provider.TaskStrategyFactory;

import java.util.concurrent.Callable;

public class BaseTaskCommand implements Callable<BaseRspDto<Object>> {

    private String key;

    private String req;

    private TaskStrategyFactory taskStrategyFactory;


    public BaseTaskCommand(String key, String req, TaskStrategyFactory taskStrategyFactory) {
        this.key = key;
        this.req = req;
        this.taskStrategyFactory = taskStrategyFactory;
    }

    @Override
    public BaseRspDto<Object> call() throws Exception {

        BaseRspDto<Object> baseRspDto = taskStrategyFactory.executeTask(key,req);
        return baseRspDto;
        /*switch(key) {
            case "1":

                BaseRspDto<Object> userInfBaseRspDTO = taskStrategyFactory.executeTask(key,req);
                userInfBaseRspDTO.setKey("userInfoDTO");
                userInfBaseRspDTO.setData(BannerDto.builder().name("banner").build());
                return userInfBaseRspDTO;
            case "2":
                BaseRspDto<Object> bannerBaseRspDTO = taskStrategyFactory.executeTask(key,req);
                bannerBaseRspDTO.setKey("bannerDTO");
                bannerBaseRspDTO.setData(BannerDto.builder().name("banner").build());
                return bannerBaseRspDTO;

            case "3":
                BaseRspDto<Object> labelBaseRspDTO = taskStrategyFactory.executeTask(key,req);
                labelBaseRspDTO.setKey("labelDTO");
                labelBaseRspDTO.setData(BannerDto.builder().name("banner").build());
                return labelBaseRspDTO;
           default:
               return null;
        }*/


    }
}
