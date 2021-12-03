package com.qifeng.will.srv.impl;

import com.alibaba.fastjson.JSON;
import com.qifeng.will.base.util.ExportThread;
import com.qifeng.will.base.warrior.entity.ErrorInfo;
import com.qifeng.will.srv.service.DzUserService;
import com.qifeng.will.srv.service.ExportService;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Service
public class ExportServiceImpl implements ExportService {

    private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    @Autowired
    private DzUserService dzUserService;

    @Override
    public Map<String,Object> importData(MultipartFile file) throws Exception{
        final Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("{},开始导入数据...", format.format(now));
        //设置一个信号量为5的信号量，限制同时运行的线程数量最大为5
        Semaphore semaphore = new Semaphore(10);

        Map<String,Object> map = new HashMap<>();
        //多线程编程需要一个线程安全的ArrayList
        List<ErrorInfo> list = Collections.synchronizedList(new ArrayList<ErrorInfo>());
        Workbook workbook = null;
        String filename = file.getOriginalFilename();
        if(filename.endsWith("xls")){
            workbook = new HSSFWorkbook(file.getInputStream());
        }else if(filename.endsWith("xlsx")){
            workbook = new XSSFWorkbook(file.getInputStream());
        }else {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setErrorMsg("请上传xlx或xlsx格式的文件");
            list.add(errorInfo);
            map.put("code",501);
            map.put("data",list);
            return map;
        }
        Sheet sheet = workbook.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        logger.info("获取到workbook中的总行数:{}" ,physicalNumberOfRows);
        //第一行是表头，实际行数要减1
        int rows = physicalNumberOfRows - 1;
        //一个线程让他处理200个row,也许可以处理更多吧
        int threadNum = rows/200 + 1; //线程数量

        //设置一个倒计时门闩，用来处理主线程等待蚂蚁线程执行完成工作之后再运行
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        //查询是否重名
        Set<String> names = dzUserService.selectByExample(null);//.stream().map(DzUser::getUsername).collect(Collectors.toSet());
        //list.stream().sorted(Comparator.comparing(SeriesData::getName));
        //创建一个定长的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        logger.info("开始创建线程,数据总行数:{},线程数量:{}",rows,threadNum);

        List<Future<Integer>> futures = new ArrayList<>();
        int successCount = 0;

        for(int i = 1; i <= threadNum; i++){

            int startRow = (i-1)*200 +1;
            int endRow = i*200;
            if(i == threadNum){
                endRow = rows;
            }
            logger.info("开始执行线程方法,线程ID:<{}>,线程名称:<{}>",Thread.currentThread().getId(),Thread.currentThread().getName());
            Future<Integer> future = executorService.submit(new ExportThread(semaphore, workbook, startRow, endRow, list, names,countDownLatch));
            futures.add(future);
            logger.info("结束线程执行方法,返回结果:<{}>,当前线程ID:<{}>,当前线程名称:<{}>",JSON.toJSONString(future),Thread.currentThread().getId(),Thread.currentThread().getName());
            //get方法中可以设置超时时间，即规定时间内没有返回结果，则继续运行
            //get方法是线程阻塞的，调用get方法会导致后续线程因主线程阻塞而没有创建，达不到效果。
            //successCount += future.get();
        }
        //主线程等待子线程完成任务,60秒还没执行完成就继续执行

        for(Future<Integer> future : futures){
            successCount += future.get();

            //TODO
            //dzUserService.insertSelective(user);
        }
        //主线程等待子线程全部跑完才继续运行。设置60秒等待时间，超时后继续执行。
        countDownLatch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();

        Date endDate = new Date();
        long difference = endDate.getTime() - now.getTime();
        String duration = DurationFormatUtils.formatDuration(difference, "HH:mm:ss");
        logger.info("执行完成,错误信息:{}", JSON.toJSONString(list));
        logger.info("{},结束导入,共{}条数据，导入成功:{}，耗时={}", format.format(endDate), rows,successCount,duration);
        map.put("code",200);
        map.put("msg","结束导入,共" + rows + "条数据，导入成功" + successCount + "条，耗时:" +duration);
        map.put("data",list);
        return map;
    }

    @Override
    public Map<String, Object> importDataYiBan(MultipartFile file) {
        return null;
    }

    @Override
    public Workbook exportData() {
        return null;
    }
}
