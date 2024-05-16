package hello.monitor.server.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserResetEmailVO {
    @Email
    private String email;
    private String code;
}
