package cn.bbf.controller.system;

import cn.bbf.dto.ResultDto;
import cn.bbf.utils.RandomValidateCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: WJ
 * @date 2020/4/16 15:08
 * @description: 验证码生成校验
 */
@RequestMapping("/code")
public class RandomCodeController {

    private static final Logger logger = LoggerFactory.getLogger(RandomCodeController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping(value = "/getCode",method = RequestMethod.GET)
    public void code() {
        try {
            response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCode randomValidateCode = new RandomValidateCode();
            randomValidateCode.getRandcode(request, response);// 输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败>>>> ", e);
        }
    }

    @RequestMapping(value = "/validCode",method = RequestMethod.GET)
    public ResultDto validCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return ResultDto.resultFail("请输入验证码");
        }
        String scode = (String) request.getSession().getAttribute(RandomValidateCode.RANDOMCODEKEY) ;
        if (StringUtils.isEmpty(scode)) {
            return ResultDto.resultFail("未知验证码") ;
        }
        if (code.equalsIgnoreCase(scode)) {
            request.getSession().removeAttribute(RandomValidateCode.RANDOMCODEKEY);
            return ResultDto.resultSuccess("成功") ;
        }
        return ResultDto.resultFail("验证码错误") ;
    }

}
