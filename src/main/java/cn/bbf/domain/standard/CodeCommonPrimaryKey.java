package cn.bbf.domain.standard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class CodeCommonPrimaryKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String type;

	private String code;

}
