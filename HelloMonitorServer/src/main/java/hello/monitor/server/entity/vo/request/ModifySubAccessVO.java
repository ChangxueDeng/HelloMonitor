package hello.monitor.server.entity.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 重置子用户分配权限参数
 * @author ChangxueDeng
 */
@Data
public class ModifySubAccessVO {
    @NotNull
    @Min(1)
    private Integer subUid;
    private List<Integer> accessList;
}
