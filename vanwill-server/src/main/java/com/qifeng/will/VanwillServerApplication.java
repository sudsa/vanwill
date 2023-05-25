package com.qifeng.will;

import com.dtp.core.spring.EnableDynamicTp;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@EnableDynamicTp
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

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public static void main(String[] args) {
		try{

			SpringApplication.run(VanwillServerApplication.class, args);
		}finally {
			log.info("VanwillServer start finish!!!");
		}
	}


	//测试监听redis的key过期
	@PostConstruct
	public void init(){
		log.info("start init()");
		redisTemplate.opsForValue().set("test:key:TOM HANKS:expired_time", "an", 20, TimeUnit.SECONDS);
	}

}
