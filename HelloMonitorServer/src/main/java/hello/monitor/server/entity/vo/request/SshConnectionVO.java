package hello.monitor.server.entity.vo.request;

import lombok.Data;

@Data
public class SshConnectionVO {
    private int id;
    private String ip;
    private int port;
    private String username;
    private String password;
}
