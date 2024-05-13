package hello.monitor.client.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionConfig {
    private String address;
    private String token;
}
