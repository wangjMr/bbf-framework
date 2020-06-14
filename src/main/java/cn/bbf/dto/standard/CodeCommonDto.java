package cn.bbf.dto.standard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeCommonDto implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 代码
     */
    private String code;
    
    /**
     * 中文含义
     */
    private String value;

    /**
     * 名称备注说明
     */
    private String valueRemark;

    /**
     * 代码顺序流水号
     */
    private Integer serialNumber;

    /**
     * 有效标志0无效1有效
     */
    private Integer isValid;

}