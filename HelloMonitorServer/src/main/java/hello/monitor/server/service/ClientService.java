package hello.monitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hello.monitor.server.entity.dto.Client;
import hello.monitor.server.entity.vo.request.ClientDetailVO;
import hello.monitor.server.entity.vo.request.RenameClientVO;
import hello.monitor.server.entity.vo.request.RuntimeDetailVO;
import hello.monitor.server.entity.vo.response.ClientDetailsVO;
import hello.monitor.server.entity.vo.response.ClientPreviewVO;

import java.util.List;

public interface ClientService extends IService<Client> {
    boolean verifyAndRegister(String token);
    String registerToken();
    Client findClientById(int id);
    Client findClientByToken(String token);
    void updateClientDetail(Client client,ClientDetailVO vo);
    void updateClientRuntimeDetails(Client client, RuntimeDetailVO vo);
    List<ClientPreviewVO> getClientList();
    void renameClient(RenameClientVO vo);
    ClientDetailsVO getClientDetails(int clientId);
}
