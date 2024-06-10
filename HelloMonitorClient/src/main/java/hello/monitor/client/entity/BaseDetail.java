package hello.monitor.client.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 客户端基本信息
 * @author ChangxueDeng
 */
@Data
@Accessors(chain = true)
public class BaseDetail {
    private String osArch;
    private String osName;
    private String osVersion;
    private int osBit;
    private String cpuName;
    private int cpuCore;
    private double memory;
    private double disk;
    private String privateIp;
}
