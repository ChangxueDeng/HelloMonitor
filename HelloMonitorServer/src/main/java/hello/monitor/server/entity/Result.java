package hello.monitor.server.entity;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import hello.monitor.server.utils.StatusUtils;
import lombok.Value;

/**
 *
 * 响应数据封装
 * @author ChangxueDeng
 *
 */
@Data
@AllArgsConstructor
public class Result<T> {

    /**
     * 状态
     */
    int status;

    /**
     * 数据
     */
    T data;

    /**
     * 消息
     */
    String message;

    public static <T> Result<T> success() {
        return new Result<>(StatusUtils.STATUS_OK, null, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(StatusUtils.STATUS_OK, data, "请求成功");
    }


    public static <T> Result<T> success(T data, String message) {
        return new Result<>(StatusUtils.STATUS_OK, data, message);
    }

    public static <T> Result<T> failure(int status, T data, String message) {
        return new Result<>(status, data, message);
    }

    public static <T> Result<T> failure(int status, String message){
        return new Result<>(status, null, message);
    }
    public static <T> Result<T> isNoPermission() {
        return new Result<>(StatusUtils.STATUS_FORBIDDEN, null, StatusUtils.MESSAGE_FAILURE_FORBIDDEN);
    }
    /**
     * 将响应结果转换为Json格式的String
     * @return {@link String}
     */
    public String toJsonString(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
