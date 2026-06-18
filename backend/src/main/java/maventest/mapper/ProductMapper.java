package maventest.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.entity.Product;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
