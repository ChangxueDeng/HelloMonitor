package hello.monitor.server.entity.dto;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
@Data
@Measurement(name = "runtime")
public class RuntimeDetail {
    @Column(tag = true)
    private Integer clientId;
    @NotNull
    @Column(timestamp = true)
    private Instant timestamp;
    @Column
    @NotNull
    private double cpuUsage;
    @NotNull
    @Column
    private double memoryUsage;
    @NotNull
    @Column
    private double diskUsage;
    @NotNull
    @Column
    private double networkUpload;
    @NotNull
    @Column
    private double networkDownload;
    @NotNull
    @Column
    private double diskRead;
    @NotNull
    @Column
    private double diskWrite;
}
