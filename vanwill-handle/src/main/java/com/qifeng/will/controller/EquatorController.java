package com.qifeng.will.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.dadiyang.equator.Equator;
import com.github.dadiyang.equator.FieldInfo;
import com.github.dadiyang.equator.GetterBaseEquator;
import com.qifeng.will.base.bean.Student;
import com.qifeng.will.base.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("equator")
@RestController
public class EquatorController {

    @RequestMapping("test")
    public String equator(){

        Equator equator = new GetterBaseEquator();
        // 支持比对两个不同类型的对象，默认只比对两个类字段的交集，即两个类都有的字段才比对
        Student user1 = new Student("川田", 22);
        UserInfoDto user2 = new UserInfoDto("spring", "四级");

        // 判断属性是否完全相等
        boolean flag = equator.isEquals(user1, user2);
        log.info("flag={}",flag);
        // 获取不同的属性
        List<FieldInfo> diff = equator.getDiffFields(user1, user2);

        return JSONObject.toJSONString(diff);
    }
}
