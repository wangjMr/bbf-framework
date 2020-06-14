package cn.bbf.repository;

import cn.bbf.domain.BBFUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: WJ
 * @date 2020/3/22 16:35
 * @description: TODO
 */
public interface UserRepository  extends JpaRepository<BBFUser,String> {

    BBFUser findByUserName(String userName);

    @Query("from BBFUser where isEnable = 1")
    List<BBFUser> findAllUser();
}
