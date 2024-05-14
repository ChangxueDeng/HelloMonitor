package hello.monitor.server.entity.vo.response;

import lombok.Data;

@Data
public class ClientPreviewVO {
    private int id;
    private boolean online;
    private String name;
    private String location;
    private String ip;
    private String osName;
    private String osVersion;
    private String cpuName;
    private int cpuCore;
    private double Memory;
    private double disk;
    private double cpuUsage;
    private double memoryUsage;
    private double networkUpload;
    private double networkDownload;
}
