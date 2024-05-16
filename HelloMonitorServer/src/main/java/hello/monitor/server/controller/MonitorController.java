package hello.monitor.server.controller;

import hello.monitor.server.entity.Result;
import hello.monitor.server.entity.vo.request.RenameClientVO;
import hello.monitor.server.entity.vo.request.RenameNodeVO;
import hello.monitor.server.entity.vo.request.RuntimeDetailVO;
import hello.monitor.server.entity.vo.response.ClientDetailsVO;
import hello.monitor.server.entity.vo.response.ClientPreviewVO;
import hello.monitor.server.entity.vo.response.ClientSimpleVO;
import hello.monitor.server.entity.vo.response.RuntimeHistoryVO;
import hello.monitor.server.service.ClientService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * web端接口
 * @author ChangxueDeng
 * @date 2024/05/16
 */
@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Resource
    ClientService clientService;

    /**
     * 获取客户端列表
     * @return {@link Result}<{@link List}<{@link ClientPreviewVO}>>
     */
    @GetMapping("/list")
    public Result<List<ClientPreviewVO>> getClientList(){
        return Result.success(clientService.getClientList());
    }

    /**
     * 重命名客户端
     * @param vo {@link RenameClientVO} 重命名信息
     * @return {@link Result}<{@link Void}>
     */
    @PostMapping("/rename")
    public Result<Void> renameClient(@RequestBody @Valid RenameClientVO vo) {
        clientService.renameClient(vo);
        return Result.success();
    }

    /**
     * 重命名节点
     * @param vo {@link RenameNodeVO} 重命名节点信息
     * @return {@link Result}<{@link Void}>
     */
    @PostMapping("/rename-node")
    public Result<Void> renameNode(@RequestBody @Valid RenameNodeVO vo) {
        clientService.renameNode(vo);
        return Result.success();
    }

    /**
     * 获取客户端详情
     *
     * @param clientId 客户端id
     * @return {@link Result}<{@link ClientDetailsVO}>
     */
    @GetMapping("/details")
    public Result<ClientDetailsVO> getClientDetails(int clientId) {
        return Result.success(clientService.getClientDetails(clientId));
    }

    /**
     * 获取客户端运行时历史数据
     * @param clientId 客户端id
     * @return {@link Result}<{@link RuntimeHistoryVO}>
     */
    @GetMapping("/runtime-history")
    public Result<RuntimeHistoryVO> runtimeHistory(int clientId) {
        return Result.success(clientService.getClientRuntimeHistory(clientId));
    }

    /**
     * 获取客户端实时运行时详情
     * @param clientId 客户端id
     * @return {@link Result}<{@link RuntimeDetailVO}>
     */
    @GetMapping("/runtime-now")
    public Result<RuntimeDetailVO> runtimeNow(int clientId) {
        return Result.success(clientService.getClientRuntimeDetailsNow(clientId));
    }
    @GetMapping("/register")
    public Result<String> register() {
        return Result.success(clientService.registerToken());
    }
    @GetMapping("/delete")
    public Result<Void> deleteClient(int clientId) {
        clientService.deleteClient(clientId);
        return Result.success();
    }
    @GetMapping("/simple-list")
    public Result<List<ClientSimpleVO>> getClientSimpleList() {
        return Result.success(clientService.getClientSimpleList());
    }
}
