package maventest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import maventest.entity.CustomerInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerInfoMapper extends BaseMapper<CustomerInfo> {
}
