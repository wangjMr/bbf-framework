/*
 * $Id: ResultDto.java 9379 2018-11-02 02:33:14Z xinxudi $
 * 版权所有 2016 冠新软件。
 * 保留所有权利。
 */
package cn.bbf.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author WJ
 * @since 2020年3月8日
 * 后台给前台返回结果使用
 * 
 */
@Getter
@Setter
public class ResultDto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功返回码 0
	 */
	public static Integer CODE_SUCCESS = 0;
	/**
	 * 失败返回码 1
	 */
	public static Integer CODE_FAIL = 1;

	/**
	 * 保存成功
	 */
	public static String MESSAGE_SAVE_SUCCESS = "保存成功";
	/**
	 * 更新成功
	 */
	public static String MESSAGE_UPDATE_SUCCESS = "更新成功";

	/**
	 * 成功或失败代码 成功是0 失败是非0
	 */
	private Integer code;
	/**
	 * 后台需要给前台的信息提示，如失败后，应该设置失败的具体原因
	 */
	private String message;

	/**
	 * 成功后返回的结果
	 */
	private Object result;

	/**
	 * 系统错误
	 */
	public static Integer CODE_SYSTEM_ERROR = 500;

	public ResultDto() {
		super();
	}

	/**
	 * 
	 * @param code
	 *            成功或失败代码 成功是0 失败是非0
	 * @param message
	 *            后台需要给前台的信息提示，如失败后，应该设置失败的具体原因
	 * @param result
	 *            成功后返回的结果
	 */
	public ResultDto(Integer code, String message, Object result) {
		super();
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public static final ResultDto resultSuccess(Object result) {
		ResultDto dto = new ResultDto();
		dto.setCode(CODE_SUCCESS);
		dto.setMessage("操作成功");
		dto.setResult(result);
		return dto;
	}
	public static final ResultDto resultSuccess(Object result,String message) {
		ResultDto dto = new ResultDto();
		dto.setCode(CODE_SUCCESS);
		dto.setMessage(message);
		dto.setResult(result);
		return dto;
	}

	public static final ResultDto resultFail(String message,Object result) {
		ResultDto dto = new ResultDto();
		dto.setCode(CODE_FAIL);
		dto.setMessage(message);
		dto.setResult(result);
		return dto;
	}
	public static final ResultDto resultFail(String message) {
		ResultDto dto = new ResultDto();
		dto.setCode(CODE_FAIL);
		dto.setMessage(message);
		return dto;
	}
}
