package com.qifeng.will.controller;

import com.dtp.common.dto.ExecutorWrapper;
import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SuppressWarnings("all")
public class DynamicTpController {

    @GetMapping("/dtp-zookeeper-example/test")
    public String test() {
        new Thread(() -> {
            try {
                task();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return "success";
    }

    public void task() throws InterruptedException {
        DtpExecutor dtpExecutor1 = DtpRegistry.getDtpExecutor("dynamic-tp-test-1");
        DtpExecutor dtpExecutor2 = DtpRegistry.getDtpExecutor("dynamic-tp-test-2");
        DtpExecutor ymlDtpExecutor2 = DtpRegistry.getDtpExecutor("execute-xxl-thread-pool");
        DtpExecutor ymlDtpExecutor = DtpRegistry.getDtpExecutor("dtpExecutor1");
        DtpExecutor ioIntensiveExecutor = DtpRegistry.getDtpExecutor("ioIntensiveExecutor");
        //DtpExecutor commonExecutor = DtpRegistry.getDtpExecutor("commonExecutor");

        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            dtpExecutor1.execute(() -> {
                log.info("i am dynamic-tp-test-1 task");
            });
            dtpExecutor2.execute(() -> {
                log.info("i am dynamic-tp-test-2 task");
            });
            ymlDtpExecutor.execute(()->{
                log.info("i am ymlDtpExecutor task");
            });
            ymlDtpExecutor2.execute(()->{
                log.info("i am ymlDtpExecutor2 task");
            });
            ioIntensiveExecutor.execute(()->{
                log.info("i am ioIntensiveExecutor task");
            });
//            commonExecutor.execute(()->{
//                log.info("i am commonExecutor task");
//            });
        }
    }
}
