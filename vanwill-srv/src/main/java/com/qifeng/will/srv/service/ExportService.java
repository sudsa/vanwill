package com.qifeng.will.srv.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ExportService {

    Map<String, Object> importData(MultipartFile file) throws Exception;

    Map<String, Object> importDataYiBan(MultipartFile file);

    Workbook exportData();
}
