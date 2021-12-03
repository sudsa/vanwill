package com.qifeng.will.controller;

import com.alibaba.fastjson.TypeReference;
import com.qifeng.will.base.dto.EsLogQuery;
import com.qifeng.will.es.dto.EsPageDto;
import com.qifeng.will.es.dto.OperateDto;
import com.qifeng.will.base.vo.ResponseResult;
import com.qifeng.will.es.service.OperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(value = "[WARRIOR]-ElasticController", tags = "[WARRIOR]-ElasticController")
@RestController
@RequestMapping("els")
public class ElasticController {

    @Autowired
    private OperateLogService operateLogService;

    @ApiOperation("esLog日志分页查询")
    @PostMapping("esLog/queryPage")
    private ResponseResult<EsPageDto<OperateDto>> queryList(@RequestBody @Validated EsLogQuery esLogQuery) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        EsPageDto<OperateDto> page = setPage(esLogQuery);
        builder.from((page.getPageNum()-1)*page.getPageSize());
        builder.size(page.getPageSize());
        BoolQueryBuilder must = QueryBuilders.boolQuery();

        //must 相当于 &(与)条件。
        //must not 相当于~(非)条件。
        //should 相当于 | (或)条件。
        //filter 类似must，区别在于它不参与计算分值，在不需要用到分值计算的时候效率更高
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("businessType", esLogQuery.getBusinessType());
        TermQueryBuilder termQuery = QueryBuilders.termQuery("businessId", esLogQuery.getBusinessId());
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("operTime").gt(esLogQuery.getStartTime()).lt(esLogQuery.getEndTime());

        must.filter(matchQuery).filter(termQuery).must(rangeQuery);
        builder.sort(new FieldSortBuilder("operTime").order(SortOrder.DESC));
        builder.query(must);
        return ResponseResult.genSuccessResp(operateLogService.pageQuery(esLogQuery.getIndexName(), builder,
                new TypeReference<OperateDto>() {
                }));
    }

    private EsPageDto<OperateDto> setPage(EsLogQuery esLogQuery) {
        EsPageDto<OperateDto> page = new EsPageDto<>();
        if (esLogQuery.getPageNum() == 0 || esLogQuery.getPageSize() == 0) {
            page.setPageNum(0);
            page.setPageSize(10);
            return page;
        }else{
            page.setPageNum(esLogQuery.getPageNum());
            page.setPageSize(esLogQuery.getPageSize());
        }
        return page;
    }

    @ApiOperation("search查询")
    @PostMapping("search")
    private void search() throws IOException {

        // 条件=
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("city", "北京");
        TermQueryBuilder termQuery = QueryBuilders.termQuery("province", "福建");
        // 范围查询
        RangeQueryBuilder timeFilter = QueryBuilders.rangeQuery("log_time").gt(12345).lt(343750);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder totalFilter = QueryBuilders.boolQuery()
                .filter(matchQuery)
                .filter(timeFilter)
                .mustNot(termQuery);

        int size = 200;
        int from = 0;
        long total = 0;

        do {
            try {
                sourceBuilder.query(totalFilter).from(from).size(size);
                sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

               List<OperateDto> list = operateLogService.search("customer", sourceBuilder, OperateDto.class);
                /*SearchHit[] hits = response.getHits().getHits();
                for (SearchHit hit : hits) {
                    System.out.println(hit.getSourceAsString());
                }

                total = response.getHits().getTotalHits().value;

                System.out.println("测试:[" + total + "][" + from + "-" + (from + hits.length) + ")");

                from += hits.length;

                // from + size must be less than or equal to: [10000]
                if (from >= 10000) {
                    System.out.println("测试:超过10000条直接中断");
                    break;
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (from < total);
    }
}
