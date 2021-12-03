package com.qifeng.will.controller;

import com.qifeng.will.base.event.ConfigEventPublisher;
import com.qifeng.will.srv.service.ExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(value = "[WARRIOR]-ExportController", tags = "[WARRIOR]-ExportController")
@RestController
@RequestMapping("ex")
public class ExportController {


    @Autowired
    private ExportService exportService;

    @Autowired
    private ConfigEventPublisher configEventPublisher;
    /**
     * 多线程导入
     * @param file
     * @return
     */
    @ApiOperation("多线程导入")
    @PostMapping("/importBatch")
    public Map importData(MultipartFile file){
        Map<String, Object> map = null;
        try {
            map = exportService.importData(file);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",501);
            map.put("msg","数据出错");
            return map;
        }
    }

    /**
     * 单线程导入
     * @param file
     * @return
     */
    @ApiOperation("单线程导入")
    @PostMapping("/importSingleThread")
    public Map importData2(MultipartFile file){
        Map<String, Object> map = null;
        try {
            map = exportService.importDataYiBan(file);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",501);
            map.put("msg","数据出错");
            return map;
        }finally {
            configEventPublisher.pushListener("34565687uf8989");
        }
    }

    /**
     * 导出excel
     * @param response
     * @throws Exception
     */
    @ApiOperation("导出excel")
    @GetMapping("/export")

    public void exportData(HttpServletResponse response) throws Exception{
        Workbook workbook = exportService.exportData();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=user.xlsx");
        workbook.write(response.getOutputStream());
    }

}
