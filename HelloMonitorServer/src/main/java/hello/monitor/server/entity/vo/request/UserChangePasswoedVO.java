package hello.monitor.server.entity.vo.request;

import lombok.Data;

@Data
public class UserChangePasswoedVO {
    private String oldPassword;
    private String newPassword;
}
