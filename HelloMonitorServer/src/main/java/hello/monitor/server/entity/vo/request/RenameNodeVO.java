package hello.monitor.server.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RenameNodeVO {
    private int clientId;
    private String node;
    @Pattern(regexp = "(cn|hk|jp|us|sg|kr|de)")
    private String location;
}
