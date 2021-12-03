package com.qifeng.will.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.qifeng.will.es.dto.EsBaseDto;
import com.qifeng.will.base.dto.EsLogQuery;
import com.qifeng.will.es.dto.EsPageDto;
import com.qifeng.will.base.dto.OperateDto;
import com.qifeng.will.base.util.StringUtil;
import com.qifeng.will.base.vo.ResponseResult;
import com.qifeng.will.base.warrior.entity.SysConfig;
import com.qifeng.will.es.aop.OperateEsLog;
import com.qifeng.will.es.config.ElasticSearchConfig;
import com.qifeng.will.es.service.OperateLogService;
import io.swagger.annotations.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhw
 * @since 2021-07-22
 */
@Api(value = "[WARRIOR]-SysConfigController", tags = "[WARRIOR]-SysConfigController")
@ApiSort(10)
@Controller
@RequestMapping("/sys")
public class SysConfigController {

    private static final Logger log = LoggerFactory.getLogger(SysConfigController.class);

    @Autowired
    @Qualifier("esSafeClient")
    RestHighLevelClient restHighLevelClient;

    @Resource
    private OperateLogService esOperateService;

    @OperateEsLog(operType = "01", businessType = "配置管理", operDesc = "配置管理")
    @ApiOperation(value = "测试")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功")
            , @ApiResponse(code = 400, message = "失败")
            , @ApiResponse(code = 500, message = "服务器出错")})
    @ApiOperationSupport(order = 1, responses = @DynamicResponseParameters(properties = {
            @DynamicParameter(value = "响应码", name = "code"),
            @DynamicParameter(value = "描述", name = "message"),
            @DynamicParameter(value = "返回结果", name = "data", dataTypeClass = SysConfig.class)// 配置返回结果类
    }),author = "willson")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long")
    @ResponseBody
    @PostMapping("/operateTST")
    //public String operateTST(@ApiParam(hidden = true) @RequestBody DeviceHistory history){
    public String operateTST(){
        //初始化请求，构造函数中指定index名
        IndexRequest indexRequest = new IndexRequest("sysconfigtest");
        //设置id
        indexRequest.id(StringUtil.getUuidString());
        //indexRequest.id("2");
/*        indexRequest.source("userName", "zhangsan");
        indexRequest.source("age", 18);*/
        SysConfig sys = new SysConfig();
        sys.setSetBy("suda");
        sys.setValue("vv9");
        sys.setVariable("dd");
        String jsonString = JSON.toJSONString(sys);
        //要保存的内容
        indexRequest.source(jsonString, XContentType.JSON);
        //执行操作
        IndexResponse index = null;
        try {
            index = restHighLevelClient.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS );
        } catch (IOException e) {
            e.printStackTrace();
        }
        //提取有用的响应数据
        System.out.println(index);
        return "ok";
    }

    /**
     * esLog 日志分页查询
     *
     * @param esLogQuery 分页参数
     *                   {
     *                   "indexName":"operatelog",
     *                   "businessId":"1",
     *                   "businessType" : "qwe",
     *                   "page": {
     *                   "pageNum":"1",
     *                   "pageSize":"5"
     *                   }
     *                   }
     * @return 数据
     */
    @ApiOperation("分页查询")
    @PostMapping("queryPage")
    @ResponseBody
    public ResponseResult<EsPageDto<OperateDto>> queryList(@RequestBody EsLogQuery esLogQuery) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(esLogQuery.getPageNum());
        builder.size(esLogQuery.getPageSize());
        BoolQueryBuilder must = QueryBuilders.boolQuery();
        //queryBuilder("businessType", esLogQuery.getBusinessType(), must);
        //queryBuilder("businessId", esLogQuery.getBusinessId(), must);
        builder.query(must);
        EsPageDto<OperateDto> pageDto = esOperateService.pageQuery(esLogQuery.getIndexName(), builder,
                new TypeReference<OperateDto>() {
                });
        return ResponseResult.genSuccessResp(pageDto);
    }

    @ApiOperation("创建一个index")
    @PostMapping("create")
    @ResponseBody
    public ResponseResult createIndex(@RequestBody EsLogQuery esLogQuery) {
        EsBaseDto<EsLogQuery> esLogQueryEsBaseDto = new EsBaseDto<>();
        esLogQueryEsBaseDto.setData(esLogQuery);
        esLogQueryEsBaseDto.setId(StrUtil.uuid());
        Boolean flag = esOperateService.createIndex(esLogQuery.getIndexName(),esLogQueryEsBaseDto);
        return ResponseResult.genSuccessResp(flag);
    }

	
}
