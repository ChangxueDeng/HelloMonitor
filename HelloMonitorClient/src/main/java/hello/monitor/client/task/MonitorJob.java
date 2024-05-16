package hello.monitor.client.task;

import hello.monitor.client.entity.RuntimeDetail;
import hello.monitor.client.utils.MonitorUtils;
import hello.monitor.client.utils.NetUtils;
import jakarta.annotation.Resource;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 监控任务
 * @author ChangxueDeng
 */
@Component
public class MonitorJob extends QuartzJobBean {
    @Resource
    MonitorUtils monitorUtils;
    @Resource
    NetUtils netUtils;

    /**
     * 运行时信息发送监控任务
     * @param context 定时上下文
     * @throws JobExecutionException 任务执行异常
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        RuntimeDetail runtimeDetail = monitorUtils.monitorRuntimeDetail();
        netUtils.updateRuntimeDetail(runtimeDetail);
    }
}
