package com.qifeng.will.base.vo;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 214518705952305511L;

    private boolean success;

    private String msg;

    private String code;

    public BaseResponse() {

    }

    public BaseResponse(boolean success) {
        this.success = success;
    }

    public BaseResponse(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public BaseResponse(boolean success, String msg, String code) {
        this.success = success;
        this.msg = msg;
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public static BaseResponse genParamErrorResp(String msg) {
        return genErrorResp(msg, BaseConstant.ResponseCode.ILLEGAL_ARGUMENT);
    }

    public static BaseResponse genSystemErrorResp() {
        return genErrorResp(BaseConstant.ResponseCode.UNKNOWN_MSG, BaseConstant.ResponseCode.UNKNOWN);
    }

    public static BaseResponse genErrorResp(String msg, String code) {
        return new BaseResponse(false, msg, code);
    }

    public static BaseResponse genSuccessResp() {
        return genSuccessResp(null);
    }

    public static BaseResponse genSuccessResp(String msg) {
        return new ResponseResult(true, msg,  BaseConstant.ResponseCode.SUCCESS);
    }
}
