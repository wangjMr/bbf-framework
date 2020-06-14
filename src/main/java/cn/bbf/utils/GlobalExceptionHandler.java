package cn.bbf.utils;

import cn.bbf.dto.ResultDto;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: WJ
 * @date 2020/3/9 10:38
 * @description: TODO
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultDto jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.error("GlobalExceptionHandler", e);
        String exceptionStr = ExceptionUtils.getStackTrace(e);
        logger.error("GlobalExceptionHandler=ExceptionUtils.getFullStackTrace(e)", exceptionStr);
        String fullMsg = ExceptionUtils.getRootCauseMessage(e) + "\n" + exceptionStr;
        // 校验通用型错误
        ResultDto result = regException(fullMsg, exceptionStr);
        if (result == null) {
            result = new ResultDto(ResultDto.CODE_SYSTEM_ERROR,
                    "<title>系统错误</title>请点击【详细信息】&&" + fullMsg, null);
        }
        return result;
    }

    /**
     *
     * @author: WJ
     * @date 2020/3/9 10:38
     * 处理比较常见的数据异常
     *
     */
    public ResultDto regException(String fullMsg, String exceptionStr) {
        // 数据类型转换失败的错误
        ResultDto result = regExceptionTransfer(exceptionStr, "from String (.*?): not a valid", fullMsg);
        if (result != null) {
            return result;
        }
        // 返回多行的错误
        result = regExceptionDetail(exceptionStr, ".*result returns more than one elements.*",
                "<title>数据错误</title>期望返回一条记录，但实际返回了多条&&" + fullMsg);
        if (result != null) {
            return result;
        }
        // 值超长的错误
        result = regExceptionMaxLength(exceptionStr, "ORA-12899(.*?)\\)", fullMsg);
        if (result != null) {
            return result;
        }

        // The given id must not be null错误
        result = regExceptionDetail(exceptionStr, "The given id must not be null",
                "<title>数据错误</title>当前操作的数据有异常，某个id为值导致<br/>&&" + fullMsg);
        if (result != null) {
            return result;
        }
        // 获取不到数据库连接错误-内蒙经常出现
        result = regExceptionDetail(exceptionStr, "Exception: Could not (.*?) Connection; nested exception",
                "<title>服务器网络连接异常</title>服务器网络不稳定导致连接异常，请您稍后再试&&" + fullMsg);
        if (result != null) {
            return result;
        }
        // 值大于为此列指定的允许精度
        result = regExceptionDetail(exceptionStr, "SQLDataException: ORA-01438:",
                "<title>数据错误</title>某项输入的值大于为此列指定的允许精度<br/>&&" + fullMsg);
        if (result != null) {
            return result;
        }
        // table or view does not exist
        result = regExceptionDetail(exceptionStr, "ORA-00942",
                "<title>系统错误</title>表或视图不存在<br/>&&" + fullMsg);
        if (result != null) {
            return result;
        }

        return null;
    }

    /**
     *
     * @author: WJ
     * @date 2020/3/9 10:38
     * 通用型错误验证
     *
     */
    private ResultDto regExceptionDetail(String exceptionStr, String regEx, String message) {
        // 忽略大小写的写法
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(exceptionStr);
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        boolean rs = matcher.find();
        if (rs) {
            ResultDto result = new ResultDto(ResultDto.CODE_SYSTEM_ERROR, message, null);
            return result;
        }
        return null;
    }

    /**
     *
     * @author: WJ
     * @date 2020/3/9 10:38
     * 超长错误
     *
     */
    private ResultDto regExceptionMaxLength(String exceptionStr, String regEx, String message) {
        String info = regerMatch(regEx, exceptionStr, 0);
        // 获取超长的详细信息
        if (info != null) {
            String columnInfo = regexAll("\"(.[^\"]*?)\"\\s+的值太大\\s+\\((.*?)\\)", info, 2);
            ResultDto result = null;
            if (columnInfo != null) {
                String columnValue = "";
                String[] array = columnInfo.split("#");
                result = new ResultDto(ResultDto.CODE_SYSTEM_ERROR,
                        "<title>数据长度错误</title>" + array[0] + " 长度超出允许的最大长度( " + array[1] + ")，请核对后再保存&&" + message,
                        null);
            } else {
                result = new ResultDto(ResultDto.CODE_SYSTEM_ERROR,
                        "<title>数据长度错误</title>长度超出允许的最大长度，" + info  + message, null);
            }
            return result;
        }
        return null;
    }

    /**
     *
     * @author: WJ
     * @date 2020/3/9 10:38
     * 数据类型转换失败的错误提示
     *
     */
    private ResultDto regExceptionTransfer(String exceptionStr, String regEx, String message) {
        // 获取用户录入失败的具体的值
        String info = regerMatch(regEx, exceptionStr, 1);
        // 如果值不会空，则继续获取属性名
        if (info != null) {
            String columnInfo = regerMatch("\\[\"(.*?)\"\\]", exceptionStr, 1);
            ResultDto result = null;
            if (columnInfo != null) {
                // 如果配置了对照关系，则取中文，否则提示英文
                result = new ResultDto(ResultDto.CODE_SYSTEM_ERROR,
                        "<title>数据类型错误</title>错误的属性是：" + columnInfo + "，您录入的值是：" + info + "，请核对后再保存&&" + message,
                        null);
            }
            return result;
        }
        return null;
    }

    /**
     *
     * @author: WJ
     * @date 2020/3/9 10:38
     * 获取单个匹配結果
     *
     */
    public static String regerMatch(String regex, String content, int group) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher m = null;
        try {
            m = p.matcher(content);
        } catch (Exception e) {
            return null;
        }
        if (m.find()) {
            return m.group(group);
        }
        return null;
    }

    /**
     *
     * @author: WJ
     * @date 2020/3/9 10:38
     * 獲取多個匹配結果
     *
     */
    public static String regexAll(String regex, String content, int groupNum) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        String result = "";
        while (matcher.find()) {
            for (int i = 0; i < groupNum; i++) {
                result += matcher.group(i + 1) + "#";
            }
        }
        if ("".equals(result)) {
            return null;
        } else {
            return result.substring(0, result.length() - 1);
        }
    }

}
