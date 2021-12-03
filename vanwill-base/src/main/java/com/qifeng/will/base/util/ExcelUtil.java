package com.qifeng.will.base.util;

import cn.hutool.http.Header;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 杜志诚
 * @create 2020/12/28 0028 13:18
 */
@Component
public class ExcelUtil {

  private static ExcelUtil excelUtil;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private HttpServletResponse response;

  public static HttpServletRequest getRequest() {
    return excelUtil.request;
  }

  public static HttpServletResponse getResponse() {
    return excelUtil.response;
  }

  /**
   * 根据excel输入流，读取excel文件数据
   *
   * @param inputStream excel表格的输入流
   * @return 返回双重list的集合
   */
  public static <T> List<T> readData(InputStream inputStream, Class<T> tClass) {
    ExcelListener<T> listener = new ExcelListener<>();
    ExcelReader excelReader =
        EasyExcelFactory.read(inputStream, tClass, listener).headRowNumber(1).build();
    excelReader.read();
    List<T> data = listener.getData();
    excelReader.finish();
    return data;
  }

  /**
   * 写入 excel 文件数据 , 带样式的
   *
   * @param path 写入的路径
   * @param tClass 写入所用到的bean
   * @param list 需要写入的数据
   * @param function 满足此条件即可进行修改样式
   * @param consumer 满足条件对样式的具体操作
   * @param commonConsumer 公共样式操作
   */
  public static <T> void writeToDir(
      String path,
      Class<T> tClass,
      List<T> list,
      @NotNull(message = "样式判断逻辑不能为空") Function<T, Boolean> function,
      Consumer<WriteCellStyle> consumer,
      Consumer<WriteCellStyle> commonConsumer) {
    HorizontalCellStyleStrategy horizontalCellStyleStrategy =
        setStyle(list, function, consumer, commonConsumer);
    EasyExcel.write(path, tClass)
        .registerWriteHandler(horizontalCellStyleStrategy)
        .sheet("1")
        .doWrite(list);
  }

  /**
   * 写入excel文件数据到浏览器 , 带样式的
   *
   * @param fileName 文件名称
   * @param tClass 写入所用到的bean
   * @param list 需要写入的数据
   * @param function 满足此条件即可进行修改样式
   * @param consumer 对样式的具体操作
   */
  public static <T> void writeToBrowser(
      String fileName,
      Class<T> tClass,
      List<T> list,
      @NotNull(message = "样式判断逻辑不能为空") Function<T, Boolean> function,
      Consumer<WriteCellStyle> consumer,
      Consumer<WriteCellStyle> commonConsumer)
      throws IOException {
    OutputStream outputStream = null;
    try {
      HorizontalCellStyleStrategy horizontalCellStyleStrategy =
          setStyle(list, function, consumer, commonConsumer);
      ExcelUtil.setHeather(getRequest(), getResponse(), fileName);
      outputStream = getResponse().getOutputStream();
      EasyExcel.write(outputStream, tClass)
          .registerWriteHandler(horizontalCellStyleStrategy)
          .sheet("1")
          .doWrite(list);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      assert outputStream != null;
      outputStream.flush();
      outputStream.close();
    }
  }

  /**
   * 写入excel文件数据到指定文件
   *
   * @param path 写入路径+fileName
   * @param tClass 写入所用到的bean
   * @param list 需要写入的数据
   */
  public static <T> void writeToDir(String path, Class<T> tClass, List<T> list) {
    EasyExcel.write(path, tClass).sheet("1").doWrite(list);
  }

  /**
   * 写入excel文件数据到浏览器
   *
   * @param fileName 文件名称
   * @param tClass 写入所用到的bean
   * @param list 需要写入的数据
   */
  public static <T> void writeToBrowser(String fileName, Class<T> tClass, List<T> list) {
    ServletOutputStream outputStream = null;
    try {
      ExcelUtil.setHeather(getRequest(), getResponse(), fileName);
      outputStream = getResponse().getOutputStream();
      EasyExcel.write(getResponse().getOutputStream(), tClass).sheet("1").doWrite(list);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        assert outputStream != null;
        outputStream.flush();
        outputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 下载 xlsx 模板文件
   *
   * @param request 请求
   * @param response 响应
   * @param fileName 文件名称 不加后缀 统一.xlsx
   * @param path 全路径,并且加上文件名
   */
  public static void downloadFile(
      HttpServletRequest request, HttpServletResponse response, String fileName, String path) {
    BufferedInputStream bis = null;
    OutputStream os = null;
    try {
      setHeather(request, response, fileName);
      response.addHeader("Content-Length", String.valueOf(new File(path).length()));
      byte[] buff = new byte[1024];
      os = response.getOutputStream();
      bis = new BufferedInputStream(new FileInputStream(path));
      int i = bis.read(buff);
      while (i != -1) {
        os.write(buff, 0, buff.length);
        os.flush();
        i = bis.read(buff);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        assert bis != null;
        assert os != null;
        bis.close();
        os.flush();
        os.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void setHeather(
      HttpServletRequest request, HttpServletResponse response, String fileName) {
    String agent = request.getHeader(Header.USER_AGENT.getValue()).toLowerCase();
    String codedFileName;
    try {
      codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
      if (agent.contains("firefox")) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader(
            "content-disposition",
            "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xlsx");
      } else {
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  private static <T> HorizontalCellStyleStrategy setStyle(
      List<T> list,
      @NotNull(message = "样式判断条件不能为空") Function<T, Boolean> function,
      Consumer<WriteCellStyle> consumer,
      Consumer<WriteCellStyle> consumer1) {
    List<WriteCellStyle> listStyle = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      listStyle.add(new WriteCellStyle());
    }
    for (int i = 0; i < list.size(); i++) {
      WriteCellStyle writeCellStyle = listStyle.get(i);
      if (Objects.nonNull(consumer1)) {
        consumer1.accept(writeCellStyle);
      }
      if (Objects.requireNonNull(function).apply(list.get(i))) {
        if (Objects.nonNull(consumer)) {
          consumer.accept(writeCellStyle);
        }
      }
    }
    return new HorizontalCellStyleStrategy(new WriteCellStyle(), listStyle);
  }

  @PostConstruct
  public void init() {
    excelUtil = this;
    excelUtil.request = this.request;
    excelUtil.response = this.response;
  }
}
