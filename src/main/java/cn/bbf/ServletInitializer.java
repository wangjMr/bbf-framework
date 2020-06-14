package cn.bbf;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author: WJ
 * @date 2020/3/7 17:33
 * @description: * 打 war 包的步骤是：
 * 1.将 pom.xml 中的 packaging 的 jar 改为 war
 * 2.往 pom.xml
 * 中增加依赖：org.springframework.boot:spring-boot-starter-tomcat:provided
 * 3.写一个继承 SpringBootServletInitializer 的类
 */
public class ServletInitializer extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(BBFApplication.class);
    }
}
