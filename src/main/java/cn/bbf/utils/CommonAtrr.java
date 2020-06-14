/*
 * $Id: codetemplates.xml 2918 2014-01-22 15:29:26Z hn $
 * 版权所有 2016 冠新软件。
 * 保留所有权利。
 */
package cn.bbf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bbf.domain.standard.CodeSet;

/**
 *
 * 公共属性类
 * 
 */
public class CommonAtrr {
	/**
	 * Excel配置信息
	 */
	public static List<Map<String, Object>> EXCEL_CONFIG_JSON_LIST = new ArrayList<>();

	/**
	 * 获取系统错误提示对照语
	 */
	public static List<Map<String, String>> ALERT_CONTRAST = new ArrayList<>();

	/**
	 * 常用代码信息
	 */
	public static List<CodeSet> CODESETS = new ArrayList<>();


	/**
	 * 系统参数
	 */
//	public static SystemParam SP = new SystemParam();
	
	/**
	 * 活动表头存储
	 */
//	public static List<ExcelColumn> EC = new ArrayList<>();

	/**
	 * 日志log关键字段集合
	 */
	public static Map<String,List<String>> TABLE_KEY_MAP = new HashMap<>();



}
