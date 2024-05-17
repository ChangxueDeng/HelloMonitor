package hello.monitor.server.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ChangxueDeng
 */
@Data
@TableName("client_ssh")
public class ClientSsh {
    private int id;
    private String ip;
    private int port;
    private String username;
    private String password;
}
