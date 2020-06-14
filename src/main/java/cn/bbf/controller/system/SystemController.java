package cn.bbf.controller.system;

import cn.bbf.domain.standard.CodeCommon;
import cn.bbf.dto.ResultDto;
import cn.bbf.dto.standard.CodeCommonDto;
import cn.bbf.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: WJ
 * @date 2020/3/7 19:55
 * @description: TODO
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "/testAdmin" ,method = RequestMethod.GET)
    public ResultDto testAdmin(){
        return systemService.testAdmin();
    }

    /**
     * 根据类型查询codeCommon表
     */
    @RequestMapping(value = "/findCodeCommonByType/{type}", method = RequestMethod.GET)
    public ResultDto findCodeCommonByType(@PathVariable String type) {
        List<CodeCommon> lists = systemService.findCodeCommonByType(type);
        List<CodeCommonDto> data = new ArrayList<>();
        for (CodeCommon codeCommon : lists) {
            CodeCommonDto dto = new CodeCommonDto();
            dto.setCode(codeCommon.getCodeCommonPrimaryKey().getCode());
            dto.setValue(codeCommon.getValue());
            dto.setSerialNumber(codeCommon.getSerialNumber());
            dto.setValueRemark(codeCommon.getValueRemark());
            dto.setIsValid(codeCommon.getIsValid());
            data.add(dto);
        }
        return new ResultDto(ResultDto.CODE_SUCCESS, "", data);
    }
}
