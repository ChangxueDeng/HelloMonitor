package hello.monitor.client.utils;

import com.alibaba.fastjson2.JSONObject;
import hello.monitor.client.entity.BaseDetail;
import hello.monitor.client.entity.ConnectionConfig;
import hello.monitor.client.entity.Response;
import hello.monitor.client.entity.RuntimeDetail;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author ChangxueDeng
 */
@Slf4j
@Component
public class NetUtils {
    @Lazy
    @Resource
    ConnectionConfig config;
    private final HttpClient client = HttpClient.newHttpClient();
    public boolean registerToServer(String address, String token) {
        log.info("正在向服务端注册，请稍候...");
        Response response = this.doGet("/register", address, token);
        if (response.success()) {
            log.info("客户端注册完成!");
        } else {
            log.error("客户端注册失败");
        }
        return response.success();
    }
    public void updateBaseDetail(BaseDetail baseDetail) {
        Response response = this.doPost("/details", baseDetail);
        if (response.success()) {
            log.info("客户端更新基本信息完成!");
        } else {
            log.error("客户端更新基本信息失败:{}", response.message());
        }
    }
    public void updateRuntimeDetail(RuntimeDetail runtimeDetail) {
        Response response = this.doPost("/runtime", runtimeDetail);
        if (response.success()) {
            log.info("客户端更新实时信息完成!");
        } else {
            log.error("客户端更新实时信息失败:{}", response.message());
        }
    }
    private Response doGet(String url) {
        return this.doGet(url, config.getAddress(), config.getToken());
    }
    private Response doGet(String url, String address, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(address + "/monitor" + url))
                    .header("Authorization", token)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return JSONObject.parseObject(response.body()).to(Response.class);
        } catch (Exception e) {
            log.error("在发起服务端请求时出现问题");
            return Response.error(e);
        }
    }
    private Response doPost(String url, Object data) {
        try {
            String rawData = JSONObject.from(data).toJSONString();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(rawData))
                    .uri(new URI(config.getAddress() + "/monitor" + url))
                    .header("Authorization", config.getToken())
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return JSONObject.parseObject(response.body()).to(Response.class);
        } catch (Exception e) {
            log.error("在发起服务端请求时出错", e);
            return Response.error(e);
        }
    }
}
