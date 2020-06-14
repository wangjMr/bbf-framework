package cn.bbf.aspect;

import element.TimeConsum;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @date 2020/3/7
 */
@Aspect
@Component
public class TimeConsumAspect {

    private final static Logger logger = LoggerFactory.getLogger(TimeConsumAspect.class);

    ThreadLocal<Long> beginTime = new ThreadLocal<>();

    @Pointcut("@annotation(timeConsum)")
    public void serviceStatistics(TimeConsum timeConsum) {

    }

    @Before("serviceStatistics(timeConsum)")
    public void doBefore(TimeConsum timeConsum) {
        beginTime.set(System.currentTimeMillis());
    }

    @After("serviceStatistics(timeConsum)")
    public void doAfter(TimeConsum timeConsum) {
        logger.info(timeConsum.name()+"执行时间："+(System.currentTimeMillis()-beginTime.get())+"ms");
    }
}
