package hello.monitor.server.entity.vo.response;


import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {
    String username;
    String role;
    String email;
    String token;
    Date expire;
}
