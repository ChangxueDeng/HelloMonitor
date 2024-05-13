package hello.monitor.client.config;

import hello.monitor.client.task.MonitorJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class QuartzConfiguration {
    @Bean
    public JobDetail jobDetailFactoryBean() {
        return JobBuilder.newJob(MonitorJob.class)
                .withIdentity("monitor-task")
                .storeDurably()
                .build();
    }
    @Bean
    public Trigger cronTriggerFactoryBean(JobDetail detail) {
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("*/10 * * * * ?");
        return TriggerBuilder.newTrigger().forJob(detail).withIdentity("monitor-trigger").withSchedule(cron).build();
    }
}
