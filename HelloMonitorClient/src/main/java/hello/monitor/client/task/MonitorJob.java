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
 * @author ChangxueDeng
 */
@Component
public class MonitorJob extends QuartzJobBean {
    @Resource
    MonitorUtils monitorUtils;
    @Resource
    NetUtils netUtils;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        RuntimeDetail runtimeDetail = monitorUtils.monitorRuntimeDetail();
        netUtils.updateRuntimeDetail(runtimeDetail);
    }
}
