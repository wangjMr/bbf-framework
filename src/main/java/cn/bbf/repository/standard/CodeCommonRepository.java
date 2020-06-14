package cn.bbf.repository.standard;

import cn.bbf.domain.standard.CodeCommon;
import cn.bbf.domain.standard.CodeCommonPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: WJ
 * @date 2020/3/9 12:07
 * @description: TODO
 */
public interface CodeCommonRepository extends JpaRepository<CodeCommon,CodeCommonPrimaryKey>{

    @Query("from CodeCommon c where c.codeCommonPrimaryKey.type = ?1 and c.isValid = 1 order by c.serialNumber, c.codeCommonPrimaryKey.code desc")
    List<CodeCommon> findCodeCommonByType(String type);
}
