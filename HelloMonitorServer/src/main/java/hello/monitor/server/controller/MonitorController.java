package hello.monitor.server.controller;

import hello.monitor.server.entity.Result;
import hello.monitor.server.entity.vo.request.RenameClientVO;
import hello.monitor.server.entity.vo.request.RenameNodeVO;
import hello.monitor.server.entity.vo.request.RuntimeDetailVO;
import hello.monitor.server.entity.vo.response.ClientDetailsVO;
import hello.monitor.server.entity.vo.response.ClientPreviewVO;
import hello.monitor.server.entity.vo.response.RuntimeHistoryVO;
import hello.monitor.server.service.ClientService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Resource
    ClientService clientService;
    @GetMapping("/list")
    public Result<List<ClientPreviewVO>> getClientList(){
        return Result.success(clientService.getClientList());
    }
    @PostMapping("/rename")
    public Result<Void> renameClient(@RequestBody @Valid RenameClientVO vo) {
        clientService.renameClient(vo);
        return Result.success();
    }
    @PostMapping("/rename-node")
    public Result<Void> renameNode(@RequestBody @Valid RenameNodeVO vo) {
        clientService.renameNode(vo);
        return Result.success();
    }
    @GetMapping("/details")
    public Result<ClientDetailsVO> getClientDetails(int clientId) {
        return Result.success(clientService.getClientDetails(clientId));
    }
    @GetMapping("/runtime-history")
    public Result<RuntimeHistoryVO> runtimeHistory(int clientId) {
        return Result.success(clientService.getClientRuntimeHistory(clientId));
    }
    @GetMapping("/runtime-now")
    public Result<RuntimeDetailVO> runtimeNow(int clientId) {
        return Result.success(clientService.getClientRuntimeDetailsNow(clientId));
    }
}
