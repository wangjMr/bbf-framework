package cn.bbf.service;

import cn.bbf.domain.TestAdmin;
import cn.bbf.domain.mapper.ExcelMapper;
import cn.bbf.domain.mapper.SystemMapper;
import cn.bbf.domain.standard.CodeCommon;
import cn.bbf.dto.ResultDto;
import cn.bbf.repository.standard.CodeCommonRepository;
import element.TimeConsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: WJ
 * @date 2020/3/7 19:55
 * @description: TODO
 */
@Service
public class SystemService {
    @Autowired
    private SystemMapper systemMapper;

    @Autowired
    private ExcelMapper excelMapper;

    @Autowired
    private CodeCommonRepository codeCommonRepository;

    @TimeConsum(name = "测试使用")
    public ResultDto testAdmin(){
        List<TestAdmin> allAdmin = systemMapper.findAllAdmin();
        return new ResultDto(0,"", allAdmin);
    }

    /**
     * 根据代码类型获取代码
     */
    public List<CodeCommon> findCodeCommonByType(String type) {
        return codeCommonRepository.findCodeCommonByType(type);
    }

    public List<Map<String, Object>> findResultForExportExcel(
            Map<String, Object> param, String sqlId) {
        Map<String, Object> filterParam = new HashMap<>();
        for (String key : param.keySet()) {
            if (param.get(key) != null
                    && !"null".equals(param.get(key).toString())) {
                filterParam.put(key, param.get(key));
            }
        }
        return excelMapper.findResultForExportExcel(filterParam, sqlId);
    }
}
