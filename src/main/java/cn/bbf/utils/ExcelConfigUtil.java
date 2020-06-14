/*
 * $Id: ExcelConfigUtil.java 6566 2017-12-20 07:30:38Z tangwei $
 * 版权所有 2016 冠新软件。
 * 保留所有权利。
 */
package cn.bbf.utils;

import java.util.Map;

public class ExcelConfigUtil {

	/**
	 * 
	 * @author WJ
	 * @since
	 * 获取Excel要导出的文件名称
	 *
	 */
	public static String getExportFileName(String id) {
		String result = null;
		for (Map<String, Object> map : CommonAtrr.EXCEL_CONFIG_JSON_LIST) {
			if (map.get("id").toString().equals(id)) {
				result = map.get("exportFileName").toString();
				break;
			}
		}
		return result;
	}

	/**
	 * 
	 * @author WJ
	 * @since
	 * 获取sqlId
	 *
	 */
	public static String getSqlId(String id) {
		String result = null;
		for (Map<String, Object> map : CommonAtrr.EXCEL_CONFIG_JSON_LIST) {
			if (map.get("id").toString().equals(id)) {
				result = map.get("sqlId").toString();
				break;
			}
		}
		return result;
	}

	/**
	 * 
	 * @author WJ
	 * @since
	 * 获取模板名称
	 *
	 */
	public static String getTemplateFile(String id) {
		String result = null;
		for (Map<String, Object> map : CommonAtrr.EXCEL_CONFIG_JSON_LIST) {
			if (map.get("id").toString().equals(id)) {
				result = map.get("templateFile").toString();
				break;
			}
		}
		return result;
	}
}
