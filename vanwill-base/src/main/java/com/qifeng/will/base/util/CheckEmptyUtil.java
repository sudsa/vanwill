

package com.qifeng.will.base.util;

import java.util.Collection;
import java.util.Map;

/**
 * 判空
 * 
 * @author huyang
 */
public class CheckEmptyUtil {

    /**
     * @Description: 集合判空
     * @param col
     *            待处理的集合
     * @param @return 是否为空
     * @return boolean 空：true，非空false
     */
    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    /**
     * @Description: 集合判空
     * @param obj
     *            待处理的集合
     * @param @return 是否为空
     * @return boolean 空：true，非空false
     */
    public static boolean isEmpty(Object obj) {
    	if(obj!=null && !isEmpty(obj.toString())){
    		return false;
    	}
        return true;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        if (null == map || map.size() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 字符串数字判空
     * 
     * @param str
     *            待判空的字符串数组
     * @return boolean 空：true，非空：false
     */
    public static boolean isEmpty(String[] str) {
        if (str == null || str.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * @Description: 字符串判空
     * @param str
     *            待判断的字符串
     * @return boolean 空：true，非空：false
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }

        return false;

    }

    /**
     * @Description: 字符串非空判断
     * @param str
     *            待处理的字符串
     * @return boolean 非空：true，空：false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);

    }

}
