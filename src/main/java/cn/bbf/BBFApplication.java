package cn.bbf;

import cn.bbf.utils.CommonAtrr;
import cn.bbf.utils.JwtUtil;
import cn.bbf.utils.ReadFileUtil;
import cn.bbf.utils.SystemSettings;
import com.alibaba.fastjson.JSON;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * @author: WJ
 * @date 2020/3/7 17:12
 * @description: 应用启动类
 *@EnableConfigurationProperties相当于对SystemSettings类进行了一次注入；如果此类上有@Commponet注解，可以省略
 *
 */
@SpringBootApplication
@EnableConfigurationProperties({SystemSettings.class})
@EnableScheduling
@EnableAsync
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class BBFApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BBFApplication.class, args);
    }
    /**
     平常开发中有可能需要实现在项目启动后执行的功能，
     SpringBoot提供的一种简单的实现方案就是添加一个model并实现CommandLineRunner接口，
     实现功能的代码放在实现的run方法中
     */
    @Override
    public void run(String... args) throws Exception {
        String configPath = BBFApplication.class.getResource("/config/").getPath();
        // 获取Excel相关的配置
        try {
            getExceJsonConfig(URLDecoder.decode(configPath, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static void getExceJsonConfig(String configPath) {
        String excelJsonPath = configPath + "excel.json";
        String jsonStr = ReadFileUtil.readFileByLines(excelJsonPath);
        List<Map<String, Object>> list = (List<Map<String, Object>>) JSON.parse(jsonStr);
        CommonAtrr.EXCEL_CONFIG_JSON_LIST = list;
    }
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
