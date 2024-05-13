package hello.monitor.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import hello.monitor.server.entity.dto.Account;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
