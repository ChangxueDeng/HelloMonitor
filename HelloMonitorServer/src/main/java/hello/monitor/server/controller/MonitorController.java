package hello.monitor.server.controller;

import com.alibaba.fastjson2.JSONArray;
import hello.monitor.server.entity.Result;
import hello.monitor.server.entity.vo.request.*;
import hello.monitor.server.entity.vo.response.*;
import hello.monitor.server.mapper.AccountMapper;
import hello.monitor.server.service.ClientService;
import hello.monitor.server.utils.Const;
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
    @Resource
    AccountMapper accountMapper;

    /**
     * 获取客户端列表
     * @return {@link Result}<{@link List}<{@link ClientPreviewVO}>>
     */
    @GetMapping("/list")
    public Result<List<ClientPreviewVO>> getClientList(@RequestAttribute(Const.USER_ROLE) String role,
                                                       @RequestAttribute(Const.USER_ID) int id){
        List<ClientPreviewVO> clients = clientService.getClientList();
        if (role.substring(5).equals(Const.ROLE_ADMIN)) {
            return Result.success(clients);
        } else {
            JSONArray ids = getPermissionClients(id);
            return Result.success(clients.stream().filter(c -> ids.contains(c.getId())).toList());
        }
    }

    /**
     * 重命名客户端
     * @param vo {@link RenameClientVO} 重命名信息
     * @return {@link Result}<{@link Void}>
     */
    @PostMapping("/rename")
    public Result<Void> renameClient(@RequestBody @Valid RenameClientVO vo,
                                     @RequestAttribute(Const.USER_ROLE) String role,
                                     @RequestAttribute(Const.USER_ID) int id) {
        if (this.isPermissionForClient(role, id, vo.getId())) {
            clientService.renameClient(vo);
            return Result.success();
        } else {
            return Result.isNoPermission();
        }
    }

    /**
     * 重命名节点
     * @param vo {@link RenameNodeVO} 重命名节点信息
     * @return {@link Result}<{@link Void}>
     */
    @PostMapping("/rename-node")
    public Result<Void> renameNode(@RequestBody @Valid RenameNodeVO vo,
                                   @RequestAttribute(Const.USER_ROLE) String role,
                                   @RequestAttribute(Const.USER_ID) int id) {
        if (this.isPermissionForClient(role, id, vo.getClientId())) {
            clientService.renameNode(vo);
            return Result.success();
        } else {
            return Result.isNoPermission();
        }

    }
    @PostMapping("/modify-public-ip")
    public Result<Void> modifyPublicIp(@RequestBody @Valid ModifyPublicIpVO vo,
                                       @RequestAttribute(Const.USER_ROLE) String role,
                                       @RequestAttribute(Const.USER_ID) int id) {
        if (this.isPermissionForClient(role, id, vo.getClientId())) {
            clientService.modifyPublicIp(vo);
            return Result.success();
        } else {
            return Result.isNoPermission();
        }
    }

    /**
     * 获取客户端详情
     *
     * @param clientId 客户端id
     * @return {@link Result}<{@link ClientDetailsVO}>
     */
    @GetMapping("/details")
    public Result<ClientDetailsVO> getClientDetails(int clientId,
                                                    @RequestAttribute(Const.USER_ROLE) String role,
                                                    @RequestAttribute(Const.USER_ID) int id) {
        if (isPermissionForClient(role, id, clientId)) {
            return Result.success(clientService.getClientDetails(clientId));

        } else {
            return Result.isNoPermission();
        }
    }

    /**
     * 获取客户端运行时历史数据
     * @param clientId 客户端id
     * @return {@link Result}<{@link RuntimeHistoryVO}>
     */
    @GetMapping("/runtime-history")
    public Result<RuntimeHistoryVO> runtimeHistory(int clientId,
                                                   @RequestAttribute(Const.USER_ROLE) String role,
                                                   @RequestAttribute(Const.USER_ID) int id) {
        if (isPermissionForClient(role, id, clientId)) {
            return Result.success(clientService.getClientRuntimeHistory(clientId));
        } else {
            return Result.isNoPermission();
        }
    }

    /**
     * 获取客户端实时运行时详情
     * @param clientId 客户端id
     * @return {@link Result}<{@link RuntimeDetailVO}>
     */
    @GetMapping("/runtime-now")
    public Result<RuntimeDetailVO> runtimeNow(int clientId,
                                              @RequestAttribute(Const.USER_ROLE) String role,
                                              @RequestAttribute(Const.USER_ID) int id) {
        if (isPermissionForClient(role, id, clientId)) {
            return Result.success(clientService.getClientRuntimeDetailsNow(clientId));
        } else {
            return Result.isNoPermission();
        }
    }
    @GetMapping("/register")
    public Result<String> register(@RequestAttribute(Const.USER_ROLE) String role) {
        if (isRoleAdmin(role)) {
            return Result.success(clientService.registerToken());
        } else {
            return Result.isNoPermission();
        }
    }
    @GetMapping("/delete")
    public Result<Void> deleteClient(int clientId,@RequestAttribute(Const.USER_ROLE) String role) {
        if (isRoleAdmin(role)) {
            clientService.deleteClient(clientId);
            return Result.success();
        } else {
            return Result.isNoPermission();
        }

    }
    @GetMapping("/simple-list")
    public Result<List<ClientSimpleVO>> getClientSimpleList(@RequestAttribute(Const.USER_ROLE) String role) {
        if (isRoleAdmin(role)) {
            return Result.success(clientService.getClientSimpleList());
        } else {
            return Result.isNoPermission();
        }
    }
    @PostMapping("/ssh-sava")
    public Result<Void> savaSshConnection(@RequestBody SshConnectionVO vo,
                                          @RequestAttribute(Const.USER_ROLE) String role,
                                          @RequestAttribute(Const.USER_ID) int id) {
        if (isPermissionForClient(role, id, vo.getId())) {
            clientService.saveSshConnection(vo);
            return Result.success();
        } else {
            return Result.isNoPermission();
        }
    }
    @GetMapping("/ssh")
    public Result<SshConfigVO> sshConfig(int clientId,
                                         @RequestAttribute(Const.USER_ROLE) String role,
                                         @RequestAttribute(Const.USER_ID) int id) {
        if (isPermissionForClient(role, id, clientId)) {
            return Result.success(clientService.getSshConfig(clientId));
        } else {
            return Result.isNoPermission();
        }
    }

    private JSONArray getPermissionClients(int uid) {
        return JSONArray.parse(accountMapper.selectById(uid).getClients());
    }
    private boolean isRoleAdmin(String role) {
        return role.substring(5).equals(Const.ROLE_ADMIN);
    }
    private boolean isPermissionForClient(String role, int uid, int clientId) {
        if (isRoleAdmin(role)) {
            return true;
        } else {
            return getPermissionClients(uid).contains(clientId);
        }
    }
}
