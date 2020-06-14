package cn.bbf.service;

import cn.bbf.domain.BBFUser;
import cn.bbf.dto.ResultDto;
import cn.bbf.repository.UserRepository;
import cn.bbf.utils.CommonUtils;
import cn.bbf.utils.JwtUtil;
import element.TimeConsum;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: WJ
 * @date 2020/3/22 16:36
 * @description: TODO
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 注册
     * @param bbfUser
     * @return
     */
    @TimeConsum(name = "添加用户")
    public ResultDto addUser(BBFUser bbfUser){
        //判断是否用户名重复
        BBFUser findByUserName = userRepository.findByUserName(bbfUser.getUserName());
        if(!CommonUtils.isBlankOrEmpty(findByUserName)){
            return ResultDto.resultFail("已存在用户");
        }
        //密码加密（用springSecurity的）
        bbfUser.setPassword(bCryptPasswordEncoder.encode(bbfUser.getPassword()));
        bbfUser.addBaseInfoForCreate();
        userRepository.save(bbfUser);
        return ResultDto.resultSuccess(null);
    }

    /**
     * 登录
     * @param bbfUser
     * @return
     */
    @TimeConsum(name = "登录")
    public ResultDto login(BBFUser bbfUser){
        //判断是否存在
        BBFUser findByUserName = userRepository.findByUserName(bbfUser.getUserName());
        if(!CommonUtils.isBlankOrEmpty(findByUserName) && bCryptPasswordEncoder.matches(bbfUser.getPassword(),findByUserName.getPassword())){
            String token = jwtUtil.createJWT(findByUserName.getUserId(), findByUserName.getUserName(), "admin");
            Map<String,Object> map = new HashMap();
            map.put("access_token",token);
            map.put("name",findByUserName.getUserName());
            return new ResultDto(ResultDto.CODE_SUCCESS,"登录成功",map);
        }
        return ResultDto.resultFail("用户名或密码错误");
    }

    public ResultDto findAllUser() {
        List<BBFUser> result = userRepository.findAllUser();
        return ResultDto.resultSuccess(result);
    }

}
