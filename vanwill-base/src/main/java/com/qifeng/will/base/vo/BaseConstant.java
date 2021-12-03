package com.qifeng.will.base.vo;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

public abstract class BaseConstant {

    public static final String CONTENT_TYPE = ContentType.create("text/html", Consts.UTF_8).toString();

    public static final String LBS_AK = "1E5D7d20345949abcac20c9b3b2d7a22";
    /**
     * 文件
     *
     * @author huyang
     */
    public interface AspectOrder {
        public static final int Validate = 1001;

        public static final int RequireSign = 1000;
    }
    /**
     * 文件
     *
     * @author huyang
     */
    public interface File {
        public static final String TXT = ".txt";

        public static final String XLS = ".xls";

        public static final String XLSX = ".xlsx";

        public static final String SEPARATOR = "/";
    }

    /**
     * Session
     *
     * @author huyang
     */
    public interface Session {
        /**
         * 用户
         */
        public static final String USER = "user";
        public final static String USERID = "userId";
        public final static String USERNAME = "userName";
        public final static String AUTH = "md5Auth";
        public final static String SERVICETICKET = "ServiceTicket";
        public final static String MENUSTR = "menustr";
    }

    public interface Cookie {
        //用户名Cookie Name
        String CAS_COOKIE_NAME = "CASLOGINUSER";
        //用户名Cookie Name
        String CAS_COOKIE_AUTH = "CASAUTHINFO";
    }

    /**
     * 分隔符
     *
     * @author huyang
     */
    public interface Separate {
        public static final String COMMA = ",";
        public static final String UNDERLINE = "_";
        public static final String TWO_UNDERLINE = "__";
        public static final String PERIODS = ".";
        public static final String AND = "&";
        public static final String QUESTION = "?";
        public static final String PERCENT = "%";
    }

    /**
     * 表单默认状态
     *
     * @author huyang
     */
    public interface Status {
        /**
         * 禁用
         */
        public static final String DISABLED = "00";

        /**
         * 启用
         */
        public static final String ENABLE = "01";

        /**
         * 删除
         */
        public static final String DELETE = "02";

        /**
         * 未修改默认密码
         */
        public static final String UN_CHANGED = "03";


    }

    /**
     * 布尔值
     *
     * @author huyang
     */
    public interface Bool {
        public static final String TRUE = "1";

        public static final String FALSE = "0";
    }

    /**
     * 效验方式
     *
     * @author Administrator
     * @version V1.0
     * @Description TODO
     * @date 2015年12月10日
     */
    public interface LoginType {
        /**
         * 用户输入数据提交表单方式登录
         */
        String FORM = "form";
        /**
         * 单点登录服务器同步登录
         */
        String SYNC = "sync";
    }

    public interface PermissionType {
        /**
         * 父菜单
         */
        String PARRENT = "00";
        /**
         * 子菜单
         */
        String LEAF = "01";
        /**
         * 动作
         */
        String ACTION = "02";
    }

    /**
     * 返回码
     */
    public interface ResponseCode {
        /**
         * 成功
         */
        String SUCCESS = "0000";
        /**
         * 参数错误
         */
        String ILLEGAL_ARGUMENT = "0001";
        /**
         * 系统异常
         */
        String UNKNOWN = "0002";
        /**
         * 业务错误
         */
        String BUSINESS_ERROR = "0003";
        String UNKNOWN_MSG = "系统异常";
    }
}
