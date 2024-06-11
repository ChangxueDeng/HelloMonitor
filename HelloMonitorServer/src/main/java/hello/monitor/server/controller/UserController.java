package hello.monitor.server.controller;

import hello.monitor.server.entity.Result;
import hello.monitor.server.entity.vo.request.CreateSubAccountVO;
import hello.monitor.server.entity.vo.request.UserChangePasswoedVO;
import hello.monitor.server.entity.vo.request.UserResetEmailVO;
import hello.monitor.server.entity.vo.response.SubAccountVO;
import hello.monitor.server.service.AccountService;
import hello.monitor.server.utils.Const;
import hello.monitor.server.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口
 * 包含密码重置、邮箱修改和子用户创建和管理操作
 * @author ChangxueDeng
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    AccountService accountService;
    @Resource
    ControllerUtils controllerUtils;

    /**
     * 重置密码
     * @param id 用户id
     * @param vo 重置密码参数
     * @return {@link Result }<{@link String }>
     */
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestAttribute(Const.USER_ID) int id,
                                         @RequestBody UserChangePasswoedVO vo) {
        return controllerUtils.messageHandler(()-> accountService.changePassword(id, vo));
    }

    /**
     * 重置邮箱
     * @param id 用户id
     * @param vo 重置邮箱参数
     * @return {@link Result }<{@link String }>
     */
    @PostMapping("/reset-email")
    public Result<String> resetEmail(@RequestAttribute(Const.USER_ID) int id,
                                     @RequestBody @Valid UserResetEmailVO vo){
        return controllerUtils.messageHandler(()-> accountService.resetEmail(id, vo));
    }

    /**
     * 创建子用户
     * @param vo 创建子用户参数
     * @return {@link Result }<{@link String }>
     */
    @PostMapping("/sub/create")
    public Result<String> createSubAccount(@RequestBody @Valid CreateSubAccountVO vo) {
        return controllerUtils.messageHandler(()-> accountService.createSubAccount(vo));
    }

    /**
     * 删除子用户
     * @param id 用户Id
     * @param subUid 子账号Id
     * @return {@link Result }<{@link String }>
     */
    @GetMapping("/sub/delete")
    private Result<String> deleteSubAccount(@RequestAttribute(Const.USER_ID) int id, int subUid){
        return controllerUtils.messageHandler(()-> accountService.deleteSubAccount(id, subUid));
    }

    /**
     * 获取子用户列表
     * @return {@link Result }<{@link List }<{@link SubAccountVO }>>
     */
    @GetMapping("/sub/list")
    private Result<List<SubAccountVO>> getSubAccountList(){
        return Result.success(accountService.getSubAccountList());
    }
}
