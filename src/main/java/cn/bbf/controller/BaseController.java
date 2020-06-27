package cn.bbf.controller;

import cn.bbf.domain.BBFUser;
import cn.bbf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author: WJ
 * @date 2020/6/27 17:22
 * @description: TODO
 */
@Controller
public class BaseController {

    @Autowired
    private UserRepository userRepository;

    public BBFUser getCurrentUser(HttpServletRequest request){
        String userId = request.getAttribute("userId").toString();
        BBFUser user = userRepository.findById(userId).get();
        return user;
    }
}
