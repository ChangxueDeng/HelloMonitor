package hello.monitor.server.utils;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import hello.monitor.server.entity.dto.RuntimeDetail;
import hello.monitor.server.entity.vo.request.RuntimeDetailVO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InfluxDbUtils {
    @Value("${influxdb.url}")
    private String url;
    @Value("${influxdb.username}")
    private String username;
    @Value("${influxdb.password}")
    private String password;
    @Value("${influxdb.bucket}")
    private String bucket;
    @Value("${influxdb.org}")
    private String org;
    private InfluxDBClient client;
    @PostConstruct
    private void init() {
        client = InfluxDBClientFactory.create(url, username, password.toCharArray());
    }
    public void writeRuntime(int clientId, RuntimeDetailVO vo) {
        RuntimeDetail detail = new RuntimeDetail();
        BeanUtils.copyProperties(vo, detail);
        detail.setTimestamp(Instant.ofEpochMilli(vo.getTimestamp()));
        detail.setClientId(clientId);
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writeMeasurement(bucket, org, WritePrecision.NS, detail);
    }
}
