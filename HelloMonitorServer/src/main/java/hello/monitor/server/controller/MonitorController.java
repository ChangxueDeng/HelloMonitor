package hello.monitor.server.controller;

import hello.monitor.server.entity.Result;
import hello.monitor.server.entity.vo.response.ClientPreviewVO;
import hello.monitor.server.service.ClientService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
