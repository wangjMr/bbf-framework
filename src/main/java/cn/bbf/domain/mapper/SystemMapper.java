package cn.bbf.domain.mapper;

import cn.bbf.domain.BBFUser;
import cn.bbf.domain.TestAdmin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: WJ
 * @date 2020/3/7 19:34
 * @description: TODO
 */
@Mapper
public interface SystemMapper {

    List<TestAdmin> findAllAdmin();

    List<BBFUser> findAllUser();
}
