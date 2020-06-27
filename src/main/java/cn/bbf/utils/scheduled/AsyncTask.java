package cn.bbf.utils.scheduled;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author: WJ
 * @date 2020/4/20 17:03
 * @description: TODO
 */
@Component
public class AsyncTask {

    @Async
    public void asyncTaskTest(){
        //模拟执行时间
        System.out.println("线程开始");
        try {
            System.out.println("线程进行中");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程结束");
    }
}
