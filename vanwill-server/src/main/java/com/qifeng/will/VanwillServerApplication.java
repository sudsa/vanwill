package com.qifeng.will;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = {"com.qifeng.will.base.warrior.mapper" })
//@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.qifeng.will.srv.feign" })
@Slf4j
@EnableAsync
@EnableScheduling
@EnableAdminServer
public class VanwillServerApplication {

	public static void main(String[] args) {
		try{

			SpringApplication.run(VanwillServerApplication.class, args);
		}finally {
			log.info("VanwillServer start finish!!!");
		}
	}

}
