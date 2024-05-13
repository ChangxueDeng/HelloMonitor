package hello.monitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hello.monitor.server.entity.dto.Client;
import hello.monitor.server.entity.vo.request.ClientDetailVO;
import hello.monitor.server.entity.vo.request.RuntimeDetailVO;

public interface ClientService extends IService<Client> {
    boolean verifyAndRegister(String token);
    String registerToken();
    Client findClientById(int id);
    Client findClientByToken(String token);
    void updateClientDetail(Client client,ClientDetailVO vo);
    void updateClientRuntimeDetails(Client client, RuntimeDetailVO vo);
}
