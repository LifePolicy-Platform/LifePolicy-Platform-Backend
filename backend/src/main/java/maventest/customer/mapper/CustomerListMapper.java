package maventest.customer.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.customer.entity.CustomerList;

@Mapper
public interface CustomerListMapper extends BaseMapper<CustomerList> {
}
