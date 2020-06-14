package cn.bbf.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: WJ
 * @date 2020/3/7 17:15
 * @description: 系统配置文件, 注:.yml 格式不支持 @PropertySource 注解导入配置。
 */
@ConfigurationProperties(prefix = "system")
@PropertySource("classpath:/system.properties")
public class SystemSettings {

    private String testurl;

    public String getTesturl() {
        return testurl;
    }

    public void setTesturl(String testurl) {
        this.testurl = testurl;
    }
}
