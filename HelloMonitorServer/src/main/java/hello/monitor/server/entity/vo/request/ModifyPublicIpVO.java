package hello.monitor.server.entity.vo.request;

import lombok.Data;

@Data
public class ModifyPublicIpVO {
    private Integer clientId;
    private String publicIp;
}
