package hello.monitor.server.entity.vo.response;

import lombok.Data;

@Data
public class SshConfigVO {
    private int id;
    private String ip;
    private int port;
    private String username;
    private String password;
}

