package hello.monitor.server.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateSubAccountVO {
    @Length(min = 1, max = 10)
    private String username;
    @Email
    private String email;
    @Length(min = 6, max = 20)
    private String password;
    @Size(min = 1)
    private List<Integer> clients;
}
