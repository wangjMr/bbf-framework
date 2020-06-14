package cn.bbf.controller;

import cn.bbf.dto.ResultDto;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: WJ
 * @date 2020/3/8 19:28
 * @description: TODO
 */
@RestController
@RequestMapping("/jas")
public class JasyptController {
    @Autowired
    private StringEncryptor encryptor;

    @GetMapping("/ypt/{username}/{password}")
    public ResultDto buildPwd(@PathVariable String username, @PathVariable String password) {
        username = encryptor.encrypt(username);
        password = encryptor.encrypt(password);
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        return new ResultDto(ResultDto.CODE_SUCCESS, null, data);
    }
}
