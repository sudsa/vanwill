package com.qifeng.will.base.util;

import com.qifeng.will.base.vo.BaseConstant;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 字符串处理工具类
 *
 * @Description: 字符串处理工具类
 * @author huyang
 * @date 2015年9月7日 下午9:08:24
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {
	
    /**
     * 将json转成html识别的
     * @param input
     * @return
     */
    public static String escapeHtml(String input){
        return StringEscapeUtils.escapeHtml3(input);
    }
    
    /**
     * json字符串中含&quot; 解码问题
     * @param input
     * @return
     */
    public static String unescapeHtml(String input){
        return StringEscapeUtils.unescapeHtml3(input);
    }
    
    /**
     * 组装查询条件
     * @param cond
     * @param left
     * @param right
     * @return   
     * String
     */
    public static String genLikeWhere(String cond, boolean left, boolean right){
        StringBuilder sb = new StringBuilder();
        if (left){
            sb.append(BaseConstant.Separate.PERCENT);
        }
        sb.append(cond);
        if (right){
            sb.append(BaseConstant.Separate.PERCENT);
        }
        return sb.toString();
    }
    
    /**
     * 组装查询条件
     * @param cond
     * @return   
     * String
     */
    public static String genLikeWhere(String cond){
        return genLikeWhere(cond, true, true);
    }
    
    /**
     * 获取str所在数组的位置
     * @param str
     * @param strs
     * @return
     */
    public static int getIndex(String str, String[] strs){
        int index = -1;
        if (strs==null || strs.length<=0){
            return index;
        }
        for (int i=0; i<strs.length; i++){
            if (str.equals(strs[i])){
                index = -1;
                break;
            }
        }
        return index;
    }

    /**
     * 返回32位唯一UUID字符串
     * 
     * @return
     */
    public static String getUuidString() {
        return new String(UUID.randomUUID().toString().replace("-", ""));
    }

    /**
     * 获取路径的最后面字符串<br>
     * 如：<br>
     * <code>str = "com.b510.base.bean.User"</code><br>
     * <code> return "User";<code>
     * 
     * @param str
     * @return
     */
    public static String getLastChar(String str) {
        if ((str != null) && (str.length() > 0)) {
            int dot = str.lastIndexOf('.');
            if ((dot > -1) && (dot < (str.length() - 1))) {
                return str.substring(dot + 1);
            }
        }
        return str;
    }

    /**
     * 获取随机字母数字组合
     * 
     * @param length
     *            字符串长度
     * @return
     */
    public static String getRandomCharAndNumr(Integer length) {
        String str = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            boolean b = random.nextBoolean();
            if (b) { // 字符串
                // int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
                str += (char) (65 + random.nextInt(26));// 取得大写字母
            } else { // 数字
                str += String.valueOf(random.nextInt(10));
            }
        }
        return str;
    }
    
	public static String getRandomChar(Integer length) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
             int choice = random.nextBoolean() ? 65 : 97;//取得65大写字母还是97小写字母
			str += (char) (choice + random.nextInt(26));// 取得大写字母
		}
		return str;
	}
    
	public static String getRandomCharExceptLI(Integer length) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
             int choice = random.nextBoolean() ? 65 : 97;//取得65大写字母还是97小写字母
             char c = (char) (choice + random.nextInt(26));
			while (c == 'l' || c == 'L' || c == 'i' || c == 'I') {
	             c = (char) (choice + random.nextInt(26));
			}
			str += c;// 取得大写字母
		}
		return str;
	}
    
	public static String getRandomUpperChar(Integer length) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
             int choice = 65;//取得65大写字母
			str += (char) (choice + random.nextInt(26));// 取得大写字母
		}
		return str;
	}
	
    public static String getRandomNumr(Integer length) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			str += String.valueOf(random.nextInt(10));
		}
        return str;
    }

    /**
     * 判断字符串中是否包含在字符串数组中
     * 
     * @param str
     *            待查找的字符串
     * @param strs
     *            字符串集合
     * @return 判断字符串集合是否存在指定的字符串
     */
    public static boolean inStrs(String str, String[] strs) {
        if (CheckEmptyUtil.isEmpty(str) || CheckEmptyUtil.isEmpty(strs)) {
            return false;
        }
        boolean isIn = false;
        for (String s : strs) {
            if (s.equals(str)) {
                isIn = true;
                break;
            }
        }
        return isIn;
    }

    /**
     * 将字符串左边填充指定字符到len长度
     * 
     * @param str
     *            原始字符串
     * @param paddingStr
     *            填充字符串
     * @param len
     *            长度
     * @return 补位后的字符串
     */
    public static String paddingStrLeft(String str, String paddingStr, int len) {
        int strLen = str.length();
        StringBuffer zeros = new StringBuffer("");
        for (int loop = len - strLen; loop > 0; loop--) {
            zeros.append(paddingStr);
        }
        return zeros.append(str).toString();
    }

    /**
     * 首字母转小写
     * 
     * @param str
     *            待处理的字符串
     * @return 首字母小写的字符串
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder())
                    .append(Character.toLowerCase(str.charAt(0)))
                    .append(str.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     * 
     * @param str
     *            待处理的字符串
     * @return 首字母大写的字符串
     */
    public static String toUpperCaseFirstOne(String str) {
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder())
                    .append(Character.toUpperCase(str.charAt(0)))
                    .append(str.substring(1)).toString();
        }
    }

    public static final char UNDERLINE = '_';

    /**
     * 驼峰型转换成下划线型 首字母自动转为小写,后面大写字母转换为_a
     * 
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (i == 0) {
                // 首字母自动转小写
                c = Character.toLowerCase(c);
            }
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线型转换为驼峰型
     * 
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        param = param.toLowerCase();
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 将字符串转成数组
     * @param str
     * @return
     */
    public static List<String> string2List(String str){
        List<String> strList = null;
        if (!CheckEmptyUtil.isEmpty(str)){
//            str = new String(str.substring(1,str.length()-1));
            if (!CheckEmptyUtil.isEmpty(str)){
                String[] strs = str.trim().split(BaseConstant.Separate.COMMA);
                strList = new ArrayList<String>();
                for (String string:strs){
                    strList.add(string);
                }
            }
        }
        return strList;
    }
    
    /**
     * 将字符串转成数组
     * @param str
     * @return
     */
    public static Integer[] splitInteger(String str, String separatorChars){
    	String[] strs = split(str, separatorChars);
    	Integer[] ints = new Integer[strs.length];
    	for (int i = 0; i < strs.length; i++) {
    		ints[i] = Integer.parseInt(strs[i]);
		}
        return ints;
    }

    public static void main(String[] args) {
    	System.out.println(getRandomCharExceptLI(10));
	}
}
