package hello.monitor.server.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import hello.monitor.server.entity.BaseData;

import java.util.Date;

/**
 * @author ChangxueDeng
 * @date 2024/05/16
 */
@TableName("account")
@AllArgsConstructor
@Data
public class Account implements BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    @TableField("username")
    String username;
    @TableField("password")
    String password;
    @TableField("email")
    String email;
    @TableField("role")
    String role;
    @TableField("register_time")
    Date registerTime;
    private String clients;
}
