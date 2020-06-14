package cn.bbf.controller;

import cn.bbf.domain.BBFUser;
import cn.bbf.dto.ResultDto;
import cn.bbf.service.UserService;
import cn.bbf.utils.scheduled.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: WJ
 * @date 2020/3/22 16:30
 * @description: TODO
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AsyncTask asyncTask;

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public ResultDto addUser(@RequestBody BBFUser bbfUser){
        return userService.addUser(bbfUser);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultDto login(@RequestBody BBFUser bbfUser){
        return userService.login(bbfUser);
    }

    /**
     * 查询所有可用用户
     * @return
     */
    @RequestMapping(value = "/findAllUser",method = RequestMethod.GET)
    public ResultDto findAllUser(){
        asyncTask.asyncTaskTest();
        System.out.println("我不等你了");
        //测试结果：1.我不等你了 2.线程开始 3.线程结束
        //结论：主线程中使用线程池中的线程来实现，主线程直接将结果进行了返回，然后才是线程池在执行业务逻辑，减少了请求响应时长
        return userService.findAllUser();
    }

}
