package hello.monitor.client.config;

import hello.monitor.client.task.MonitorJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**定时任务配置类
 * @author ChangxueDeng
 * @date 2024/05/16
 */
@Configuration
@Slf4j
public class QuartzConfiguration {
    /**
     * 创建任务描述
     * @return {@link JobDetail}
     */
    @Bean
    public JobDetail jobDetailFactoryBean() {
        return JobBuilder.newJob(MonitorJob.class)
                .withIdentity("monitor-task")
                .storeDurably()
                .build();
    }

    /**
     * 创建触发器
     * @param detail 任务详情
     * @return {@link Trigger}
     */
    @Bean
    public Trigger cronTriggerFactoryBean(JobDetail detail) {
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("*/10 * * * * ?");
        return TriggerBuilder.newTrigger().forJob(detail).withIdentity("monitor-trigger").withSchedule(cron).build();
    }
}
