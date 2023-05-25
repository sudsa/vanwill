package com.qifeng.will.controller;

import com.google.common.collect.Lists;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.qifeng.will.base.warrior.entity.DzUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value ="")
public class BloomFilterController {

    /** 预计插入的数据 */
    private static Integer expectedInsertions = 1000;

    /** 误判率 */
    private static Double fpp = 0.005;

    /** 布隆过滤器 */
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), expectedInsertions, fpp);

    //Funnel funnel：数据类型，由Funnels类指定即可
    //long expectedInsertions：预期插入的值的数量
    //fpp：错误率
    //BloomFilter.Strategy：hash算法

    public void test1() {
        // 插入 1千万数据
        for (int i = 0; i < expectedInsertions; i++) {
            bloomFilter.put(i);
        }

        // 用1千万数据测试误判率
        int count = 0;
        for (int i = expectedInsertions; i < expectedInsertions *2; i++) {
            if (bloomFilter.mightContain(i)) {
                count++;
            }
        }
        System.out.println("一共误判了：" + count);

    }


    /*public static void main(String[] args) {
        // Redis连接配置，无密码
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6378");
        // config.useSingleServer().setPassword("123456");

        // 初始化布隆过滤器
        RedissonClient client = Redisson.create(config);
        RBloomFilter<Object> bloomFilter = client.getBloomFilter("user");
        bloomFilter.tryInit(expectedInsertions, fpp);

        // 布隆过滤器增加元素
        *//*for (Integer i = 0; i < expectedInsertions; i++) {
            bloomFilter.add(i);
        }*//*

        // 统计元素
        int count = 0;
        for (int i = expectedInsertions; i < expectedInsertions*2; i++) {
            if (bloomFilter.contains(i)) {
                count++;
            }
        }

        System.out.println(bloomFilter.contains(999));
        System.out.println(bloomFilter.contains(9999999));
        System.out.println("误判次数" + count);

    }*/


    public void test3(){
        List<DzUser> dzUserList = Lists.newArrayList();
        DzUser dzUser = DzUser.builder().id("15").username("the cat").password("11").email("111").build();
        dzUserList.add(DzUser.builder().id("1").username("jim").password("111").email("111").build());
        dzUserList.add(DzUser.builder().id("3").username("tom").password("11").email("111").build());
        dzUserList.add(DzUser.builder().id("4").username("gam").password("11").email("111").build());
        dzUserList.add(DzUser.builder().id("2").username("amy").password("11").email("111").build());
        dzUserList.add(dzUser);

        List<DzUser> dzUserList2 = Lists.newArrayList();
        dzUserList2.add(DzUser.builder().id("11").username("big cat").password("111").email("111").build());

        dzUserList2.add(dzUser);
        dzUserList2.add(DzUser.builder().id("3").username("one cat").password("11").email("111").build());

        List<Integer> list = Lists.newArrayList();
        list.add(111);
        list.add(222);
        list.add(333);
        list.add(444);


        System.out.println(Collections.binarySearch(list, 222));

        Collections.reverse(list);
        System.out.println(list);

        System.out.println("sort before:"+dzUserList);


        System.out.println("sort after:"+dzUserList);


        CollectionUtils.union(dzUserList,dzUserList2);

    }
}
