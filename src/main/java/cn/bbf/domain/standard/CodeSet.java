
package cn.bbf.domain.standard;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class CodeSet implements Serializable {
	private static final long serialVersionUID = -815064758104677008L;

	private final CodeType type;

	private final List<CodeCommon> codes;

	private final Map<String, CodeCommon> codeMap;

	public CodeSet(CodeType type, List<CodeCommon> codes) {
		this.type = type;
		this.codes = codes;
		this.codeMap = new HashMap<>();
		for (CodeCommon code : codes) {
			codeMap.put(code.getCodeCommonPrimaryKey().getCode(), code);
		}
	}

	public CodeType getType() {
		return type;
	}

	public List<CodeCommon> getCodes() {
		return codes;
	}

	public CodeCommon getCode(String codeString) {
		return codeMap.get(codeString);
	}

	public String getValue(String codeString) {
		CodeCommon code = getCode(codeString);
		if (code == null) {
			return null;
		}
		return code.getValue();
	}
}
