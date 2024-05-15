package hello.monitor.server.entity.vo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RenameClientVO {
    @NotNull
    private int id;
    @Length(min = 1, max = 10)
    private String name;
}
