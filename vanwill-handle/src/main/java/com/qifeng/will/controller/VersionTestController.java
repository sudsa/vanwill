package com.qifeng.will.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("version")
public class VersionTestController {

    @RequestMapping("test")
    public String test(){
        return "version test 2.0.o.9";
    }
}
