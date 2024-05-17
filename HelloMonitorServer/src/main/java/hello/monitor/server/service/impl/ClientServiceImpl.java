package hello.monitor.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hello.monitor.server.entity.dto.Client;
import hello.monitor.server.entity.dto.ClientDetail;
import hello.monitor.server.entity.dto.ClientSsh;
import hello.monitor.server.entity.vo.request.*;
import hello.monitor.server.entity.vo.response.*;
import hello.monitor.server.mapper.ClientDetailMapper;
import hello.monitor.server.mapper.ClientMapper;
import hello.monitor.server.mapper.ClientSshMapper;
import hello.monitor.server.service.ClientService;
import hello.monitor.server.utils.Const;
import hello.monitor.server.utils.InfluxDbUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService{

    @Resource
    ClientDetailMapper detailMapper;
    @Resource
    InfluxDbUtils influxDbUtils;
    @Resource
    ClientSshMapper clientSshMapper;

    private String registerToken = this.generateNewToken();
    private final int TOKEN_LENGTH = 24;
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
            Client client = new Client(id, "未命名服务器", token,"cn", "未命名节点", new Date());
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

    @Override
    public void updateClientDetail(Client client, ClientDetailVO vo) {
        ClientDetail detail = new ClientDetail();
        BeanUtils.copyProperties(vo, detail);
        detail.setId(client.getId());
        if (Objects.nonNull(detailMapper.selectById(client.getId()))) {
            detailMapper.updateById(detail);
        } else {
            detailMapper.insert(detail);
        }
    }

    private Map<Integer,RuntimeDetailVO> runtimeDetail = new HashMap<>();

    @Override
    public void updateClientRuntimeDetails(Client client, RuntimeDetailVO vo) {
        runtimeDetail.put(client.getId(), vo);
        influxDbUtils.writeRuntime(client.getId(), vo);
    }

    @Override
    public List<ClientPreviewVO> getClientList() {
        return clientIdCache.values().stream().map(client -> {
            ClientPreviewVO vo = new ClientPreviewVO();
            BeanUtils.copyProperties(client, vo);
            BeanUtils.copyProperties(detailMapper.selectById(client.getId()), vo);
            RuntimeDetailVO runtime = runtimeDetail.get(client.getId());
            //在线
            if (isOnline(runtime)) {
                BeanUtils.copyProperties(runtime, vo);
                vo.setOnline(true);
            }
            return vo;
        }).toList();
    }

    @Override
    public void renameClient(RenameClientVO vo) {
        this.update(Wrappers.<Client>update().eq("id", vo.getId()).set("name", vo.getName()));
        this.init();
    }

    @Override
    public void renameNode(RenameNodeVO vo) {
        this.update(Wrappers.<Client>update()
                .eq("id", vo.getClientId())
                .set("node", vo.getNode())
                .set("location", vo.getLocation()));
        this.init();
    }

    @Override
    public ClientDetailsVO getClientDetails(int clientId) {
        ClientDetailsVO vo = new ClientDetailsVO();
        BeanUtils.copyProperties(this.clientIdCache.get(clientId), vo);
        BeanUtils.copyProperties(detailMapper.selectById(clientId), vo);
        vo.setOnline(this.isOnline(runtimeDetail.get(clientId)));
        return vo;
    }
    private boolean isOnline(RuntimeDetailVO runtime) {
        return runtime != null && System.currentTimeMillis() - runtime.getTimestamp() < 1000 * 60;
    }

    @Override
    public RuntimeHistoryVO getClientRuntimeHistory(int clientId) {
        RuntimeHistoryVO vo = influxDbUtils.readRuntimeHistoryData(clientId);
        ClientDetail detail = detailMapper.selectById(clientId);
        BeanUtils.copyProperties(detail, vo);
        return vo;
    }

    @Override
    public RuntimeDetailVO getClientRuntimeDetailsNow(int clientId) {
        return runtimeDetail.get(clientId);
    }

    @Override
    public void deleteClient(int clientId) {
        this.removeById(clientId);
        detailMapper.deleteById(clientId);
        this.init();
        runtimeDetail.remove(clientId);
    }

    @Override
    public List<ClientSimpleVO> getClientSimpleList() {
        return clientIdCache.values().stream().map(client -> {
            ClientSimpleVO vo = new ClientSimpleVO();
            BeanUtils.copyProperties(client, vo);
            BeanUtils.copyProperties(detailMapper.selectById(client.getId()), vo);
            return vo;
        }).toList();
    }

    @Override
    public void saveSshConnection(SshConnectionVO vo) {
        Client client = clientIdCache.get(vo.getId());
        if (client != null) {
            ClientSsh ssh = new ClientSsh();
            BeanUtils.copyProperties(vo, ssh);
            if (clientSshMapper.exists(Wrappers.<ClientSsh>query().eq("id", ssh.getId()))) {
                clientSshMapper.updateById(ssh);
            } else {
                clientSshMapper.insert(ssh);
            }
        }
    }

    @Override
    public SshConfigVO getSshConfig(int clientId) {
        ClientSsh ssh = clientSshMapper.selectById(clientId);
        SshConfigVO vo = new SshConfigVO();
        if (ssh != null) {
            BeanUtils.copyProperties(ssh, vo);
        } else {
            ClientDetail detail = detailMapper.selectById(clientId);
            vo.setId(clientId);
            vo.setIp(detail.getIp());
            vo.setPort(22);
        }
        return vo;
    }

    @PostConstruct
    public void init() {
        clientIdCache.clear();
        clientTokenCache.clear();
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
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        System.out.println(token);
        return token.toString();
    }
}
