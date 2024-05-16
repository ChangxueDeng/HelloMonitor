package hello.monitor.client.config;

import com.alibaba.fastjson2.JSONObject;
import hello.monitor.client.entity.ConnectionConfig;
import hello.monitor.client.utils.MonitorUtils;
import hello.monitor.client.utils.NetUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 连接服务器配置类
 * @author ChangxueDeng
 */
@Slf4j
@Configuration
public class ServerConfiguration{
    @Resource
    private NetUtils netUtils;
    @Resource
    private MonitorUtils monitorUtils;

    /**
     * 创建连接配置
     * @return {@link ConnectionConfig}
     */
    @Bean
    ConnectionConfig connectionConfig() {
        log.info("正在加载服务端连接配置...");
        ConnectionConfig config = this.readConfigurationFromFile();
        if (config == null) {
            config = this.registerToServer();
        }
        System.out.println(monitorUtils.monitorBaseDetail());
        return config;
    }

    /**
     * 注册到服务端
     * @return {@link ConnectionConfig}
     */
    private ConnectionConfig registerToServer() {
        Scanner scanner = new Scanner(System.in);
        String token, address;
        do {
            log.info("请输入需要注册的服务端地址，地址类似于 'http://192.168.0.22:8080' 这种写法:");
            address = scanner.nextLine();
            log.info("请输入服务端生成的用于注册客户端的Token密钥");
            token = scanner.nextLine();
        } while (!netUtils.registerToServer(address, token));
        ConnectionConfig config = new ConnectionConfig(address, token);
        this.savaConfigurationToFile(config);
        return config;
    }

    /**
     * 保存连接配置到文件
     * @param config 连接配置
     */
    private void savaConfigurationToFile(ConnectionConfig config) {
        File dir = new File("config");
        if (!dir.exists() && dir.mkdir()) {
            log.info("创建配置文件目录成功");
        }
        File file = new File("config/server.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(JSONObject.toJSONString(config));
        } catch (IOException e) {
            log.error("保存配置文件错误");
        }
        log.info("保存配置文件成功");
    }

    /**
     * 从文件读取连接配置
     * @return {@link ConnectionConfig}
     */
    private ConnectionConfig readConfigurationFromFile() {
        File file = new File("config/server.json");
        if (file.exists()) {
            try (FileInputStream stream = new FileInputStream(file)){
                String in = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                return JSONObject.parseObject(in).to(ConnectionConfig.class);
            } catch (IOException e) {
                log.error("读取配置文件失败", e);
            }
        }
        return null;
    }
}
