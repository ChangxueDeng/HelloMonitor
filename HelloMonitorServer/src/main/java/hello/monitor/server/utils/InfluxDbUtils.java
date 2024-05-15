package hello.monitor.server.utils;

import com.alibaba.fastjson2.JSONObject;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.Run;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import hello.monitor.server.entity.dto.RuntimeDetail;
import hello.monitor.server.entity.vo.request.RuntimeDetailVO;
import hello.monitor.server.entity.vo.response.RuntimeHistoryVO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

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
    public RuntimeHistoryVO readRuntimeHistoryData(int clientId) {
        RuntimeHistoryVO vo = new RuntimeHistoryVO();
        String query = """
                from(bucket: "%s")
                |> range(start: %s)
                |> filter(fn: (r) => r["_measurement"] == "runtime")
                |> filter(fn: (r) => r["clientId"] == "%s")
                """;
        String format = String.format(query, bucket, "-1h", clientId);
        List<FluxTable> tables = client.getQueryApi().query(format, org);
        System.out.println("tables：" + tables);
        int size = tables.size();
        if (size == 0) {
            return vo;
        }
        List<FluxRecord> records = tables.get(0).getRecords();
        System.out.println("records：" + records);
        for (int i = 0; i < records.size(); i++) {
            JSONObject object = new JSONObject();
            object.put("timestamp", records.get(i).getTime());
            for (int j = 0; j < size; j++) {
                FluxRecord record = tables.get(j).getRecords().get(i);
                object.put(record.getField(), record.getValue());
            }
            vo.getList().add(object);
        }
        return vo;
    }
}
