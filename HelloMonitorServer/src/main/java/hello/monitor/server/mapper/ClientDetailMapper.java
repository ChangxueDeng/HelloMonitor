package hello.monitor.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hello.monitor.server.entity.dto.ClientDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientDetailMapper extends BaseMapper<ClientDetail> {
}
