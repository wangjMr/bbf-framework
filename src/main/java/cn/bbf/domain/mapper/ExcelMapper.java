package cn.bbf.domain.mapper;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: WJ
 * @date 2020/4/5 22:48
 * @description: 导出Excel 通用Mapper
 */
@Component
public class ExcelMapper {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     *
     * @param param
     * @param sqlId
     * @return  导出Excel公用方法
     */
    public List<Map<String, Object>> findResultForExportExcel(Map<String, Object> param, String sqlId) {
        return this.sqlSessionTemplate.selectList(sqlId, param);
    }

}
