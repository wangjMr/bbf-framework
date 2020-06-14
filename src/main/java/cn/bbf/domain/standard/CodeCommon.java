package cn.bbf.domain.standard;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "BBF_CODE_COMMON")
@Getter
@Setter
public class CodeCommon implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    private CodeCommonPrimaryKey codeCommonPrimaryKey;

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