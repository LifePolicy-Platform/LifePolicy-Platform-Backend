package maventest.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import maventest.customer.entity.CustomerInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerInfoMapper extends BaseMapper<CustomerInfo> {
}
