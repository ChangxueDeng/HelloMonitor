package hello.monitor.client.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 客户端运行时信息
 * @author ChangxueDeng
 * @date 2024/05/16
 */
@Data
@Accessors(chain = true)
public class RuntimeDetail {
    private long timestamp;
    private double cpuUsage;
    private double memoryUsage;
    private double diskUsage;
    private double networkUpload;
    private double networkDownload;
    private double diskRead;
    private double diskWrite;
}
