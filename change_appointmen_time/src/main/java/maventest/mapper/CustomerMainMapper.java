package maventest.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.entity.CustomerMain;

@Mapper
public interface CustomerMainMapper extends BaseMapper<CustomerMain> {
}
