package hello.monitor.server.controller;


import hello.monitor.server.entity.Result;
import hello.monitor.server.entity.dto.Client;
import hello.monitor.server.entity.vo.request.ClientDetailVO;
import hello.monitor.server.entity.vo.request.RuntimeDetailVO;
import hello.monitor.server.service.ClientService;
import hello.monitor.server.utils.Const;
import hello.monitor.server.utils.ControllerUtils;
import hello.monitor.server.utils.StatusUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author ChangxueDeng
 */
@RestController
@RequestMapping("/monitor")
public class ClientController {
    @Resource
    ClientService clientService;
    @Resource
    ControllerUtils controllerUtils;
    @GetMapping("/register")
    public Result<Void> clientRegister(@RequestHeader(Const.HEAD_TOKEN) String token) {
        return clientService.verifyAndRegister(token) ? Result.success() : Result.failure(StatusUtils.STATUS_UNAUTHORIZED, "客户端注册失败，请检查token是否正常");
    }
    @PostMapping("/details")
    public Result<Void> updateClientDetails(@RequestAttribute(Const.CLIENT)Client client,
                                            @RequestBody @Valid ClientDetailVO vo) {
        clientService.updateClientDetail(client, vo);
        return Result.success();
    }
    @PostMapping("/runtime")
    public Result<Void> updateClientRuntimeDetails(@RequestAttribute(Const.CLIENT)Client client,
                                                   @RequestBody @Valid RuntimeDetailVO vo) {
        clientService.updateClientRuntimeDetails(client, vo);
        return Result.success();
    }
}
