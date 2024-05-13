package hello.monitor.server.controller;


import hello.monitor.server.entity.Result;
import hello.monitor.server.service.ClientService;
import hello.monitor.server.utils.Const;
import hello.monitor.server.utils.ControllerUtils;
import hello.monitor.server.utils.StatusUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
