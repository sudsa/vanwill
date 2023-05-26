package com.qifeng.will.es.service;

import com.alibaba.fastjson.TypeReference;
import com.qifeng.will.base.dto.OperateDto;
import com.qifeng.will.es.dto.EsBaseDto;
import com.qifeng.will.es.dto.EsPageDto;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: OperateLogService
 * Description:
 *
 * @author FUIOU
 * @date 2023/5/24 16:28
 */
@Service
public interface OperateLogService {

    EsPageDto<OperateDto> pageQuery(String index, SearchSourceBuilder builder, TypeReference reference);

    Boolean createIndex(String indexName, EsBaseDto esBaseDto);

    List<OperateDto> search(String indexName,  SearchSourceBuilder builder, Class clazz);
}
