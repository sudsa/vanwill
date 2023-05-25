package com.qifeng.will.controller;

import com.qifeng.will.base.dto.*;
import com.qifeng.will.base.vo.BaseResponse;
import com.qifeng.will.base.vo.ResponseResult;
import com.qifeng.will.srv.command.BaseTaskCommand;
import com.qifeng.will.srv.provider.TaskStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping(value ="para")
@Slf4j
public class ParalledController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> limitRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisObjectTemplate;

    @Autowired
    private TaskStrategyFactory taskStrategyFactory;

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    @RequestMapping("redis")
    public BaseResponse redis(){
        UserInfoDto userInfoDto = UserInfoDto.builder().name("JAY").build();
        log.info("userInfoDto = {}",userInfoDto);
        redisTemplate.opsForValue().set("useIrnfoDto1",userInfoDto.toString());
        System.out.println(redisTemplate.opsForValue().get("useIrnfoDto1"));
        limitRedisTemplate.opsForValue().set("useIrnfoDto2",userInfoDto);
        UserInfoDto userInfo = (UserInfoDto) limitRedisTemplate.opsForValue().get("useIrnfoDto2");
        System.out.println(userInfo.getName());

        redisObjectTemplate.opsForValue().set("useIrnfoDto3",userInfoDto);
        UserInfoDto dtp = (UserInfoDto) redisObjectTemplate.opsForValue().get("useIrnfoDto3");
        System.out.println(dtp.getName());


        return ResponseResult.genSuccessResp();
    }


    @RequestMapping("many")
    public ResponseResult parall(){
        BuildCombineInfoResponse response = parallelQueryAppHeadPageInfo("");
        log.info("BuildCombineInfoResponse = {}",response);
        return ResponseResult.genSuccessResp(response);
    }


    //并行查询App首页信息
    public BuildCombineInfoResponse parallelQueryAppHeadPageInfo(String req) {

        long beginTime = System.currentTimeMillis();
        System.out.println("开始并行信息，开始时间：" + beginTime);

        CompletionService<BaseRspDto<Object>> baseDTOCompletionService = new ExecutorCompletionService<BaseRspDto<Object>>(executor);

        baseDTOCompletionService.submit(new BaseTaskCommand("1",null, taskStrategyFactory));
        baseDTOCompletionService.submit(new BaseTaskCommand("2",null, taskStrategyFactory));
        baseDTOCompletionService.submit(new BaseTaskCommand("3",null, taskStrategyFactory));
        UserInfoDto userInfoDTO = null;
        BannerDto bannerDTO = null;
        LabelDto labelDTO = null;

        try {
            //因为提交了3个任务，所以获取结果次数是3
            for (int i = 0; i < 3; i++) {
                // 获取任务结果；poll方法不会阻塞等待结果，获取时如果是没有结果，则返回null，可以执行在指定时间内等待
                Future<BaseRspDto<Object>> baseRspDTOFuture = baseDTOCompletionService.poll(1, TimeUnit.SECONDS);
                BaseRspDto baseRspDTO = baseRspDTOFuture.get();
                if ("1".equals(baseRspDTO.getKey())) {
                    userInfoDTO = (UserInfoDto) baseRspDTO.getData();
                } else if ("2".equals(baseRspDTO.getKey())) {
                    bannerDTO = (BannerDto) baseRspDTO.getData();
                } else if ("3".equals(baseRspDTO.getKey())) {
                    labelDTO = (LabelDto) baseRspDTO.getData();
                }
            }




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("结束并行信息,总耗时：" + (System.currentTimeMillis() - beginTime));

        //
        List<Callable<BaseRspDto<Object>>> taskList = new ArrayList<>();
        /*taskList.add(userInfoDTOCallableTask);
        taskList.add(bannerDTOCallableTask);
        taskList.add(labelDTODTOCallableTask);*/
        taskList.add(new BaseTaskCommand("1",null, taskStrategyFactory));
        taskList.add(new BaseTaskCommand("2",null, taskStrategyFactory));
        taskList.add(new BaseTaskCommand("3",null, taskStrategyFactory));
        List<BaseRspDto<Object>> resultList = executeTask(taskList);

        for (int i = 0; i < resultList.size(); i++) {
            //
            BaseRspDto<Object> baseRspDto = resultList.get(i);
            if ("1".equals(baseRspDto.getKey())) {
                userInfoDTO = (UserInfoDto) baseRspDto.getData();
            } else if ("2".equals(baseRspDto.getKey())) {
                bannerDTO = (BannerDto) baseRspDto.getData();
            } else if ("3".equals(baseRspDto.getKey())) {
                labelDTO = (LabelDto) baseRspDto.getData();
            }
        }

        return buildResponse(userInfoDTO, bannerDTO, labelDTO);
    }

    private BuildCombineInfoResponse buildResponse(UserInfoDto userInfoDto, BannerDto bannerDto, LabelDto labelDto){
        return  new BuildCombineInfoResponse(userInfoDto,bannerDto,labelDto);
    }

    public List<BaseRspDto<Object>> executeTask(List<Callable<BaseRspDto<Object>>> taskList) {

        List<BaseRspDto<Object>> resultList = new ArrayList<>();
        //校验参数
        if (taskList == null || taskList.size() == 0) {
            return resultList;
        }


        CompletionService<BaseRspDto<Object>> baseDTOCompletionService = new ExecutorCompletionService<BaseRspDto<Object>>(executor);
        //提交任务
        for (Callable<BaseRspDto<Object>> task : taskList) {
            baseDTOCompletionService.submit(task);
        }

        try {
            //遍历获取结果
            for (int i = 0; i < taskList.size(); i++) {
                Future<BaseRspDto<Object>> baseRspDTOFuture = baseDTOCompletionService.poll(2, TimeUnit.SECONDS);
                resultList.add(baseRspDTOFuture.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return resultList;
    }


}
