package com.qifeng.will.base.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杜志诚
 * @create 2021/6/21 0021 17:10
 */
public class ExcelListener<T> extends AnalysisEventListener<T> {

  /** 自定义用于暂时存储data 可以通过实例获取该值 */
  private List<T> data = new ArrayList<>();

  /**
   * 每解析一行都会回调invoke()方法
   *
   * @param object 读取后的数据对象
   * @param context 内容
   */
  @Override
  public void invoke(T object, AnalysisContext context) {
    // 数据存储到list，供批量处理，或后续自己业务逻辑处理。
    int count = 3000;
    if (data.size() < count) {
      data.add(object);
    }

    // 根据自己业务做处理
  }

  /**
   * 全部解析完成
   *
   * @param context 内容
   */
  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {}

  /**
   * 返回数据
   *
   * @return 返回读取的数据集合
   */
  public List<T> getData() {
    return data;
  }

  /**
   * 设置读取的数据集合
   *
   * @param data 设置读取的数据集合
   */
  public void setData(List<T> data) {
    this.data = data;
  }
}
