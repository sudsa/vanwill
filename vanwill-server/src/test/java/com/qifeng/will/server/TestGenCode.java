package com.qifeng.will.server;
/*
import com.bpaas.doc.framework.gencode.common.GenCodeUtil;
import com.bpaas.doc.framework.gencode.param.dto.JDBCConnectionConfiguration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGenCode {

	private JDBCConnectionConfiguration config;

	public JDBCConnectionConfiguration getJdbcConfig() {
		if (config == null) {
			config = new JDBCConnectionConfiguration();
			config.setConnectionURL("jdbc:mysql://localhost:3306/acc?useUnicode=true&characterEncoding=utf8");
			config.setDriverClass("com.mysql.jdbc.Driver");
			config.setUserId("root");
			config.setPassword("root");
		}
		return config;
	}

	//web项目路径
	private static final String basePath = "E:/test";

	// js文件地址
	private static final String basePathJs = basePath + "/src/main/webapp/resources/pages/modules";

	// jsp文件地址
	private static final String basePathJsp = basePath + "/src/main/webapp/WEB-INF/views/modules";

	// 生成js和jsp文件时的文件夹名
	private static final String baseModule = "base";

	// service和controller的所在包名
	private static final String basePackage = "com.whty.dzuser.base";

	*/
/**
	 * 获取需要生成的完整类名集合
	 *
	 * @return 需要生成的完整类名集合
	 *//*

	public List<String> getBeanNameList() {
		List<String> beanNameList = new ArrayList<String>();
		//beanNameList.add(UcsUserShortcut.class.getName());
		//beanNameList.add("DzApplicationAccess");
		beanNameList.add("DzUser");
		return beanNameList;
	}

	*/
/**
	 * 生成Flat类型代码
	 *//*

//	@Test
	public void genFlatTest() {
		getGenCodeUtil().genCommon();
	}

	*/
/**
	 * 生成Dialog类型代码
	 *//*

//	@Test
	public void genDialogTest() {
		getGenCodeUtil().genDialog();
	}
	*/
/**
	 * 生成Controller类型代码
	 *//*

	@Test
	public void genControllerTest() {
		getGenCodeUtil().genController();
	}

	*/
/**
	 * 生成页面代码
	 *//*

//	@Test
	public void genPageTest(){
		getGenCodeUtil().genPage();
	}
	*/
/**
	 * 生成Service类型代码
	 *//*

	@Test
	public void genServiceTest() {
		getGenCodeUtil().genService();
	}

	public GenCodeUtil getGenCodeUtil() {
		GenCodeUtil genCodeUtil = new GenCodeUtil("vue");
		Map<String, Object> extra = new HashMap<>();
		extra.put("exportXls","true");
 		genCodeUtil.setVmParams(getBeanNameList(), basePathJs, basePathJsp, baseModule, basePackage,getJdbcConfig(), extra);
		return genCodeUtil;
	}
}
*/
