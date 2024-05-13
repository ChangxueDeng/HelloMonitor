package hello.monitor.server.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@TableName(value = "client")
public class Client {
    @TableId(value = "id")
    private Integer id;
    private String name;
    private String token;
    private Date registerTime;
}
