package hello.monitor.client.entity;

import com.alibaba.fastjson2.JSONObject;

import java.util.Random;
import java.util.UUID;

/**
 * http响应
 * @author ChangxueDeng
 */
public record Response(int id, int status, Object data, String message) {
    public boolean success() {
        return status == 200;
    }
    public Response(int status, Object data, String message) {

        this(new Random().nextInt(90000000) + 10000000, status, data, message);
    }
    public JSONObject asJson() {
        return JSONObject.from(data);
    }
    public String asString() {
        return data.toString();
    }
    public static Response error(Exception e) {
        return new Response(0, 500, null, e.getMessage());
    }
}
