package hello.monitor.client.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 连接配置
 * @author ChangxueDeng
 * @date 2024/05/16
 */
@Data
@AllArgsConstructor
public class ConnectionConfig {
    private String address;
    private String token;
}
