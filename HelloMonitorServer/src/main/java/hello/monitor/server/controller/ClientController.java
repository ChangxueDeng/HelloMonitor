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

    /** 客户端注册
     * @param token 注册token
     * @return {@link Result}<{@link Void}>
     */
    @GetMapping("/register")
    public Result<Void> clientRegister(@RequestHeader(Const.HEAD_TOKEN) String token) {
        return clientService.verifyAndRegister(token) ? Result.success() : Result.failure(StatusUtils.STATUS_UNAUTHORIZED, "客户端注册失败，请检查token是否正常");
    }

    /**
     * 更新客户端基本信息
     * @param client 客户端
     * @param vo 基本信息
     * @return {@link Result}<{@link Void}>
     */
    @PostMapping("/details")
    public Result<Void> updateClientDetails(@RequestAttribute(Const.CLIENT)Client client,
                                            @RequestBody @Valid ClientDetailVO vo) {
        clientService.updateClientDetail(client, vo);
        return Result.success();
    }

    /**
     * 更新客户端运行时信息
     * @param client 客户端
     * @param vo 运行时信息
     * @return {@link Result}<{@link Void}>
     */
    @PostMapping("/runtime")
    public Result<Void> updateClientRuntimeDetails(@RequestAttribute(Const.CLIENT)Client client,
                                                   @RequestBody @Valid RuntimeDetailVO vo) {
        clientService.updateClientRuntimeDetails(client, vo);
        return Result.success();
    }
}
