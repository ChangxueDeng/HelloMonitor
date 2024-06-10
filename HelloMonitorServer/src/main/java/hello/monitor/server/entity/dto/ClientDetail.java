package hello.monitor.server.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ChangxueDeng
 */
@Data
@TableName(value = "client_detail")
public class ClientDetail {
    @TableId(value = "id")
    private Integer id;
    private String osArch;
    private String osName;
    private String osVersion;
    private int osBit;
    private String cpuName;
    private int cpuCore;
    private double memory;
    private double disk;
    private String privateIp;
    private String publicIp;
}
