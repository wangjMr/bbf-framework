package cn.bbf.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: WJ
 * @date 2020/3/7 17:39
 * @description: 配置拦截器
 */
@Configuration
public class MyWebConfigurer implements WebMvcConfigurer {

    @Value("${web.upload-path}")
    private String uploadBaseDir;
    /**
     * 这么写的目的是为了在SessionInterceptor中能注入spring中的service，可参考http://
     *               stackoverflow.com/questions/23349180/java-config-for-spring
     *               -interceptor-where-interceptor-is-using-autowired-spring-b
     * @return
     */
    @Bean
    TokenInterceptor myInterceptor(){
        return new TokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(myInterceptor()).addPathPatterns("/**");
    }

    /**
     * 资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(StringUtils.isNotBlank(uploadBaseDir)){
            registry.addResourceHandler("/upload/**")
                    .addResourceLocations("file:///" + uploadBaseDir);
        }
    }
}
