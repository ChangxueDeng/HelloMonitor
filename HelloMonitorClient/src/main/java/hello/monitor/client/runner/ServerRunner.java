package hello.monitor.client.runner;

import hello.monitor.client.utils.MonitorUtils;
import hello.monitor.client.utils.NetUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author ChangxueDeng
 */
@Component
@Slf4j
public class ServerRunner implements ApplicationRunner {
    @Resource
    NetUtils netUtils;
    @Resource
    MonitorUtils monitorUtils;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("正在向服务端更新基本信息...");
        netUtils.updateBaseDetail(monitorUtils.monitorBaseDetail());
    }
}
