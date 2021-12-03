

package com.qifeng.will.base.vo;

import com.google.gson.Gson;

/**
 * @ClassName: ResponseResult
 * @Description: 统一请求返回结果model
 * ResponseResult<UcsMember> resp = gson.fromJson(result, new TypeToken<ResponseResult<UcsMember>>() {}.getType());
 * @author Qing.Luo
 * @date 2015年5月28日 上午10:07:59
 * @version
 * @param <T>
 */
public class ResponseResult<T> extends BaseResponse {

	private static final long serialVersionUID = 7999301345854022355L;

    public ResponseResult() {
        super();
    }

    public ResponseResult(boolean success) {
        super(success);
    }

    public ResponseResult(boolean success, String msg){
        super(success, msg);
    }

    public ResponseResult(boolean success, String msg, String code){
        super(success, msg, code);
    }
    
    private T data;

    /* 不提供直接设置errorCode的接口，只能通过setErrorInfo方法设置错误信息 */
    // private String errorCode;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

//    public static ResponseResult genParamErrorResp(String msg) {
//        return genErrorResp(msg, BaseConstant.ResponseCode.ILLEGAL_ARGUMENT);
//    }
//
//    public static ResponseResult genSystemErrorResp() {
//        return genErrorResp(BaseConstant.ResponseCode.UNKNOWN_MSG, BaseConstant.ResponseCode.UNKNOWN);
//    }
//
//    public static ResponseResult genErrorResp(String msg, String code) {
//        return new ResponseResult(false, msg, code);
//    }

    public static <T> ResponseResult<T> genSuccessResp(T data) {
        return genSuccessResp(null, data);
    }

    public static <T> ResponseResult<T> genSuccessResp(String msg, T data) {
        ResponseResult rs = new ResponseResult(true, msg,  BaseConstant.ResponseCode.SUCCESS);
        rs.setData(data);
        return rs;
    }
}
