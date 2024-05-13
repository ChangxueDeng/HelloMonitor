package hello.monitor.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hello.monitor.server.entity.dto.Client;
import hello.monitor.server.mapper.ClientMapper;
import hello.monitor.server.service.ClientService;
import hello.monitor.server.utils.Const;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService{
    private String registerToken = this.generateNewToken();

    private final Map<Integer, Client> clientIdCache = new ConcurrentHashMap<>();
    private final Map<String, Client> clientTokenCache = new ConcurrentHashMap<>();

    @Override
    public String registerToken() {
        return registerToken;
    }

    @Override
    public boolean verifyAndRegister(String token) {
        if (token.equals(registerToken)) {
            int id = this.randomClientId();
            Client client = new Client(id, "未命名主机", token, new Date());
            if (this.save(client)) {
                registerToken = this.generateNewToken();
                this.addClientCache(client);
                return true;
            }
        }
        return false;
    }

    @Override
    public Client findClientById(int id) {
        return clientIdCache.get(id);
    }

    @Override
    public Client findClientByToken(String token) {
        return clientTokenCache.get(token);
    }

    @PostConstruct
    public void init() {
        this.list().forEach(this::addClientCache);
    }
    private void addClientCache(Client client) {
        clientIdCache.put(client.getId(), client);
        clientTokenCache.put(client.getToken(), client);
    }
    private int randomClientId() {
        return new Random().nextInt(90000000) + 10000000;
    }
    private String generateNewToken() {
        String chars = Const.TOKEN_CHARS;
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(24);
        for (int i = 0; i < 24; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        System.out.println(token);
        return token.toString();
    }
}