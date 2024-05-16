package hello.monitor.server.entity.vo.response;

import lombok.Data;

@Data
public class ClientSimpleVO {
    private int id;
    private String name;
    private String location;
    private String osName;
    private String osVersion;
    private String ip;
}
