package hello.monitor.server.controller;

import hello.monitor.server.entity.Result;
import hello.monitor.server.entity.vo.request.ResetPasswordVO;
import hello.monitor.server.entity.vo.request.UserChangePasswoedVO;
import hello.monitor.server.entity.vo.request.UserResetEmailVO;
import hello.monitor.server.service.AccountService;
import hello.monitor.server.utils.Const;
import hello.monitor.server.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    AccountService accountService;
    @Resource
    ControllerUtils controllerUtils;
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestAttribute(Const.USER_ID) int id,
                                         @RequestBody UserChangePasswoedVO vo) {
        return controllerUtils.messageHandler(()-> accountService.changePassword(id, vo));
    }
    @PostMapping("/reset-email")
    public Result<String> resetEmail(@RequestAttribute(Const.USER_ID) int id,
                                     @RequestBody @Valid UserResetEmailVO vo){
        return controllerUtils.messageHandler(()-> accountService.resetEmail(id, vo));
    }
}
