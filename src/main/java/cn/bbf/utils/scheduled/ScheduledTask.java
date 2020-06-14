package cn.bbf.utils.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: WJ
 * @date 2020/4/20 18:17
 * @description: TODO
 */
@Component
public class ScheduledTask {

    @Scheduled(cron = "0 * * * * ? ")
    public void scheduledTest(){
        System.out.println("测试定时任务"+new Date());
    }
}
