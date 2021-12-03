package com.qifeng.will.base.util;

import com.qifeng.will.base.warrior.entity.DzUser;
import com.qifeng.will.base.warrior.entity.ErrorInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class ExportThread implements Callable<Integer> {

    private Logger logger = LoggerFactory.getLogger(ExportThread.class);

    private Workbook workbook;

    private Integer startRow;

    private Integer endRow;

    private List<ErrorInfo> errorInfoList;

    private Set<String> names;

    //private DzUserService userService;

    private Semaphore semaphore;

    private CountDownLatch latch;

    public ExportThread(Semaphore semaphore, Workbook workbook, Integer startRow, Integer endRow, List<ErrorInfo> errorInfoList, Set<String> names, CountDownLatch latch){
        this.workbook = workbook;
        this.startRow = startRow;
        this.endRow = endRow;
        this.errorInfoList = errorInfoList;
        this.names = names;
        //this.userService = userService;
        this.semaphore = semaphore;
        this.latch = latch;
    }

    @Override
    public Integer call() throws Exception {
        logger.info("线程ID：<{}>开始运行,startRow:{},endRow:{}",Thread.currentThread().getId(),startRow,endRow);
        semaphore.acquire();
        logger.info("消耗了一个信号量，剩余信号量为:{}",semaphore.availablePermits());
        latch.countDown();
        Sheet sheet = workbook.getSheetAt(0);
        int count = 0;
        for(int i = startRow; i <= endRow; i++){
            DzUser user = new DzUser();
            Row row = sheet.getRow(i);
            Cell cell1 = row.getCell(0);
            String username = cell1.getStringCellValue();
            user.setUsername(username);
            user.setPassword("123456");
            Cell cell2 = row.getCell(1);
            String realname = cell2.getStringCellValue();
            user.setEmail(realname);
            if(names.contains(username)){
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setRow(startRow);
                errorInfo.setColumn(1);
                errorInfo.setErrorMsg("第" + startRow + "行用户账号已存在");
                errorInfoList.add(errorInfo);
                break;
            }
            //if(userService.insertSelective(user)){
                count ++;
            //}
        }
        semaphore.release();
        return count;
    }

}
