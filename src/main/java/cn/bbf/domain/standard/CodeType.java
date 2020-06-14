
package cn.bbf.domain.standard;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BBF_CODE_TYPE")
@Getter
@Setter
public class CodeType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String type;

	private String remark;

}
