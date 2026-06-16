package maventest.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.entity.City;

@Mapper
public interface CityMapper extends BaseMapper<City> {
}
