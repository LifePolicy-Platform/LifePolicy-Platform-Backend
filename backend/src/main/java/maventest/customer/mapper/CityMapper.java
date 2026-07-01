package maventest.customer.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.customer.entity.City;

@Mapper
public interface CityMapper extends BaseMapper<City> {
}
