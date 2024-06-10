package hello.monitor.server.entity.vo.response;

import lombok.Data;

@Data
public class ClientDetailsVO {
    private int id;
    private String name;
    private boolean online;
    private String location;
    private String node;
    private String privateIp;
    private String publicIp;
    private String osName;
    private String osVersion;
    private String cpuName;
    private double memory;
    private int cpuCore;
    private double disk;
}
