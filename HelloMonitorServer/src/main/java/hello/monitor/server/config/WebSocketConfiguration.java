package hello.monitor.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author ChangxueDeng
 */
@Configuration
public class WebSocketConfiguration {
    /**
     * 注册WebSocket端点
     * @return {@link ServerEndpointExporter }
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
