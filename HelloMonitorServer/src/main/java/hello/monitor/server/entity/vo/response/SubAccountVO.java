package hello.monitor.server.entity.vo.response;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

@Data
public class SubAccountVO {
    private int id;
    private String username;
    private String email;
    private JSONArray clients;
}
