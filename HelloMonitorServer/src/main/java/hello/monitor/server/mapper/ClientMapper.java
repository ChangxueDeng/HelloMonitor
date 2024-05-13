package hello.monitor.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hello.monitor.server.entity.dto.Client;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientMapper extends BaseMapper<Client> {
}
